package com.foxconn.fii.data.primary.model;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Data
public class CpkFromDatabaseRequest {

    private String factory;

    private String customer;

    private String modelName;

    private String groupName;

    private String stationName;

    private List<String> parameters = new ArrayList<>();

    private String timeSpan;
}
