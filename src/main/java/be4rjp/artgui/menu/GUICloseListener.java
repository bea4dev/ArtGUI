package be4rjp.artgui.menu;

import org.bukkit.event.inventory.InventoryCloseEvent;

public interface GUICloseListener {
    
    void onClose(InventoryCloseEvent event, Menu menu);
    
}
