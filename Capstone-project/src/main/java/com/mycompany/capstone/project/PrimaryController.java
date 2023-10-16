package com.mycompany.capstone.project;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        System.out.println("Test");
        System.out.println("TEST MERGE");
        System.out.println("Sher  was here");
    }
}
