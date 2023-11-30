package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import Model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author amnasajid
 */
public class ItemDescriptionController {

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

    public void setItemDetails(Item details) {
        productNameLabel.setText(details.getProductName());
        productPriceLabel.setText(String.format("%.2f", details.getPrice()));
        productImageView.setImage(new Image(details.getImageUrl()));
        productConditionLabel.setText(details.getCondition());
        productCategoryLabel.setText(details.getCategory());
    }  
}
