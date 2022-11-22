package com.example.coursebankdeposit;

import com.example.coursebankdeposit.Controller.AddOptionController;
import com.example.coursebankdeposit.Controller.ChooseOptionController;
import com.example.coursebankdeposit.Controller.DepositOverviewController;
import com.example.coursebankdeposit.Controller.InfoOptionController;
import com.example.coursebankdeposit.DefaultDeposit.DefaultDeposit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private ObservableList<DefaultDeposit> depositData = FXCollections.observableArrayList();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
    addFromDatabase();
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("MainApp");
    showDepositOverview();
    }

    public void addFromDatabase() {
        DefaultDeposit depositDatabase;
        String url = "jdbc:sqlserver://DESKTOP-V1JT0BJ\\MSSQLSERVERDEV;databaseName=BankDeposit;encrypt=true;trustServerCertificate=true;";
        String user = "SqlJava";
        String password = "1";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM DepositData";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                String companyNameBase = result.getString("Company Name");
                String depositNameBase = result.getString("Deposit Name");
                Double amountMoneyBase = result.getDouble("Amount Money");
                Integer termBase = result.getInt("Term Of Deposit");
                Double earningsBase = result.getDouble("Earnings");
                Double percentBase = result.getDouble("Percentage");
                Integer monthlyAddBase = result.getInt("Amount Monthly Add");
                String curBase = result.getString("Currency");
                Integer earlyTermBase = result.getInt("EarlyTerm");
                Double earlyAmountBase = result.getDouble("EarlyAmount");
                depositDatabase = DefaultDeposit.builder()
                        .setCompanyName(companyNameBase)
                        .setDepositName(depositNameBase)
                        .setPercentage(percentBase)
                        .build();
                depositDatabase.setAmountMoney(amountMoneyBase);
                depositDatabase.setTermOfDeposit(termBase);
                depositDatabase.setEarlyTerm(earlyTermBase);
                depositDatabase.setEarlyAmount(earlyAmountBase);
                depositDatabase.setMonthlyCapitalization(0);
                depositDatabase.setAmountMonthlyAdd(monthlyAddBase);
                depositDatabase.setCurrency(curBase);
                depositDatabase.setMayEarnMoney(earningsBase);
                depositData.add(depositDatabase);
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("OOOU");
            throw new RuntimeException(e);
        }
    }

    public void showDepositOverview() throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("DepositOverview.fxml"));
            AnchorPane depositOverview = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(depositOverview, 600, 800);
            DepositOverviewController controller = fxmlLoader.getController();
            primaryStage.setScene(scene);
            primaryStage.show();
            controller.setMain(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<DefaultDeposit> getDepositData() {
        return depositData;
    }


    public boolean showAddDeposit(DefaultDeposit defaultDeposit) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("AddDeposit.fxml"));
        AnchorPane page = (AnchorPane) fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Додавання депозиту");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddOptionController controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);
        controller.setDeposit(defaultDeposit);

        dialogStage.showAndWait();
        return controller.isOkClick();

    }

    public void showInfoDeposit(DefaultDeposit defaultDeposit) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("DepositInfo.fxml"));
        AnchorPane page = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Інформація щодо депозиту");
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        InfoOptionController controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);
        controller.showInfo(defaultDeposit);
        dialogStage.show();
    }

    public boolean showChooseDeposit(DefaultDeposit defaultDeposit) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("ChooseOption.fxml"));
        AnchorPane page = fxmlLoader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Інформація щодо депозитів:");
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        ChooseOptionController controller = fxmlLoader.getController();
        controller.setDialogStage(dialogStage);
        controller.chooseDeposit(defaultDeposit);
        dialogStage.showAndWait();
        return controller.isOkClick();
    }
}

