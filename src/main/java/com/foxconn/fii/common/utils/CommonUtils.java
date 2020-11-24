package com.foxconn.fii.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

    public boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public String getExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int index = filename.lastIndexOf(".");
            return index == -1 ? "" : filename.substring(index + 1);
        }
    }
}
