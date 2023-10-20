/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

/**
 *
 * @author amnasajid
 */
/**
 *
 * @author amnasajid
 */
public class user {
    private String firstName;
    private String lastName;
    private String password;
    private String id;
    //email
    //date joined
    //

    public user(String firstName, String lastName, String password, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id = id;
    }
    
    public user() {
        firstName = "";
        lastName = "";
        password = "";
        id = "";
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
