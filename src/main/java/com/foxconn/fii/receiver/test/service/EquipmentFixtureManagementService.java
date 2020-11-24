package com.foxconn.fii.receiver.test.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface EquipmentFixtureManagementService {

    Map<String, Object> b06OeeAteStatus();

    // E%F overall API
    Map<String, Map<String, Object>> b06EquipmentStatusSumaryV2(String ownerType);

    Map<String, Map<String, Object>> b06FixtureStatusSumaryV2(String ownerType);

    Map<String, Map<String, Object>> b06EquipmentPerformanceTrend(String timeSpanStr) throws ParseException;

    Map<String, Map<String, Object>> b06FixturePerformanceTrend(String timeSpanStr) throws ParseException;

    List<Map<String, Object>> b06EquipmentStatusOnlineByType(String timeSpanStr);

    List<Map<String, Object>> b06FixtureStatusOnlineByType(String timeSpanStr);

    Map<String, List<Map<String, Object>>> b06EquipmentDetailListV2(String timeSpanStr);

    Map<String, List<Map<String, Object>>> b06FixtureDetailListV2(String timeSpanStr);

    Map<String, List<Map<String, Object>>> b06EquipmentDueDateReminder();

    Map<String, List<Map<String, Object>>> b06FixtureDueDateReminder();

    Map<String, Map<String, Object>> b06EquipmentOwnerSumary();

    Map<String, Map<String, Object>> b06FixtureOwnerSumary();

    // E&F provider & supplier
    Object b06OwnerEquipmentList();

    Object b06OwnerFixtureList();

    boolean b06EquipmentOwnerUpdate(Map<String, Object> data) throws ParseException;

    boolean b06FixtureOwnerUpdate(Map<String, Object> data) throws ParseException;

        // Equipment Buy
    Object b06EquipmentBuyHistoryList(String equipmentName);

    Object b06EquipmentBuyHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06EquipmentBuyHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06EquipmentBuyHistoryDelete(Map<String, Object> data);

        // Fixture Buy
    Object b06FixtureBuyHistoryList(String fixtureCode);

    Object b06FixtureBuyHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06FixtureBuyHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06FixtureBuyHistoryDelete(Map<String, Object> data);

        // Equipment Borrow
    Object b06EquipmentBorrowHistoryList(String equipmentName);

    Object b06EquipmentBorrowHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06EquipmentBorrowHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06EquipmentBorrowHistoryDelete(Map<String, Object> data);

        // Fixture Borrow
    Object b06FixtureBorrowHistoryList(String fixtureCode);

    Object b06FixtureBorrowHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06FixtureBorrowHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06FixtureBorrowHistoryDelete(Map<String, Object> data);

        // Equipment Rent
    Object b06EquipmentRentHistoryList(String equipmentName);

    Object b06EquipmentRentHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06EquipmentRentHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06EquipmentRentHistoryDelete(Map<String, Object> data);

    // Fixture Rent
    Object b06FixtureRentHistoryList(String fixtureCode);

    Object b06FixtureRentHistoryAdd(Map<String, Object> data) throws ParseException;

    Object b06FixtureRentHistoryUpdate(Map<String, Object> data) throws ParseException;

    Object b06FixtureRentHistoryDelete(Map<String, Object> data);

}
