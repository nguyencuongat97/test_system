package com.foxconn.fii.receiver.re.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.b04.model.B04Resource;
import com.foxconn.fii.data.b04.repository.B04ResourceRepository;
import com.foxconn.fii.data.primary.model.entity.*;
import com.foxconn.fii.data.primary.repository.*;
import com.foxconn.fii.receiver.re.service.Repair8sService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Repair8sServiceImpl implements Repair8sService {

    @Autowired
    private Repair8sCategoriesRepository repair8sCategoriesRepository;

    @Autowired
    private Repair8sFormsRepository repair8sFormsRepository;

    @Autowired
    private Repair8sFormStepsRepository repair8sFormStepsRepository;

    @Autowired
    private Repair8sStepsRepository repair8sStepsRepository;

    @Autowired
    private Repair8sUsersRepository repair8sUsersRepository;

    @Autowired
    private Repair8sLocationsRepository repair8sLocationsRepository;

    @Autowired
    private Repair8sUsersLocationsRepository repair8sUsersLocationsRepository;

    @Autowired
    private Repair8sRolesRepository repair8sRolesRepository;

    @Autowired
    private Repair8sShiftRepository repair8sShiftRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private B04ResourceRepository b04ResourceRepository;

    @Override
    public Object re8sIntroduceGetdata(){

        List<Repair8sCategories> categoriesList = repair8sCategoriesRepository.findAllByOrderByNumberAsc();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Repair8sCategories categories : categoriesList) {
            Map<String, Object> map = new HashMap<>();
            List<Repair8sSteps> stepsList = repair8sStepsRepository.findByIdCateOrderByNumberAsc(categories.getId());
            map.put("name", categories.getName());
            map.put("title", categories.getTitle());
            map.put("number", categories.getNumber());
            map.put("description", categories.getDescription());
            map.put("stepsList", stepsList);
            result.add(map);
        }
        return result;
    }

    @Override
    public Object re8sChecklistByDailyGetdata(String owner, TimeSpan timeSpan) {
        Map<String, Object> result = new LinkedHashMap<>();
        long idOwner = 0;
        long idLocation = 0;
        long idShift = 0;
        result.put("owner", owner);
        result.put("data", Collections.EMPTY_MAP);
        Page<Map<String, Object>> usersLocationsPage = repair8sUsersLocationsRepository.jpqlGetUserConfig(owner, PageRequest.of(0, 1));
        if (usersLocationsPage.getTotalElements() == 0 || usersLocationsPage.getContent().get(0).get("location") == null || usersLocationsPage.getContent().get(0).get("shift") == null) {
            result.put("ownerLogin", false);
            return result;
        } else {
            result.put("ownerLogin", true);
            idOwner = ((Repair8sUsers)usersLocationsPage.getContent().get(0).get("user")).getId();
            idLocation = ((Repair8sLocations)usersLocationsPage.getContent().get(0).get("location")).getId();
            idShift = ((Repair8sShift)usersLocationsPage.getContent().get(0).get("shift")).getId();
        }
        String location = ((Repair8sLocations)usersLocationsPage.getContent().get(0).get("location")).getLocation();
        result.put("location", location);
        Page<Repair8sForms> formsPage = repair8sFormsRepository.jpqlGetFormsBetween(owner, timeSpan.getStartDate(), timeSpan.getEndDate(), PageRequest.of(0, 1));
        List<Repair8sFormSteps> formStepsList = new ArrayList<>();
        Repair8sForms forms;
        if (formsPage.isEmpty()) {
            forms = new Repair8sForms();
//            forms.setOwner(owner);
            forms.setIdUserOwner(idOwner);
//            forms.setLocation(location);
            forms.setIdLocation(idLocation);
            forms.setIdShift(idShift);
            repair8sFormsRepository.save(forms);
            List<Repair8sCategories> categoriesList = repair8sCategoriesRepository.findAllByOrderByNumberAsc();
            for (Repair8sCategories categories : categoriesList) {
                List<Repair8sSteps> stepsList = repair8sStepsRepository.findByIdCateOrderByNumberAsc(categories.getId());
                for (Repair8sSteps steps : stepsList) {
                    Repair8sFormSteps formSteps = new Repair8sFormSteps();
                    formSteps.setIdForm(forms.getId());
                    formSteps.setIdStep(steps.getId());
                    formSteps.setConfirm(0);
                    formSteps.setCheck(0);
                    formStepsList.add(formSteps);
                }
            }
            repair8sFormStepsRepository.saveAll(formStepsList);
        } else {
            forms = formsPage.getContent().get(0);
        }

        Map<Integer, Map<String, Object>> dataResult = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = repair8sFormStepsRepository.jpqlGetFormStepsByIdForm(forms.getId());
        for (Map<String, Object> dataMap : dataList) {
            Integer cateNumber = (Integer) dataMap.get("cateNumber");
            String cateName = (String) dataMap.get("cateName");
            String cateTitle = (String) dataMap.get("cateTitle");
            Integer stepsNumber = (Integer) dataMap.get("stepsNumber");
            String stepsDescription = (String) dataMap.get("stepsDescription");
            Long formStepsId = (Long) dataMap.get("formStepsId");
            Integer formStepsConfirm = (Integer) dataMap.get("formStepsConfirm");

            if (dataResult.containsKey(cateNumber)) {
                Map<String, Object> map = new HashMap<>();
                map.put("number", stepsNumber);
                map.put("description", stepsDescription);
                map.put("confirm", formStepsConfirm);
                map.put("id", formStepsId);
                ((List) dataResult.get(cateNumber).get("list")).add(map);
            } else {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("number", stepsNumber);
                map.put("description", stepsDescription);
                map.put("confirm", formStepsConfirm);
                map.put("id", formStepsId);
                list.add(map);
                Map<String, Object> mapCate = new HashMap<>();
                mapCate.put("list", list);
                mapCate.put("name", cateName);
                mapCate.put("title", cateTitle);
                dataResult.put(cateNumber, mapCate);
            }
        }

        result.put("data", dataResult);
        return result;
    }

    @Override
    public Object re8sChecklistByDailySavedata(String jsonString) throws IOException {
        List<Map<String, Object>> list = objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});
        for (Map<String, Object> map : list) {
            Optional<Repair8sFormSteps> formStepsOptional = repair8sFormStepsRepository.findById(Long.parseLong(map.get("id").toString()));
            if (formStepsOptional.isPresent()) {
                Integer confirm = Integer.parseInt(map.get("confirm").toString());
                if (confirm >= 1) {
                    confirm = 1;
                } else {
                    confirm = 0;
                }
                formStepsOptional.get().setConfirm(confirm);
                repair8sFormStepsRepository.save(formStepsOptional.get());
            }
        }
        return true;
    }

    @Override
    public Object re8sLeaderconfirmByDailyGetdata(String leaderId, String owner, TimeSpan timeSpan) {
        Map<String, Object> result = new LinkedHashMap<>();
        long idOwner = 0;
        long idLocation = 0;
        long idShift = 0;
        result.put("leaderId", leaderId);
        result.put("owner", owner);
        result.put("data", Collections.EMPTY_MAP);
        Page<Map<String, Object>> repair8sRolesPage = repair8sRolesRepository.jpqlGetUserRoleByUserId(leaderId, PageRequest.of(0, 1));
        Repair8sRoles roles = (Repair8sRoles) repair8sRolesPage.getContent().get(0).get("role");

        if (roles == null || !(roles.getName().equalsIgnoreCase("leader") || roles.getName().equalsIgnoreCase("auditor"))) {
            result.put("leaderLogin", false);
            result.put("ownerLogin", false);
            return result;
        } else {
            result.put("leaderLogin", true);
        }

        Page<Map<String, Object>> usersLocationsPage = repair8sUsersLocationsRepository.jpqlGetUserConfig(owner, PageRequest.of(0, 1));
        if (usersLocationsPage.getTotalElements() == 0) {
            result.put("ownerLogin", false);
            return result;
        } else {
            result.put("ownerLogin", true);
            idOwner = ((Repair8sUsers)usersLocationsPage.getContent().get(0).get("user")).getId();
            idLocation = ((Repair8sLocations)usersLocationsPage.getContent().get(0).get("location")).getId();
            idShift = ((Repair8sShift)usersLocationsPage.getContent().get(0).get("shift")).getId();
        }
        String location = ((Repair8sLocations)usersLocationsPage.getContent().get(0).get("location")).getLocation();
        result.put("location", location);

        Page<Repair8sForms> formsPage = repair8sFormsRepository.jpqlGetFormsBetween(owner, timeSpan.getStartDate(), timeSpan.getEndDate(), PageRequest.of(0, 1));
        List<Repair8sFormSteps> formStepsList = new ArrayList<>();
        Repair8sForms forms;
        if (formsPage.isEmpty()) {
            forms = new Repair8sForms();
//            forms.setOwner(owner);
            forms.setIdUserOwner(idOwner);
//            forms.setLocation(location);
            forms.setIdLocation(idLocation);
            forms.setIdShift(idShift);
            repair8sFormsRepository.save(forms);
            List<Repair8sCategories> categoriesList = repair8sCategoriesRepository.findAllByOrderByNumberAsc();
            for (Repair8sCategories categories : categoriesList) {
                List<Repair8sSteps> stepsList = repair8sStepsRepository.findByIdCateOrderByNumberAsc(categories.getId());
                for (Repair8sSteps steps : stepsList) {
                    Repair8sFormSteps formSteps = new Repair8sFormSteps();
                    formSteps.setIdForm(forms.getId());
                    formSteps.setIdStep(steps.getId());
                    formSteps.setConfirm(0);
                    formSteps.setCheck(0);
                    formStepsList.add(formSteps);
                }
            }
            repair8sFormStepsRepository.saveAll(formStepsList);
        } else {
            forms = formsPage.getContent().get(0);
        }

        Map<Integer, Map<String, Object>> dataResult = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = repair8sFormStepsRepository.jpqlGetFormStepsByIdForm(forms.getId());
        for (Map<String, Object> dataMap : dataList) {
            Integer cateNumber = (Integer) dataMap.get("cateNumber");
            String cateName = (String) dataMap.get("cateName");
            String cateTitle = (String) dataMap.get("cateTitle");
            Integer stepsNumber = (Integer) dataMap.get("stepsNumber");
            String stepsDescription = (String) dataMap.get("stepsDescription");
            Long formStepsId = (Long) dataMap.get("formStepsId");
            String formStepsDescription = (String) dataMap.get("formStepsDescription");
            Integer formStepsCheck = (Integer) dataMap.get("formStepsCheck");

            if (dataResult.containsKey(cateNumber)) {
                Map<String, Object> map = new HashMap<>();
                map.put("number", stepsNumber);
                map.put("description", stepsDescription);
                map.put("confirm", formStepsCheck);
                map.put("id", formStepsId);
                map.put("content", formStepsDescription);
                ((List) dataResult.get(cateNumber).get("list")).add(map);
            } else {
                List<Map<String, Object>> list = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                map.put("number", stepsNumber);
                map.put("description", stepsDescription);
                map.put("confirm", formStepsCheck);
                map.put("id", formStepsId);
                map.put("content", formStepsDescription);
                list.add(map);
                Map<String, Object> mapCate = new HashMap<>();
                mapCate.put("list", list);
                mapCate.put("name", cateName);
                mapCate.put("title", cateTitle);
                dataResult.put(cateNumber, mapCate);
            }
        }

        result.put("data", dataResult);
        return result;

    }

    @Override
    public Object re8sLeaderconfirmByDailySavedata(String jsonString) throws IOException {
        List<Map<String, Object>> list = objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});
        List<Repair8sFormSteps> formStepsList = new ArrayList<>();
        Long idForm = 0L;
        String leaderId = "";
        for (Map<String, Object> map : list) {
            Optional<Repair8sFormSteps> formStepsOptional = repair8sFormStepsRepository.findById(Long.parseLong(map.get("id").toString()));
            if (formStepsOptional.isPresent()) {
                Integer confirm = Integer.parseInt(map.get("confirm").toString());
                if (confirm >= 3) {
                    confirm = 3;
                } else if (confirm < 0){
                    confirm = 0;
                }
                formStepsOptional.get().setCheck(confirm);
                formStepsList.add(formStepsOptional.get());
                idForm = formStepsOptional.get().getIdForm();
                leaderId = map.get("leaderId").toString();
                String description = map.get("description").toString();
                formStepsOptional.get().setDescription(description);
            }
        }
        if (idForm > 0 && !StringUtils.isEmpty(leaderId)) {
            Optional<Repair8sForms> formsOptional = repair8sFormsRepository.findById(idForm);
            Page<Map<String, Object>> repair8sRolesPage = repair8sRolesRepository.jpqlGetUserRoleByUserId(leaderId, PageRequest.of(0, 1));
            Repair8sRoles roles = (Repair8sRoles) repair8sRolesPage.getContent().get(0).get("role");

            if (roles == null || !(roles.getName().equalsIgnoreCase("leader") || roles.getName().equalsIgnoreCase("auditor"))) {
                return false;
            }
            Repair8sUsers repair8sUsers = (Repair8sUsers) repair8sRolesPage.getContent().get(0).get("user");
            formsOptional.get().setIdUserLeader(repair8sUsers.getId());
            repair8sFormsRepository.save(formsOptional.get());
            repair8sFormStepsRepository.saveAll(formStepsList);
        }
        return true;
    }

    @Override
    public Object re8sChecklistStatus() {
        List<Map<String, Object>> userLocationList = repair8sUsersLocationsRepository.jpqlGetAllUserConfig();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        Date date = new Date();

        List<Map<String, Object>> resultData = new ArrayList<>();
        for (Map<String, Object> map : userLocationList) {
            long idUserOwner = ((Repair8sUsers) map.get("user")).getId();
            String userIdOwner = ((Repair8sUsers) map.get("user")).getUserId();
            String fullName = ((Repair8sUsers) map.get("user")).getFullName();
            long idLocation = ((Repair8sLocations) map.get("location")).getId();
            String location = ((Repair8sLocations) map.get("location")).getLocation();
            long idShift = ((Repair8sShift) map.get("shift")).getId();
            String shift = ((Repair8sShift) map.get("shift")).getShift();

            List<Map<String, Object>> statusList = new ArrayList<>();
            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.setTime(calendar.getTime());
            List<Repair8sFormSteps> formStepsList = repair8sFormStepsRepository.jpqlGetFormStepsByIdUserOwnerAndIdLocationAndIdShift(idUserOwner, idLocation, idShift, tmpCalendar.getTime(), date);
            while (tmpCalendar.getTime().before(date)) {
                int status = 1; // White ~ non of its bussiness
                TimeSpan tmpTimeSpan = TimeSpan.from(tmpCalendar, TimeSpan.Type.DAILY);
                if (shift.equalsIgnoreCase(tmpTimeSpan.getShiftType().name())) {
                    TimeSpan fullTimeSpan = TimeSpan.from(tmpCalendar, TimeSpan.Type.FULL);
                    List<Repair8sFormSteps> tmpRepair8sFormStepsList = formStepsList.stream().filter(e -> e.getCreatedAt().after(fullTimeSpan.getStartDate()) && e.getCreatedAt().before(fullTimeSpan.getEndDate())).collect(Collectors.toList());
                    if (tmpRepair8sFormStepsList.isEmpty()) {
                        status = 2; // Yellow ~ not check yet
                    } else {
                        tmpRepair8sFormStepsList = tmpRepair8sFormStepsList.stream().filter(e -> e.getConfirm() != 0).collect(Collectors.toList());
                        if (tmpRepair8sFormStepsList.isEmpty()) {
                            status = 2; // Yellow ~ not check yet
                        } else {
                            status = 0; // Green ~ OK
                        }
                    }
                    if (status > 0 && !tmpTimeSpan.getStartDate().equals(timeSpan.getStartDate())) {
                        status = 3; // Red ~ not check
                    }
                }
                Map<String, Object> tmpStatus = new HashMap<>();
                tmpStatus.put("date", simpleDateFormat.format(tmpCalendar.getTime()));
                tmpStatus.put("shiftType", tmpTimeSpan.getShiftType());
                tmpStatus.put("status", status);
                statusList.add(tmpStatus);
                tmpCalendar.add(Calendar.HOUR_OF_DAY, 12);
            }

            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("idUser", idUserOwner);
            tmpMap.put("userId", userIdOwner);
            tmpMap.put("fullName", fullName);
            tmpMap.put("idLocation", idLocation);
            tmpMap.put("location", location);
            tmpMap.put("idShift", idShift);
            tmpMap.put("shift", shift);
            tmpMap.put("statusList", statusList);
            resultData.add(tmpMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", resultData);
        return result;
    }

    @Override
    public Object re8sChecklistReport(String startDateStr, String endDateStr) {
        TimeSpan timeSpan;
        if (StringUtils.isEmpty(startDateStr) || StringUtils.isEmpty(endDateStr)) {
            timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.setTime(timeSpan.getStartDate());
            tmpCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            tmpCal.set(Calendar.HOUR_OF_DAY, 7);
            if (tmpCal.getTime().after(timeSpan.getEndDate())) {
                tmpCal.add(Calendar.DAY_OF_YEAR, -7);
            }
            timeSpan.setStartDate(tmpCal.getTime());
        } else {
            timeSpan = TimeSpan.from(startDateStr + " - " + endDateStr);
        }

        List<Map<String, Object>> rawData = repair8sFormsRepository.jpqlGetFormStepsAndFormsAndUserAndConfig(timeSpan.getStartDate(), timeSpan.getEndDate());

        Map<Long, Map<String, Object>> checkListControl = new HashMap<>();
        Map<Long, Map<String, Object>> topBad = new HashMap<>();
        Map<Long, Map<String, Object>> topGood = new HashMap<>();

        rawData.forEach(e -> {
            long idUser = ((Repair8sUsers) e.get("user")).getId();
            String fullName = ((Repair8sUsers) e.get("user")).getFullName();
            long idForm = 0;
            if (e.get("forms") != null) {
                idForm = ((Repair8sForms) e.get("forms")).getId();
            }
            int confirm = 0;
            int check = 0;
            if (e.get("formSteps") != null) {
                confirm = ((Repair8sFormSteps) e.get("formSteps")).getConfirm();
                check = ((Repair8sFormSteps) e.get("formSteps")).getCheck();
            }
            String description = "";
            if (e.get("steps") != null) {
                description = ((Repair8sSteps) e.get("steps")).getDescription();
            }
            if (checkListControl.containsKey(idUser) && confirm > 0) {
                Map<String, Object> tmpMap = checkListControl.get(idUser);
                HashSet<Long> tmpFormsSet = (HashSet) tmpMap.get("formsSet");
                tmpMap.put("formsCount", tmpFormsSet.size());
                tmpFormsSet.add(idForm);
            } else if (confirm > 0){
                Map<String, Object> tmpMap = new HashMap<>();
                HashSet<Long> tmpFormsSet = new HashSet<>();
                tmpFormsSet.add(idForm);
                tmpMap.put("formsSet", tmpFormsSet);
                tmpMap.put("fullName", fullName);
                tmpMap.put("formsCount", tmpFormsSet.size());
                checkListControl.put(idUser, tmpMap);
            } else if (!checkListControl.containsKey(idUser)) {
                Map<String, Object> tmpMap = new HashMap<>();
                HashSet<Long> tmpFormsSet = new HashSet<>();
                tmpMap.put("formsSet", tmpFormsSet);
                tmpMap.put("fullName", fullName);
                tmpMap.put("formsCount", tmpFormsSet.size());
                checkListControl.put(idUser, tmpMap);
            }

            if (topBad.containsKey(idUser) && check == 2) {
                Map<String, Object> tmpMap = topBad.get(idUser);
                int badCount = (int) tmpMap.get("badCount");
                tmpMap.put("badCount", badCount + 1);
                HashSet<String> tmpSet = (HashSet) tmpMap.get("reasonList");
                tmpSet.add(description);
            } else if (check == 2) {
                Map<String, Object> tmpMap = new HashMap<>();
                HashSet<String> tmpSet = new HashSet<>();
                tmpSet.add(description);
                tmpMap.put("badCount", 1);
                tmpMap.put("fullName", fullName);
                tmpMap.put("reasonList", tmpSet);
                topBad.put(idUser, tmpMap);
            } else if (!topBad.containsKey(idUser)) {
                Map<String, Object> tmpMap = new HashMap<>();
                HashSet<String> tmpSet = new HashSet<>();
                tmpMap.put("badCount", 0);
                tmpMap.put("fullName", fullName);
                tmpMap.put("reasonList", tmpSet);
                topBad.put(idUser, tmpMap);
            }

            if (topGood.containsKey(idUser) && check == 3) {
                Map<String, Object> tmpMap = topGood.get(idUser);
                int goodCount = (int) tmpMap.get("goodCount");
                tmpMap.put("goodCount", goodCount + 1);
            } else if (check == 3) {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("goodCount", 1);
                tmpMap.put("fullName", fullName);
                topGood.put(idUser, tmpMap);
            } else if (!topGood.containsKey(idUser)) {
                Map<String, Object> tmpMap = new HashMap<>();
                tmpMap.put("goodCount", 0);
                tmpMap.put("fullName", fullName);
                topGood.put(idUser, tmpMap);
            }

        });

        List<Map<String, Object>> checkListControlResult = checkListControl.entrySet().stream()
                .sorted((e1, e2) -> {
                    int c1 = (int) e1.getValue().get("formsCount");
                    int c2 = (int) e2.getValue().get("formsCount");
                    if (c1 > c2) {
                        return -1;
                    } else if (c1 < c2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).map(e -> {
                    Map<String, Object> tmpMap = new HashMap<>();
                    tmpMap.put("fullName", e.getValue().get("fullName"));
                    tmpMap.put("formsCount", e.getValue().get("formsCount"));
                    return tmpMap;
                }).collect(Collectors.toList());

        List<Map<String, Object>> topBadResult = topBad.entrySet().stream()
                .sorted((e1, e2) -> {
                    int c1 = (int) e1.getValue().get("badCount");
                    int c2 = (int) e2.getValue().get("badCount");
                    if (c1 > c2) {
                        return -1;
                    } else if (c1 < c2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).map(e -> {
                    Map<String, Object> tmpMap = new HashMap<>();
                    tmpMap.put("fullName", e.getValue().get("fullName"));
                    tmpMap.put("badCount", e.getValue().get("badCount"));
                    tmpMap.put("reasonList", e.getValue().get("reasonList"));
                    return tmpMap;
                }).collect(Collectors.toList());

        List<Map<String, Object>> topGoodResult = topGood.entrySet().stream()
                .sorted((e1, e2) -> {
                    int c1 = (int) e1.getValue().get("goodCount");
                    int c2 = (int) e2.getValue().get("goodCount");
                    if (c1 > c2) {
                        return -1;
                    } else if (c1 < c2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).map(e -> {
                    Map<String, Object> tmpMap = new HashMap<>();
                    tmpMap.put("fullName", e.getValue().get("fullName"));
                    tmpMap.put("goodCount", e.getValue().get("goodCount"));
                    return tmpMap;
                }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("topGood", topGoodResult);
        result.put("topbad", topBadResult);
        result.put("checklistControl", checkListControlResult);

        return result;
    }

    @Override
    public Object re8sChecklistDailyReport() {
        Map<String, Object> result = new HashMap<>();
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.add(Calendar.HOUR_OF_DAY, -12);
        timeSpan = TimeSpan.of(calendar.getTime(), timeSpan.getStartDate());

        List<Map<String, Object>> resultData = new ArrayList<>();
        List<Map<String, Object>> userLocationList = repair8sUsersLocationsRepository.jpqlGetAllUserLocation();
        for (Map<String, Object> map : userLocationList) {
            long idUser = (long) map.get("idUser");
            String userId = map.get("userId").toString();
            String fullName = map.get("fullName").toString();
            long idLocation = (long) map.get("idLocation");
            String location = map.get("location").toString();

            List<Map<String, Object>> formStepsList = repair8sFormStepsRepository.jpqlGetFormsAndFormStepsByOwnerAndLocation(userId, location, timeSpan.getStartDate(), timeSpan.getEndDate());
            int checked = formStepsList.stream().filter(e -> (int) e.get("confirm") > 0).collect(Collectors.toList()).size();
            int good = formStepsList.stream().filter(e -> (int) e.get("check") == 3).collect(Collectors.toList()).size();
            int bad = formStepsList.stream().filter(e -> (int) e.get("check") == 2).collect(Collectors.toList()).size();

            List<String> badList = formStepsList.stream().filter(e -> (int) e.get("check") == 2).map(e -> e.get("description").toString()).collect(Collectors.toList());

            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("idUser", idUser);
            tmpMap.put("userId", userId);
            tmpMap.put("fullName", fullName);
            tmpMap.put("idLocation", idLocation);
            tmpMap.put("location", location);
            tmpMap.put("checked", checked);
            tmpMap.put("good", good);
            tmpMap.put("bad", bad);
            tmpMap.put("badList", badList);
            resultData.add(tmpMap);
        }
        resultData = resultData.stream()
                .sorted((e1, e2) -> {
                    if ((int)e1.get("checked") > (int)e2.get("checked")) {
                        return -1;
                    } else if ((int)e1.get("checked") < (int)e2.get("checked")) {
                        return 1;
                    } else {
                        if ((int)e1.get("good") > (int)e2.get("good")) {
                            return -1;
                        } else if ((int)e1.get("good") < (int)e2.get("good")) {
                            return 1;
                        } else {
                            if ((int)e1.get("bad") > (int)e2.get("bad")) {
                                return 1;
                            } else if ((int)e1.get("bad") < (int)e2.get("bad")) {
                                return -1;
                            }
                        }
                    }
                    return 0;
                })
                .collect(Collectors.toList());
        List<Map<String, Object>> topGood = new ArrayList<>();
        List<Map<String, Object>> topBad = new ArrayList<>();
        if (resultData.size() > 3) {
            Map<String, Object> good = resultData.get(2);
            Map<String, Object> bad = resultData.get(resultData.size() - 3);
            for (int i = 0; i <= resultData.size() - 1; i++) {
                if (i <= 2) {
                    topGood.add(resultData.get(i));
                    topBad.add(resultData.get(resultData.size() - 1 - i));
                } else {
                    if ((int)resultData.get(i).get("checked") == (int)good.get("checked")
                        && (int)resultData.get(i).get("good") == (int)good.get("good")
                        && (int)resultData.get(i).get("bad") == (int)good.get("bad")) {
                        topGood.add(resultData.get(i));
                    }

                    if ((int)resultData.get(resultData.size() - 1 - i).get("checked") == (int)bad.get("checked")
                            && (int)resultData.get(resultData.size() - 1 - i).get("good") == (int)bad.get("good")
                            && (int)resultData.get(resultData.size() - 1 - i).get("bad") == (int)bad.get("bad")) {
                        topBad.add(resultData.get(resultData.size() - 1 - i));
                    }
                }
            }
        }

        result.put("chartData", resultData);
        result.put("topGood", topGood);
        result.put("topBad", topBad);
        return result;
    }

    @Override
    public Object re8sUserGetconfig(String userId) {
        Map<String, Object> result = new HashMap<>();
        Page<Map<String, Object>> pageData = repair8sUsersLocationsRepository.jpqlGetUserConfig(userId, PageRequest.of(0, 1));
        Map<String, Object> resultData = new HashMap<>();

        if (pageData.isEmpty()) {
            Repair8sUsers data = repair8sUsersRepository.findByUserIdOrderByIdDesc(userId);
            if (data == null){
                result.put("login", false);
                result.put("data", resultData);
                return result;
            }else{
               Map<String, Object> userData = new HashMap<>();
               userData.put("shift", "");
               userData.put("location", "");
               userData.put("userId", data.getUserId());
               userData.put("fullName", data.getFullName());
                userData.put("status", data.getIsActive());
                result.put("login", true);
                result.put("data", userData);
                return result;
            }

        } else {
            result.put("login", true);
        }
        resultData.put("userId", ((Repair8sUsers)pageData.getContent().get(0).get("user")).getUserId() );
        resultData.put("fullName", ((Repair8sUsers)pageData.getContent().get(0).get("user")).getFullName());
        resultData.put("status", ((Repair8sUsers)pageData.getContent().get(0).get("user")).getIsActive());
        if (pageData.getContent().get(0).get("location") == null) {
            resultData.put("location", "");
        } else {
            resultData.put("location", ((Repair8sLocations)pageData.getContent().get(0).get("location")).getLocation());
        }
        if ((Repair8sShift)pageData.getContent().get(0).get("shift") == null) {
            resultData.put("shift", "");
        } else {
            resultData.put("shift", ((Repair8sShift)pageData.getContent().get(0).get("shift")).getShift());
        }

        result.put("data", resultData);
        return result;
    }

    @Override
    public Object re8sUserSaveconfig(String jsonString) throws IOException {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> mapData = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
        String userId = (String) mapData.get("userId");
        long idShift = Long.valueOf(mapData.get("idShift").toString());
        long idLocation = Long.valueOf(mapData.get("idLocation").toString());
        Page<Map<String, Object>> pageData = repair8sUsersLocationsRepository.jpqlGetUserConfig(userId, PageRequest.of(0, 1));
        if (pageData.isEmpty() || !repair8sLocationsRepository.findById(idLocation).isPresent() || !repair8sShiftRepository.findById(idShift).isPresent()) {
            result.put("update", false);
            return result;
        }
        Repair8sUsersLocations usersLocations = (Repair8sUsersLocations) pageData.getContent().get(0).get("userLocation");
        usersLocations.setIdShift(idShift);
        usersLocations.setIdLocation(idLocation);
        repair8sUsersLocationsRepository.save(usersLocations);

        result.put("update", true);
        return result;
    }

    @Override
    public Object re8sUserSaveUpdateconfig(String jsonString) throws  IOException{
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> mapData = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
        long type = Long.valueOf(mapData.get("type").toString());
        String fullName = (String) mapData.get("fullName");
        String userId = (String) mapData.get("userId");
        long idShift = Long.valueOf(mapData.get("idShift").toString());
        long idLocation = Long.valueOf(mapData.get("idLocation").toString());
        boolean status = (boolean) mapData.get("status");
        if (type == 2){
            Page<Map<String, Object>> pageData = repair8sUsersLocationsRepository.jpqlGetUserConfig(userId, PageRequest.of(0, 1));
            if (pageData.isEmpty() || !repair8sLocationsRepository.findById(idLocation).isPresent() || !repair8sShiftRepository.findById(idShift).isPresent()) {
                Repair8sUsersLocations repair8sUsersLocations = new Repair8sUsersLocations();
                repair8sUsersLocations.setIdShift(idShift);
                repair8sUsersLocations.setIdLocation(idLocation);
                Repair8sUsers repair8sUsers = repair8sUsersRepository.findByUserIdOrderByIdDesc(userId);
                repair8sUsersLocations.setIdUser(repair8sUsers.getId());
                repair8sUsersLocationsRepository.save(repair8sUsersLocations);

                repair8sUsers.setIsActive(status);
                repair8sUsers.setFullName(fullName);
                repair8sUsersRepository.save(repair8sUsers);
                result.put("update", true);
                return result;
            }
            Repair8sUsersLocations usersLocations = (Repair8sUsersLocations) pageData.getContent().get(0).get("userLocation");
            usersLocations.setIdShift(idShift);
            usersLocations.setIdLocation(idLocation);
            repair8sUsersLocationsRepository.save(usersLocations);
            Repair8sUsers repair8sUsers =(Repair8sUsers) pageData.getContent().get(0).get("user");
            repair8sUsers.setIsActive(status);
            repair8sUsers.setFullName(fullName);
            repair8sUsersRepository.save(repair8sUsers);
            result.put("update", true);
        }

        if (type == 1){
            Repair8sUsers dataRepar = new Repair8sUsers();
            Repair8sUsers repair8sUsers = repair8sUsersRepository.findByUserIdOrderByIdDesc(userId);
            if (repair8sUsers == null){
                dataRepar.setFullName(fullName);
                dataRepar.setUserId(userId);
                dataRepar.setIdRole(type);
                dataRepar.setIsActive(status);
                try {
                    repair8sUsersRepository.save(dataRepar);
                }catch (Exception e){
                    log.info("Error save!"+ e);
                }
            }
            Long idUs = dataRepar.getId();
            Repair8sUsersLocations usersLocationUpdate = new Repair8sUsersLocations();
            usersLocationUpdate.setIdUser(idUs);
            usersLocationUpdate.setIdLocation(idLocation);
            usersLocationUpdate.setIdShift(idShift);
            repair8sUsersLocationsRepository.save(usersLocationUpdate);
            result.put("save", true);
        }
        return result;
    }
    @Override
    public List<Map<String, Object>> re8sUserGetListDataDetail(){
        List<Map<String, Object>> list8sUsers = repair8sUsersRepository.jpqlGetAllDetailUser();
        log.info("pause");
        return list8sUsers;
    }


  //  public void testUser(){
   //     List<B04Resource> data = b04ResourceRepository.findByDem("RE");
//        for (B04Resource value: data) {
//            Repair8sUsers dt = repair8sUsersRepository.findByUserIdOrderByIdDesc(value.getEmployeeNo());
//            if (dt == null){
//                Repair8sUsers dataRepar = new Repair8sUsers();
//                dataRepar.setIsActive(true);
//                dataRepar.setIdRole((long) 1);
//                dataRepar.setUserId(value.getEmployeeNo());
//                dataRepar.setFullName(value.getName());
//                repair8sUsersRepository.save(dataRepar);
//                log.info("pause");
//            }
//            log.info("pause");
//        }
//        List<Repair8sUsers> res = repair8sUsersRepository.findByIdLessThan((long) 35);
//        for (Repair8sUsers tmpRes : res){
//            B04Resource dt = b04ResourceRepository.findByEmployeeNo(tmpRes.getUserId());
//            if (dt == null){
//                tmpRes.setIsActive(false);
//                repair8sUsersRepository.save(tmpRes);
//
//            }
//        }
//        log.info("pause");
//
//    }

}
