package com.github.bea4dev.artgui.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ArtGUIHolder implements InventoryHolder {
    
    private final ArtMenu artMenu;
    
    private int page;
    
    private final HistoryData historyData;
    
    public ArtGUIHolder(ArtMenu artMenu, HistoryData historyData){
        this.artMenu = artMenu;
        this.historyData = historyData;
    }
    
    public ArtMenu getArtMenu() {return artMenu;}
    
    public HistoryData getHistoryData() {return historyData;}
    
    public int getPage() {return page;}
    
    public void setPage(int page) {this.page = page;}
    
    private Map<Integer, Object> pageContents = null;
    
    private Menu menu = null;
    
    public Map<Integer, Object> getPageContents() {return pageContents;}
    
    public void setPageContents(Map<Integer, Object> pageContents) {this.pageContents = pageContents;}
    
    public Menu getMenu() {return menu;}
    
    public void setMenu(Menu menu) {this.menu = menu;}
    
    
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
