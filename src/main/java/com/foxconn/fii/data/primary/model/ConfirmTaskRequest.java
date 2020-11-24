package com.foxconn.fii.data.primary.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
public class ConfirmTaskRequest {

    private Long trackingId;

    private String employee;

    private String errorCode;

    private Integer solutionId;

    private String solutionName;

    private String action;

    private List<String> captionFiles;

    private List<MultipartFile> uploadingFiles;

    private Boolean auto;
}
