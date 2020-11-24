package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.common.utils.BeanUtils;
import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.data.primary.model.entity.TestTaskDailyConfirm;
import com.foxconn.fii.data.primary.repository.TestTaskDailyConfirmRepository;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.security.model.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestApiResourceController {

    @Autowired
    private TestResourceService testResourceService;

    @Autowired
    private TestTaskDailyConfirmRepository testTaskDailyConfirmRepository;

    @Value("${path.data}")
    private String dataPath;

    @RequestMapping("resource")
    public List<TestResource> getResources(String factory, String shift, String dem) {
        return testResourceService.findByFactory(factory)
                .stream().filter(resource ->
                        ((StringUtils.isEmpty(shift) || "ALL".equalsIgnoreCase(shift)) || (resource.getShift()!= null && shift.equalsIgnoreCase(resource.getShift().toString()))) &&
                        ((StringUtils.isEmpty(dem) || "ALL".equalsIgnoreCase(dem)) || (resource.getDem()!= null && dem.equalsIgnoreCase(resource.getDem())))
                )
                .collect(Collectors.toList());
    }

    @RequestMapping("resource/level")
    public List<TestResource> getResources(String factory, int level) {
        return testResourceService.findByFactoryAndGroupLevel(factory, level);
    }

    @RequestMapping("resource/action")
    public Map<Integer, String> getResourceAction(String factory, String timeSpan) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
//        return testResourceService.getSolutionIdSetByFactory(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate());
        Map<Integer, String> result = new HashMap<>();
        result.put(1, "T00001");
        result.put(2, "T00003");
        result.put(3, "T00010");
        result.put(4, "T00016");
        result.put(5, "T00021");
        result.put(6, "T00031");
        return result;
    }

    @RequestMapping("resource/experience")
    public Map<String, List<TestResource>> getResources(String factory, Integer level, String shift) {
        if (StringUtils.isEmpty(shift) || "ALL".equalsIgnoreCase(shift)) {
            return testResourceService.findByFactoryAndGroupLevel(factory, level)
                    .stream().sorted(Comparator.comparing(TestResource::getExperienceYears, Comparator.reverseOrder()))
                    .collect(Collectors.groupingBy(TestResource::getGroup));
        }
        return testResourceService.findByFactoryAndGroupLevel(factory, level)
                .stream().filter(resource -> resource.getShift()!= null && shift.equalsIgnoreCase(resource.getShift().toString()))
                .sorted(Comparator.comparing(TestResource::getExperienceYears, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TestResource::getGroup));
    }

    @RequestMapping("resource/status/top3")
    public Map<String, Integer> getTop3ResourcesStatus(String factory, String timeSpan, Integer solutionId) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestResource> resourceStatus = testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestResource::getTaskNumber, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < resourceStatus.size(); i++) {
            TestResource resource = resourceStatus.get(i);
            if (i < 3) {
                result.put(resource.getName() + "(" + resource.getChineseName() + ")", resource.getTaskNumber());
            } else {
                int other = result.getOrDefault("other", 0);
                other += resource.getTaskNumber();
                result.put("Other", other);
            }
        }

        return result;
    }

    @RequestMapping("resource/time/top3")
    public Map<String, Long> getTop3ResourcesByTime(String factory, String timeSpan, Integer solutionId) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestResource> resourceStatus = testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestResource::getProcessingTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Map<String, Long> result = new LinkedHashMap<>();
        for (int i = 0; i < resourceStatus.size(); i++) {
            TestResource resource = resourceStatus.get(i);
            if (i < 3) {
                result.put(resource.getName() + "(" + resource.getChineseName() + ")", resource.getProcessingTime());
            } else {
                long other = result.getOrDefault("other", 0L);
                other += resource.getProcessingTime();
                result.put("Other", other);
            }
        }

        return result;
    }

    @RequestMapping("resource/result/top3")
    public Map<String, Integer>  getTop3ResourcesByResult(String factory, String timeSpan, Integer solutionId) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestResource> resourceStatus = testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestResource::getTaskSuccess, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < resourceStatus.size(); i++) {
            TestResource resource = resourceStatus.get(i);
            if (i < 3) {
                result.put(resource.getName() + "(" + resource.getChineseName() + ")", resource.getTaskSuccess());
            } else {
                int other = result.getOrDefault("other", 0);
                other += resource.getTaskSuccess();
                result.put("Other", other);
            }
        }

        return result;
    }

    @RequestMapping("resource/status")
    public Map<String, List<TestResource>> getStatisticsResourcesStatus(String factory, String timeSpan, Integer solutionId, String dem) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<TestResource> resourceStatus = testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().filter(resource -> StringUtils.isEmpty(dem) || dem.equalsIgnoreCase(resource.getDem()))
                .sorted(Comparator.comparing(TestResource::getTaskNumber, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fullTimeSpan.getStartDate());
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        fullTimeSpan.setStartDate(calendar.getTime());

        Map<String, Integer> totalPointMap = testTaskDailyConfirmRepository.findByFactoryAndInputDateBetween(factory, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream()
                .filter(confirm -> confirm.getScore() != null)
                .collect(Collectors.toMap(TestTaskDailyConfirm::getEmployeeId, TestTaskDailyConfirm::getScore, (s1, s2) -> s1 + s2));

        resourceStatus.forEach(resource -> resource.setTotalScore(totalPointMap.getOrDefault(resource.getEmployeeNo(), 0)));
        resourceStatus.sort(Comparator.comparing(TestResource::getTotalScore, Comparator.reverseOrder()));

        Map<String, List<TestResource>> result = new HashMap<>();
        result.put("ALL", resourceStatus);
        return result;
    }

    @RequestMapping("resource/time")
    public Map<String, List<TestResource>> getStatisticsResourcesByTime(String factory, String timeSpan, Integer solutionId) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        return testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestResource::getProcessingTime, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TestResource::getGroup));
    }

    @RequestMapping("resource/result")
    public Map<String, List<TestResource>> getStatisticsResourcesByResult(String factory, String timeSpan, Integer solutionId) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        return testResourceService.findByFactoryAndGroupLevel(factory, 1, solutionId, fullTimeSpan.getStartDate(), fullTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestResource::getTaskSuccess, Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(TestResource::getGroup));
    }

    @RequestMapping("resource/time/{employeeNo}")
    public Map<String, List<Integer>> getStatisticsResourcesByTime(String factory, String timeSpan, Integer solutionId, @PathVariable String employeeNo) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, List<Integer>> result = new HashMap<>();
        result.put("T00001", Arrays.asList(5, 60));
        result.put("T00003", Arrays.asList(3, 40));
        result.put("T00010", Arrays.asList(2, 10));
        result.put("T00016", Arrays.asList(1, 5));
        result.put("T00021", Arrays.asList(1, 3));

        return result;
    }

    @RequestMapping("resource/result/{employeeNo}")
    public Map<String, List<Integer>> getStatisticsResourcesByResult(String factory, String timeSpan, Integer solutionId, @PathVariable String employeeNo) {
        TimeSpan fullTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, List<Integer>> result = new HashMap<>();
        result.put("T00001", Arrays.asList(5, 4));
        result.put("T00003", Arrays.asList(3, 3));
        result.put("T00010", Arrays.asList(2, 1));
        result.put("T00016", Arrays.asList(1, 1));
        result.put("T00021", Arrays.asList(1, 0));

        return result;
    }

    @RequestMapping("resource/{employeeNo}")
    public TestResource getResourceByEmployeeNo(@PathVariable("employeeNo") String employeeNo) {
        return testResourceService.findByEmployeeNo(employeeNo);
    }

    @RequestMapping(value = "resource", method = RequestMethod.POST)
    public TestResource postResource(@ModelAttribute TestResource resource, MultipartFile avatarFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }

        TestResource current = testResourceService.findByEmployeeNo(employee);
        if (current == null || current.getGroupLevel() < 3 || (current.getGroupLevel() == 3 && current.getShift() != resource.getShift())) {
            return null;
        }

        if (avatarFile != null) {
            String fileName = System.currentTimeMillis() + '-' + new Random().nextInt(100) + ".jpg";
            File file = new File(dataPath + "image/" + fileName);
            try {
                avatarFile.transferTo(file);
                resource.setAvatar("/ws-data/image/" + fileName);
            } catch (IOException e) {
                log.error("### postResource error", e);
            }
        }

        if (resource.getId() != 0) {
            TestResource testResource = testResourceService.findById(resource.getId());
            if (testResource == null) {
                return null;
            }
            BeanUtils.copyPropertiesIgnoreNull(resource, testResource, "id");

            return testResourceService.save(testResource);
        }
        return testResourceService.save(resource);
    }

    @RequestMapping(value = "resource/{id}", method = RequestMethod.DELETE)
    public String postResource(@PathVariable Integer id) {
        testResourceService.remove(id);
        return "success";
    }

    @RequestMapping(value = "resource-value", produces = "text/csv; charset=utf-8")
    public void exportResourceValue(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=resource-value.xls");
        httpServletResponse.setCharacterEncoding("UTF-8");

        String[] headers = {"No", "Repair time", "Model", "Station", "Tester", "Error code", "Error description", "Root cause", "Action", "Downtime", "Engineer", "Owner", "Result", "Action code"};

        List<List<String>> rows = new ArrayList<>();
        rows.add(Arrays.asList("1", "2019/3/20 07:36:51", "40-2060", "RC1", "AMMAN_TCXO-S13", "HD0001", "HDMI Port No totalOutput singnal", "probe TP21 damaged", "Change probe TP21", "45", "V0916127", "阮文貴", "1", "T00001"));
        rows.add(Arrays.asList("2", "2019/3/21 13:18:12", "40-1672-0501", "FVT", "RFPAN-01", "SMLAC0", "High TX Measure Frequency: 130 Power", "probe TP32 damaged", "Change probe TP32", "20", "V0906901", "阮文情", "0", "T00016"));
        rows.add(Arrays.asList("3", "2019/3/23 13:18:12", "45-1185-0401", "FVT1", "RFPAN-02", "SMLAO1", "ZigBee Packet Test Packets", "probe TP21 damaged", "Change probe TP21", "33", "V0001050", "阮文禄", "1", "T00021"));
        rows.add(Arrays.asList("4", "2019/3/24 13:18:12", "26-1905-1003", "FVT", "RFPAN-03", "FLAG00", "ZigBee Packet Test Packets", "probe TP22 damaged", "Change probe TP22", "48", "V0906901", "杜德云", "1", "T00022"));
        rows.add(Arrays.asList("5", "2019/3/25 13:18:12", "26-1905-2000", "FVT", "RFPAN-04", "IDMA05", "ZigBee Packet Test Packets", "probe TP23 damaged", "Change probe TP23", "68", "V0906901", "宋文矩", "0", "T00023"));
        rows.add(Arrays.asList("6", "2019/3/26 13:18:12", "26-1905-2001", "FVT", "RFPAN-05", "FLAG01", "ZigBee Packet Test Packets", "probe TP24 damaged", "Change probe TP24", "29", "V0906901", "劉忠線", "1", "T00024"));
        rows.add(Arrays.asList("7", "2019/3/27 13:18:12", "40-2411-1000", "FVT1", "RFPAN-06", "SMLAO0", "ZigBee Packet Test Packets", "probe TP25 damaged", "Change probe TP25", "30", "V0906901", "阮文前", "0", "T00025"));
        rows.add(Arrays.asList("8", "2019/3/28 13:18:12", "40-2411-1001", "FVT", "RFPAN-07", "IDMA05", "ZigBee Packet Test Packets", "probe TP26 damaged", "Change probe TP26", "54", "V0906901", "阮文情", "1", "T00026"));
        rows.add(Arrays.asList("9", "2019/3/29 13:18:12", "40-2412-1000", "FVT", "RFPAN-08", "FLAG01", "ZigBee Packet Test Packets", "probe TP27 damaged", "Change probe TP27", "76", "V0906901", "裴庭展", "0", "T00027"));
        rows.add(Arrays.asList("10", "2019/3/30 13:18:12", "40-2412-1001", "FVT1", "RFPAN-09", "SMLAO1", "ZigBee Packet Test Packets", "probe TP28 damaged", "Change probe TP28", "21", "V0906901", "阮文貴", "1", "T00028"));
        rows.add(Arrays.asList("11", "2019/3/31 13:18:12", "40-1284", "FVT1", "RFPAN-10", "SMLAO1", "ZigBee Packet Test Packets", "probe TP29 damaged", "Change probe TP29", "49", "V0906901", "裴庭展", "1", "T00029"));
        rows.add(Arrays.asList("12", "2019/4/1 13:18:12", "40-1672-0401", "FVT", "RFPAN-11", "SMLAO1", "ZigBee Packet Test Packets", "probe TP30 damaged", "Change probe TP30", "61", "V0906901", "阮文情", "1", "T00030"));

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("resource-value");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(0);
        for (int i=0; i<headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        CellStyle style = workbook.createCellStyle();
//        style.setWrapText(true);

        for (int i=0; i<rows.size(); i++) {
            Row row = sheet.createRow(i+1);
            for (int j=0; j<rows.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rows.get(i).get(j));
                cell.setCellStyle(style);
            }
        }

        try {
            workbook.write(httpServletResponse.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("### exportResourceValue error", e);
        }
    }
}
