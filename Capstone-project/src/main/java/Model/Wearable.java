/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Model.Brand;

/**
 *
 * @author amnasajid
 */
public class Wearable extends Brand {
   private String gender;
   private String size; 
   private String material;
   private String color;

    public Wearable(String gender, String size, String material, String color, String brand, String category, String comments, String condition, String description, double price,String imageUrl, String productName) {
        super(brand, category, comments, condition, description, price, imageUrl, productName);
        this.gender = gender;
        this.size = size;
        this.material = material;
        this.color = color;
    }
   
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
   
   
   
   
}
