/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Elan
 */
public class CurrentUser extends User {

    private static CurrentUser instance;

    //Private constructor to prevent direct instantiation
    private CurrentUser(String firstName, String lastName, String password, String id) {
        super(firstName, lastName, password, id);
    }

    //Static method to get the one and only one instance
    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser("", "", "", "");
        }
        return instance;
    }

    //Static method to set the current user upon login
    public static void login(String firstName, String lastName, String password, String id) {
        instance = new CurrentUser(firstName, lastName, password, id);
    }

    //Static method to clear the current user upon logout
    public static void logout() {
        instance = null;
    }

    /**
     *
     * @return @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        //Prevent cloning of the one and only one instance
        throw new CloneNotSupportedException();
    }
}
