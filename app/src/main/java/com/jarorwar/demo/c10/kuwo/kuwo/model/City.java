package com.jarorwar.demo.c10.kuwo.kuwo.model;

/**
 * Created by marvinmin on 10/18/16.
 */

public class City extends AddressBase{
    private String provinceCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }
}
