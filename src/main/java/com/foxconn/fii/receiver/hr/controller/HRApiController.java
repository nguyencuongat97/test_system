package com.foxconn.fii.receiver.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.primary.model.entity.HrEmployeeTrackingWorkShiftMeta;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingOfficeUnitInfoRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingWorkResultMetaRepository;
import com.foxconn.fii.data.primary.repository.HrEmployeeTrackingWorkShiftMetaRepository;
import com.foxconn.fii.receiver.hr.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/hr")
public class HRApiController {

    @Autowired
    private HrEmployeeTrackingService hrEmployeeTrackingService;

    @Autowired
    private HrEmployeeTrackingOfficeUnitInfoRepository hrEmployeeTrackingOfficeUnitInfoRepository;

    @Autowired
    private HrEmployeeTrackingWorkShiftMetaRepository hrEmployeeTrackingWorkShiftMetaRepository;

    @Autowired
    private HrEmployeeTrackingWorkResultMetaRepository hrEmployeeTrackingWorkResultMetaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HrEtWorkCountService hrEtWorkCountService;

    @Autowired
    private HrEtGetDataService hrEtGetDataService;

    @Autowired
    private HrEtEmployeeInfoService hrEtEmployeeInfoService;

    @Autowired
    private HrEtReportService hrEtReportService;

