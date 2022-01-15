package be4rjp.artgui.button;

import be4rjp.artgui.menu.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * ボタンがクリックされたときの動作を定義するためのインターフェース
 */
public interface ButtonClickListener {
    
    void onClick(InventoryClickEvent event, Menu menu);
    
}
