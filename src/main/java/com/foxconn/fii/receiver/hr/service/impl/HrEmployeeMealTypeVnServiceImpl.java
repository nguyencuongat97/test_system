package com.foxconn.fii.receiver.hr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.data.primary.model.entity.HrEmployeeInfoEatRice;
import com.foxconn.fii.data.primary.repository.HrEmployeeInfoEatRiceRepository;
import com.foxconn.fii.receiver.hr.service.HrEmployeeMealTypeVnService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HrEmployeeMealTypeVnServiceImpl implements HrEmployeeMealTypeVnService {

    @Autowired
    private HrEmployeeInfoEatRiceRepository hrEmployeeInfoEatRiceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    @Override
    public void HrSyncDataCanteenFromAPI(Date startDate) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String body = "{\n" +
                "\t\"reqData\" : {\n" +
                "\t\t\"EmployeeCode\" : \"\",\n" +
                "\t\t\"Department\" : \"\",\n" +
                "\t\t\"DepartmentCode\" : \"\",\n" +
                "\t\t\"endDate\" : \"\",\n" +
                "\t\t\"startDate\" : \""+dateFormat.format(startDate)+"\"\n" +
                "\t}\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String url = "http://10.134.159.235:8010/api/Vietnam/VietnamConsumeDate";
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### DuongTungErrorHR01 : ", e);
            return ;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            map = objectMapper.readValue(responseEntity.getBody(),  new TypeReference<Map<String, Object>>(){});
        }catch (Exception e){
            log.info(String.valueOf(e));
        }

        List<Map<String, Object>> arrData = (List<Map<String, Object>>) map.get("VietnamConsumeDateList");
        List<HrEmployeeInfoEatRice> responsi = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        if (arrData.size() > 0){
            for (Map<String, Object> value : arrData) {
                HrEmployeeInfoEatRice hrEmployeeInfoEatRice = new HrEmployeeInfoEatRice();
                MealData mealData = new MealData(value);

                String dateTime = mealData.consumeTime.split(" ")[0]+" "+mealData.consumeTime.split(" ")[2];
                Date timeConsume = simpleDateFormat.parse(dateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timeConsume);

                Calendar calendar1TimeUpdate = Calendar.getInstance();
                String getTimeUpdate = mealData.getTime.split(" ")[0]+" "+mealData.getTime.split(" ")[2];
                calendar1TimeUpdate.setTime(simpleDateFormat.parse(getTimeUpdate));
                if (mealData.consumeTime.split(" ")[1].equalsIgnoreCase("下午")&& calendar.get(Calendar.HOUR_OF_DAY) != 12){
                    //chieu
                    calendar.add(Calendar.HOUR_OF_DAY, 12);
                }
                if (mealData.getTime.split(" ")[1].equalsIgnoreCase("下午") && calendar1TimeUpdate.get(Calendar.HOUR_OF_DAY) != 12){

                    calendar1TimeUpdate.add(Calendar.HOUR_OF_DAY, 12);
                }

                if (calendar.get(Calendar.HOUR_OF_DAY)== 12 && mealData.consumeTime.split(" ")[1].equalsIgnoreCase("上午")){
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                }
                if (calendar1TimeUpdate.get(Calendar.HOUR_OF_DAY)== 12 && mealData.getTime.split(" ")[1].equalsIgnoreCase("上午")){
                    calendar1TimeUpdate.set(Calendar.HOUR_OF_DAY, 0);
                }

                switch (mealData.mealtype){
                    case "早餐":
                        hrEmployeeInfoEatRice.setTypeMealVn(HrEmployeeInfoEatRice.TypeMeal.BREAKFAST);
                        break;
                    case "中餐":
                        hrEmployeeInfoEatRice.setTypeMealVn(HrEmployeeInfoEatRice.TypeMeal.LUNCH);
                        break;
                    case "晚餐":
                        hrEmployeeInfoEatRice.setTypeMealVn(HrEmployeeInfoEatRice.TypeMeal.DINNER);
                        break;
                    case "夜宵":
                        hrEmployeeInfoEatRice.setTypeMealVn(HrEmployeeInfoEatRice.TypeMeal.SUPPER);
                        break;
                    default:
                        hrEmployeeInfoEatRice.setTypeMealVn(HrEmployeeInfoEatRice.TypeMeal.OTHER);
                }
                hrEmployeeInfoEatRice.setEmpName(mealData.employeeName);
                hrEmployeeInfoEatRice.setEmpCode(mealData.employeeCode);
                hrEmployeeInfoEatRice.setDeviceId(mealData.deviceId);
                hrEmployeeInfoEatRice.setDepartment(mealData.department);
                hrEmployeeInfoEatRice.setBusGroup(mealData.businessGroup);
                hrEmployeeInfoEatRice.setCardId(mealData.cardID);
                hrEmployeeInfoEatRice.setMealType(mealData.mealtype);
                hrEmployeeInfoEatRice.setRestaurant(mealData.consumeRestaurant);
                hrEmployeeInfoEatRice.setBreakfastM((int) mealData.breakfastMoney);
                hrEmployeeInfoEatRice.setLunchM((int) mealData.lunchMoney);
                hrEmployeeInfoEatRice.setDinnerM((int) mealData.dinnerMoney);
                hrEmployeeInfoEatRice.setSupperM((int) mealData.supperMoney);
                hrEmployeeInfoEatRice.setConsumeTime(calendar.getTime());
                hrEmployeeInfoEatRice.setTimeUpdateSV(calendar1TimeUpdate.getTime());

                responsi.add(hrEmployeeInfoEatRice);
            }
        }else {
            System.out.println("###NO DATA Type-meal Hr-eat-rice");
        }
        log.info("DuongTung-HrSyncDataCanteenFromAPI-SIZE: "+ responsi.size());
        saveHrDataMealFromAPI(responsi);
    }


    public int[] saveHrDataMealFromAPI(List<HrEmployeeInfoEatRice> groupList) {
        if (groupList.isEmpty()) {
            log.error("### DuongTungErrorHr08");
            return null;
        }
        return jdbcTemplate.batchUpdate(
                "merge into hr_et_info_eat_rice as target " +
                        "using(select employee_code=?, employee_name=?, card_id=?, department=?, business_group=?, consume_time=?, get_time_update=?, meal_type=?, restaurant=?, device_id=?, breakfast_money=?, lunch_money=?, dinner_money=?, supper_money=?, type_meal_vn=?, created_at=?) as source " +
                        "   on target.employee_code=source.employee_code and target.consume_time = source.consume_time " +
                        "when matched then " +
                        "   update set " +
                        "   target.employee_code=source.employee_code, " +
                        "   target.consume_time=source.consume_time " +
                        "when not matched then " +
                        "   insert (employee_code, employee_name, card_id, department, business_group, consume_time, get_time_update, meal_type, restaurant, device_id, breakfast_money, lunch_money, dinner_money, supper_money, type_meal_vn, created_at) " +
                        "   values(source.employee_code, source.employee_name, source.card_id, source.department, source.business_group, source.consume_time, source.get_time_update, source.meal_type, source.restaurant, source.device_id, source.breakfast_money, source.lunch_money, source.dinner_money, source.supper_money,source.type_meal_vn, source.created_at);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        HrEmployeeInfoEatRice group = groupList.get(i);
                        preparedStatement.setString(1, group.getEmpCode());
                        preparedStatement.setString(2, group.getEmpName());
                        preparedStatement.setString(3, group.getCardId());
                        preparedStatement.setString(4, group.getDepartment());
                        preparedStatement.setString(5, group.getBusGroup());
                        preparedStatement.setTimestamp(6, new Timestamp(group.getConsumeTime().getTime()));
                        preparedStatement.setTimestamp(7, new Timestamp(group.getTimeUpdateSV().getTime()));
                        preparedStatement.setString(8, group.getMealType());
                        preparedStatement.setString(9, group.getRestaurant());
                        preparedStatement.setInt(10, group.getDeviceId());
                        preparedStatement.setInt(11, group.getBreakfastM());
                        preparedStatement.setInt(12, group.getLunchM());
                        preparedStatement.setInt(13, group.getDinnerM());
                        preparedStatement.setInt(14, group.getSupperM());
                        preparedStatement.setInt(15, group.getTypeMealVn().ordinal());
                        preparedStatement.setTimestamp(16, new Timestamp(new Date().getTime()));

                    }

                    @Override
                    public int getBatchSize() {
                        return groupList.size();
                    }
                });
    }


    @Data
    public class MealData {
        private String employeeCode;
        private String employeeName;
        private String cardID;
        private String department;
        private String businessGroup;
        private String consumeTime;
        private String getTime;
        private String mealtype;
        private String consumeRestaurant;
        private int deviceId;
        private double breakfastMoney;
        private double lunchMoney;
        private double dinnerMoney;
        private double supperMoney;

        public MealData (Map<String, Object> input) {
            this.employeeCode =(String) input.get("EmployeeCode");
            this.employeeName = (String)  input.get("EmployeeName");
            this.cardID = (String) input.get("CardID");
            this.department =(String) input.get("Department");
            this.businessGroup = (String) input.get("BusinessGroup");
            this.consumeTime = (String) input.get("ConsumeTime");
            this.getTime = (String) input.get("GetTime");
            this.mealtype = (String) input.get("Mealtype");
            this.consumeRestaurant = (String) input.get("ConsumeRestaurant");
            this.deviceId = Integer.parseInt((String) input.get("DeviceId"));
            this.breakfastMoney = (double) input.get("BreakfastMoney");
            this.lunchMoney = (double) input.get("LunchMoney");
            this.dinnerMoney = (double) input.get("DinnerMoney");
            this.supperMoney = (double) input.get("SupperMoney");
        }
    }

    @Override
    public Object getDataMealBNAndBN3SendMail(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        List<Integer> listDeviceId = new ArrayList<>();
        listDeviceId.add(15);
        listDeviceId.add(18);
        List<Object[]> dataBN3 = hrEmployeeInfoEatRiceRepository.countMealBN3(listDeviceId, "GWCT", startDate, endDate);
        List<Object[]> dataBN = hrEmployeeInfoEatRiceRepository.countMealBN(listDeviceId, "GWCT", startDate, endDate);
        Map<String, Long> dtBN = dataBN.stream().collect(Collectors.toMap(e-> ((HrEmployeeInfoEatRice.TypeMeal) e[0]).name() , e -> (Long) e[1]));
        Map<String, Long> dtBN3 = dataBN3.stream().collect(Collectors.toMap(e-> ((HrEmployeeInfoEatRice.TypeMeal) e[0]).name() , e -> (Long) e[1]));
        String title = "Meal data statistics " + df.format(startDate);
        String contentHeader = "Compare the number of rice output !\n";
        String connentB = "\n早餐 - BN: "+dtBN.get("BREAKFAST")+ "\t\t\t BN3: " + dtBN3.get("BREAKFAST")+"\n";
        String connentL = "\n中餐\t\t\t - BN: "+dtBN.get("LUNCH")+ "\t\t\t BN3: " + dtBN3.get("LUNCH")+"\n";
        String connentD = "\n晚餐\t\t\t - BN: "+dtBN.get("DINNER")+ "\t\t\t BN3: " + dtBN3.get("DINNER")+"\n";
        String connentS = "\n夜宵\t\t\t - BN: "+dtBN.get("SUPPER")+ "\t\t\t BN3: " + dtBN3.get("SUPPER")+"\n";
        String content = contentHeader + connentB + connentL + connentD + connentS;
        sendMail("cpe-vn-fii-sw@mail.foxconn.com,vietphuc.ha@mail.foxconn.com", title, content);
        return true;
    }


    public void sendMail(String listUser, String title, String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> tmpMap = new HashMap<>();
        tmpMap.put("title",title);
        tmpMap.put("body", content);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("system", "MAIL");
        bodyMap.put("type", "TEXT");
        bodyMap.put("source", "WS");
        bodyMap.put("from", "");
        bodyMap.put("toUser", listUser);
        bodyMap.put("toGroup", null);
        bodyMap.put("message", objectMapper.writeValueAsString(tmpMap));
        String body = objectMapper.writeValueAsString(bodyMap);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange("http://10.224.81.120:8888/notify-service/api/notify", HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            log.error("### uploadFile error ", e);
            //return false;
        }
    }
}
