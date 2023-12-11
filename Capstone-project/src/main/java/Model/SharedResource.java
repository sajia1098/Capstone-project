/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Elan
 */
public class SharedResource {
    private static SharedResource instance;
    private String ownerId;

    private SharedResource() {
    }

    public static SharedResource getInstance() {
        if (instance == null) {
            instance = new SharedResource();
        }
        return instance;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }
}
