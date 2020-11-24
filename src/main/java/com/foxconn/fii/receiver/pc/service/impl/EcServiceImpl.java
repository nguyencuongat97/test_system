package com.foxconn.fii.receiver.pc.service.impl;


import com.foxconn.fii.common.exception.CommonException;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.MailMessage;
import com.foxconn.fii.data.primary.model.entity.PcEcn;
import com.foxconn.fii.data.primary.repository.PcEcnRepository;
import com.foxconn.fii.receiver.pc.service.EcService;
import com.foxconn.fii.receiver.test.service.NotifyService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EcServiceImpl implements EcService {

    @Value("${path.autoit-sap}")
    private String sapPath;

    @Autowired
    private PcEcnRepository pcEcnRepository;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PcEcn> getEcnList(String factory) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR_OF_DAY);
        return pcEcnRepository.findByFactoryAndEffectiveDateAfter(factory, calendar.getTime());
    }

    @Override
    public void saveAll(List<PcEcn> ecnList) {
        if (ecnList.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
                "merge into pc_ecn as target " +
                        "using(select factory=?, ecn_number=?, old_material=?, new_material=?, model_list=?, description=?, ecn_date=?, effective_date=?, created_at=?, updated_at=?) as source " +
                        "   on target.ecn_number=source.ecn_number " +
                        "when matched then " +
                        "   update set " +
                        "   target.effective_date=source.effective_date, " +
                        "   target.updated_at=source.updated_at " +
                        "when not matched then " +
                        "   insert (factory, ecn_number, old_material, new_material, model_list, description, ecn_date, effective_date, created_at, updated_at) " +
                        "   values(source.factory, source.ecn_number, source.old_material, source.new_material, source.model_list, source.description, source.ecn_date, source.effective_date, source.created_at, source.updated_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        PcEcn error = ecnList.get(i);
                        preparedStatement.setString(1, error.getFactory());
                        preparedStatement.setString(2, error.getEcnNumber());
                        preparedStatement.setString(3, error.getOldMaterial());
                        preparedStatement.setString(4, error.getNewMaterial());
                        preparedStatement.setString(5, error.getModelList());
                        preparedStatement.setString(6, error.getDescription());
                        preparedStatement.setString(7, error.getEcnDate());
                        preparedStatement.setTimestamp(8, error.getEffectiveDate() != null ? new Timestamp(error.getEffectiveDate().getTime()) : new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                        preparedStatement.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
                    }

                    @Override
                    public int getBatchSize() {
                        return ecnList.size();
                    }
                });
    }

    @Override
    public void evaluatedEffectiveTime(List<PcEcn> ecnList) {
        if (ecnList.isEmpty()) {
            return;
        }

        Set<String> materialSet = new HashSet<>();
        Set<String> modelSet = new HashSet<>();
        for (PcEcn ecn : ecnList) {
            if (!StringUtils.isEmpty(ecn.getModelList())) {
                materialSet.add(ecn.getOldMaterial());
                String[] models = ecn.getModelList().split(" ");
                modelSet.addAll(Arrays.asList(models));
            }
        }

        Path materialListPath = Paths.get(sapPath + "list-material.txt");
        Path modelListPath = Paths.get(sapPath + "list-model.txt");

        try {
            Files.write(materialListPath, materialSet, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            Files.write(modelListPath, modelSet, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.error("evaluatedEffectiveTime error", e);
        }

        loginSap();
        downloadOPO();
        downloadWO();
        downloadBOM(modelSet);
        downloadForecast();

        Map<String, Double> opo = getOPO();
        Map<String, List<Wo>> woMap = getWO();
        Map<String, Map<String, Double>> bom = getBOM(modelSet);
        Map<String, List<Wo>> forecast = getForecast();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<Wo> woMaterialList = new ArrayList<>();
        for (Map.Entry<String, List<Wo>> entry : woMap.entrySet()) {
            if (bom.containsKey(entry.getKey())) {
                for (Map.Entry<String, Double> bomEntry : bom.get(entry.getKey()).entrySet()) {
                    if (materialSet.contains(bomEntry.getKey())) {
                        for (Wo wo : entry.getValue()) {
                            Wo woMaterial = new Wo();
                            BeanUtils.copyPropertiesIgnoreNull(wo, woMaterial);
                            woMaterial.setMaterial(bomEntry.getKey());
                            woMaterial.setQuantity(woMaterial.getQuantity() * bomEntry.getValue());
                            woMaterialList.add(woMaterial);
                        }
                    }
                }
            }
        }
        Map<String, List<Wo>> woMaterialMap = woMaterialList.stream().collect(Collectors.groupingBy(Wo::getMaterial));

        Map<String, Map<String, Double>> materialByDay = new HashMap<>();
        for (Map.Entry<String, List<Wo>> entry : woMaterialMap.entrySet()) {
            Map<String, Double> plan = new TreeMap<>();
            for (Wo wo : entry.getValue()) {
                int diff = (int) Math.ceil((wo.getEndDate().getTime() - wo.getStartDate().getTime()) * 1.0f / 86400000) + 1;
                for (calendar.setTime(wo.getStartDate()); !calendar.getTime().after(wo.getEndDate()) && diff != 0; calendar.add(Calendar.DAY_OF_YEAR, 1)) {
                    plan.put(df.format(calendar.getTime()), plan.getOrDefault(df.format(calendar.getTime()), 0D) + (int) Math.ceil(wo.getQuantity() * 1.0f / diff));
                }
            }
            materialByDay.put(entry.getKey(), plan);
        }

        for (Map.Entry<String, List<Wo>> entry : forecast.entrySet()) {
            Map<String, Double> plan = new TreeMap<>();
            for (Wo wo : entry.getValue()) {
                calendar.setTime(wo.getStartDate());
                for (int i = 0; i < 7; i++) {
                    plan.put(df.format(calendar.getTime()), plan.getOrDefault(df.format(calendar.getTime()), 0D) + (int) Math.ceil(wo.getQuantity() * 1.0f / 7));
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
            Map<String, Double> woPlan = materialByDay.getOrDefault(entry.getKey(), new HashMap<>());
            plan.forEach(woPlan::putIfAbsent);
            materialByDay.put(entry.getKey(), woPlan);
        }

        Map<String, String> effectiveDateMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : opo.entrySet()) {
            String material = entry.getKey();
            Double qty = entry.getValue();

            Map<String, Double> materialPlan = materialByDay.getOrDefault(material, new HashMap<>());

            double remainQty = qty;
            String effectiveDate = "";
            for (Map.Entry<String, Double> planEntry : materialPlan.entrySet()) {
                remainQty -= planEntry.getValue();
                if (remainQty < 0) {
                    effectiveDate = planEntry.getKey();
                    break;
                }
            }
            effectiveDateMap.put(material, effectiveDate);
        }

        log.debug("{}", effectiveDateMap);
        List<PcEcn> ecnUpdatedList = new ArrayList<>();
        List<PcEcn> ecnNotifyList = new ArrayList<>();
        for (PcEcn ecn : ecnList) {
            if (effectiveDateMap.get(ecn.getOldMaterial()) == null) {
                continue;
            }
            try {
                Date effectiveDate = df.parse(effectiveDateMap.get(ecn.getOldMaterial()));
                ecn.setEffectiveDate(effectiveDate);
                ecnUpdatedList.add(ecn);
                if (effectiveDate.getTime() >= System.currentTimeMillis() && effectiveDate.getTime() - System.currentTimeMillis() < 14 * 86400000) {
                    ecnNotifyList.add(ecn);
                }
            } catch (Exception e) {
                log.error("evaluatedEffectiveTime error", e);
            }
        }

        saveAll(ecnUpdatedList);

        if (!ecnNotifyList.isEmpty()) {
            try {
                String header = "<html><body><b>Dear Users,</b><br/><br/>";
                String body = "<p>All ECN effective date less than 3 days: </p>";
                String footer = "<br/><br/>This message is automatically sent, please do not reply directly!<br/>Ext: 26152</body></html>";
                body += "<table style=\"border: 1px solid #bbb;\"><thead><tr><th style=\"border: 1px solid #bbb;\">ECN</th><th style=\"border: 1px solid #bbb;\">Old Material</th><th style=\"border: 1px solid #bbb;\">New Material</th><th style=\"border: 1px solid #bbb;\">Effective Date</th></tr></thead><tbody>";
                for (PcEcn ecn : ecnNotifyList) {
                    body += String.format("<tr><td style=\"border: 1px solid #bbb;\">%s</td><td style=\"border: 1px solid #bbb;\">%s</td><td style=\"border: 1px solid #bbb;\">%s</td><td style=\"border: 1px solid #bbb;\">%s</td></tr>", ecn.getEcnNumber(), ecn.getOldMaterial(), ecn.getNewMaterial(), df.format(ecn.getEffectiveDate()));
                }
                body += "</tbody></table>";

                MailMessage message = MailMessage.of("[ECN] ECN Change List", header + body + footer, null, null);
                notifyService.notifyToMail(message, "", "cpe-vn-fii-sw@mail.foxconn.com");
            } catch (Exception e) {
                throw CommonException.of(String.format("publish plan error %s", e.getMessage()));
            }
        }
    }

    private void loginSap() {
        ProcessBuilder processBuilder = new ProcessBuilder(sapPath + "LoginExecute.exe");
        processBuilder.directory(new File(sapPath));
        try {
            Process process = processBuilder.start();
            boolean wait = process.waitFor(15, TimeUnit.MINUTES);
            log.info("loginSap exit value {}", wait);
//            process.wait();
        } catch (Exception e) {
            log.error("### loginSap error", e);
        }
    }

    private void downloadOPO() {
        ProcessBuilder processBuilder = new ProcessBuilder(sapPath + "mb52.exe");
        processBuilder.directory(new File(sapPath));
        try {
            Process process = processBuilder.start();
            boolean wait = process.waitFor(15, TimeUnit.MINUTES);
            log.info("downloadOPO exit value {}", wait);
//            process.wait();
        } catch (Exception e) {
            log.error("### downloadOPO error", e);
        }
    }

    private void downloadWO() {
        ProcessBuilder processBuilder = new ProcessBuilder(sapPath + "coois.exe");
        processBuilder.directory(new File(sapPath));
        try {
            Process process = processBuilder.start();
            boolean wait = process.waitFor(15, TimeUnit.MINUTES);
            log.info("downloadWO exit value {}", wait);
//            process.wait();
        } catch (Exception e) {
            log.error("### downloadWO error", e);
        }
    }

    private void downloadBOM(Set<String> modelSet) {
        for (String model : modelSet) {
            ProcessBuilder processBuilder = new ProcessBuilder(sapPath + "cs12.exe", model);
            processBuilder.directory(new File(sapPath));
            try {
                Process process = processBuilder.start();
                boolean wait = process.waitFor(15, TimeUnit.MINUTES);
                log.info("downloadBOM exit value {}", wait);
//                process.wait();
            } catch (Exception e) {
                log.error("### downloadBOM error", e);
            }
        }
    }

    private void downloadForecast() {
        ProcessBuilder processBuilder = new ProcessBuilder(sapPath + "znsd64.exe");
        processBuilder.directory(new File(sapPath));
        try {
            Process process = processBuilder.start();
            boolean wait = process.waitFor(15, TimeUnit.MINUTES);
            log.info("downloadForecast exit value {}", wait);
//            process.wait();
        } catch (Exception e) {
            log.error("### downloadForecast error", e);
        }
    }

    private Map<String, Double> getOPO() {
//        Workbook workbook;
//        try {
//            if (Files.exists(Paths.get(sapPath + "target/mb52.xls"))) {
//                workbook = new HSSFWorkbook(new FileInputStream(sapPath + "target/mb52.xls"));
//            } else if (Files.exists(Paths.get(sapPath + "target/mb52.xlsx"))) {
//                workbook = new XSSFWorkbook(sapPath + "target/mb52.xlsx");
//            } else {
//                throw CommonException.of("upload mps file support only xls and xlsx");
//            }
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            Map<String, Integer> opo = new HashMap<>();
//            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                Cell cell = row.getCell(0);
//                String oldMaterial = ExcelUtils.getStringValue(cell);
//
//                cell = row.getCell(4);
//                Integer qty = ExcelUtils.getIntegerValue(cell);
//
//                if (StringUtils.isEmpty(oldMaterial) || qty == null) {
//                    continue;
//                }
//                Integer total = opo.getOrDefault(oldMaterial, 0);
//                opo.put(oldMaterial, total + qty);
//            }
//
//            return opo;
//        } catch (IOException e) {
//            log.error("### uploadFileMPS error", e);
//            return new HashMap<>();
//        }

        Map<String, Double> opo = new HashMap<>();
        try {
            if (Files.exists(Paths.get(sapPath + "target/mb52.csv"))) {
                List<String> lines = Files.readAllLines(Paths.get(sapPath + "target/mb52.csv"));
                for (int i = 3; i < lines.size(); i++) {
                    String[] columns = lines.get(i).split("\t");
                    if (columns.length < 8) {
                        continue;
                    }

                    String material = columns[2].trim();
                    if (!StringUtils.isEmpty(material)) {
                        double qty = Double.parseDouble(columns[6].trim().replace(",", ""));
                        double total = opo.getOrDefault(material, 0D);
                        opo.put(material, total + qty);
                    }
                }
            }
        } catch (Exception e) {
            log.error("### uploadFileMPS error", e);
        }

        return opo;
    }

    private Map<String, List<Wo>> getWO() {
//        Workbook workbook;
//        try {
//            if (Files.exists(Paths.get(sapPath + "target/coois.xls"))) {
//                workbook = new HSSFWorkbook(new FileInputStream(sapPath + "target/coois.xls"));
//            } else if (Files.exists(Paths.get(sapPath + "target/coois.xlsx"))) {
//                workbook = new XSSFWorkbook(sapPath + "target/coois.xlsx");
//            } else {
//                throw CommonException.of("upload mps file support only xls and xlsx");
//            }
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            Map<String, List<Wo>> woMap = new HashMap<>();
//            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
//                Wo wo = new Wo();
//                Row row = sheet.getRow(i);
//                Cell cell = row.getCell(1);
//                wo.setModel(ExcelUtils.getStringValue(cell));
//
//                cell = row.getCell(6);
//                wo.setQuantity(ExcelUtils.getIntegerValue(cell));
//
//                cell = row.getCell(8);
//                wo.setStartDate(ExcelUtils.getDateValue(cell, "MM/dd/yyyy"));
//
//                cell = row.getCell(9);
//                wo.setEndDate(ExcelUtils.getDateValue(cell, "MM/dd/yyyy"));
//
//                List<Wo> woList = woMap.getOrDefault(wo.getModel(), new ArrayList<>());
//                woList.add(wo);
//                woMap.put(wo.getModel(), woList);
//            }
//
//            return woMap;
//        } catch (IOException e) {
//            log.error("### uploadFileMPS error", e);
//            return new HashMap<>();
//        }

        Map<String, List<Wo>> woMap = new HashMap<>();
        try {
            if (Files.exists(Paths.get(sapPath + "target/coois.csv"))) {
                List<String> lines = Files.readAllLines(Paths.get(sapPath + "target/coois.csv"));
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                for (int i = 5; i < lines.size(); i++) {
                    String[] columns = lines.get(i).split("\t");
                    if (columns.length < 12) {
                        continue;
                    }

                    Wo wo = new Wo();
                    wo.setWoNumber(columns[1].trim());
                    wo.setModel(columns[2].trim());
                    wo.setQuantity(Double.parseDouble(columns[7].trim().replace(",", "")));
                    wo.setStartDate(df.parse(columns[9].trim()));
                    wo.setEndDate(df.parse(columns[10].trim()));

                    List<Wo> woList = woMap.getOrDefault(wo.getModel(), new ArrayList<>());
                    woList.add(wo);
                    woMap.put(wo.getModel(), woList);
                }
            }
        } catch (Exception e) {
            log.error("### uploadFileMPS error", e);
        }

        return woMap;
    }

    private Map<String, Map<String, Double>> getBOM(Set<String> modelList) {
        Map<String, Map<String, Double>> data = new HashMap<>();
        for (String modelName : modelList) {
//            Workbook workbook;
//            try {
//                if (Files.exists(Paths.get(sapPath + "target/cs12-" + modelName + ".xls"))) {
//                    workbook = new HSSFWorkbook(new FileInputStream(sapPath + "target/cs12" + modelName + ".xls"));
//                } else if (Files.exists(Paths.get(sapPath + "target/cs12-" + modelName + ".xlsx"))) {
//                    workbook = new XSSFWorkbook(sapPath + "target/cs12-" + modelName + ".xlsx");
//                } else {
//                    throw CommonException.of("upload mps file support only xls and xlsx");
//                }
//
//                Sheet sheet = workbook.getSheetAt(0);
//
//                Map<String, Integer> bom = new HashMap<>();
//                for (int i = 2; i <= sheet.getLastRowNum(); i++) {
//                    Row row = sheet.getRow(i);
//                    Cell cell = row.getCell(2);
//                    String material = ExcelUtils.getStringValue(cell);
//
//                    cell = row.getCell(10);
//                    int qty = ExcelUtils.getIntegerValue(cell);
//
//                    bom.put(material, qty);
//                }
//
//                data.put(modelName, bom);
//            } catch (IOException e) {
//                log.error("### uploadFileMPS error", e);
//                return new HashMap<>();
//            }
            try {
                if (Files.exists(Paths.get(sapPath + "target/cs12-" + modelName + ".csv"))) {
                    List<String> lines = Files.readAllLines(Paths.get(sapPath + "target/cs12-" + modelName + ".csv"));
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Map<String, Double> bom = new HashMap<>();
                    for (int i = 11; i < lines.size(); i++) {
                        String[] columns = lines.get(i).split("\t");
                        if (columns.length < 19) {
                            continue;
                        }

                        String material = columns[3].trim();
                        double qty = Double.parseDouble(columns[6].trim()) ;

                        bom.put(material, qty);
                    }
                    data.put(modelName, bom);
                }
            } catch (Exception e) {
                log.error("### uploadFileMPS error", e);
            }
        }

        return data;
    }

    private Map<String, List<Wo>> getForecast() {
//        try {
//            Workbook workbook;
//            if (Files.exists(Paths.get(sapPath + "target/znsd64.xls"))) {
//                workbook = new HSSFWorkbook(new FileInputStream(sapPath + "target/znsd64.xls"));
//            } else if (Files.exists(Paths.get(sapPath + "target/znsd64.xlsx"))) {
//                workbook = new XSSFWorkbook(sapPath + "target/znsd64.xlsx");
//            } else {
//                throw CommonException.of("upload mps file support only xls and xlsx");
//            }
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Row header = sheet.getRow(0);
//
//            Map<String, List<Wo>> woMap = new HashMap<>();
//            for (int i = 2; i <= sheet.getLastRowNum() && i + 2 < sheet.getLastRowNum(); i += 3) {
//                if (!"Delta".equals(ExcelUtils.getStringValue(sheet.getRow(i + 2).getCell(0)))) {
//                    continue;
//                }
//
//                Row row = sheet.getRow(i);
//                Cell cell = row.getCell(2);
//                String material = ExcelUtils.getStringValue(cell);
//                List<Wo> woList = new ArrayList<>();
//                for (int j = 11; j < header.getLastCellNum(); j++) {
//                    Wo wo = new Wo();
//                    wo.setModel(material);
//
//                    wo.setQuantity(ExcelUtils.getIntegerValue(sheet.getRow(i + 1).getCell(j)));
//
//                    wo.setStartDate(ExcelUtils.getDateValue(header.getCell(j), "yyyyMMdd"));
//
////                    cell = row.getCell(8);
////                    wo.setEndDate(ExcelUtils.getDateValue(cell, "yyyyMMdd"));
////                    woList.add(wo);
//                }
//                woMap.put(material, woList);
//            }
//
//            return woMap;
//        } catch (IOException e) {
//            log.error("### uploadFileMPS error", e);
//            return new HashMap<>();
//        }

        Map<String, List<Wo>> woMap = new HashMap<>();

        try {
            if (Files.exists(Paths.get(sapPath + "target/znsd64.csv"))) {
                List<String> lines = Files.readAllLines(Paths.get(sapPath + "target/znsd64.csv"));
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

                if (lines.size() > 3) {
                    String[] headers = lines.get(3).split("\t");
                    for (int i = 5; i < lines.size(); i += 3) {
                        String[] columns = lines.get(i+1).split("\t");
                        if (columns.length < 52) {
                            continue;
                        }

                        String material = columns[3].trim();
                        List<Wo> woList = new ArrayList<>();
                        for (int j = 13; j < columns.length; j++) {
                            Wo wo = new Wo();
                            wo.setMaterial(material);
                            wo.setQuantity(Double.parseDouble(columns[j].trim().replace(",", "")));
                            wo.setStartDate(df.parse(headers[j].trim()));
                            woList.add(wo);
                        }

                        woMap.put(material, woList);
                    }
                }
            }
        } catch (Exception e) {
            log.error("### uploadFileMPS error", e);
        }

        return woMap;
    }

    @Data
    public static class Wo {
        private String woNumber;
        private String model;
        private String material;
        private Double quantity;
        private Date startDate;
        private Date endDate;
    }
}
