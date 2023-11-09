/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

/**
 *
 * @author amnasajid
 */
public class Wearable extends Brand {
   private String gender;
   private String size; 
   private String material;
   private String color;

    public Wearable(String gender, String size, String material, String color, String brand, String description, double price, String condition) {
        super(brand, description, price, condition);
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
