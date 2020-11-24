package com.foxconn.fii.data.primary.model;

import lombok.Data;

@Data
public class ProductionResponse {

    private int pass;

    private int fail;

    private int uph;

    public static ProductionResponse of(int pass, int fail, int uph) {
        ProductionResponse ins = new ProductionResponse();
        ins.setPass(pass);
        ins.setFail(fail);
        ins.setUph(uph);

        return ins;
    }
}
