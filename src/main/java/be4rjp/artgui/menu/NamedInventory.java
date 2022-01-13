package be4rjp.artgui.menu;

import org.bukkit.inventory.Inventory;

public class NamedInventory {
    
    public final String replacedName;
    
    public final String displayName;
    
    public final Inventory inventory;
    
    public NamedInventory(String replacedName, String displayName, Inventory inventory) {
        this.replacedName = replacedName;
        this.displayName = displayName;
        this.inventory = inventory;
    }
}
