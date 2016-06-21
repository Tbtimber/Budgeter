package com.timber.mdelpierre.budgeter.model;

/**
 * Created by Matthieu on 21/06/2016.
 */
public class TransactionGroup {
    private String tagName;
    private double value;

    public TransactionGroup(String tagName, double value) {
        this.tagName = tagName;
        this.value = value;
    }

    public void setTagName(String tagName, double value) {
        this.tagName = tagName;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTagName() {
        return tagName;
    }

    public double getValue() {
        return value;
    }

    public void addToTotal(double value) {
        this.value += value;
    }
}
