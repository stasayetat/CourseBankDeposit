package com.example.coursebankdeposit;

import com.example.coursebankdeposit.Controller.DepositOverviewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static org.junit.Assert.*;

public class MainTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("src/main/resources/com/example/coursebankdeposit/DepositOverview.fxml"));
        stage.setTitle("Test");
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }



    @Test
    public void testClickData() {
        clickOn("#searchField").write("Alfa");
        clickOn("#SearchOption");
    }

    @Before
    public void setUp() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}