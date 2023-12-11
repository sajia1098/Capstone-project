/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author amnasajid
 */
public class Electronic extends Brand {

    private String device_type;

    public Electronic(String device_type, String brand, String category, String comments, String condition, String description, double price, String imageUrl, String productName, String ownerId) {
        super(brand, category, comments, condition, description, price, imageUrl, productName, ownerId);
        this.device_type = device_type;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
