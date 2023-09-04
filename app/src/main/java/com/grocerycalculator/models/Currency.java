package com.grocerycalculator.models;

public class Currency {

    private String countryCode;
    private String countryName;
    private String currencySymbol;
    private String flagName;

    public Currency(String countryCode, String countryName, String currencySymbol, String flagName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.currencySymbol = currencySymbol;
        this.flagName = flagName;
    }

    public String getCountryCode() {

        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getFlagName() {
        return flagName;
    }

    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }
}
