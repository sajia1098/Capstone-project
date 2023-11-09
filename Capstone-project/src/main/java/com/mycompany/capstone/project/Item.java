/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

/**
 *
 * @author amnasajid
 */
public class Item {

    private String description;
    private double price;
    private String condition;

    public Item(String description, double price, String condition) {
        this.description = description;
        this.price = price;
        this.condition = condition;
    }
    
    public Item(){
        
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
