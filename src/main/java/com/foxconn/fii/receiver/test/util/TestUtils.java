package com.foxconn.fii.receiver.test.util;

import com.foxconn.fii.common.ShiftType;
import com.foxconn.fii.data.primary.model.SpcData;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class TestUtils {

    public static <T> Map<String, T> getHourlyMap(Date startDate, Date endDate) {
        Map<String, T> byTimeMap = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.MINUTE, 30);

        Date now = new Date();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd HH:mm");
        Date tmp = calendar.getTime();
        while (tmp.before(now) && tmp.before(endDate)) {
            String key = df.format(tmp);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            tmp = calendar.getTime();
            key += " - " + df.format(tmp);
            byTimeMap.put(key, null);
        }

        return byTimeMap;
    }

    public static <T> Map<String, T> getWeeklyMap(Date startDate, Date endDate, ShiftType shiftType) {
        Map<String, T> byTimeMap = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        Date now = new Date();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        String suffix1 = shiftType == ShiftType.DAY ? " DAY" : " NIGHT";
        String suffix2 = shiftType == ShiftType.DAY ? " NIGHT" : " DAY";

        Date tmp = calendar.getTime();
        while (tmp.before(now) && tmp.before(endDate)) {
            byTimeMap.put(df.format(calendar.getTime()) + suffix1, null);

            calendar.add(Calendar.HOUR_OF_DAY, 12);
            if ((endDate.getTime() - calendar.getTimeInMillis() < 12 * 60 * 60 * 1000) ||
                    (now.getTime() - calendar.getTimeInMillis() < 12 * 60 * 60 * 1000)) {
                break;
            }

            byTimeMap.put(df.format(calendar.getTime()) + suffix2, null);
            calendar.add(Calendar.HOUR_OF_DAY, 12);
            tmp = calendar.getTime();
        }

        return byTimeMap;
    }

    public static <T> Map<String, T> getWeeklyMap(Date startDate, Date endDate) {
        Map<String, T> byTimeMap = new LinkedHashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date tmp = calendar.getTime();

        Date now = new Date();

        while (tmp.before(now) && tmp.before(endDate)) {
            byTimeMap.put(df.format(tmp), null);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            tmp = calendar.getTime();
        }

        return byTimeMap;
    }

    public static boolean isOutSpec(int wip, Integer wipSpec, float retestRate, Float retestRateSpec, float firstPassRate, Float firstPassRateSpec) {
        if (wipSpec == null && retestRateSpec == null && firstPassRateSpec == null) {
            return false;
        }

        return (wipSpec == null || wip > wipSpec) && (retestRateSpec == null || retestRate > retestRateSpec) && (firstPassRateSpec == null || firstPassRate < firstPassRateSpec);

    }

    public static String generateDetail(int wip, Integer wipSpec, float retestRate, Float retestRateSpec, float firstPassRate, Float firstPassRateSpec) {
        String detail = "";
//        if (wipSpec != null) {
//            detail += String.format("WIP(%d)>%d", wip, wipSpec);
//        }
        if (retestRateSpec != null) {
            if (!detail.isEmpty()) {
                detail += " and ";
            }
            detail += String.format("RETEST RATE(%.2f%%)>%.2f%%", retestRate, retestRateSpec);
        }
        if (firstPassRateSpec != null) {
            if (!detail.isEmpty()) {
                detail += " and ";
            }
            detail += String.format("F.P.R(%.2f%%)<%.2f%%", firstPassRate, firstPassRateSpec);
        }

        return detail;
    }

    public static Double calculateCPK(List<Double> values, Double lowSpec, Double highSpec) {
        if (values == null) {
            return null;
        }

        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (values.isEmpty() || (lowSpec == null && highSpec == null)) {
            return null;
        }

        StandardDeviation standardDeviation = new StandardDeviation();
        double[] convertedValues = new double[values.size()];
        double sum = 0;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) == null) continue;
            convertedValues[i] = values.get(i);
            sum += values.get(i);
        }

        double average = sum / values.size();
        double sigma = standardDeviation.evaluate(convertedValues);
        double cpk = 10000;
        if (sigma != 0) {
            double cpkLower = Double.MAX_VALUE;
            if (lowSpec != null) {
                cpkLower = (average - lowSpec) / (sigma * 3);
            }

            double cpkHigher = Double.MAX_VALUE;
            if (highSpec != null) {
                cpkHigher = (highSpec - average) / (sigma * 3);
            }
            cpk = cpkLower < cpkHigher ? cpkLower : cpkHigher;
        }

        return cpk == 10000 ? 0 : cpk;
    }

    public static SpcData calculateCPKData(List<Double> values, Double lowSpec, Double highSpec) {
        if (values == null) {
            return null;
        }

        values = values.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (values.isEmpty() || (lowSpec == null && highSpec == null)) {
            return null;
        }

//        double d2 = 1.128;
//        int sizeSubgroup = 2;
        double d2 = 1.693;
        int sizeSubgroup = 3;
//        double d2 = 2.059;
//        int sizeSubgroup = 4;
//        double d2 = 2.326;
//        int sizeSubgroup = 5;
        int numSubgroup = (int) Math.ceil(values.size() * 1.0f / sizeSubgroup);

        double[] overall = new double[values.size()];
        double[][] subgroups = new double[numSubgroup][sizeSubgroup];
        double sum = 0;
        Double max = null;
        Double min = null;
        Double target = lowSpec != null && highSpec != null ? (highSpec - lowSpec) / 2 : null;
        Double cpmSigma = null;
        for (int i = 0; i < values.size(); i++) {
            overall[i] = values.get(i);
            subgroups[i / sizeSubgroup][i % sizeSubgroup] = values.get(i);
            if (target != null) {
                if (cpmSigma == null) {
                    cpmSigma = Math.pow(values.get(i) - target, 2);
                } else {
                    cpmSigma += Math.pow(values.get(i) - target, 2);
                }
            }

            sum += values.get(i);
            if (max == null || max < values.get(i)) {
                max = values.get(i);
            }
            if (min == null || min > values.get(i)) {
                min = values.get(i);
            }
        }
        double average = sum / values.size();
        if (cpmSigma != null) {
            cpmSigma = Math.sqrt(cpmSigma / (values.size() - 1));
        }

        SpcData spc = new SpcData();
        spc.setValues(values);
        spc.setSampleSize(values.size());
        spc.setAverage(average);
        spc.setMax(max);
        spc.setMin(min);
        spc.setLsl(lowSpec);
        spc.setUsl(highSpec);
        spc.setCpm(cpmSigma != null ? (highSpec - lowSpec) / 6 * cpmSigma : null);

        StandardDeviation sd = new StandardDeviation();
        double overallSigma = sd.evaluate(overall);
        spc.setSigmaOverall(overallSigma);

        // calculate within sigma from pooled standard deviation
//        sd.clear();
//        double withinSigma = 0;
//        for (int i = 0; i < numSubgroup; i ++) {
//            int size = ((i + 1) * sizeSubgroup < overall.length) ? sizeSubgroup : overall.length - (i * sizeSubgroup);
//            sd.clear();
//            withinSigma += (size - 1) * Math.pow(sd.evaluate(subgroups[i]), 2);
//        }
//        withinSigma = Math.sqrt(withinSigma / (overall.length - numSubgroup));

        // calculate within sigma from r-bar
        double withinSigma = 0;
        double xBar = 0;
        double rBar = 0;
        for (int i = 0; i < numSubgroup; i ++) {
            int size = ((i + 1) * sizeSubgroup < overall.length) ? sizeSubgroup : overall.length - (i * sizeSubgroup);
            double sumSubgroup = 0;
            Double minSubgroup = null;
            Double maxSubgroup = null;
            for (int j =0; j<size; j++) {
                sumSubgroup += subgroups[i][j];
                if (maxSubgroup == null || maxSubgroup < subgroups[i][j]) {
                    maxSubgroup = subgroups[i][j];
                }
                if (minSubgroup == null || minSubgroup > subgroups[i][j]) {
                    minSubgroup = subgroups[i][j];
                }
            }
            xBar += (sumSubgroup/size);
            if (maxSubgroup != null && minSubgroup != null) {
                rBar += (maxSubgroup - minSubgroup);
            }
        }
        xBar /= numSubgroup;
        rBar /= numSubgroup;
        withinSigma = rBar / d2;
        spc.setSigmaWithin(withinSigma);
        spc.setXBar(xBar);

        Double cp = null;
        Double cpk = null;
        Double cpkLower = null;
        Double cpkHigher = null;
        if (withinSigma != 0) {
            if (lowSpec == null) {
//                cp = highSpec / (3 * withinSigma);
                cpkHigher = (highSpec - xBar) / (withinSigma * 3);
                cpk = cpkHigher;
            } else if (highSpec == null) {
//                cp = lowSpec / (3 * withinSigma);
                cpkLower = (xBar - lowSpec) / (withinSigma * 3);
                cpk = cpkLower;
            } else {
                cp = (highSpec - lowSpec) / (6 * withinSigma);
                cpkHigher = (highSpec - xBar) / (withinSigma * 3);
                cpkLower = (xBar - lowSpec) / (withinSigma * 3);
                cpk = Math.min(cpkHigher, cpkLower);
            }
        }
        spc.setCpl(cpkLower);
        spc.setCpu(cpkHigher);
        spc.setCpk(cpk == null ? 0 : cpk);
        spc.setCp(cp);

        Double pp = null;
        Double ppk = null;
        Double ppLower = null;
        Double ppHigher = null;
        if (overallSigma != 0) {
            if (lowSpec == null) {
//                pp = highSpec / (3 * overallSigma);
                ppHigher = (highSpec - average) / (overallSigma * 3);
                ppk = ppHigher;
            } else if (highSpec == null) {
//                pp = lowSpec / (3 * overallSigma);
                ppLower = (average - lowSpec) / (overallSigma * 3);
                ppk = ppLower;
            } else {
                pp = (highSpec - lowSpec) / (6 * overallSigma);
                ppHigher = (highSpec - average) / (overallSigma * 3);
                ppLower = (average - lowSpec) / (overallSigma * 3);
                ppk = Math.min(ppHigher, ppLower);
            }
        }
        spc.setPpl(ppLower);
        spc.setPpu(ppHigher);
        spc.setPpk(ppk == null ? 0 : ppk);
        spc.setPp(pp);

        return spc;
    }

    public static double calculateCPK(double variance, double average, Double lowSpec, Double highSpec) {
        double sigma = Math.sqrt(variance);
        double cpk = 10000;
        if (sigma != 0) {
            double cpkLower = Double.MAX_VALUE;
            if (lowSpec != null) {
                cpkLower = (average - lowSpec) / (sigma * 3);
            }

            double cpkHigher = Double.MAX_VALUE;
            if (highSpec != null) {
                cpkHigher = (highSpec - average) / (sigma * 3);
            }
            cpk = cpkLower < cpkHigher ? cpkLower : cpkHigher;
        }

        return cpk == 10000 ? 0 : cpk;
    }

    public static Double calculateVariance(double[] values, double oldVariance, double oldAverage, int oldNumberOfValue) {
        for (double value : values) {
            oldVariance = (oldNumberOfValue * 1.0 / (oldNumberOfValue + 1)) * (oldVariance + Math.pow((value - oldAverage), 2) / ((oldNumberOfValue + 1)));
            oldAverage = (oldAverage * oldNumberOfValue + value) / (oldNumberOfValue + 1);
            oldNumberOfValue++;
        }
        return oldVariance;
    }

    public static boolean isTestSection(String sectionName, String groupName) {
        if (!"SI".equalsIgnoreCase(sectionName) && !"TEST".equalsIgnoreCase(sectionName)) {
            return true;
        }

        if (StringUtils.isEmpty(groupName)) {
            return true;
        }

        if (groupName.equalsIgnoreCase("SOLDE_VI")) {
            return false;
        }

        if (groupName.contains("ASSY") || groupName.contains("OBA") || groupName.contains("PACK_") || groupName.contains("CHECK_") || groupName.contains("R_") || groupName.contains("VI")) {
            return true;
        }

        return false;
    }

    public static String getWorkSectionCondition(ShiftType shiftType) {
        if (shiftType == null) {
            return "(work_date || trim(to_char(work_section,'00'))) >= (:current || '08') and (work_date || trim(to_char(work_section,'00'))) < (:next || '08') ";
        } else if (shiftType == ShiftType.DAY) {
            return "(work_date || trim(to_char(work_section,'00'))) >= (:current || '08') and (work_date || trim(to_char(work_section,'00'))) < (:current || '20') ";
        } else {
            return "(work_date || trim(to_char(work_section,'00'))) >= (:current || '20') and (work_date || trim(to_char(work_section,'00'))) < (:next || '08') ";
        }
    }

    public static String getWorkTimeColumn() {
        return "(case when work_section < 8 then (to_char(to_date(work_date, 'yyyymmdd') - TO_DSINTERVAL('1 00:00:00'), 'yyyymmdd') || ' NIGHT') when work_section < 20 then (work_date || ' DAY') else (work_date || ' NIGHT') end)";
    }

    public static String getWorkDateBetween() {
        return "(work_date || trim(to_char(work_section,'00'))) >= (:current || '08') and (work_date || trim(to_char(work_section,'00'))) < (:next || '08') ";
    }

    public  static String getWorkDateWorkSectionByMonth(){
        return "(case when (work_section < 8 and substr(work_date, 6, 2) = '01')  then to_char(to_date(work_date, 'yyyymmdd') - TO_DSINTERVAL('1 00:00:00'), 'yyyy/mm') else to_char(to_date(work_date, 'yyyymmdd'), 'yyyy/mm') end) ";

    }

    public  static String getWorkDateWorkSectionByWeek(){
        return "(case when (work_section < 8 and to_char(to_date(work_date, 'yyyymmdd'), 'D') = '1')  then to_char(to_date(work_date, 'yyyymmdd') - TO_DSINTERVAL('1 00:00:00'), 'yyyy-iw') else to_char(to_date(work_date, 'yyyymmdd'), 'yyyy/iw') end) ";

    }

    public  static String getWorkDateWorkSectionByDay(){
        return "(case when work_section < 8 then to_char(to_date(work_date, 'yyyymmdd') - TO_DSINTERVAL('1 00:00:00'), 'yyyy/mm/dd') else to_char(to_date(work_date, 'yyyymmdd'), 'yyyy/mm/dd') end) ";

    }
}
