package com.mycompany.capstone.project;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Elan
 */
public class ProductformController implements Initializable {

    @FXML
    private TextField tfProductname;
    @FXML
    private ComboBox<?> cbCategory;
    @FXML
    private RadioButton rbNew;
    @FXML
    private RadioButton rbUsed;
    @FXML
    private RadioButton rbRefurbished;
    @FXML
    private TextField tfPrice;
    @FXML
    private Button bnUpload;
    @FXML
    private TextArea taDescription;
    @FXML
    private TextArea taComment;
    @FXML
    private Button bnSubmit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
