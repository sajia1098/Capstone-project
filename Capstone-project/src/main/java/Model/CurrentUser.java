/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.IOException;
import project.App;

/**
 *
 * @author Elan
 */
public class CurrentUser extends User{
    

    public CurrentUser(String firstName, String lastName, String password, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = "PROTECTED";
        this.id = id;
    }
    
    public CurrentUser() {
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.id = "";
    }
    
    public void logOut() throws IOException {
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.id = "";
        
        //Switches user to login controller
        App.setRoot("login");
    }

}
