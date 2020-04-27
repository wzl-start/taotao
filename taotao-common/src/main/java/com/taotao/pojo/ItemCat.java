package com.taotao.pojo;

import java.io.Serializable;
import java.util.List;

public class ItemCat implements Serializable{
    private String u;
    private String n;
    private List<?> i;

    @Override
    public String toString() {
        return "ItemCat{" +
                "u='" + u + '\'' +
                ", n='" + n + '\'' +
                ", i=" + i +
                '}';
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public List<?> getI() {
        return i;
    }

    public void setI(List<?> i) {
        this.i = i;
    }
}
