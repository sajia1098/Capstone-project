/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author amnasajid
 */
public class User {

    protected String firstName;
    protected String lastName;
    protected String password;
    protected String id;

    public User(String firstName, String lastName, String password, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id = id;
    }

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.id = "";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
