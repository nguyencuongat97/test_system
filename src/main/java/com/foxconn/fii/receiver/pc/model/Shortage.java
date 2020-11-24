package com.foxconn.fii.receiver.pc.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class Shortage {
    private String buyerCode;
    private String buyerName;
    private String part;
    private String partDescription;
    private String partCompany;
    private String partModelList;
    private String partNote;
    private double partLeadTime;
    private int stock;
    private int grStock;
    private String actions;
    private List<String> modelList = new ArrayList<>();
    private int shortage6w = 0;
    private int shortage10w = 0;

    public Map<String, Integer> getActionMap() {
        Map<String, Integer> actionMap = new HashMap<>();
        if (StringUtils.isEmpty(actions) || !actions.contains(": ")) {
            return actionMap;
        }

        String[] trimmedAction = actions.split(": ");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String[] actionList = trimmedAction[1].split(",");
        for (String action : actionList) {
            String[] actionItem = action.split("\\*");
            if (actionItem.length == 2) {
                try {
                    calendar.setTime(df.parse(actionItem[0]));
                    if (calendar.get(Calendar.DAY_OF_WEEK) < 3) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    } else {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        calendar.add(Calendar.WEEK_OF_YEAR, 2);
                    }
                    actionMap.put(df2.format(calendar.getTime()), Integer.parseInt(actionItem[1]));
                } catch (Exception e) {
                    log.debug("### get action map error cause by datetime {} format is not support", actionItem[0]);
                }
            }
        }
        return actionMap;
    }

    public int getStatus() {
        if (shortage6w < 0) {
            return 0;
        } else if (shortage10w < 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public int getShortageTotal() {
        if (getStatus() == 0) {
            return shortage6w;
        } else {
            return shortage10w;
        }
    }
}
