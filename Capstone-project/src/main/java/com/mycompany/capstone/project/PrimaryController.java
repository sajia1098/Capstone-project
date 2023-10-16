package com.mycompany.capstone.project;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private Button bnNeedAccount;

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        System.out.println("Test");
        System.out.println("TEST MERGE");
        System.out.println("Sher  was here");
    }
}
