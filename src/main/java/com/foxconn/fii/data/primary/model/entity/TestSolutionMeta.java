package com.foxconn.fii.data.primary.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.foxconn.fii.data.b06te.model.B06TestSolutionMeta;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.AttributeConverter;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "test_solution_meta")
public class TestSolutionMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    private String factory;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "solution")
    private String solution;

    @Column(name = "action")
    private String action;

    @Column(name = "employee")
    private String author;

    @Column(name = "number_used")
    private int numberUsed;

    @Column(name = "number_success")
    private int numberSuccess;

    @Convert(converter = StepConverter.class)
    private List<GuidingStep> steps;

    @Column(name = "official", columnDefinition = "TINYINT(2)")
    private Boolean official = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Transient
    private String errorDescription;

    @Data
    public static class GuidingStep {

        private String text;

        private String image;

        public static GuidingStep of (String text, String image) {
            GuidingStep step = new GuidingStep();
            step.setText(text);
            step.setImage(image);
            return step;
        }
    }

    @Slf4j
    @Converter
    public static class StepConverter implements AttributeConverter<List<GuidingStep>, String> {
        private static final ObjectMapper mapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<GuidingStep> stringObject) {
            try {
                return mapper.writeValueAsString(stringObject);
            } catch (JsonProcessingException e) {
                log.error("### convertToDatabaseColumn", e);
                return "";
            }
        }

        @Override
        public List<GuidingStep> convertToEntityAttribute(String s) {
            try {
                return mapper.readValue(s, new TypeReference<List<GuidingStep>>(){});
            } catch (IOException e) {
                log.error("### convertToEntityAttribute", e);
                return null;
            }
        }
    }

//    public static TestSolutionMeta of(B06TestSolutionMeta b06TestSolutionMeta) {
//        TestSolutionMeta solutionMeta = new TestSolutionMeta();
//        solutionMeta.setFactory("B06");
//        solutionMeta.setModelName(b06TestSolutionMeta.getModelName());
//        solutionMeta.setErrorCode(b06TestSolutionMeta.getErrorCode());
//        solutionMeta.setNumberSuccess(b06TestSolutionMeta.getNumberSuccess());
//        solutionMeta.setId(b06TestSolutionMeta.getId());
//        solutionMeta.setSolution(b06TestSolutionMeta.getSolution());
//        solutionMeta.setAction(b06TestSolutionMeta.getAction());
//        solutionMeta.setSteps(new ArrayList<>());
//        solutionMeta.setAuthor(b06TestSolutionMeta.getAuthor());
//        solutionMeta.setCreatedAt(b06TestSolutionMeta.getCreatedAt());
//        return solutionMeta;
//    }
}
