package com.example.coursebankdeposit.Controller;

import com.example.coursebankdeposit.DefaultDeposit.DefaultDeposit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class InfoOptionController {
    @FXML
    private TableView earnTable;
    @FXML
    private TableColumn<Map, Integer> monthColumn;
    @FXML
    private TableColumn<Map, Double> earnColumn;
    @FXML
    private Label companyNameField;
    @FXML
    private Label depositNameField;
    @FXML
    private Label percentageField;
    @FXML
    private Label amountMoneyField;
    @FXML
    private Label termField;
    @FXML
    private Label monthlyAddField;
    @FXML
    private Label earnField;
    @FXML
    private Label curField;

    private Stage dialogStage;

    @FXML
    private void initialize() {
        monthColumn.setCellValueFactory(new MapValueFactory<>("Місяць"));
        earnColumn.setCellValueFactory(new MapValueFactory<>("Заробіток"));
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void showInfo(DefaultDeposit defaultDeposit){
        if(defaultDeposit != null) {
            companyNameField.setText(defaultDeposit.getCompanyName());
            depositNameField.setText(defaultDeposit.getDepositName());
            percentageField.setText(String.valueOf(defaultDeposit.getPercentage()));
            amountMoneyField.setText(String.valueOf(defaultDeposit.getAmountMoney()));
            termField.setText(String.valueOf(defaultDeposit.getTermOfDeposit()));
            monthlyAddField.setText(String.valueOf(defaultDeposit.getMonthlyAdd()));
            earnField.setText(String.valueOf(defaultDeposit.getMayEarnMoney()));
            curField.setText(defaultDeposit.getCurrency());
        }
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
        Map<String, Object> item1 = new HashMap<>();
        defaultDeposit.calcInvest();
        Map<Integer, Double> depositMap = defaultDeposit.getEarnInfo();
        for(Map.Entry mp : depositMap.entrySet()){
            item1.put("Місяць", mp.getKey());
            item1.put("Заробіток", mp.getValue());
            items.add(item1);
            item1 = new HashMap<>();
        }
        earnTable.getItems().addAll(items);
    }

    @FXML
    private void buttonCansel() {
        dialogStage.close();
    }

    @FXML
    private void showInfoEarn(DefaultDeposit deposit) {
            return;
    }


}
