package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.b06te.repository.B06TeEquipmentFixtureRepository;
import com.foxconn.fii.data.b06te.repository.TeEFOwnerTypeRepository;
import com.foxconn.fii.receiver.test.service.EquipmentFixtureManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/test/efmanager")
public class TestApiEquipmentFixtureController {

    @Autowired
    private B06TeEquipmentFixtureRepository b06TeEquipmentFixtureRepository;

    @Autowired
    private EquipmentFixtureManagementService equipmentFixtureManagementService;

    @Autowired
    private TeEFOwnerTypeRepository teEFOwnerTypeRepository;

    @GetMapping("/time/shift/now")
    public Object timeShiftNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        timeSpan.getEndDate().setTime(timeSpan.getEndDate().getTime() + 1);

        Map<String, Object> result = new HashMap<>();

        result.put("startTime", sdf.format(timeSpan.getStartDate()));
        result.put("endTime", sdf.format(timeSpan.getEndDate()));

        return result;
    }

    @GetMapping("/time/timespan/add")
    public String vnTimePreset( String timeSpan
                                ,@RequestParam(required = false, defaultValue = "0") int addShift
                                ,@RequestParam(required = false, defaultValue = "0") int addMonth
                                ,@RequestParam(required = false, defaultValue = "0") int addYear) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(sdf.parse(timeSpan.split(" - ")[0]));
        startCal.add(Calendar.HOUR_OF_DAY, addShift * 12);
        startCal.add(Calendar.MONTH, addMonth);
        startCal.add(Calendar.YEAR, addYear);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(sdf.parse(timeSpan.split(" - ")[1]));
        endCal.add(Calendar.HOUR_OF_DAY, addShift * 12);
        endCal.add(Calendar.MONTH, addMonth);
        endCal.add(Calendar.YEAR, addYear);

        return sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
    }

    @GetMapping("/oee/ate/status")
    public Map<String, Object> oeeAteStatus(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06OeeAteStatus();
        }

        return Collections.emptyMap();
    }

    // E%F overall API
    @GetMapping("/equipment/status/sumary")
    public Map<String, Map<String, Object>> equipmentStatusSumary(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String ownerType) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentStatusSumaryV2(ownerType);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/fixture/status/sumary")
    public Map<String, Map<String, Object>> fixtureStatusSumary(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String ownerType) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureStatusSumaryV2(ownerType);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/equipment/performance/trend")
    public Map<String, Map<String, Object>> equipmentPerformanceTrend(
            @RequestParam(required = true) String factory
            , @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException {
        if (factory.equalsIgnoreCase("b06")) {
            return equipmentFixtureManagementService.b06EquipmentPerformanceTrend(timeSpan);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/fixture/performance/trend")
    public Map<String, Map<String, Object>> fixturePerformanceTrend(
            @RequestParam(required = true) String factory
            , @RequestParam(required = false, defaultValue = "") String timeSpan) throws ParseException {
        if (factory.equalsIgnoreCase("b06")) {
            return equipmentFixtureManagementService.b06FixturePerformanceTrend(timeSpan);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/equipment/status/online/by/type")
    public List<Map<String, Object>> equipmentStatusOnlineByType(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentStatusOnlineByType(timeSpan);
        }

        return Collections.emptyList();
    }

    @GetMapping("/fixture/status/online/by/type")
    public List<Map<String, Object>> fixtureStatusOnlineByType(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureStatusOnlineByType(timeSpan);
        }

        return Collections.emptyList();
    }

    @GetMapping("/equipment/detail/list")
    public Map<String, List<Map<String, Object>>> equipmentDetailList(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentDetailListV2(timeSpan);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/fixture/detail/list")
    public Map<String, List<Map<String, Object>>> fixtureDetailList(@RequestParam String factory, @RequestParam(required = false, defaultValue = "") String timeSpan) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureDetailListV2(timeSpan);
        }

        return Collections.emptyMap();
    }

    @GetMapping("/equipment/duedate/reminder")
    public Map<String, List<Map<String, Object>>> equipmentDueDateReminder(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentDueDateReminder();
        }

        return Collections.emptyMap();
    }

    @GetMapping("/fixture/duedate/reminder")
    public Map<String, List<Map<String, Object>>> fixtureDueDateReminder(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureDueDateReminder();
        }

        return Collections.emptyMap();
    }

    @GetMapping("/equipment/owner/sumary")
    public Map<String, Map<String, Object>> equipmentOwnerSumary(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentOwnerSumary();
        }

        return Collections.emptyMap();
    }

    @GetMapping("/fixture/owner/sumary")
    public Map<String, Map<String, Object>> fixtureOwnerSumary(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureOwnerSumary();
        }

        return Collections.emptyMap();
    }

    // E&F provider & supplier
    @GetMapping("/owner/equipment/list")
    public Object ownerEquipmentList(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06OwnerEquipmentList();
        }

        return Collections.emptyMap();
    }

    @GetMapping("/owner/fixture/list")
    public Object ownerFixtureList(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06OwnerFixtureList();
        }

        return Collections.emptyMap();
    }

    @GetMapping("/owner/gettype")
    public Object ownerGetType(@RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return teEFOwnerTypeRepository.findAll().stream().map(e -> e.getOwnerType()).collect(Collectors.toSet());
        }

        return Collections.emptyList();
    }

    @PostMapping("/equipment/owner/update")
    public Boolean equipmentInfoUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentOwnerUpdate(data);
        }

        return false;
    }

    @PostMapping("/fixture/owner/update")
    public Boolean fixtureInfoUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureOwnerUpdate(data);
        }

        return false;
    }


        // Equipment Buy
    @GetMapping("/equipment/buy/history/list")
    public Object equipmentBuyHistoryList(@RequestParam String factory, @RequestParam String equipmentName) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBuyHistoryList(equipmentName);
        }

        return Collections.emptyList();
    }

    @PostMapping("/equipment/buy/history/add")
    public Object equipmentBuyHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBuyHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/equipment/buy/history/update")
    public Object equipmentBuyHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBuyHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/equipment/buy/history/delete")
    public Object equipmentBuyHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBuyHistoryDelete(data);
        }

        return false;
    }

        // Fixture Buy
    @GetMapping("/fixture/buy/history/list")
    public Object fixtureBuyHistoryList(@RequestParam String factory, @RequestParam String fixtureCode) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBuyHistoryList(fixtureCode);
        }

        return Collections.emptyList();
    }

    @PostMapping("/fixture/buy/history/add")
    public Object fixtureBuyHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBuyHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/fixture/buy/history/update")
    public Object fixtureBuyHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBuyHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/fixture/buy/history/delete")
    public Object fixtureBuyHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBuyHistoryDelete(data);
        }

        return false;
    }

        // Equipment Borrow
    @GetMapping("/equipment/borrow/history/list")
    public Object equipmentBorrowHistoryList(@RequestParam String factory, @RequestParam String equipmentName) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBorrowHistoryList(equipmentName);
        }

        return Collections.emptyList();
    }

    @PostMapping("/equipment/borrow/history/add")
    public Object equipmentBorrowHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBorrowHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/equipment/borrow/history/update")
    public Object equipmentBorrowHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBorrowHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/equipment/borrow/history/delete")
    public Object equipmentBorrowHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentBorrowHistoryDelete(data);
        }

        return false;
    }

        // Fixture borrow
    @GetMapping("/fixture/borrow/history/list")
    public Object fixtureBorrowHistoryList(@RequestParam String factory, @RequestParam String fixtureCode) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBorrowHistoryList(fixtureCode);
        }

        return Collections.emptyList();
    }

    @PostMapping("/fixture/borrow/history/add")
    public Object fixtureBorrowHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBorrowHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/fixture/borrow/history/update")
    public Object fixtureBorrowHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBorrowHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/fixture/borrow/history/delete")
    public Object fixtureBorrowHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureBorrowHistoryDelete(data);
        }

        return false;
    }

        // Equipment rent
    @GetMapping("/equipment/rent/history/list")
    public Object equipmentRentHistoryList(@RequestParam String factory, @RequestParam String equipmentName) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentRentHistoryList(equipmentName);
        }

        return Collections.emptyList();
    }

    @PostMapping("/equipment/rent/history/add")
    public Object equipmentRentHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentRentHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/equipment/rent/history/update")
    public Object equipmentRentHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentRentHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/equipment/rent/history/delete")
    public Object equipmentRentHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06EquipmentRentHistoryDelete(data);
        }

        return false;
    }

        // Fixture rent
    @GetMapping("/fixture/rent/history/list")
    public Object fixtureRentHistoryList(@RequestParam String factory, @RequestParam String fixtureCode) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureRentHistoryList(fixtureCode);
        }

        return Collections.emptyList();
    }

    @PostMapping("/fixture/rent/history/add")
    public Object fixtureRentHistoryAdd(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureRentHistoryAdd(data);
        }

        return false;
    }

    @PostMapping("/fixture/rent/history/update")
    public Object fixtureRentHistoryUpdate(@RequestBody Map<String, Object> data, @RequestParam String factory) throws ParseException {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureRentHistoryUpdate(data);
        }

        return false;
    }

    @DeleteMapping("/fixture/rent/history/delete")
    public Object fixtureRentHistoryDelete(@RequestBody Map<String, Object> data, @RequestParam String factory) {
        if (factory.equalsIgnoreCase("B06")) {
            return equipmentFixtureManagementService.b06FixtureRentHistoryDelete(data);
        }

        return false;
    }

}