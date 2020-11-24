package com.foxconn.fii.data;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MoType {
    /** 2150 2151*/
    NORMAL("2150", "2151"),
    /** 2153 2279*/
    REWORK("2153", "2279"),
    /** 2154*/
    NPI("2154"),
    /** 2155*/
    CONTROL_RUN("2155"),

    ALL("2");

    List<String> prefixList = new ArrayList<>();

    MoType(String... prefixes) {
        this.prefixList = Arrays.asList(prefixes);
    }

    public List<String> getPrefixList() {
        return this.prefixList;
    }

    public boolean contain(String mo) {
        if (StringUtils.isEmpty(mo)) {
            return true;
        }

        for (String prefix : prefixList) {
            if (mo.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
