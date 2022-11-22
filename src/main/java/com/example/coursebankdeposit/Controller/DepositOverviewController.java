package com.example.coursebankdeposit.Controller;

import com.example.coursebankdeposit.DefaultDeposit.DefaultDeposit;
import com.example.coursebankdeposit.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DepositOverviewController {
    @FXML
    private TableView<DefaultDeposit> depositTable;
    @FXML
    private TableColumn<DefaultDeposit, String> companyNameColumn;
    @FXML
    private TableColumn<DefaultDeposit, String> depositNameColumn;
    @FXML
    private TableColumn<DefaultDeposit, Double> percentageColumn;
    @FXML
    private TableColumn<DefaultDeposit, Double> amountMoneyColumn;
    @FXML
    private TableColumn<DefaultDeposit, Double> earningsColumn;
    @FXML
    private javafx.scene.control.Button ExitButton;
    @FXML
    private TextField searchField;
    private Main main;

    @FXML
    private void initialize() {
        companyNameColumn.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        depositNameColumn.setCellValueFactory(cellData -> cellData.getValue().depositNameProperty());
        percentageColumn.setCellValueFactory(cellData -> cellData.getValue().percentageProperty().asObject());
        amountMoneyColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        earningsColumn.setCellValueFactory(cellData -> cellData.getValue().earningsProperty().asObject());
    }

    public void setMain(Main main){
        this.main = main;
        depositTable.setItems(main.getDepositData());
    }

    @FXML
    private void deleteOption() {
        try{
            int selectIndex = depositTable.getSelectionModel().getSelectedIndex();
            depositTable.getItems().remove(selectIndex);
        } catch (IndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Спробуйте ще раз!");
            alert.setHeaderText("Ви не вибрали депозит");
            alert.showAndWait();
        }
    }

    @FXML
    private void exitOption() {
        Stage stage = (Stage)ExitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addNewDeposit() throws IOException {
        DefaultDeposit defaultDeposit = new DefaultDeposit("Users company");
        boolean okClick = main.showAddDeposit(defaultDeposit);
        if(okClick){
            main.getDepositData().add(defaultDeposit);
        }
    }

    @FXML
    private void searchOption(){
        depositTable.setItems(filterList(main.getDepositData(), searchField.getText()));
    }

    private ObservableList<DefaultDeposit> filterList(List<DefaultDeposit> list, String searchText){
        List<DefaultDeposit> filteredList = new ArrayList<>();
        for(DefaultDeposit defaultDeposit : list){
            if(searchFind(defaultDeposit, searchText)) filteredList.add(defaultDeposit);
        }
        return FXCollections.observableList(filteredList);
    }

    private boolean searchFind(DefaultDeposit defaultDeposit, String searchText){
        return (defaultDeposit.getCompanyName().toLowerCase().contains(searchText.toLowerCase())) ||
                (defaultDeposit.getDepositName().toLowerCase().contains(searchText.toLowerCase())) ||
                (defaultDeposit.getPercentage().toString().contains(searchText.toLowerCase())) ||
                (defaultDeposit.getAmountMoney().toString().contains(searchText.toLowerCase())) ||
                (defaultDeposit.getMayEarnMoney().toString().contains(searchText.toLowerCase()));
    }

    @FXML
    private  void infoOption() throws IOException {
        try{
            main.showInfoDeposit(depositTable.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Спробуйте ще раз!");
            alert.setHeaderText("Ви не вибрали депозит");
            alert.showAndWait();
        }

    }

    @FXML
    private void writeFileOption() throws IOException {
        String consoleOutPut = null;
        FileChooser fileChooser = new FileChooser();
        File resp = fileChooser.showOpenDialog(null);
        FileOutputStream outputFile = new FileOutputStream(resp);
        for( DefaultDeposit tb : depositTable.getItems()){
            consoleOutPut += tb.printString();
        }
        byte[] strToBytes = consoleOutPut.getBytes();
        outputFile.write(strToBytes);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Запис у файл пройшла успішно");
        alert.setHeaderText("ВІТАЄМО!");
        alert.showAndWait();
    }

    @FXML
    private void saveDatabase() {
        String url = "jdbc:sqlserver://DESKTOP-V1JT0BJ\\MSSQLSERVERDEV;databaseName=BankDeposit;encrypt=true;trustServerCertificate=true;";
        String user = "SqlJava";
        String password = "1";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "DELETE FROM DepositData";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            int id = 1;
            for(DefaultDeposit tb : depositTable.getItems()){
                sql = "INSERT INTO DepositData" +
                        " VALUES("+id+",'"+ tb.getCompanyName() + "', " +
                        "'"+ tb.getDepositName() + "', " +
                        tb.getAmountMoney() + ", " +
                        tb.getTermOfDeposit() + ", " +
                        tb.getMayEarnMoney() + ", " +
                        tb.getPercentage() + ", " +
                        tb.getMonthlyAdd() + ", '" +
                        tb.getCurrency() + "', " +
                        tb.getEarlyTerm() + ", " +
                        tb.getEarlyAmount() +")";
                statement.execute(sql);
                id++;
            }
            connection.close();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Запис у базу даних пройшла успішно");
            alert.setHeaderText("ВІТАЄМО!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Запис у базу даних не пройшла");
            alert.setHeaderText("Спробуйте ще раз!");
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void ReadFileOption() throws FileNotFoundException {
        DefaultDeposit defaultDepositRead  = new DefaultDeposit();
        FileChooser fileChooser = new FileChooser();
        File resp = fileChooser.showOpenDialog(null);
        FileInputStream fileInputStream = new FileInputStream(resp);
        Scanner sc = new Scanner(fileInputStream);
        while(sc.hasNextLine()){
            defaultDepositRead = DefaultDeposit.builder()
                    .setCompanyName(sc.nextLine())
                    .setDepositName(sc.nextLine())
                    .setPercentage(Double.valueOf(sc.nextLine()))
                    .build();
            defaultDepositRead.setAmountMoney(Double.valueOf(sc.nextLine()));
            defaultDepositRead.setTermOfDeposit(Integer.valueOf(sc.nextLine()));
            defaultDepositRead.setEarlyTerm(Integer.valueOf(sc.nextLine()));
            defaultDepositRead.setEarlyAmount(Double.valueOf(sc.nextLine()));
            defaultDepositRead.setMonthlyCapitalization(Integer.valueOf(sc.nextLine()));
            defaultDepositRead.setAmountMonthlyAdd(Integer.valueOf(sc.nextLine()));
            defaultDepositRead.setCurrency(sc.nextLine());
            defaultDepositRead.calcInvest();
            main.getDepositData().add(defaultDepositRead);
        }
        sc.close();
    }

    @FXML
    private void ChooseOption() throws IOException {
        DefaultDeposit defaultDeposit = new DefaultDeposit();
        boolean okClick = main.showChooseDeposit(defaultDeposit);
        if(okClick){
            main.getDepositData().add(defaultDeposit);
        }
    }
}
