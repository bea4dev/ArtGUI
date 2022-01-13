package be4rjp.artgui;

import be4rjp.artgui.button.*;
import be4rjp.artgui.frame.ArtFrame;
import be4rjp.artgui.frame.Artist;
import be4rjp.artgui.menu.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class EventListener implements Listener {
    
    private final ArtGUI artGUI;
    
    public EventListener(ArtGUI artGUI){
        this.artGUI = artGUI;
    }
    
    private String data = null;
    
    @EventHandler
    public void onPlayerClick(PlayerAnimationEvent e){
        Player player = e.getPlayer();
        if(!player.isSneaking()) return;
    
        Artist artist = new Artist(() -> {
            
            ArtButton V = null;
            ArtButton G = new ArtButton(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("&a").build());
            
            PageNextButton N = new PageNextButton(new ItemBuilder(Material.ARROW).name("&nNEXT").build());
            N.setAlternativeButton(new ArtButton(new ItemBuilder(Material.EMERALD).name("&a&n次のページを開放").build())
                    .listener((event, menu) -> {
                menu.addPage();
                menu.getArtMenu().nextPage(player);
            }));
            
            PageBackButton P = new PageBackButton(new ItemBuilder(Material.ARROW).name("&nBACK").build());
            MenuBackButton B = new MenuBackButton(new ItemBuilder(Material.OAK_DOOR).name("&nBACK TO {PreviousName}").build());
            
            return new ArtButton[]{
                    V, V, V, V, V, V, V, V, V,
                    V, V, V, V, V, V, V, V, V,
                    G, G, G, P, B, N, G, G, G,
            };
        });
        
    
        ArtMenu artMenu = artist.createMenu(artGUI, "&nTEST GUI &r[{CurrentPage}/{MaxPage}]");
        
        artMenu.asyncCreate(menu -> {
            if(data != null) menu.deserializeItems(data);
        });
        
        artMenu.onClose((event, menu) -> {
            
            data = menu.serializeItems();
            /*
            List<ItemStack> itemStackList = new ArrayList<>();
            for(int page = 0; page <= menu.getCurrentMaxPage(); page++){
                Map<Integer, Object> pageContents = menu.getPageComponents(page);
                for(Object content : pageContents.values()){
                    if(content == null) continue;
                    if(content instanceof ItemStack){
                        itemStackList.add((ItemStack) content);
                    }
                }
            }
            player.sendMessage("Size -> " + itemStackList.size());
            for(ItemStack itemStack : itemStackList){
                player.sendMessage(itemStack.getItemMeta().getDisplayName());
            }*/
        });
        
        artMenu.open(player);
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        InventoryHolder inventoryHolder = inventory.getHolder();
    
        if(inventoryHolder == null) return;
        if(inventoryHolder instanceof ArtGUIHolder) {
            ArtGUIHolder artGUIHolder = ((ArtGUIHolder) inventoryHolder);
            
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
    
            if(component instanceof StandardButton){
                if(menu.isAltButton((StandardButton) component)){
                    ButtonClickListener buttonClickListener = ((StandardButton) component).getAlternativeButton().getEventListener();
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
