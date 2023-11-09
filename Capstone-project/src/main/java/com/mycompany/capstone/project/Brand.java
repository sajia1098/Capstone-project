/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

/**
 *
 * @author amnasajid
 */
public class Brand extends Item {

    private String brand;
   

    public Brand(String brand, String description, double price, String condition) {
        super(description, price, condition);
        this.brand = brand;
        
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    
}
