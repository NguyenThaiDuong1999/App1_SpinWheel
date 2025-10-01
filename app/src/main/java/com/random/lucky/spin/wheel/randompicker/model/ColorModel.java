package com.random.lucky.spin.wheel.randompicker.model;

public class ColorModel {
    private int imgPreview;
    private String colorCode;
    private boolean isSelect;

    public ColorModel() {

    }

    public ColorModel(String colorCode, boolean isSelect) {
        this.colorCode = colorCode;
        this.isSelect = isSelect;
    }

    public ColorModel(int imgPreview, String colorCode) {
        this.imgPreview = imgPreview;
        this.colorCode = colorCode;
    }

    public ColorModel(int imgPreview, String colorCode, boolean isSelect) {
        this.imgPreview = imgPreview;
        this.colorCode = colorCode;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(int imgPreview) {
        this.imgPreview = imgPreview;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
