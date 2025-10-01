package com.random.lucky.spin.wheel.randompicker.language;

public class LanguageModel {
    String languageName;
    String isoLanguage;
    int image;
    boolean isCheck;

    public LanguageModel(String languageName, String isoLanguage, int image, boolean isCheck) {
        this.languageName = languageName;
        this.isoLanguage = isoLanguage;
        this.image = image;
        this.isCheck = isCheck;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getIsoLanguage() {
        return isoLanguage;
    }

    public void setIsoLanguage(String isoLanguage) {
        this.isoLanguage = isoLanguage;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
