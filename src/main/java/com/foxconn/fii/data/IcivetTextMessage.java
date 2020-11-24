package com.foxconn.fii.data;

import lombok.Data;

@Data
public class IcivetTextMessage {

    private String content;

    public static IcivetTextMessage of (String content) {
        IcivetTextMessage instance = new IcivetTextMessage();
        instance.setContent(content);
        return instance;
    }
}
