package com.foxconn.fii.receiver.re.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.b04.model.B04RepairCheckIn;
import com.foxconn.fii.data.b04.repository.B04RepairBc8mRepository;
import com.foxconn.fii.data.b04.repository.B04RepairCheckInRepository;
import com.foxconn.fii.data.b04.repository.B04RepairSMTCheckInRepository;
import com.foxconn.fii.data.b04.repository.B04ResourceRepository;
import com.foxconn.fii.common.response.ListResponse;
import com.foxconn.fii.common.response.SortableMapResponse;
import com.foxconn.fii.data.primary.model.entity.RepairIODaily;
import com.foxconn.fii.data.primary.model.entity.TestRepairSerialError;
import com.foxconn.fii.data.primary.repository.Repair8sLocationsRepository;
import com.foxconn.fii.data.primary.repository.Repair8sShiftRepository;
import com.foxconn.fii.data.primary.repository.TestRepairSerialErrorRepository;
import com.foxconn.fii.receiver.re.service.*;
import com.foxconn.fii.receiver.test.service.TestRepairSerialErrorService;
import com.foxconn.fii.receiver.test.util.TestUtils;
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
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/re")
public class ReApiController {
    @Autowired
    private TestRepairSerialErrorService testRepairSerialErrorService;

    @Autowired
    private B04RepairCheckInRepository repairCheckInRepository;

    @Autowired
    private RepairCheckInService repairCheckInService;

    @Autowired
    private RepairIODailyService repairIODailyService;

    @Autowired
    private B04RepairBc8mRepository b04RepairBc8mRepository;

    @Autowired
    private Repair8sService repair8sService;

    @Autowired
    private Repair8sShiftRepository repair8sShiftRepository;

    @Autowired
    private Repair8sLocationsRepository repair8sLocationsRepository;

    @Autowired
    private RepairOnlineWipService repairOnlineWipService;

    @Autowired
    private B04RepairSMTCheckInRepository b04RepairSMTCheckInRepository;

    @Autowired
    private TestRepairSerialErrorRepository testRepairSerialErrorRepository;

    @Autowired
    private B04ResourceRepository b04ResourceRepository;

