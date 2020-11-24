package com.foxconn.fii.data.primary.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CpkFromFileRequest {

    private String tmpFile;

    private List<TestItemRequest> testItemList = new ArrayList<>();

    @Data
    public static class TestItemRequest {
        private String testItem;

        private int columnIndex;

        private String sampleValue;

        private Double lsl;

        private Double usl;
    }
}
