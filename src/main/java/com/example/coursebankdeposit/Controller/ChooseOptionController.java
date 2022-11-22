package com.example.coursebankdeposit.Controller;

import com.example.coursebankdeposit.DefaultDeposit.DefaultDeposit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class ChooseOptionController{
    @FXML
    private TableView<DefaultDeposit> chooseTable;
    @FXML
    private TableColumn<DefaultDeposit, String> companyNameColumn;
    @FXML
    private TableColumn<DefaultDeposit, String> depositNameColumn;
    @FXML
    private TableColumn<DefaultDeposit, Double> percentageColumn;
    @FXML
    private TableColumn<DefaultDeposit, Integer> minMoneyColumn;
    @FXML
    private TextField termField;
    @FXML
    private TextField amountMoneyField;

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
    private ObservableList<DefaultDeposit> chooseData = FXCollections.observableArrayList();

    @FXML
    public void chooseDeposit(DefaultDeposit defaultDeposit){
        setTable();
        this.defaultDeposit = defaultDeposit;
    }

    @FXML
    private void initialize() {
        currencyChoiceBox.getItems().addAll(cur);
        capitalizationField.getItems().addAll(yesNo);
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        depositNameColumn.setCellValueFactory(cellData -> cellData.getValue().depositNameProperty());
        percentageColumn.setCellValueFactory(cellData -> cellData.getValue().percentageProperty().asObject());
        minMoneyColumn.setCellValueFactory(cellData -> cellData.getValue().minMoneyProperty().asObject());
    }

    public void setTable() {
        DefaultDeposit depositDatabase;
        String url = "jdbc:sqlserver://DESKTOP-V1JT0BJ\\MSSQLSERVERDEV;databaseName=BankDeposit;encrypt=true;trustServerCertificate=true;";
        String user = "SqlJava";
        String password = "1";
        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM ChooseData";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
            String companyNameChoose = result.getString("Company Name");
            String depositNameChoose = result.getString("Deposit Name");
            Double percentageChoose = result.getDouble("Percentage");
            Integer minInvestChoose = result.getInt("MinInvestMoney");
                DefaultDeposit defDeposit = DefaultDeposit.builder()
                        .setCompanyName(companyNameChoose)
                        .setDepositName(depositNameChoose)
                        .setPercentage(percentageChoose)
                        .setMinInvestMoney(minInvestChoose)
                        .build();
                chooseData.add(defDeposit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        chooseTable.setItems(chooseData);
    }

    public boolean isOkClick() {
        return okClick;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    @FXML
    private void buttonCansel() {
        dialogStage.close();
    }

    @FXML
    private void buttonOk(){
        if(isInputValid()) {
            defaultDeposit.setCompanyName(chooseTable.getSelectionModel().getSelectedItem().getCompanyName());
            defaultDeposit.setDepositNameName(chooseTable.getSelectionModel().getSelectedItem().getDepositName());
            defaultDeposit.setPercentage(chooseTable.getSelectionModel().getSelectedItem().getPercentage());
            defaultDeposit.setAmountMoney(Double.parseDouble(amountMoneyField.getText()));
            defaultDeposit.setTermOfDeposit(Integer.parseInt(termField.getText()));
            defaultDeposit.setMonthlyCapitalization(Integer.valueOf(capitalizationField.getValue()));
            defaultDeposit.setCurrency(currencyChoiceBox.getValue());
            defaultDeposit.setAmountMonthlyAdd(Integer.valueOf(monthlyAddField.getText()));
            defaultDeposit.calcInvest();
            okClick = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMes = new String();
        try {
            if(chooseTable.getSelectionModel().getSelectedItem() == null){
                errorMes += "Не вибрано шаблон депозиту!\n";
                throw new NumberFormatException();
            }
            if(Double.parseDouble(termField.getText())  <= 0){
                errorMes += "Неправильний час!\n";
            }
            if(Double.parseDouble(amountMoneyField.getText()) <= 0 || Double.parseDouble(amountMoneyField.getText()) <= chooseTable.getSelectionModel().getSelectedItem().getMinInvestMoney()){
                errorMes += "Неправильна кількість грошей!\n";
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