    @GetMapping("/employee/tracking/by/day")
    public Object employeeTrackingByDay(@RequestParam(required = false, defaultValue = "") String startDate, @RequestParam(required = false, defaultValue = "")String endDate, @RequestParam(required = false, defaultValue = "") String ouCodeList, @RequestParam(required = false, defaultValue = "") String departName) throws ParseException {
        Set<String> departList = new HashSet<>();
        if (!StringUtils.isEmpty(ouCodeList)) {
            departList.addAll(hrEmployeeTrackingOfficeUnitInfoRepository.findByOfficeUnitCodeIn(Arrays.asList(ouCodeList.split(";"))).stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toList()));
        }
        if (departList.isEmpty() && StringUtils.isEmpty(departName)) {
            departList = hrEmployeeTrackingOfficeUnitInfoRepository.findByFactoryName("NBB").stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet());
        }
        return hrEtEmployeeInfoService.countWorkingStatusByDay(startDate, endDate, departName, departList);
    }

    @GetMapping("/employee/tracking/working/list/by/day")
    public Object employeeTrackingWorkingListByDay(@RequestParam String date, @RequestParam(required = false, defaultValue = "") String workShift, @RequestParam(required = false, defaultValue = "") String ouCodeList, @RequestParam(required = false, defaultValue = "") String departName) throws ParseException {
        Set<String> departList = new HashSet<>();
        if (!StringUtils.isEmpty(ouCodeList)) {
            departList.addAll(hrEmployeeTrackingOfficeUnitInfoRepository.findByOfficeUnitCodeIn(Arrays.asList(ouCodeList.split(";"))).stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet()));
        }
        if (departList.isEmpty() && StringUtils.isEmpty(departName)) {
            departList = hrEmployeeTrackingOfficeUnitInfoRepository.findByFactoryName("NBB").stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet());
        }
        return hrEtEmployeeInfoService.listEmployee(date, workShift, departName, departList, "WORKING");
    }

    @GetMapping("/employee/tracking/missing/list/by/day")
    public Object employeeTrackingMissingListByDay(@RequestParam String date, @RequestParam(required = false, defaultValue = "") String ouCodeList, @RequestParam(required = false, defaultValue = "") String departName) throws ParseException {
        Set<String> departList = new HashSet<>();
        if (!StringUtils.isEmpty(ouCodeList)) {
            departList.addAll(hrEmployeeTrackingOfficeUnitInfoRepository.findByOfficeUnitCodeIn(Arrays.asList(ouCodeList.split(";"))).stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet()));
        }
        if (departList.isEmpty() && StringUtils.isEmpty(departName)) {
            departList = hrEmployeeTrackingOfficeUnitInfoRepository.findByFactoryName("NBB").stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet());
        }
        return hrEtEmployeeInfoService.listEmployee(date, "", departName, departList, "MISSING");
    }

    @GetMapping("/employee/tracking/alert/list/by/day")
    public Object employeeTrackingAlertListByDay(@RequestParam String date, @RequestParam(required = false, defaultValue = "") String ouCodeList, @RequestParam(required = false, defaultValue = "") String departName) throws ParseException {
        Set<String> departList = new HashSet<>();
        if (!StringUtils.isEmpty(ouCodeList)) {
            departList.addAll(hrEmployeeTrackingOfficeUnitInfoRepository.findByOfficeUnitCodeIn(Arrays.asList(ouCodeList.split(";"))).stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toList()));
        }
        if (departList.isEmpty() && StringUtils.isEmpty(departName)) {
            departList = hrEmployeeTrackingOfficeUnitInfoRepository.findByFactoryName("NBB").stream().map(e -> e.getOfficeUnitName()).collect(Collectors.toSet());
        }
        return hrEtEmployeeInfoService.listEmployee(date, "", departName, departList, "ALERT");
    }

    @GetMapping("/employee/tracking/office/unit/list")
    public Object employeeTrackingOfficeUnitList(@RequestParam(required = false)String factory) {
        if (StringUtils.isEmpty(factory)) {
            return hrEmployeeTrackingOfficeUnitInfoRepository.jpqlGetAll().stream().sorted((e1, e2) -> ((String) e1.get("ouName")).compareTo((String) e2.get("ouName"))).collect(Collectors.toList());
        } else {
            return hrEmployeeTrackingOfficeUnitInfoRepository.jpqlGetAll().stream().filter(e -> e.get("factoryName").equalsIgnoreCase(factory)).sorted((e1, e2) -> ((String) e1.get("ouName")).compareTo((String) e2.get("ouName"))).collect(Collectors.toList());
        }
    }

    @GetMapping("/employee/tracking/office/factory/list")
    public Object employeeTrackingOfficeFactoryList() {
        return hrEmployeeTrackingOfficeUnitInfoRepository.findAll().stream().map(e -> e.getFactoryName()).collect(Collectors.toSet());
    }

    @GetMapping("/employee/history")
    public Object employeeHistory(@RequestParam String empNo, @RequestParam String starDate, @RequestParam String endDate) throws ParserConfigurationException, IOException, SAXException, ParseException {
        return hrEmployeeTrackingService.getDataHistoryByEmpNo(empNo, starDate, endDate);
    }

    @GetMapping("/test")
    public void test() throws ParseException {

//        return hrEtReportService.monthlySalartReport(empNo.toUpperCase(), month, basicSalary, relatedPerson);

//        WorkCountResultMeta generate list
        /*
        List<String> label = Arrays.asList("上午早退", "上午遲到", "下午早退", "下午遲到");
        List<String> meta = Arrays.asList("A", "B", "C", "D", "E");

        for (String strA : label) {
            for (String strB : label) {
                if (strA.equalsIgnoreCase(strB)) {
                    continue;
                }
                for (String mA : meta) {
                    for (String mB : meta) {
                        String str = strA + mA + strB + mB;
                        HrEmployeeTrackingWorkResultMeta workResultMeta = hrEmployeeTrackingWorkResultMetaRepository.findTop1ByWorkResultCn(str);
                        if (workResultMeta == null) {
                            workResultMeta = new HrEmployeeTrackingWorkResultMeta();
                            workResultMeta.setWorkResultCn(str);
                            if (str.contains("D") || str.contains("E")) {
                                workResultMeta.setWorkResultVi("N");
                                workResultMeta.setIsWorkDay(false);
                                workResultMeta.setNoneWorkDay(true);
                            } else {
                                workResultMeta.setWorkResultVi("LV");
                                workResultMeta.setIsWorkDay(true);
                                workResultMeta.setNoneWorkDay(false);
                            }
                            hrEmployeeTrackingWorkResultMetaRepository.save(workResultMeta);
                        }
                    }
                }
            }
        }
        */
    }

    @GetMapping("/monthly/workcount")
    public Object monthlyWorkCount(@RequestParam String cardId, @RequestParam String departName, @RequestParam(required = false, defaultValue = "0") int year, @RequestParam int month, @RequestParam(required = false, defaultValue = "0") int startDay, @RequestParam(required = false, defaultValue = "0") int endDay) throws IOException, ParserConfigurationException, SAXException, ParseException {
        if (year == 0) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        return hrEtWorkCountService.workCount(cardId, departName, year, month, startDay, endDay);
    }

    @GetMapping("/monthly/workcount/export")
    public ResponseEntity<Resource> monthlyWorkCountExport(@RequestParam String cardId, @RequestParam String departName, @RequestParam(required = false, defaultValue = "0") int year, @RequestParam int month, @RequestParam(required = false, defaultValue = "0") int startDay, @RequestParam(required = false, defaultValue = "0") int endDay) throws IOException, ParserConfigurationException, SAXException, ParseException {
        if (year == 0) {
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        File file = hrEtWorkCountService.excelMonthlyReport("WorkCount.xls", hrEtWorkCountService.workCount(cardId, departName, year, month, startDay, endDay));

        if (file != null) {
            long fileLength = file.length();
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            file.delete();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=workCount.xls");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/workshift")
    public Object getWorkShift(@RequestParam(required = false, defaultValue = "") String shiftCode) {
        List<HrEmployeeTrackingWorkShiftMeta> workShiftMetaList = new ArrayList<>();
        if (StringUtils.isEmpty(shiftCode)) {
            workShiftMetaList.addAll(hrEmployeeTrackingWorkShiftMetaRepository.findAll());
        } else {
            HrEmployeeTrackingWorkShiftMeta workShiftMeta = hrEmployeeTrackingWorkShiftMetaRepository.findTop1ByShiftCode(shiftCode);
            if (workShiftMeta != null) {
                workShiftMetaList.add(workShiftMeta);
            }
        }

        return workShiftMetaList;
    }

    @PostMapping("/workshift")
    public Object createWorkShift(@RequestBody HrEmployeeTrackingWorkShiftMeta workShiftRaw) {
        HrEmployeeTrackingWorkShiftMeta workShiftMeta = new HrEmployeeTrackingWorkShiftMeta();
        BeanUtils.copyProperties(workShiftRaw, workShiftMeta, "id", "createdAt", "updatedAt");
        if (hrEmployeeTrackingWorkShiftMetaRepository.findTop1ByShiftCode(workShiftMeta.getShiftCode()) == null) {
            hrEmployeeTrackingWorkShiftMetaRepository.save(workShiftMeta);
            return true;
        }

        return false;
    }

    @PutMapping("/workshift/{id}")
    public Object updateWorkShift(@PathVariable("id") Long id, @RequestBody HrEmployeeTrackingWorkShiftMeta workShiftRaw) {
        HrEmployeeTrackingWorkShiftMeta workShiftMeta = hrEmployeeTrackingWorkShiftMetaRepository.findById(id).orElse(null);
        if (workShiftMeta != null) {
            BeanUtils.copyProperties(workShiftRaw, workShiftMeta, "id", "createdAt", "updatedAt");
            hrEmployeeTrackingWorkShiftMetaRepository.save(workShiftMeta);
            return true;
        }

        return false;
    }

    @DeleteMapping("/workshift/{id}")
    public Object removeWorkShift(@PathVariable("id") Long id) {
        HrEmployeeTrackingWorkShiftMeta workShiftMeta = hrEmployeeTrackingWorkShiftMetaRepository.findById(id).orElse(null);
        if (workShiftMeta != null) {
            hrEmployeeTrackingWorkShiftMetaRepository.delete(workShiftMeta);
            return true;
        }

        return false;
    }

    @GetMapping("/employee/foreign/clock/hítory")
    public Object foreignEmployeeClockHistory(@RequestParam String empNoStr, @RequestParam String timeSpan) throws ParserConfigurationException, SAXException, IOException, ParseException {
        return hrEmployeeTrackingService.getForeignEmployeeDuty(Arrays.asList(empNoStr.toUpperCase().split(" |\\n")).stream().filter(e -> !StringUtils.isEmpty(e.trim())).map(e -> e.trim()).collect(Collectors.toList()), timeSpan);
    }

}
