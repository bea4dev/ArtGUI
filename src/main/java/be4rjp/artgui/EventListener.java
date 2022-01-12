package be4rjp.artgui;

import be4rjp.artgui.button.*;
import be4rjp.artgui.frame.ArtFrame;
import be4rjp.artgui.menu.ArtGUIHolder;
import be4rjp.artgui.menu.ArtMenu;
import be4rjp.artgui.menu.Menu;
import be4rjp.artgui.menu.MenuHistory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

public class EventListener implements Listener {
    
    private final ArtGUI artGUI;
    
    public EventListener(ArtGUI artGUI){
        this.artGUI = artGUI;
    }
    
    @EventHandler
    public void onPlayerClick(PlayerAnimationEvent e){
        Player player = e.getPlayer();
        if(!player.isSneaking()) return;
    
        ArtMenu artMenu = new ArtMenu(artGUI, 3, "&nTEST GUI");
    
        ArtFrame artFrame = new ArtFrame(3);
        artFrame.putButton(0, new PageBackButton(new ItemBuilder(Material.ARROW).name("&nBACK").build()).listener(event -> {
            artMenu.backPage(player);
        }));
    
        artFrame.putButton(1, new PageNextButton(new ItemBuilder(Material.ARROW).name("&nNEXT").build()));
        
        artFrame.putButton(2, new MenuBackButton(new ItemBuilder(Material.OAK_DOOR).name("&nBACK TO %PreviousName").build()));
        
        artMenu.setArtFrame(artFrame);
        
        artMenu.asyncCreate(menu -> {
            for(int i = 0; i <= 100; i++){
                menu.addItem(new ItemBuilder(Material.EMERALD_BLOCK).name("&rNormal item : " + i).build());
            }
            
            menu.addButton(new ArtButton(new ItemBuilder(Material.LIME_STAINED_GLASS).name("&a&nSUB MENU").build()).listener(event -> {
                ArtMenu subMenu = new ArtMenu(artGUI, 5, "&nSUB GUI");
                subMenu.asyncCreate(sub -> {
                    sub.addButton(new MenuBackButton(new ItemBuilder(Material.OAK_DOOR).name("&nBACK TO %PreviousName").build()));
                });
                subMenu.open(player);
            }));
        });
        
        artMenu.open(player);
    }
    
    //@EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
    
        if(inventoryHolder == null) return;
        if(inventoryHolder instanceof ArtGUIHolder) {
            ArtGUIHolder artGUIHolder = (ArtGUIHolder) inventoryHolder;
            
            artGUI.runSyncDelayed(() -> {
                Inventory currentInventory = event.getPlayer().getInventory();
                InventoryHolder currentHolder = currentInventory.getHolder();
        
                if (currentHolder == null) return;
                if (!(currentHolder instanceof ArtGUIHolder)) {
                    artGUIHolder.clearGUIQueue();
                }
            }, 1);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
        
        if(inventoryHolder == null) return;
        if(inventoryHolder instanceof ArtGUIHolder){
            ArtGUIHolder artGUIHolder = (ArtGUIHolder) inventoryHolder;
            MenuHistory menuHistory = artGUIHolder.getCurrentMenu();
            
            ArtMenu artMenu = menuHistory.getArtMenu();
    
            Menu menu = artMenu.getMenu();
            if(menu == null) return;
            
            int page = artGUIHolder.getPage(artMenu);
            Map<Integer, Object> components = menu.getPageComponents(page);
            if(components == null) return;
            
            Object component = components.get(event.getSlot());
            if(component == null) return;
            
            if(component instanceof MenuBackButton){
                artGUIHolder.back();
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
                    listener.onClick(event);
                }
                event.setCancelled(true);
            }
        }
    }
    
}