    @Autowired
    private RepairCapacityService repairCapacityService;
    @RequestMapping("/status")
    public Map<String, SortableMapResponse> getRepairByStatus(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Map<String, SortableMapResponse> result = new LinkedHashMap<>();

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndSectionNameAndStatus(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        Map<String, Long> total = new LinkedHashMap<>();
        Long totalSize = 0L;

        Map<String, Long> output = new LinkedHashMap<>();
        Long outputSize = 0L;

        Map<String, Long> remain = new LinkedHashMap<>();
        Long remainSize = 0L;

        for (Object[] objects : data) {
            String modelName = (String) objects[0];
            String sectionName = (String) objects[1];
            TestRepairSerialError.Status status = (TestRepairSerialError.Status) objects[2];
            Long count = ((Number) objects[3]).longValue();

            Long tmpCount = total.getOrDefault(modelName, 0L);
            total.put(modelName, tmpCount + count);
            totalSize += count;

            if (status == TestRepairSerialError.Status.UNDER_REPAIR) {
                tmpCount = output.getOrDefault(modelName, 0L);
                output.put(modelName, tmpCount + count);
                outputSize += count;
            } else if (status == TestRepairSerialError.Status.REPAIRED) {
                tmpCount = remain.getOrDefault(modelName, 0L);
                remain.put(modelName, tmpCount + count);
                remainSize += count;
            }
        }
        total = sortMapByValue(total);
        output = sortMapByValue(output);
        remain = sortMapByValue(remain);

        List<Object[]> inputData = repairCheckInService.countByModelNameAndSection(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> input = new LinkedHashMap<>();
        Long inputSize = 0L;

        for (Object[] objects : inputData) {
            String modelName = (String) objects[0];
            String sectionName = (String) objects[1];
            Number count = (Number) objects[2];

            Long tmpCount = input.getOrDefault(modelName, 0L);
            input.put(modelName, tmpCount + count.longValue());
            inputSize += count.longValue();
        }

        input = sortMapByValue(input);

        Map<String, Long> pd = new LinkedHashMap<>();

        result.put("TOTAL", SortableMapResponse.of(total, totalSize.intValue()));
        result.put("RE INPUT", SortableMapResponse.of(input, inputSize.intValue()));
        result.put("RE OUTPUT", SortableMapResponse.of(output, outputSize.intValue()));
        result.put("RE REMAIN", SortableMapResponse.of(remain, remainSize.intValue()));
        result.put("PD OUTPUT", SortableMapResponse.of(pd, pd.size()));

        return result;
    }

    @RequestMapping("/status/daily")
    public Map<String, RepairIODaily> getRepairDailyStatus(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.DAY_OF_YEAR, -28);
        List<RepairIODaily> repairIODailyList = repairIODailyService.findByFactoryAndInputTimeBetween(factory, TestRepairSerialError.Status.REPAIRED, calendar.getTime(), dailyTimeSpan.getEndDate());

        Map<String, RepairIODaily> result = new LinkedHashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        for (int i = 0; i < 3; i++) {
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 7);

            Optional<RepairIODaily> optionalRepairIOWeekly = repairIODailyList.stream()
                    .filter(repair -> repair.getStartDate().before(calendar.getTime()) && repair.getStartDate().after(startDate))
                    .reduce((r1, r2) -> {
                        RepairIODaily tmp = new RepairIODaily();
                        BeanUtils.copyProperties(r1, tmp);
                        tmp.setTotal(r2.getTotal() + tmp.getTotal());
                        tmp.setInput(r2.getInput() + tmp.getInput());
                        tmp.setOutput(r2.getOutput() + tmp.getOutput());
                        tmp.setRemain(r2.getRemain() + tmp.getRemain());
                        return tmp;
                    });

            optionalRepairIOWeekly.ifPresent(repairIODaily -> {
                repairIODaily.setTotal(repairIODaily.getTotal() / 7);
                repairIODaily.setInput(repairIODaily.getInput() / 7);
                repairIODaily.setOutput(repairIODaily.getOutput() / 7);
                repairIODaily.setRemain(repairIODaily.getRemain() / 7);

                result.put(df.format(startDate) + " - " + df.format(calendar.getTime()), repairIODaily);
            });
        }

        calendar.setTime(dailyTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        dailyTimeSpan.setStartDate(calendar.getTime());

        result.putAll(TestUtils.getWeeklyMap(calendar.getTime(), dailyTimeSpan.getEndDate()));

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        Map<String, RepairIODaily> repairDaily = repairIODailyList.stream()
                .filter(repair -> repair.getStartDate().before(dailyTimeSpan.getEndDate()) && repair.getStartDate().after(dailyTimeSpan.getStartDate()))
                .collect(Collectors.toMap(
                        repair -> {
                            calendar.setTime(repair.getStartDate());
                            TimeSpan ts = TimeSpan.from(calendar, TimeSpan.Type.DAILY);
                            return df.format(ts.getStartDate());
                        },
                        repair -> {
                            RepairIODaily tmp = new RepairIODaily();
                            BeanUtils.copyProperties(repair, tmp);
                            return tmp;
                        }, (repair, tmp) -> {
                            repair.setTotal(repair.getTotal() + tmp.getTotal());
                            repair.setInput(repair.getInput() + tmp.getInput());
                            repair.setOutput(repair.getOutput() + tmp.getOutput());
                            repair.setRemain(repair.getRemain() + tmp.getRemain());
                            return repair;
                        }, HashMap::new));
        repairDaily.forEach(result::replace);

        return result;
    }


    @RequestMapping("/time")
    public Map<String, SortableMapResponse<Integer>> getRepairByTime(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();
        result.put("<4H", new SortableMapResponse<>());
        result.put(">4H", new SortableMapResponse<>());
        result.put(">12H", new SortableMapResponse<>());
        result.put(">24H", new SortableMapResponse<>());
        result.put(">48H", new SortableMapResponse<>());
        result.put(">72H", new SortableMapResponse<>());
        result.put(">7D", new SortableMapResponse<>());
        result.put(">15D", new SortableMapResponse<>());

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndTime(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        for (Object[] objects : data) {
            String modelName = (String) objects[0];
            String repairer = (String) objects[1];
            Integer diff = (Integer) objects[2];
            Integer count = (Integer) objects[3];

            SortableMapResponse<Integer> map;
            if (diff < 4) {
                map = result.get("<4H");
            } else if (diff < 12) {
                map = result.get(">4H");
            } else if (diff < 24) {
                map = result.get(">12H");
            } else if (diff < 48) {
                map = result.get(">24H");
            } else if (diff < 72) {
                map = result.get(">48H");
            } else if (diff < 24 * 7) {
                map = result.get(">72H");
            } else if (diff < 24 * 15) {
                map = result.get(">7D");
            } else {
                map = result.get(">15D");
            }
            map.put(modelName, map.get(modelName, 0) + count);
            map.setSize(map.getSize() + count);
        }

        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });

        return result;
    }

