package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import Model.Item;
import Model.ItemDetails;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import project.App;

/**
 * FXML Controller class
 *
 * @author amnasajid
 */
public class ItemDescriptionController implements Initializable{

    @FXML
    private ImageView productImageView;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productConditionLabel;
    @FXML
    private Label productCategoryLabel;
    @FXML
    private Button bnHome;
    @FXML
    private Button openMessageButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Retrieve item details
        Item currentItem = ItemDetails.getInstance().getCurrentItem();
        
        //Setup view with currentitem
        setItemDetails(currentItem);
    }
    
    public void setItemDetails(Item details) {
        productNameLabel.setText(details.getProductName());
        productPriceLabel.setText(String.format("%.2f", details.getPrice()));
        productImageView.setImage(new Image(details.getImageUrl()));
        productConditionLabel.setText(details.getCondition());
        productCategoryLabel.setText(details.getCategory());
    }

    @FXML
    public void backToHome() {
        try {
            App.switchScene("home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    public void openMessaging () {
        try {
            App.switchScene("message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
