package be4rjp.artgui.menu;

import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * メニューを閉じた時の動作を定義するためのインターフェース
 */
public interface GUICloseListener {
    
    void onClose(InventoryCloseEvent event, Menu menu);
    
}