    @RequestMapping("/defected")
    public Map<String, Long> getRepairDefected(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = testRepairSerialErrorService.countByReason(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("UNDER REPAIR", 0L);
        result.put("NTF", 0L);
        result.put("PROCESS", 0L);
        result.put("COMPONENT", 0L);
        result.put("OTHER", 0L);

        for (Object[] value : data) {
            if (StringUtils.isEmpty(value[0])) {
                result.put("UNDER REPAIR", result.get("UNDER REPAIR") + (Long) value[1]);
                continue;
            }
            switch ((String) value[0]) {
                case "H003":
                    result.put("NTF", result.get("NTF") + (Long) value[1]);
                    break;
                case "B000":
                    result.put("COMPONENT", result.get("COMPONENT") + (Long) value[1]);
                    break;
                case "C001":
                    result.put("OTHER", result.get("OTHER") + (Long) value[1]);
                    break;
                default:
                    result.put("PROCESS", result.get("PROCESS") + (Long) value[1]);
            }
        }

        return result;
    }

    @RequestMapping("/under-repair/model/top15")
    public Map<String, Long> getTop15UnderRepair(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndSectionNameAndStatus(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String modelName = (String) value[0];
            String sectionName = (String) value[1];
            TestRepairSerialError.Status status = (TestRepairSerialError.Status) value[2];
            Long count = ((Number) value[3]).longValue();

            if (status == TestRepairSerialError.Status.UNDER_REPAIR) {
                result.put(modelName, result.getOrDefault(modelName, 0L) + count);
            }
        }

        result = result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(15)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return result;
    }

    @RequestMapping("/under-repair/model")
    public Map<String, Long> getUnderRepair(String factory, String timeSpan, String modelName) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndErrorCode(factory, TestRepairSerialError.Status.UNDER_REPAIR, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String model = (String) value[0];
            String errorCode = (String) value[1];
            Long count = (Long) value[2];

            if (model.equalsIgnoreCase(modelName)) {
                result.put(errorCode, result.getOrDefault(errorCode, 0L) + count);
            }
        }

        result = result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(15)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return result;
    }

