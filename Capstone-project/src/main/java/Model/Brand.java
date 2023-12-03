/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author amnasajid
 */
public class Brand extends Item {

    private String brand;

    public Brand(String brand, String category, String comments, String condition, String description, double price, String imageUrl, String productName) {
        super(category, comments, condition, description, price, imageUrl, productName);
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
