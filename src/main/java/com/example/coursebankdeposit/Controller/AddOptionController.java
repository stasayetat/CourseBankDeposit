package com.example.coursebankdeposit.Controller;

import com.example.coursebankdeposit.DefaultDeposit.DefaultDeposit;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddOptionController {
    @FXML
    private TextField depositNameField;
    @FXML
    private TextField percentageField;
    @FXML
    private TextField amountMoneyField;
    @FXML
    private TextField termOfDepositField;
    @FXML
    private TextField earlyTermField;
    @FXML
    private TextField earlyAmountField;
    @FXML
    private TextField monthlyAddField;
    @FXML
    private ChoiceBox<String> currencyChoiceBox;
    @FXML
    private ChoiceBox<String> capitalizationField;
    private String[] cur = {"UAH", "USD", "EUR"};
    private String[] yesNo = {"Yes", "No"};
    private DefaultDeposit defaultDeposit;
    private Stage dialogStage;
    private boolean okClick = false;

    public void setDeposit(DefaultDeposit defaultDeposit){
        this.defaultDeposit = defaultDeposit;
    }
    @FXML
    private void initialize() {
        currencyChoiceBox.getItems().addAll(cur);
        capitalizationField.getItems().addAll(yesNo);
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public boolean isOkClick() {
        return okClick;
    }

    @FXML
    private void buttonOk() {

        if(isInputValid()) {
            defaultDeposit.setDepositNameName(depositNameField.getText());
            defaultDeposit.setPercentage(Double.parseDouble(percentageField.getText()));
            defaultDeposit.setAmountMoney(Double.parseDouble(amountMoneyField.getText()));
            defaultDeposit.setTermOfDeposit(Integer.parseInt(termOfDepositField.getText()));
            defaultDeposit.setEarlyTerm(Integer.parseInt(earlyTermField.getText()));
            defaultDeposit.setEarlyAmount(Double.parseDouble(earlyAmountField.getText()));
            defaultDeposit.setMonthlyCapitalization(Integer.valueOf(capitalizationField.getValue()));
            defaultDeposit.setAmountMonthlyAdd(Integer.parseInt(monthlyAddField.getText()));
            defaultDeposit.setCurrency(currencyChoiceBox.getValue());
            defaultDeposit.calcInvest();
            okClick = true;
            dialogStage.close();
        }
    }

    @FXML
    private void buttonCansel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMes = new String();
        try {
            if(Double.parseDouble(percentageField.getText())  <= 0){
                errorMes += "Неправильний відсоток!\n";
            }
            if(Double.parseDouble(amountMoneyField.getText()) <= 0){
                errorMes += "Неправильна кількість грошей!\n";
            }
            if(Integer.parseInt(termOfDepositField.getText()) <= 0){
                errorMes += "Неправильний термін депозиту!\n";
            }
            if(Integer.parseInt(earlyTermField.getText()) < 0 || Integer.parseInt(earlyTermField.getText()) > Integer.parseInt(termOfDepositField.getText())){
                errorMes += "Неправильний час для забирання грошей!\n";
            }
            if(Integer.parseInt(earlyTermField.getText()) < 0 || Integer.parseInt(earlyTermField.getText()) > Double.parseDouble(amountMoneyField.getText())){
                errorMes += "Неправильний кількість грошей для забирання грошей!\n";
            }
            if(currencyChoiceBox.getValue() == null)
                errorMes += "Невибрано валюту!\n";

            if(capitalizationField.getValue() == null)
                errorMes += "Невибрано капіталізацію!\n";
            else {
                if(capitalizationField.getValue().equals("Yes")){
                    capitalizationField.setValue("1");
                } else {
                    capitalizationField.setValue("0");
                }
            }
            if(Integer.parseInt(monthlyAddField.getText()) < 0){
                errorMes += "Неправильне щомісячне додавання грошей!\n";
            }

        } catch (NumberFormatException e){
            errorMes += "Неправильний ввід!\n";
        }
        if(errorMes.length() == 0){
            return true;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Помилка вводу");
            alert.setHeaderText("Спробуйте ще раз");
            alert.setContentText(errorMes);
            alert.showAndWait();
            return false;
        }

    }
}
