package com.foxconn.fii.receiver.test.util;

import com.foxconn.fii.common.TimeSpan;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TestUtilsTest {

    @Test
    public void isOutSpecTest() {
        boolean result = TestUtils.isOutSpec(51, 50, 3.25F, 3.00F, 96.75F, 97.00F);
        assertThat(result).isTrue();

        result = TestUtils.isOutSpec(31, 50, 3.25F, 3.00F, 96.75F, 97.00F);
        assertThat(result).isFalse();

        result = TestUtils.isOutSpec(31, null, 3.25F, 3.00F, 96.75F, 97.00F);
        assertThat(result).isTrue();

        result = TestUtils.isOutSpec(31, null, 3.25F, null, 96.75F, 97.00F);
        assertThat(result).isTrue();

        result = TestUtils.isOutSpec(31, null, 3.25F, null, 96.75F, null);
        assertThat(result).isFalse();
    }

    @Test
    public void cpkTest() {

        Variance variance = new Variance();

        double[] total = {3.32965,3.335946,3.3002,3.32686,3.308965,3.309266,3.298811,3.318104,3.333747,3.319136,3.307658,3.32978,3.295257,3.358453,3.308929,3.325136,3.307175,3.317422,3.32182,3.322035,3.317535,3.325035,3.340075,3.311233,3.329294,3.316306,3.315928,3.324422,3.319436,3.304415,3.3015,3.309742,3.347321,3.318511,3.356328,3.317086,3.320405,3.317616,3.316007,3.2961,3.331484,3.314061,3.328325,3.327009,3.313792,3.341389,3.322345,3.343929,3.293647,3.31436,3.320738,3.296329,3.311598,3.330188,3.315791,3.319907,3.341865,3.332327,3.355337,3.312778,3.330959,3.311041,3.352304,3.296854,3.324441,3.33461,3.336262,3.324942,3.321214,3.336289,3.334783,3.35067,3.304695,3.315869,3.326043,3.352221,3.319281,3.343513,3.32691,3.321489,3.290982,3.326604,3.329857,3.273889,3.315429,3.314488,3.346205,3.293236,3.314133,3.330828,3.326516,3.316268,3.345109,3.329901,3.31371,3.3105,3.311599,3.338818,3.340404,3.306933,3.286706,3.335728,3.311263,3.336782,3.322114,3.342716,3.322855,3.33489,3.335228,3.324389,3.310028,3.30561,3.324769,3.319613,3.319859,3.312877,3.300323,3.313848,3.326706,3.332669,3.323316,3.327352,3.306458,3.322415,3.306748,3.334206,3.323362,3.320912,3.316073,3.349255,3.326244,3.320398};

        double[] sub1 = {3.32965,3.335946,3.3002,3.32686,3.308965,3.309266,3.298811,3.318104,3.333747,3.319136,3.307658,3.32978,3.295257,3.358453,3.308929,3.325136,3.307175,3.317422,3.32182,3.322035,3.317535,3.325035,3.340075,3.311233,3.329294,3.316306,3.315928,3.324422,3.319436,3.304415,3.3015,3.309742,3.347321,3.318511,3.356328,3.317086,3.320405,3.317616,3.316007,3.2961,3.331484,3.314061,3.328325,3.327009,3.313792,3.341389,3.322345,3.343929,3.293647,3.31436,3.320738,3.296329,3.311598,3.330188,3.315791,3.319907,3.341865,3.332327,3.355337,3.312778,3.330959,3.311041,3.352304,3.296854};
        double[] sub2 = {3.324441,3.33461,3.336262,3.324942,3.321214,3.336289,3.334783,3.35067,3.304695,3.315869,3.326043,3.352221,3.319281,3.343513,3.32691,3.321489,3.290982,3.326604,3.329857,3.273889,3.315429,3.314488,3.346205,3.293236,3.314133,3.330828,3.326516,3.316268,3.345109,3.329901,3.31371,3.3105,3.311599,3.338818,3.340404,3.306933,3.286706,3.335728,3.311263,3.336782,3.322114,3.342716,3.322855,3.33489,3.335228,3.324389,3.310028,3.30561,3.324769,3.319613,3.319859,3.312877,3.300323,3.313848,3.326706,3.332669,3.323316,3.327352,3.306458,3.322415,3.306748,3.334206,3.323362,3.320912,3.316073,3.349255,3.326244,3.320398};


        int n = total.length;
        int n1 = sub1.length;
        int n2 = sub2.length;

        double avg = Arrays.stream(total).average().getAsDouble();
        double avg1 = Arrays.stream(sub1).average().getAsDouble();
        double avg2 = Arrays.stream(sub2).average().getAsDouble();

        double var = variance.evaluate(total);
        double var1 = variance.evaluate(sub1);
        double var2 = variance.evaluate(sub2);

        double avgC = (n1 * avg1 + n2 * avg2) / (n1 + n2);

        double varC = (n1*(var1 + Math.pow((avg1 - avgC),2)) + n2*(var2 + Math.pow((avg2 - avgC),2))) / (n1 + n2);

        double varC3 = TestUtils.calculateVariance(sub2, var1, avg1, n1);

        double[] sub3 = new double[n1 + 1];
        for(int i = 0; i<n1; i++) {
            sub3[i] = sub1[i];
        }
        sub3[n1] = 3.324441;

        /**
         * x1: the value n+1
         * x: old average
         * varNew = (n / (n+1)) * ( varOld + ( (x1 - x)^2 / (n+1) ))
         * */
        double varC1 = (n1*1.0/(n1+1)) * (var1 + Math.pow((3.324441 - avg1),2)/((n1+1)));
        double varC2 = variance.evaluate(sub3);

        log.info("variance1 {} {} {}", sub1.length, avg1, var1);
        log.info("variance2 {} {} {}", sub2.length, avg2, var2);
        log.info("variance {} {} {}", total.length, avg, var);
        log.info("varianceC {} {} {}", n1 + n2, avgC, varC);
        log.info("varianceC3 {}", varC3);

        log.info("varianceC1 {}", Math.sqrt(varC1));
        log.info("varianceC2 {}", Math.sqrt(varC2));
    }

    @Test
    public void getByTimeMapTest() {
        TimeSpan timeSpan = TimeSpan.from("2019/03/19 00:00 - 2019/03/19 23:59");
        Map<String, Object> map = TestUtils.getHourlyMap(timeSpan.getStartDate(), timeSpan.getEndDate());
        assertThat(map.size()).isEqualTo(12);
    }

    @Test
    public void getWeeklyMapTest() {
        TimeSpan timeSpan = TimeSpan.from("2019/03/18 19:30 - 2019/03/19 07:30");

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        calendar.setTime(timeSpan.getStartDate());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        timeSpan.setStartDate(calendar.getTime());

        Map<String, Object> map = TestUtils.getWeeklyMap(timeSpan.getStartDate(), timeSpan.getEndDate(), timeSpan.getShiftType());
        assertThat(map.size()).isEqualTo(15);

        map = TestUtils.getWeeklyMap(timeSpan.getStartDate(), timeSpan.getEndDate());
        assertThat(map.size()).isEqualTo(15);
    }
}
