/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author amnasajid
 */
public class TextBook extends Item {

    private String title;
    private String isbn;
    private int edition;

    public TextBook(String title, String isbn, int edition, String category, String comments, String condition, String description, double price, String imageUrl, String productName) {
        super(category, comments, condition, description, price, imageUrl, productName);
        this.title = title;
        this.isbn = isbn;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }
}
