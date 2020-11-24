package com.foxconn.fii.data.b06te.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "TE_FIXTURE_BORROW_HISTORY")
public class TeFixtureBorrowHistory {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TeFixtureBorrowHistory_SEQ")
    @SequenceGenerator(sequenceName = "TE_FIXTURE_BORROW_HISTORY_SEQ", allocationSize = 1, name = "TeFixtureBorrowHistory_SEQ")
    private long id;

    @Column(name = "FIXTURE_ID")
    private Long fixtureId;

    @Column(name = "RECEIVE_TIME")
    private Date receiveTime;

    @Column(name = "RECEIVE_EMPLOYEE_ID")
    private String receiveEmployeeId;

    @Column(name = "RECEIVE_NOTE")
    private String receiveNote;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "RETURN_TIME")
    private Date returnTime;

    @Column(name = "RETURN_EMPLOYEE_ID")
    private String returnEmployeeId;

    @Column(name = "RETURN_NOTE")
    private String returnNote;

    public TeFixtureBorrowHistory(){}

    public TeFixtureBorrowHistory(Map<String, Object> data) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (data.containsKey("fixtureId")) {
            this.setFixtureId(Long.valueOf((int)data.get("fixtureId")));
        }

        if (data.containsKey("receiveTime") && !StringUtils.isEmpty(data.get("receiveTime"))) {
            this.setReceiveTime(sdf.parse((String) data.get("receiveTime")));
        } else {
            this.setReceiveTime(null);
        }

        if (data.containsKey("receiveEmployeeId")) {
            this.setReceiveEmployeeId((String) data.get("receiveEmployeeId"));
        }

        if (data.containsKey("receiveNote")) {
            this.setReceiveNote((String) data.get("receiveNote"));
        }

        if (data.containsKey("dueDate") && !StringUtils.isEmpty(data.get("dueDate"))) {
            this.setDueDate(sdf.parse((String) data.get("dueDate")));
        } else {
            this.setDueDate(null);
        }

        if (data.containsKey("returnTime") && !StringUtils.isEmpty(data.get("returnTime"))) {
            this.setReturnTime(sdf.parse((String) data.get("returnTime")));
        } else {
            this.setReturnTime(null);
        }

        if (data.containsKey("returnEmployeeId")) {
            this.setReturnEmployeeId((String) data.get("returnEmployeeId"));
        }

        if (data.containsKey("returnNote")) {
            this.setReturnNote((String) data.get("returnNote"));
        }
    }

    public void readData(Map<String, Object> data) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (data.containsKey("fixtureId")) {
            this.setFixtureId(Long.valueOf((int)data.get("fixtureId")));
        }

        if (data.containsKey("receiveTime") && !StringUtils.isEmpty(data.get("receiveTime"))) {
            this.setReceiveTime(sdf.parse((String) data.get("receiveTime")));
        } else {
            this.setReceiveTime(null);
        }

        if (data.containsKey("receiveEmployeeId")) {
            this.setReceiveEmployeeId((String) data.get("receiveEmployeeId"));
        }

        if (data.containsKey("receiveNote")) {
            this.setReceiveNote((String) data.get("receiveNote"));
        }

        if (data.containsKey("dueDate") && !StringUtils.isEmpty(data.get("dueDate"))) {
            this.setDueDate(sdf.parse((String) data.get("dueDate")));
        } else {
            this.setDueDate(null);
        }

        if (data.containsKey("returnTime") && !StringUtils.isEmpty(data.get("returnTime"))) {
            this.setReturnTime(sdf.parse((String) data.get("returnTime")));
        } else {
            this.setReturnTime(null);
        }

        if (data.containsKey("returnEmployeeId")) {
            this.setReturnEmployeeId((String) data.get("returnEmployeeId"));
        }

        if (data.containsKey("returnNote")) {
            this.setReturnNote((String) data.get("returnNote"));
        }
    }

}
