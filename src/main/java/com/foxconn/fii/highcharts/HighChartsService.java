package com.foxconn.fii.highcharts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class HighChartsService {

    @Value("${path.data}")
    private String dataPath;

    @Value("${path.phantomjs}")
    private String phantomjsPath;

    @Value("${path.highcharts-export}")
    private String exportPath;

    public synchronized String export(String options) {
        try {
            Path optionFile = Paths.get(exportPath + "option.js");
            Files.write(optionFile, options.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String outfile = dataPath + "tmp/chart-" + df.format(new Date()) + "-" + (new Random()).nextInt(100) + ".png";
            ProcessBuilder processBuilder = new ProcessBuilder(phantomjsPath + "phantomjs", exportPath + "highcharts-convert.js", "-infile", exportPath + "option.js", "-outfile", outfile, "-width 800");
            processBuilder.directory(new File(phantomjsPath));

            Process process = processBuilder.start();
            int wait = process.waitFor();
            log.info("export exit value {}", wait);

            return outfile;
        } catch (Exception e) {
            log.error("### export error", e);
        }
        return null;
    }
}
