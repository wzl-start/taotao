package com.taotao.pojo;

public class PictureData {
    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "PictureData{" +
                "src='" + src + '\'' +
                '}';
    }
}
