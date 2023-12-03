/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Elan
 */
public class ItemDetails {

    private static ItemDetails instance;
    private Item currentItem;

    private ItemDetails() {
    }

    public static ItemDetails getInstance() {
        if (instance == null) {
            instance = new ItemDetails();
        }
        return instance;
    }

    public static void setInstance(ItemDetails instance) {
        ItemDetails.instance = instance;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }
}
