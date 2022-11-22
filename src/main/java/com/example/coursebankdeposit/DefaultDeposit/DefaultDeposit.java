package com.example.coursebankdeposit.DefaultDeposit;

import javafx.beans.property.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultDeposit {
    private StringProperty companyName = new SimpleStringProperty();
    private StringProperty depositName = new SimpleStringProperty();
    private IntegerProperty earlyTerm = new SimpleIntegerProperty(0);

    private DoubleProperty earlyAmount  = new SimpleDoubleProperty(0);
    private DoubleProperty percentage  = new SimpleDoubleProperty(0);
    private IntegerProperty minInvestMoney  = new SimpleIntegerProperty(0);
    private DoubleProperty amountMoney  = new SimpleDoubleProperty(0);
    private IntegerProperty termOfDeposit  = new SimpleIntegerProperty(0);
    private IntegerProperty amountMonthlyAdd  = new SimpleIntegerProperty(0);
    private IntegerProperty monthlyCapitalization = new SimpleIntegerProperty(0);
    private DoubleProperty mayEarnMoney = new SimpleDoubleProperty(0);
    private StringProperty currency = new SimpleStringProperty(null);
    public Map<Integer, Double> earnMonthly = new LinkedHashMap<>();

    public DefaultDeposit() {

    }

    public DefaultDeposit(String companyName) {
        this.companyName = new SimpleStringProperty(companyName);
    }


    public String getCompanyName() {
        return companyName.get();
    }

    public Integer getMonthlyAdd(){
        return amountMonthlyAdd.get();
    }
     public Double getMayEarnMoney() {
        return mayEarnMoney.get();
    }
    public String getDepositName() {
        return depositName.get();
    }

    public Double getPercentage() {
        return percentage.get();
    }

    public Double getAmountMoney() {
        return amountMoney.get();
    }

    public Integer getMinInvestMoney() {
        return minInvestMoney.get();
    }

    public String getCurrency() {
        return currency.get();
    }

    public Integer getTermOfDeposit() {
        return termOfDeposit.get();
    }

    public Integer getEarlyTerm() {
        return  earlyTerm.get();
    }

    public Double getEarlyAmount() {
        return earlyAmount.get();
    }

    public Integer getCapital() {
        return monthlyCapitalization.get();
    }

    public Map getEarnInfo() {
        return earnMonthly;
    }
    public void setPercentage(Double percentage) {
        this.percentage.set(percentage);
    }
    public void setAmountMoney(Double amountMoney) {
        this.amountMoney.set(amountMoney);
    }

    public void setTermOfDeposit(Integer termOfDeposit) {
        this.termOfDeposit.set(termOfDeposit);
    }

    public void setAmountMonthlyAdd(Integer amountMonthlyAdd) {
        this.amountMonthlyAdd.set(amountMonthlyAdd);
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public void setEarlyAmount(Double earlyAmount) {
        this.earlyAmount.set(earlyAmount);
    }

    public void setEarlyTerm(Integer earlyTerm) {
        this.earlyTerm.set(earlyTerm);
    }

    public void setMonthlyCapitalization(Integer monthlyCapitalization) {
        this.monthlyCapitalization.set(monthlyCapitalization);
    }

    public void setMayEarnMoney(Double mayEarnMoney) {
        this.mayEarnMoney.set(mayEarnMoney);
    }

    public void setDepositNameName(String depositName) {
        this.depositName.set(depositName);
    }

    public void setCompanyName(String companyName){
        this.companyName.set(companyName);
    }

    public StringProperty companyNameProperty() {
        return  companyName;
    }

    public StringProperty depositNameProperty() {
        return  depositName;
    }

    public DoubleProperty amountProperty() {
        return amountMoney;
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }

    public DoubleProperty earningsProperty() {
        return mayEarnMoney;
    }

    public IntegerProperty minMoneyProperty(){return minInvestMoney;}

    public static DepositBuilder builder() {
        return new DepositBuilder();
    }



    public static class DepositBuilder {

        private DefaultDeposit deposit;

        DepositBuilder() {
            this.deposit = new DefaultDeposit();
        }

        public DepositBuilder setCompanyName(String companyName) {
            deposit.companyName.set(companyName);
            return this;
        }

        public DepositBuilder setDepositName(String depositName) {
            deposit.depositName.set(depositName);
            return this;
        }


        public DepositBuilder setPercentage(Double percentage) {
            deposit.percentage.set(percentage);
            return this;
        }

        public DepositBuilder setMinInvestMoney(Integer minInvestMoney) {
            deposit.minInvestMoney.set(minInvestMoney);
            return this;
        }

        public DefaultDeposit build() {
            return deposit;
        }
    }

    public double calcInvest() {
        int chMonth = 1;
        double sumEarn = 0;
        Double tmpAmount = this.getAmountMoney();
        double earnMoney = 0;
        double tmpPercent = this.getPercentage();
        if (this.getCurrency() != "UAH")
            tmpPercent /= 10;

        while(chMonth  <= this.getTermOfDeposit()){
            if(chMonth == this.getEarlyTerm())
                tmpAmount -= this.getEarlyAmount();
            earnMoney = (((double)tmpAmount/100)*(tmpPercent/365)*31);
            this.earnMonthly.put(chMonth, earnMoney);
            tmpAmount += this.getMonthlyAdd();
            if(this.getCapital() == 1)
                tmpAmount += earnMoney;
            sumEarn += earnMoney;
            chMonth++;
        }
        tmpAmount -= this.getMonthlyAdd();
        if(this.getCapital() == 0)
            sumEarn += tmpAmount;
        else{
            sumEarn = tmpAmount;
        }
        this.setMayEarnMoney(sumEarn);
        return sumEarn;
    }

    public String printString() {
        String earlyTermStr;
        String capStr;
        if(this.getEarlyTerm() != 0)
            earlyTermStr = "можливо";
        else
            earlyTermStr = "не можливо";
        if(this.getCapital() == 1)
            capStr = "можливо";
        else
            capStr = "не можливо";
        return "Компанія:" + this.getCompanyName() + ", ім'я депозиту: " + this.getDepositName() + ", відсоток: " + this.getPercentage() + ", можливість раннього зняття коштів: "
                + earlyTermStr + "\n" + ", вкладених грошей: " + this.getAmountMoney() + "\nкількість часу: "
                + this.getTermOfDeposit() + ", капіталізація: " + capStr
                + ", валюта: " + this.getCurrency() + ", можливо зароблені гроші: " + this.getMayEarnMoney() + "\n\n";
    }


}