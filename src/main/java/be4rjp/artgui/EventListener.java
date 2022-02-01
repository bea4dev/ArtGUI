package be4rjp.artgui;

import be4rjp.artgui.button.*;
import be4rjp.artgui.menu.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EventListener implements Listener {
    
    private final ArtGUI artGUI;
    
    public EventListener(ArtGUI artGUI){
        this.artGUI = artGUI;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        HistoryData.removeHistoryData(artGUI, event.getPlayer());
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
    
        if(inventoryHolder == null) return;
        if(inventoryHolder instanceof ArtGUIHolder) {
            ArtGUIHolder artGUIHolder = ((ArtGUIHolder) inventoryHolder);
            
            if(artGUIHolder.getArtMenu().getArtGUI() != this.artGUI) return;
            
            Map<Integer, Object> pageContents = artGUIHolder.getPageContents();
            for(int slot = 0; slot < inventory.getSize(); slot++){
                if(pageContents.get(slot) instanceof ArtButton) continue;
                
                ItemStack itemStack = inventory.getItem(slot);
                if(itemStack == null){
                    pageContents.put(slot,  null);
                    continue;
                }
                
                pageContents.put(slot, itemStack);
            }
            
            HistoryData historyData = artGUIHolder.getHistoryData();
            ArtMenu nowOpeningMenu = historyData.getNowOpeningMenu();
            int nowOpeningPage = historyData.getNowOpeningPage();
            
            ArtMenu artMenu = artGUIHolder.getArtMenu();
            
            if(nowOpeningMenu != artMenu){
                if(artMenu.getGuiCloseListener() != null){
                    artMenu.getGuiCloseListener().onClose(event, artGUIHolder.getMenu());
                    return;
                }
            }
            if(nowOpeningMenu == artMenu && nowOpeningPage == artGUIHolder.getPage()){
                if(artMenu.getGuiCloseListener() != null) artMenu.getGuiCloseListener().onClose(event, artGUIHolder.getMenu());
                historyData.clearGUIQueue();
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if(inventory == null) return;
        
        InventoryHolder inventoryHolder = inventory.getHolder();
        
        if(inventoryHolder == null) return;
        if(inventoryHolder instanceof ArtGUIHolder){
            ArtGUIHolder artGUIHolder = ((ArtGUIHolder) inventoryHolder);
    
            if(artGUIHolder.getArtMenu().getArtGUI() != this.artGUI) return;
            
            HistoryData historyData = artGUIHolder.getHistoryData();
            MenuHistory menuHistory = historyData.getCurrentMenu();
            if(menuHistory == null) return;
            
            ArtMenu artMenu = menuHistory.getArtMenu();
    
            Menu menu = menuHistory.getMenu();
            if(menu == null) return;
            
            int page = historyData.getPage(artMenu);
            Map<Integer, Object> components = menu.getPageComponents(page);
            if(components == null) return;
            
            Object component = components.get(event.getSlot());
            if(component == null) return;
            
            
    
            if(component instanceof ReplaceableButton){
                if(menu.isAltButton((ReplaceableButton) component)){
                    ButtonClickListener buttonClickListener = ((ReplaceableButton) component).getAlternativeButton().getEventListener();
                    if(buttonClickListener != null) buttonClickListener.onClick(event, menu);
                    event.setCancelled(true);
                    return;
                }
            }
            
            if(component instanceof MenuBackButton){
                historyData.back();
            }
            if(component instanceof PageBackButton){
                if(event.getWhoClicked() instanceof Player) artMenu.backPage((Player) event.getWhoClicked());
            }
            if(component instanceof PageNextButton){
                if(event.getWhoClicked() instanceof Player) artMenu.nextPage((Player) event.getWhoClicked());
            }
            
            if(component instanceof ArtButton){
                ButtonClickListener listener = ((ArtButton) component).getEventListener();
                if(listener != null){
                    listener.onClick(event, menu);
                }
                event.setCancelled(true);
            }
        }
    }
    
}
