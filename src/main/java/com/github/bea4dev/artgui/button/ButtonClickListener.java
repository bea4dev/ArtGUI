package com.github.bea4dev.artgui.button;

import com.github.bea4dev.artgui.menu.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * ボタンがクリックされたときの動作を定義するためのインターフェース
 */
public interface ButtonClickListener {
    
    void onClick(InventoryClickEvent event, Menu menu);
    
}
