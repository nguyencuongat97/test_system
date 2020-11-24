package com.foxconn.fii.data.ap.model;

import lombok.Data;

@Data
public class CustKpOnline {

    private String groupWo;

    private String custKpNo;

    private int standardQty;

    private int checkoutPH;

    private int woRequest;

    private int deliverQty;

    private int checkoutQty;

    private int returnQty;

    public float getTime() {
        return (deliverQty - checkoutQty /*- returnQty*/) * 60.0f / checkoutPH / standardQty;
    }
}
