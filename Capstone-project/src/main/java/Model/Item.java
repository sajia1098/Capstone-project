/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author amnasajid
 */
public class Item {
    private String category;
    private String comments;
    private String description;
    private String condition;
    private double price;
    private String imageUrl;
    private String productName;

    public Item(String category, String comments, String condition, String description, double price,String imageUrl, String productName) {
        this.category = category;
        this.comments = comments;
        this.condition = condition;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.productName = productName;      
    }
    
    public Item(){
        
        
    }public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getCondition() {
        return condition;
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
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
