package com.pranavprksn.hedp.models;

public class DataModel {

    String inputText;
    String encryptedText;

    public String getInputText() {
        return inputText;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public DataModel(String inputText, String encryptedText) {
        this.inputText = inputText;
        this.encryptedText = encryptedText;
    }

    public DataModel() {
    }
}