    @RequestMapping("/under-repair/time")
    public Map<String, SortableMapResponse<Integer>> getUnderRepairByTime(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndTime(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();
        result.put("<4H", new SortableMapResponse<>());
        result.put(">4H", new SortableMapResponse<>());
        result.put(">12H", new SortableMapResponse<>());
        result.put(">24H", new SortableMapResponse<>());
        result.put(">48H", new SortableMapResponse<>());
        result.put(">72H", new SortableMapResponse<>());
        result.put(">7D", new SortableMapResponse<>());
        result.put(">15D", new SortableMapResponse<>());


        for (Object[] value : data) {
            String modelName = (String) value[0];
            String repairer = (String) value[1];
            Integer diff = (Integer) value[2];
            Integer count = (Integer) value[3];

            if (StringUtils.isEmpty(repairer)) {
                SortableMapResponse<Integer> map;
                if (diff < 4) {
                    map = result.get("<4H");
                } else if (diff < 12) {
                    map = result.get(">4H");
                } else if (diff < 24) {
                    map = result.get(">12H");
                } else if (diff < 48) {
                    map = result.get(">24H");
                } else if (diff < 72) {
                    map = result.get(">48H");
                } else if (diff < 24 * 7) {
                    map = result.get(">72H");
                } else if (diff < 24 * 15) {
                    map = result.get(">7D");
                } else {
                    map = result.get(">15D");
                }
                map.put(modelName, map.get(modelName, 0) + count);
                map.setSize(map.getSize() + count);
            }
        }

        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });
        return result;
    }

    @RequestMapping("/bc8m/daily")
    public Map<String, RepairIODaily> getBc8mDailyStatus(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.DAY_OF_YEAR, -28);
        Date sDate = calendar.getTime();

        List<RepairIODaily> repairIODailyList = repairIODailyService.findByFactoryAndInputTimeBetween(factory, TestRepairSerialError.Status.BC8M, calendar.getTime(), dailyTimeSpan.getEndDate());

        Map<String, RepairIODaily> result = new LinkedHashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        for (int i = 0; i < 3; i++) {
            Date startDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 7);

            Optional<RepairIODaily> optionalRepairIOWeekly = repairIODailyList.stream()
                    .filter(repair -> repair.getStartDate().before(calendar.getTime()) && repair.getStartDate().after(startDate))
                    .reduce((r1, r2) -> {
                        RepairIODaily tmp = new RepairIODaily();
                        BeanUtils.copyProperties(r1, tmp);
                        tmp.setTotal(r2.getTotal() + tmp.getTotal());
                        tmp.setInput(r2.getInput() + tmp.getInput());
                        tmp.setOutput(r2.getOutput() + tmp.getOutput());
                        tmp.setRemain(r2.getRemain() + tmp.getRemain());
                        return tmp;
                    });

            RepairIODaily repairIODaily = optionalRepairIOWeekly.orElse(null);
            result.put(df.format(startDate) + " - " + df.format(calendar.getTime()), repairIODaily);
        }

        calendar.setTime(dailyTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        dailyTimeSpan.setStartDate(calendar.getTime());

        result.putAll(TestUtils.getWeeklyMap(calendar.getTime(), dailyTimeSpan.getEndDate()));

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        Map<String, RepairIODaily> repairDaily = repairIODailyList.stream()
                .filter(repair -> repair.getStartDate().before(dailyTimeSpan.getEndDate()) && repair.getStartDate().after(dailyTimeSpan.getStartDate()))
                .collect(Collectors.toMap(
                        repair -> {
                            calendar.setTime(repair.getStartDate());
                            TimeSpan ts = TimeSpan.from(calendar, TimeSpan.Type.DAILY);
                            return df.format(ts.getStartDate());
                        },
                        repair -> {
                            RepairIODaily tmp = new RepairIODaily();
                            BeanUtils.copyProperties(repair, tmp);
                            return tmp;
                        }, (repair, tmp) -> {
                            repair.setTotal(repair.getTotal() + tmp.getTotal());
                            repair.setInput(repair.getInput() + tmp.getInput());
                            repair.setOutput(repair.getOutput() + tmp.getOutput());
                            repair.setRemain(repair.getRemain() + tmp.getRemain());
                            return repair;
                        }, HashMap::new));
        repairDaily.forEach(result::replace);

        RepairIODaily tmp = new RepairIODaily();
        List<Object[]> objects = repairIODailyService.findByFactoryAndInputTimeBefore("B04", TestRepairSerialError.Status.BC8M, sDate);
        if (!objects.isEmpty()) {
            Object[] data = objects.get(0);
            tmp.setTotal(((Number)data[0]).intValue());
            tmp.setInput(((Number)data[1]).intValue());
            tmp.setOutput(((Number)data[2]).intValue());
            tmp.setRemain(((Number)data[3]).intValue());
        }

        for (Map.Entry<String, RepairIODaily> entry : result.entrySet()) {
            RepairIODaily repair  = entry.getValue();

            if (repair == null) {
                repair = new RepairIODaily();
                result.put(entry.getKey(), repair);
            }
            repair.setTotal(repair.getTotal() + tmp.getTotal());
            repair.setInput(repair.getInput() + tmp.getInput());
            repair.setOutput(repair.getOutput() + tmp.getOutput());
            repair.setRemain(repair.getRemain() + tmp.getRemain());

            tmp = repair;
        }

        Map<String, RepairIODaily> repairedDaily = getRepairDailyStatus(factory, timeSpan);

        for (Map.Entry<String, RepairIODaily> entry : result.entrySet()) {
            RepairIODaily repaired = repairedDaily.get(entry.getKey());
            if (repaired == null) {
                continue;
            }

            RepairIODaily repair  = entry.getValue();
            if (repair != null) {
                repair.setTat(repair.getRemain() * 1.0f / repaired.getOutput());
            }
        }

        return result;
    }

    @RequestMapping("/bc8m/io")
    public Map<String, Long> getBc8mInOut(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        RepairIODaily tmp = new RepairIODaily();
        List<Object[]> objects = repairIODailyService.findByFactoryAndInputTimeBefore(factory, TestRepairSerialError.Status.BC8M, dailyTimeSpan.getEndDate());
        if (!objects.isEmpty()) {
            Object[] data = objects.get(0);
            tmp.setTotal(((Number)data[0]).intValue());
            tmp.setInput(((Number)data[1]).intValue());
            tmp.setOutput(((Number)data[2]).intValue());
            tmp.setRemain(((Number)data[3]).intValue());
        }

        List<RepairIODaily> data = repairIODailyService.findByFactoryAndInputTimeBetween(factory, TestRepairSerialError.Status.BC8M, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();
        result.put("INPUT", 0L);
        result.put("OUTPUT", 0L);
        result.put("REMAIN", 0L);

        for (RepairIODaily repairIODaily : data) {
            result.put("INPUT", result.getOrDefault("INPUT", 0L) + repairIODaily.getInput());
            result.put("OUTPUT", result.getOrDefault("OUTPUT", 0L) + repairIODaily.getOutput());
        }
        result.put("REMAIN", (long)tmp.getRemain());

        return result;
    }

    @RequestMapping("/bc8m/model")
    public Map<String, Long> getBc8mByModel(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<RepairIODaily> data = repairIODailyService.findByFactoryAndInputTimeBetween(factory, TestRepairSerialError.Status.BC8M, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();

        for (RepairIODaily repairIODaily : data) {
            result.put(repairIODaily.getModelName(), result.getOrDefault(repairIODaily.getModelName(), 0L) + repairIODaily.getRemain());
        }

        result = result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return result;
    }

    @RequestMapping("/bc8m/model/error")
    public Map<String, Long> getBc8mOfModelByErrorCode(String factory, String timeSpan, String modelName) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.YEAR, -1);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndErrorCode(factory, TestRepairSerialError.Status.BC8M, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, Long> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String model = (String) value[0];
            String errorCode = (String) value[1];
            Long count = (Long) value[2];

            if (model.equalsIgnoreCase(modelName)) {
                result.put(errorCode, result.getOrDefault(errorCode, 0L) + count);
            }
        }

        result = result.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return result;
    }

    @RequestMapping("/bc8m/data")
    public ListResponse<TestRepairSerialError> getBc8mData(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.MONTH, -3);
        dailyTimeSpan.setStartDate(calendar.getTime());

        List<TestRepairSerialError> inputList = testRepairSerialErrorService.findByFactoryAndStatus(factory, TestRepairSerialError.Status.BC8M, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate())
                .stream().sorted(Comparator.comparing(TestRepairSerialError::getUpdatedAt, Comparator.reverseOrder()))
                .limit(50).collect(Collectors.toList());
        return ListResponse.success(inputList);
    }

    @RequestMapping("/input/section")
    public Map<String, SortableMapResponse<Integer>> getInputBySection(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = repairCheckInService.countByModelNameAndSection(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String modelName = (String) value[0];
            String sectionName = (String) value[1];
            Number count = (Number) value[2];

            SortableMapResponse<Integer> map = result.get(sectionName);
            if (map == null) {
                map = new SortableMapResponse<>();
                result.put(sectionName, map);
            }

            map.put(modelName, map.get(modelName, 0) + count.intValue());
            map.setSize(map.getSize() + count.intValue());
        }

        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });
        return result;
    }

    @RequestMapping("status/inout")
    public  Map<String, Object> getInputOutputBySectionName(String factory, String timeSpan){
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStrart = Calendar.getInstance();
        calendarEnd.setTime(dailyTimeSpan.getEndDate());
        calendarStrart.setTime(dailyTimeSpan.getStartDate());
        dailyTimeSpan.getStartDate();

        Map<String, Map<String, Integer>> resSI = new HashMap<>();
        Map<String, Map<String, Integer>> resSMT = new HashMap<>();
        while (calendarStrart.getTime().compareTo(calendarEnd.getTime()) <= 0) {
            Calendar calendarMtp = Calendar.getInstance();
            calendarMtp.setTime(calendarStrart.getTime());
            calendarMtp.add(Calendar.DAY_OF_MONTH, 1);
            List<Object[]> input = repairCheckInService.countByModelNameAndSection(factory, calendarStrart.getTime(), calendarMtp.getTime());
            List<Object[]> output = testRepairSerialErrorService.countByModelNameAndSectionNameAndStatus(factory, calendarStrart.getTime(), calendarMtp.getTime());
            Map<String, SortableMapResponse<Integer>> resultInput = new LinkedHashMap<>();

            Map<String, SortableMapResponse<Integer>> resultOutput = new LinkedHashMap<>();

            for (Object[] value : input) {
                String modelName = (String) value[0];
                String sectionName = (String) value[1];
                Number count = (Number) value[2];

                SortableMapResponse<Integer> map = resultInput.get(sectionName);
                if (map == null) {
                    map = new SortableMapResponse<>();
                    resultInput.put(sectionName, map);
                }

                map.put(modelName, map.get(modelName, 0) + count.intValue());
                map.setSize(map.getSize() + count.intValue());
            }

            resultInput.forEach((key, value) -> {
                value.sort(true);
                value.convert();
            });  // END resultInput

            for (Object[] value : output) {
                String modelName = (String) value[0];
                String sectionName = (String) value[1];
                TestRepairSerialError.Status status = (TestRepairSerialError.Status) value[2];
                Long count = (Long) value[3];

                if (status != TestRepairSerialError.Status.REPAIRED) {
                    continue;
                }

                SortableMapResponse<Integer> map = resultOutput.get(sectionName);
                if (map == null) {
                    map = new SortableMapResponse<>();
                    resultOutput.put(sectionName, map);
                }

                map.put(modelName, map.get(modelName, 0) + count.intValue());
                map.setSize(map.getSize() + count.intValue());
            }

            resultOutput.forEach((key, value) -> {
                value.sort(true);
                value.convert();
            });
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

            Map<String, Integer> tmpResult = new HashMap<>();
            Map<String, Integer> tmpResultSMT = new HashMap<>();
            Integer valInputSi = 0;
            Integer valOutputSi = 0;
            Integer valInputSMT = 0;
            Integer valOutputSMT = 0;
            if (resultInput.containsKey("SI")){
                valInputSi = resultInput.get("SI").getSize();
            }
            if (resultOutput.containsKey("SI")){
                valOutputSi = resultOutput.get("SI").getSize();
            }
            if (resultInput.containsKey("SMT")){
                valInputSMT = resultInput.get("SMT").getSize();
            }
            if (resultOutput.containsKey("SMT")){
                valOutputSMT = resultOutput.get("SMT").getSize();
            }
            tmpResult.put("input", valInputSi);
            tmpResult.put("output",valOutputSi);
            tmpResult.put("remain",valInputSi - valOutputSi);

            tmpResultSMT.put("input", valInputSMT);
            tmpResultSMT.put("output",valOutputSMT);
            tmpResultSMT.put("remain",valInputSMT - valOutputSMT);
            log.info("pause");
            resSI.put(df.format(calendarStrart.getTime()), tmpResult);
            resSMT.put(df.format(calendarStrart.getTime()), tmpResultSMT);
            calendarStrart.add(Calendar.DAY_OF_MONTH, 1);
        }
     //   List<Integer> test =  resultInput.entrySet().stream().map(e -> e.getValue().getSize()).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("SI", resSI);
        response.put("SMT", resSMT);
        return response;
    }

    @RequestMapping("status/inOutTrendChart")
    public Object getDataTrendChartBySectionName(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getEndDate());
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        dailyTimeSpan.setStartDate(calendar.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
     //   List<B04Resource> dataUser = b04ResourceRepository.findByDem("RE");
        //List<String> arrUser = dataUser.stream().map(e -> e.getEmployeeNo()).collect(Collectors.toList());
        List<Object[]> dataInputSMT = testRepairSerialErrorRepository.countInputByModelNameAndSection(factory, "SMT", dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        List<Object[]> dataInputSI = testRepairSerialErrorRepository.countInputByModelNameAndSection(factory, "SI", dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        List<Object[]> dataOutputSMT = testRepairSerialErrorRepository.countByModelNameAndSection(factory, "SMT", dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        List<Object[]> dataOutputSI = testRepairSerialErrorRepository.countByModelNameAndSection(factory, "SI", dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        Map<String, Integer> simpleMap = new LinkedHashMap<>();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(dailyTimeSpan.getEndDate());
        for (int i = 0; i <11 ; i++) {
            simpleMap.put(simpleDateFormat.format(calendar.getTime()), 0);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar.get(Calendar.DAY_OF_YEAR) == calendarEnd.get(Calendar.DAY_OF_YEAR)){
                break;
            }
        }
        Map<String, Integer> inputSI = fromListObjectToMapOutput(dataInputSI);
        Map<String, Integer> inputSMT = fromListObjectToMapOutput(dataInputSMT);
        Map<String, Integer> outputSI = fromListObjectToMapOutput(dataOutputSI);
        Map<String, Integer> outputSMT = fromListObjectToMapOutput(dataOutputSMT);
        Map<String, Object> resSI = new LinkedHashMap<>();
        Map<String, Object> resSMT = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : simpleMap.entrySet()){
            Map<String, Object> tmpMap = new HashMap<>();
            if (inputSI.get(entry.getKey()) != null){
                tmpMap.put("input", inputSI.get(entry.getKey()));
            }else{
                tmpMap.put("input", 0);
            }
            if (outputSI.get(entry.getKey()) != null){
                tmpMap.put("output", outputSI.get(entry.getKey()));
            }else{
                tmpMap.put("output", 0);
            }
            tmpMap.put("remain", (inputSI.get(entry.getKey()) == null ? 0 : inputSI.get(entry.getKey())) - (outputSI.get(entry.getKey()) == null ? 0 : outputSI.get(entry.getKey())));
            resSI.put(entry.getKey(), tmpMap);

            Map<String, Object> tmpMapSMT = new HashMap<>();
            if (inputSMT.get(entry.getKey()) != null){
                tmpMapSMT.put("input", inputSMT.get(entry.getKey()));
            }else{
                tmpMapSMT.put("input", 0);
            }
            if (outputSMT.get(entry.getKey()) != null){
                tmpMapSMT.put("output", outputSMT.get(entry.getKey()));
            }else{
                tmpMapSMT.put("output", 0);
            }
            tmpMapSMT.put("remain",(inputSMT.get(entry.getKey()) == null ? 0 : inputSMT.get(entry.getKey())) - (outputSMT.get(entry.getKey()) == null ? 0 : outputSMT.get(entry.getKey())));
            resSMT.put(entry.getKey(), tmpMapSMT);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("SI", resSI);
        result.put("SMT", resSMT);
        return result;
    }
    private Map<String, Integer> fromListObjectToMap(List<Object[]> listData){
        Map<String, Integer> result = new HashMap<>();
        if (listData.size() > 0){
            for (Object[] value : listData){
                result.put((String) value[0],(Integer) value[1]);
            }
        }
        return result;
    }
    private Map<String, Integer> fromListObjectToMapOutput(List<Object[]> listData){
        Map<String, Integer> result = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        if (listData.size() > 0){
            for (Object[] value : listData){
                String dateToString = simpleDateFormat.format((Date) value[0]);
                result.put(dateToString,(Integer) value[1]);
            }
        }
        return result;
    }

    @RequestMapping("/input/data")
    public ListResponse<B04RepairCheckIn> getInputData(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<B04RepairCheckIn> inputList = repairCheckInRepository.findByInputTimeBetween(dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());
        return ListResponse.success(inputList);
    }

    @RequestMapping("/output/section")
    public Map<String, SortableMapResponse<Integer>> getOutputBySection(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = testRepairSerialErrorService.countByModelNameAndSectionNameAndStatus(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String modelName = (String) value[0];
            String sectionName = (String) value[1];
            TestRepairSerialError.Status status = (TestRepairSerialError.Status) value[2];
            Long count = (Long) value[3];

            if (status != TestRepairSerialError.Status.REPAIRED) {
                continue;
            }

            SortableMapResponse<Integer> map = result.get(sectionName);
            if (map == null) {
                map = new SortableMapResponse<>();
                result.put(sectionName, map);
            }

            map.put(modelName, map.get(modelName, 0) + count.intValue());
            map.setSize(map.getSize() + count.intValue());
        }

        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });
        return result;
    }

    @RequestMapping("/output/defected/process")
    public Map<String, SortableMapResponse<Integer>> getOutputProcess(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = testRepairSerialErrorService.countByReasonCodeAndLocationCode(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        Map<String, SortableMapResponse<Integer>> result = new LinkedHashMap<>();

        for (Object[] value : data) {
            String errorCode = (String) value[0];
            String reason = (String) value[1];
            String locationCode = (String) value[2];
            String modelName = (String) value[3];
            Long count = (Long) value[4];

            if (StringUtils.isEmpty(reason)) {
                continue;
            }

            switch (reason) {
                case "H003":
                    break;
                case "B000":
                    break;
                case "C001":
                    break;
                default:
                    SortableMapResponse<Integer> map = result.get(reason);
                    if (map == null) {
                        map = new SortableMapResponse<>();
                        result.put(reason, map);
                    }

                    map.put(locationCode, map.get(locationCode, 0) + count.intValue());
                    map.setSize(map.getSize() + count.intValue());
            }
        }

        result = result.entrySet().stream()
                .sorted((v1, v2) -> {
                    if (v1.getValue().getSize() > v2.getValue().getSize()) {
                        return -1;
                    } else if (v1.getValue().getSize() < v2.getValue().getSize()) {
                        return 1;
                    }
                    return 0;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        result.forEach((key, value) -> {
            value.sort(true);
            value.convert();
        });
        return result;
    }

    @RequestMapping("/output/defected/ntf")
    public SortableMapResponse<Integer> getOutputNTF(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = testRepairSerialErrorService.countByReasonCodeAndLocationCode(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        SortableMapResponse<Integer> result = new SortableMapResponse<>();

        for (Object[] value : data) {
            String errorCode = (String) value[0];
            String reason = (String) value[1];
            String locationCode = (String) value[2];
            String modelName = (String) value[3];
            Long count = (Long) value[4];

            switch (reason) {
                case "H003":
                    result.put(errorCode, result.get(errorCode, 0) + count.intValue());
                    result.setSize(result.getSize() + count.intValue());
                    break;
                case "B000":
                    break;
                case "C001":
                    break;
                default:

            }
        }

        result.sort(true);
        result.convert();
        return result;
    }

    @RequestMapping("/output/defected/component")
    public SortableMapResponse<Integer> getOutputComponent(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));

        List<Object[]> data = testRepairSerialErrorService.countByReasonCodeAndLocationCode(factory, dailyTimeSpan.getStartDate(), dailyTimeSpan.getEndDate());

        SortableMapResponse<Integer> result = new SortableMapResponse<>();

        for (Object[] value : data) {
            String errorCode = (String) value[0];
            String reason = (String) value[1];
            String locationCode = (String) value[2];
            String modelName = (String) value[3];
            Long count = (Long) value[4];

            switch (reason) {
                case "H003":
                    break;
                case "B000":
                    result.put(locationCode, result.get(locationCode, 0) + count.intValue());
                    result.setSize(result.getSize() + count.intValue());
                    break;
                case "C001":
                    break;
                default:

            }
        }

        result.sort(true);
        result.convert();
        return result;
    }

    @RequestMapping("/status/daily/model")
    public Map<String, Map<String, RepairIODaily>> getRepairDailyStatusByModel(String factory, String timeSpan) {
        TimeSpan dailyTimeSpan = TimeSpan.from(timeSpan, TimeSpan.now(TimeSpan.Type.FULL));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dailyTimeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        dailyTimeSpan.setStartDate(calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        Map<String, Map<String, RepairIODaily>> result = TestUtils.getWeeklyMap(calendar.getTime(), dailyTimeSpan.getEndDate());

        List<RepairIODaily> repairIODailyList = repairIODailyService.findByFactoryAndInputTimeBetween(factory, TestRepairSerialError.Status.REPAIRED, calendar.getTime(), dailyTimeSpan.getEndDate());

        Map<String, Map<String, RepairIODaily>> repairDaily = repairIODailyList.stream()
                .collect(Collectors.groupingBy(
                        repair -> {
                            calendar.setTime(repair.getStartDate());
                            TimeSpan ts = TimeSpan.from(calendar, TimeSpan.Type.DAILY);
                            return df.format(ts.getStartDate());
                        },
                        Collectors.toMap(
                                RepairIODaily::getModelName,
                                repair -> {
                                    RepairIODaily tmp = new RepairIODaily();
                                    BeanUtils.copyProperties(repair, tmp);
                                    return tmp;
                                }, (repair, tmp) -> {
                                    repair.setTotal(repair.getTotal() + tmp.getTotal());
                                    repair.setInput(repair.getInput() + tmp.getInput());
                                    repair.setOutput(repair.getOutput() + tmp.getOutput());
                                    repair.setRemain(repair.getRemain() + tmp.getRemain());
                                    return repair;
                                }, HashMap::new)));
        log.info("pause");
        repairDaily.forEach(result::replace);

        return result;
    }

    private <T extends Comparable<? super T>> Map<String, T> sortMapByValue(Map<String, T> map) {
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    @GetMapping("/8s/introduce/getdata")
    public Object re8sIntroduce() {
        return repair8sService.re8sIntroduceGetdata();
    }

    @GetMapping("/8s/checklist/by/daily/getdata")
    public Object re8sChecklistByDailyGetdata(@RequestParam String owner) {
        return repair8sService.re8sChecklistByDailyGetdata(owner, TimeSpan.now(TimeSpan.Type.FULL));
    }

    @PostMapping("/8s/checklist/by/daily/savedata")
    public Object re8sChecklistByDailySavedata(@RequestBody String jsonString) throws IOException {
        return repair8sService.re8sChecklistByDailySavedata(jsonString);
    }

    @GetMapping("/8s/leaderconfirm/by/daily/getdata")
    public Object re8sLeaderconfirmByDailyGetData(@RequestParam String leaderId, @RequestParam String owner) {
        return repair8sService.re8sLeaderconfirmByDailyGetdata(leaderId, owner, TimeSpan.now(TimeSpan.Type.FULL));
    }

    @PostMapping("/8s/leaderconfirm/by/daily/savedata")
    public Object re8sLeaderconfirmByDailySavedata(@RequestBody String jsonString) throws IOException {
        return repair8sService.re8sLeaderconfirmByDailySavedata(jsonString);
    }

    @GetMapping("/8s/checklist/status")
    public Object re8sChecklistStatus() {
        return repair8sService.re8sChecklistStatus();
    }

    @GetMapping("/8s/checklist/report")
    public Object re8sChecklistReport(@RequestParam(required = false, defaultValue = "") String startDate, @RequestParam(required = false, defaultValue = "") String endDate) {
        return repair8sService.re8sChecklistReport(startDate, endDate);
    }

    @GetMapping("/8s/checklist/daily/report")
    public Object re8sChecklistDailyReport() {
        return repair8sService.re8sChecklistDailyReport();
    }

    @GetMapping("/8s/user/getconfig")
    public Object re8sUserGetconfig(@RequestParam String userId) {
        return repair8sService.re8sUserGetconfig(userId);
    }

    @PostMapping("/8s/user/saveconfig")
    public Object re8sUserSaveconfig(@RequestBody String jsonString) throws IOException {
        return repair8sService.re8sUserSaveconfig(jsonString);
    }

    @GetMapping("/8s/shift/list")
    public Object re8sShiftList() {
        return repair8sShiftRepository.findAll();
    }

    @GetMapping("/8s/location/list")
    public Object re8sLocationList() {
        return repair8sLocationsRepository.findAll();
    }

    @PostMapping("/8s/user/saveupdateconfig")
    public Object re8sUserSaveUpdateconfig(@RequestBody String jsonString) throws IOException {
        return repair8sService.re8sUserSaveUpdateconfig(jsonString);
    }

    @GetMapping("/8s/user/listdatadetail")
    public List<Map<String, Object>> re8sUserGetDataDeatilowner(){
        return repair8sService.re8sUserGetListDataDetail();
    }

    @GetMapping("/capacity/total")
    public Object reCapacityGetTotal( @RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairCapacityService.getTotalSiSmtRcviCapacity(factory, timeSpan);
    }

    @GetMapping("/capacity/datashift")
    public Object reCapacityGetDataShift(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException{
        return repairCapacityService.getTotalSiSmtRcviCapacityByShift(factory, timeSpan);
    }

    @GetMapping("/capacity/databymonth")
    public Map<String, Object> reCapacityGetDataUserByMonth(@RequestParam String factory) throws ParseException{
        return repairCapacityService.getDataUserByMonth(factory);
    }

    @GetMapping("/capacity/listuser/export")
    public ResponseEntity<Resource> reListUserExport() throws IOException, ParserConfigurationException, SAXException, ParseException {
        File file = repairCapacityService.listUserExport("WorkCount.xls");

        long fileLength = file.length();
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        file.delete();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=workCount.xls");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/datainoutput")
    public  Map<String, Object> testStream(@RequestParam String startDate, @RequestParam String endDate) throws ParseException {
       //  repairCapacityService.leanrTestStream();
        return repairOnlineWipService.getDataInputOutput(startDate, endDate);
    }

    @GetMapping("/repairweekly")
    public Map<String,Map<String, Integer>> repairWeeklyStatus() throws ParseException {
         return  repairOnlineWipService.getDataRepairWeeklyStatus();
    }

    @GetMapping("/importdatabymodelname")
    public void importDataByModelName(HttpServletResponse resonse, @RequestParam String modelName) throws ParseException, IOException {
          repairOnlineWipService.getDataDetailModelName(resonse, modelName);
    }

}
