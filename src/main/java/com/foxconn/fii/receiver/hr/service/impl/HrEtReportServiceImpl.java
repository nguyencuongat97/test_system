package com.foxconn.fii.receiver.hr.service.impl;

import com.foxconn.fii.data.primary.model.entity.HrEmployeeInfoEatRice;
import com.foxconn.fii.data.primary.repository.HrEmployeeInfoEatRiceRepository;
import com.foxconn.fii.receiver.hr.service.HrEtReportService;
import com.foxconn.fii.receiver.hr.service.HrEtWorkCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class HrEtReportServiceImpl implements HrEtReportService {

    @Autowired
    private HrEtWorkCountService hrEtWorkCountService;

    @Autowired
    private HrEmployeeInfoEatRiceRepository hrEmployeeInfoEatRiceRepository;

    @Override
    public Object monthlySalartReport(String empNo, int month, double basicSalary, int relatedPerson) throws ParserConfigurationException, SAXException, ParseException, IOException {

        // using 2020 tax formula

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month - 1);

        // meta prepare
        Map<String, Object> dutyData = hrEtWorkCountService.workCount(empNo, "", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        double totalDutyLv = (double) ((Map) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("totalCountResult")).get("LV");

        double totalDutyF = (double) ((Map) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("totalCountResult")).get("F");

        double totalDutyV = (double) ((Map) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("totalCountResult")).get("V");

        double totalDutyBL = (double) ((Map) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("totalCountResult")).get("BL");

        double totalDutyDay = totalDutyLv + totalDutyF + totalDutyV + totalDutyBL;


        double totalDutyL = (double) ((Map) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("totalCountResult")).get("L");

        double defaultDutyDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH) - totalDutyL;


        double totalDutyCa3Hour = (double) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("ca3TotalTime");

        double totalOvertimeNormalHour = (double) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("normalOtTotal");

        double totalOvertimeCa3Hour = (double) ((Map) ((Map) dutyData.get("data")).get(empNo)).get("ca3OtTotal");


        Calendar calStart = Calendar.getInstance();
        calStart.setTime(cal.getTime());
        calStart.set(Calendar.MILLISECOND, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(cal.getTime());
        calEnd.set(Calendar.MILLISECOND, 999);
        calEnd.set(Calendar.SECOND, 59);
        calEnd.set(Calendar.MINUTE, 59);
        calEnd.set(Calendar.HOUR_OF_DAY, 23);
        calEnd.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        List<HrEmployeeInfoEatRice> mealConsume = hrEmployeeInfoEatRiceRepository.findAllByEmpCodeAndConsumeTimeBetween(empNo, calStart.getTime(), calEnd.getTime());

        double totalMeal = mealConsume.stream().mapToDouble(e -> e.getBreakfastM() + e.getLunchM() + e.getDinnerM() + e.getSupperM()).sum();

        double mealBonus = 1066; // temporary fix

        double houseBonus = 500; // temporary fix

        double transportBonus = 200; // temporary fix

        double disciplineBonus = 500; // temporary fix

        double laborUnionCharge = 40; // temporary fix


        Map<String, Double> socialInsurance = new HashMap<String, Double>(){{
            put("limit", 29800.0);
            put("rate", 0.08);
        }};

        Map<String, Double> healthInsurance = new HashMap<String, Double>(){{
            put("limit", 29800.0);
            put("rate", 0.015);
        }};

        Map<String, Double> unemploymentInsurance = new HashMap<String, Double>(){{
            put("limit", 0.0);
            put("rate", 0.01);
        }};


        List<Map<String, Double>> personalTaxList = new ArrayList<Map<String, Double>>(){{
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 0.0);
                put("highIncomeLimit", 5000.0);
                put("taxFree", 0.0);
                put("taxRate", 0.05);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 5000.0);
                put("highIncomeLimit", 10000.0);
                put("taxFree", 250.0);
                put("taxRate", 0.1);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 10000.0);
                put("highIncomeLimit", 18000.0);
                put("taxFree", 750.0);
                put("taxRate", 0.15);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 18000.0);
                put("highIncomeLimit", 32000.0);
                put("taxFree", 1650.0);
                put("taxRate", 0.2);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 32000.0);
                put("highIncomeLimit", 52000.0);
                put("taxFree", 3250.0);
                put("taxRate", 0.25);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 52000.0);
                put("highIncomeLimit", 80000.0);
                put("taxFree", 5850.0);
                put("taxRate", 0.3);
            }});
            add(new HashMap<String, Double>(){{
                put("lowIncomeLimit", 80000.0);
                put("highIncomeLimit", 9999000.0);
                put("taxFree", 9850.0);
                put("taxRate", 0.35);
            }});
        }};

        double incomeTaxFree = 9000.0;
        double incomeDependRate = 3600.0;

        double ca3DutyRate = 1.3;
        double normalOvertimeRate = 1.5;
        double ca3OvertimeRate = 1.8;

        double totalBonus = (mealBonus - totalMeal) + houseBonus + transportBonus + disciplineBonus;

        double totalMinus = 0;
        double totalInsurance = 0;

        totalInsurance += basicSalary > socialInsurance.get("limit") && socialInsurance.get("limit") > 0 ? socialInsurance.get("limit") * socialInsurance.get("rate") : basicSalary * socialInsurance.get("rate");
        totalInsurance += basicSalary > healthInsurance.get("limit") && healthInsurance.get("limit") > 0 ? healthInsurance.get("limit") * healthInsurance.get("rate") : basicSalary * healthInsurance.get("rate");
        totalInsurance += basicSalary > unemploymentInsurance.get("limit") && unemploymentInsurance.get("limit") > 0 ? unemploymentInsurance.get("limit") * unemploymentInsurance.get("rate") : basicSalary * unemploymentInsurance.get("rate");

        totalMinus = totalInsurance + incomeTaxFree + relatedPerson * incomeDependRate + laborUnionCharge;


        // calculate real income

        double personalTaxBasic = basicSalary + (totalOvertimeNormalHour + totalOvertimeCa3Hour) * basicSalary / 208 + totalBonus - totalMinus;

        personalTaxBasic = personalTaxBasic > 0 ? personalTaxBasic : 0;

        double finalPersonalTaxBasic = personalTaxBasic;
        Map<String, Double> personalTax = personalTaxList.stream().filter(e -> finalPersonalTaxBasic > e.get("lowIncomeLimit") && finalPersonalTaxBasic <= e.get("highIncomeLimit")).findFirst().orElse(personalTaxList.get(0));

        double totalPersonalTax = personalTaxBasic * personalTax.get("taxRate") - personalTax.get("taxFree");

        double totalOvertime = (totalOvertimeNormalHour * normalOvertimeRate + totalOvertimeCa3Hour * ca3OvertimeRate) * basicSalary / 208;

        double totalDuty = totalDutyCa3Hour * (ca3DutyRate - 1) * basicSalary / 208 + totalDutyDay / defaultDutyDay * basicSalary;

        return totalDuty + totalOvertime + totalBonus - totalInsurance - totalPersonalTax;

    }

}
