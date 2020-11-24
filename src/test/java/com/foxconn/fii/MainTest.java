package com.foxconn.fii;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxconn.fii.common.TimeSpan;
import com.foxconn.fii.data.primary.model.SpcData;
import com.foxconn.fii.data.primary.model.entity.TestErrorMeta;
import com.foxconn.fii.data.primary.model.entity.TestUphTarget;
import com.foxconn.fii.receiver.test.util.TestUtils;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MainTest {

    @Test
    public void testEmail() {
//        log.info("{}", CommonUtils.validateEmail("cpe-vn-fii-sw@mail.foxconn.com"));
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        try {
//            calendar.setTime(df.parse("04/17/2020"));
//            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//            log.info("### {}", calendar.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        String oldFormula = "E3+F3+G5-G4";
//        String[] cellAddresses = oldFormula.split("[+\\-\\*\\/]");
//        log.info("### {}", cellAddresses);

//        String cell = "12-Ju";
//        log.info("{}", cell.matches(".+-Ju$"));

//        List<Double> values = Arrays.asList(1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1032.0,1147.0,1147.0,1032.0,1147.0,1147.0,1147.0,1032.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1032.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1032.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0,1147.0);
//        SpcData spc = TestUtils.calculateCPKData(values, 1000.0, null);
//        log.info("{}", spc);

        String raw = "~øþæ†x3æþ";
        String hex = Hex.encodeHexString(raw.getBytes());
        log.info("{}", hex);
    }

    @Test
    public void testLoadFail() {

        BufferedReader reader;
        try {
            List<TestErrorMeta> errorMetaList = new ArrayList<>();
            reader = new BufferedReader(new FileReader("C:\\Users\\V0946495\\Desktop\\ErrorCode.h.txt"));
            String line = reader.readLine();
            while(line != null) {
                if (line.startsWith("#define")) {
                    log.debug(line);
                    TestErrorMeta errorMeta = new TestErrorMeta();
                    String[] word = line.split(" ");
                    errorMeta.setErrorCode(word[1]);

                    word = line.split("\"");
                    errorMeta.setDescription(word[1]);
                    errorMetaList.add(errorMeta);
                }

                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testLoadErrorCodeFromB04() {
        BufferedReader reader;
        try {
            List<TestErrorMeta> errorMetaList = new ArrayList<>();
            reader = new BufferedReader(new FileReader("C:\\Users\\V0946495\\Desktop\\test-machine\\P02X001.00_ErrorCode.txt"));
            String line = reader.readLine();
            while(line != null) {
                line = StringUtils.trimWhitespace(line);
                log.debug(line);

                if (!StringUtils.isEmpty(line)) {
                    TestErrorMeta errorMeta = new TestErrorMeta();
                    errorMeta.setFactory("B04");
                    errorMeta.setModelName("P02X001.00");

                    String[] word = line.split(" = ");
                    errorMeta.setErrorCode(word[0]);

                    word = word[1].split("'");
                    errorMeta.setDescription(word[1]);
                    errorMetaList.add(errorMeta);
                }

                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void regexTest() throws Exception {
//        Pattern pattern = Pattern.compile("(.*)( device$)");
        Pattern pattern = Pattern.compile("^(-?\\d*\\.?\\d*)$");
        String str = "9.012";
        Matcher matcher = pattern.matcher(str);
        assertThat(matcher.find()).isTrue();
    }

    @Test
    public void joiningTest() throws JsonProcessingException {
        TestErrorMeta errorMeta1 = new TestErrorMeta();
        errorMeta1.setFactory("B06");
        errorMeta1.setErrorCode("12435");
        errorMeta1.setDescription("qwerty");

        TestErrorMeta errorMeta2 = new TestErrorMeta();
        errorMeta2.setFactory("B04");
        errorMeta2.setErrorCode("124356");
        errorMeta2.setDescription("qwertyu");

        List<TestErrorMeta> stringList = Arrays.asList(errorMeta1, errorMeta2);
        assertThat(new ObjectMapper().writeValueAsString(stringList)).isEqualTo("");
    }


    @Test
    public void dfTest() throws ParseException, IOException {
        SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy h:mm:ss aa");
        Date a = df.parse("3/13/2019 8:42:00 AM");
        log.info("{}", a);

//        String json = "[{\"Model\":\"U10C135.10\",\"Group\":\"RC\",\"Station\":\"L04C135RC015\",\"RetestRate\":\"5.4545455\",\"FirstPassRate\":\"94.545456\",\"Top3errorcode\":\"MI03_05\",\"Suggetions\":\"MI03_05 - SQE-PQE help to improve this issue\"}]";
//        List<TestAbnormalInfo> abnormalInfo = new ObjectMapper().readValue(json, new TypeReference<List<TestAbnormalInfo>>(){});
//        log.info("{}", abnormalInfo.size());
//        String[] errorCodeTmp = "asfaf".split(" ");
//        assertThat(errorCodeTmp.length).isEqualTo(1);

//        CivetMsgBase data = CivetTextMsg.Create("asfhasjkhf");
//        assertThat((data instanceof CivetNewsMsg)).isEqualTo(false);

//        File file = new File("C:\\Users\\V0946495\\Desktop\\b04.png");
//        byte[] encoded = Base64.encodeBase64(FileCopyUtils.copyToByteArray(file));
//        String strEncoded = new String(encoded, StandardCharsets.US_ASCII);
//        log.info("{}", strEncoded);
    }

    @Test
    public void testDate() throws IOException {
        TimeSpan timeSpan = TimeSpan.now(TimeSpan.Type.DAILY);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSpan.getStartDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);

        log.info("{}", calendar.getTime());
    }

    @Test
    public void productionSchedulerTest() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        List<MpsData> mps =  new ArrayList<>();
        mps.add(MpsData.of("U10L119.XX", Arrays.asList("N1"), 13800, df.parse("2019/08/01")));
        mps.add(MpsData.of("F01L040.10", Arrays.asList("N2"), 12900, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C150.00", Arrays.asList("N3"), 8300, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C135.10", Arrays.asList("N4"), 38500, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C135.50", Arrays.asList("N4"), 24500, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C100.30", Arrays.asList("N5"), 3400, df.parse("2019/08/01")));
        mps.add(MpsData.of("UBN2304.00", Arrays.asList("N5"), 2000, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C147.00", Arrays.asList("N6"), 22400, df.parse("2019/08/01")));
        mps.add(MpsData.of("U10C142.00", Arrays.asList("N6"), 1800, df.parse("2019/08/01")));

        Map<String, TestUphTarget> uph = new HashMap<>();
        TestUphTarget uphTarget = new TestUphTarget();
        uphTarget.setModelName("U10L119.XX");
        uphTarget.setLineName("N1");
        uphTarget.setUph(3200);
        uphTarget.setWorkTime(8);
        uph.put("U10L119.XX_N1", uphTarget);

        TestUphTarget uphTarget2 = new TestUphTarget();
        uphTarget2.setModelName("F01L040.10");
        uphTarget2.setLineName("N2");
        uphTarget2.setUph(2800);
        uphTarget2.setWorkTime(8);
        uph.put("F01L040.10_N2", uphTarget2);

        TestUphTarget uphTarget3 = new TestUphTarget();
        uphTarget3.setModelName("U10C150.00");
        uphTarget3.setLineName("N1");
        uphTarget3.setUph(1800);
        uphTarget3.setWorkTime(9);
        uph.put("U10C150.00_N3", uphTarget3);

        TestUphTarget uphTarget4 = new TestUphTarget();
        uphTarget4.setModelName("U10C135.10");
        uphTarget4.setLineName("N4");
        uphTarget4.setUph(9000);
        uphTarget4.setWorkTime(7);
        uph.put("U10C135.10_N4", uphTarget4);

        TestUphTarget uphTarget5 = new TestUphTarget();
        uphTarget5.setModelName("U10C135.50");
        uphTarget5.setLineName("N5");
        uphTarget5.setUph(9000);
        uphTarget5.setWorkTime(10);
        uph.put("U10C135.50_N4", uphTarget5);

        TestUphTarget uphTarget6 = new TestUphTarget();
        uphTarget6.setModelName("U10C100.30");
        uphTarget6.setLineName("N5");
        uphTarget6.setUph(2000);
        uphTarget6.setWorkTime(10);
        uph.put("U10C100.30_N5", uphTarget6);

        TestUphTarget uphTarget7 = new TestUphTarget();
        uphTarget7.setModelName("UBN2304.00");
        uphTarget7.setLineName("N5");
        uphTarget7.setUph(2000);
        uphTarget7.setWorkTime(10);
        uph.put("UBN2304.00_N5", uphTarget7);

        TestUphTarget uphTarget8 = new TestUphTarget();
        uphTarget8.setModelName("U10C147.00");
        uphTarget8.setLineName("N6");
        uphTarget8.setUph(3200);
        uphTarget8.setWorkTime(10);
        uph.put("U10C147.00_N6", uphTarget8);

        TestUphTarget uphTarget9 = new TestUphTarget();
        uphTarget9.setModelName("U10C142.00");
        uphTarget9.setLineName("N6");
        uphTarget9.setUph(1800);
        uphTarget9.setWorkTime(10);
        uph.put("U10C142.00_N6", uphTarget9);

        Calendar calendar = Calendar.getInstance();

        Map<String, PlanData> result = new LinkedHashMap<>();
        for (int j=0; j<6; j++) {
            String lineName = "N" + (j+1);
            calendar.setTime(mps.get(0).getStartDate());
            for (int i = 0; i < 7; i++) {
                result.put(lineName + "_" + df.format(calendar.getTime()), PlanData.of(lineName, new ArrayList<>(), new ArrayList<>(), calendar.getTime(), new ArrayList<>()));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        for (MpsData mpsData : mps) {
            int targetOutput = mpsData.getPlan();
            for (String lineName : mpsData.getLineNameList()) {
                TestUphTarget target = uph.getOrDefault(mpsData.getModelName() + "_" + lineName, uphTarget);
                calendar.setTime(mpsData.getStartDate());
                for (int i = 0; i < 7; i++) {
                    PlanData plan = result.get(lineName + "_" + df.format(calendar.getTime()));
                    double totalWork = plan.getWorkList().stream().mapToDouble(p -> p).sum();
                    if (totalWork < 16) {
                        if (targetOutput > 0) {
                            double uphV = target.getUph() / 16.0f;
                            double hour = targetOutput *1.0f / uphV;
                            if (hour < 16-totalWork) {
                                plan.getModelNameList().add(mpsData.getModelName());
                                Double output = targetOutput < (hour * uphV) ? targetOutput : (hour * uphV);
                                plan.getPlanList().add(output.intValue());
                                plan.getWorkList().add(output / uphV);
                                targetOutput -= output;
                            } else {
                                plan.getModelNameList().add(mpsData.getModelName());
                                Double output = (16-totalWork) == 16 ? target.getUph() : ((16-totalWork) * uphV);
                                output = targetOutput < output ? targetOutput : output;
                                plan.getPlanList().add(output.intValue());
                                plan.getWorkList().add(output / uphV);
                                targetOutput -= output;
                            }
                        }
                    }
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
            }
        }

        log.info("{}", result.size());
    }

    @Value(staticConstructor = "of")
    public static class MpsData {
        private String modelName;

        private List<String> lineNameList;

        private int plan;

        private Date startDate;
    }

    @Value(staticConstructor = "of")
    public static class PlanData {
        private String lineName;

        private List<String> modelNameList;

        private List<Integer> planList;

        private Date startDate;

        private List<Double> workList;
    }
}