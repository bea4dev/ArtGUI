package be4rjp.artgui.menu;

import be4rjp.artgui.ArtGUI;
import be4rjp.artgui.button.*;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ArtMenu {

    private final ArtGUI artGUI;

    private final int rows;

    private final int slots;
    
    private final String displayName;

    public ArtMenu(ArtGUI artGUI, int rows, String displayName){
        this.artGUI = artGUI;
        this.rows = rows;
        this.slots = rows * 9;
        this.displayName = ChatColor.translateAlternateColorCodes('&', displayName);
    }
    
    public String getDisplayName() {return displayName;}
    
    
    private Consumer<Menu> syncGUICreator = null;

    private Consumer<Menu> asyncGUICreator = null;

    public void syncCreate(Consumer<Menu> creator){
        this.syncGUICreator = creator;
    }

    public void asyncCreate(Consumer<Menu> creator){
        this.asyncGUICreator = creator;
    }


    private Consumer<InventoryOpenEvent> inventoryOpenEventConsumer;

    private GUICloseListener guiCloseListener;

    public void onOpen(Consumer<InventoryOpenEvent> inventoryOpenEventConsumer){
        this.inventoryOpenEventConsumer = inventoryOpenEventConsumer;
    }

    public void onClose(GUICloseListener guiCloseListener) {
        this.guiCloseListener = guiCloseListener;
    }
    
    public GUICloseListener getGuiCloseListener() {return guiCloseListener;}
    
    public Consumer<InventoryOpenEvent> getInventoryOpenEventConsumer() {return inventoryOpenEventConsumer;}


    private ArtFrame artFrame;

    public void setArtFrame(ArtFrame artFrame) {this.artFrame = artFrame;}
    
    public ArtFrame getArtFrame() {return artFrame;}
    
    
    private NamedInventory createInventory(@Nullable HistoryData historyData){
        String replaced = this.displayName;
        if(historyData != null) replaced = ReplaceNameManager.replace(replaced, historyData);
        ArtGUIHolder artGUIHolder = new ArtGUIHolder(this, historyData);
        Inventory inventory = Bukkit.createInventory(artGUIHolder, slots, replaced);
        return new NamedInventory(ChatColor.stripColor(replaced), replaced, inventory);
    }
    
    private CompletableFuture<Menu> createMenu(){
        CompletableFuture<Menu> completableFuture = new CompletableFuture<>();

        if(artFrame == null) artFrame = new ArtFrame(rows);
        Menu menu = new Menu(this, rows, slots, artFrame);
        
        if(syncGUICreator != null){
            artGUI.runSync(() -> {
                syncGUICreator.accept(menu);
                if(asyncGUICreator != null){
                    artGUI.runAsync(() -> {
                        asyncGUICreator.accept(menu);
                        completableFuture.complete(menu);
                    });
                }else{
                    completableFuture.complete(menu);
                }
            });
        }else {
            if (asyncGUICreator != null) {
                artGUI.runAsync(() -> {
                    asyncGUICreator.accept(menu);
                    completableFuture.complete(menu);
                });
            }
        }

        return completableFuture;
    }
    

    public void open(Player player){
        this.createMenu().thenAccept(menu -> {
            artGUI.runSync(() -> {
                HistoryData historyData = HistoryData.getHistoryData(artGUI, player);
                int page = historyData.getPage(this);
                openPage(player, menu, page, historyData);
            });
        });
    }
    
    public void openPage(Player player, Menu menu, int page, HistoryData historyData){
        if(menu == null) return;
    
        Map<Integer, Object> components = menu.getPageComponents(page);
        if(components == null) return;
        historyData.setCurrentPageContent(components);
        
        historyData.addQueue(this, menu, page);
        NamedInventory namedInventory = createInventory(historyData);
        menu.setNamedInventory(namedInventory);
        ArtGUIHolder artGUIHolder = (ArtGUIHolder) namedInventory.inventory.getHolder();
        if(artGUIHolder == null) return;
        
        artGUIHolder.setPageContents(components);
        artGUIHolder.setMenu(menu);
        artGUIHolder.setPage(page);
    
        for(int slot = 0; slot < slots; slot++){
            Object component = components.get(slot);
        
            MenuHistory previousMenu = historyData.getPreviousMenu();
            if(component instanceof MenuBackButton){
                if(previousMenu == null){
                    namedInventory.inventory.setItem(slot, ((StandardButton) component).getAlternativeButton().getItemStack());
                    menu.setAltButton((StandardButton) component, true);
                    continue;
                }
            }
            if(component instanceof PageBackButton){
                if(page == 0){
                    namedInventory.inventory.setItem(slot, ((StandardButton) component).getAlternativeButton().getItemStack());
                    menu.setAltButton((StandardButton) component, true);
                    continue;
                }
            }
            if(component instanceof PageNextButton){
                if(page == menu.getCurrentMaxPage()){
                    namedInventory.inventory.setItem(slot, ((StandardButton) component).getAlternativeButton().getItemStack());
                    menu.setAltButton((StandardButton) component, true);
                    continue;
                }
            }
        
            if(component instanceof StandardButton){
                namedInventory.inventory.setItem(slot, ((StandardButton) component).getRewriteNameItem(historyData));
                menu.setAltButton((StandardButton) component, false);
                continue;
            }
        
            if(component instanceof ArtButton){
                namedInventory.inventory.setItem(slot, ((ArtButton) component).getItemStack());
            }else if(component instanceof ItemStack){
                namedInventory.inventory.setItem(slot, (ItemStack) component);
            }
        }
        
        historyData.setNowOpeningMenu(this);
        historyData.setNowOpeningPage(page);
        player.openInventory(namedInventory.inventory);
    }

    public void nextPage(Player player){
        HistoryData historyData = HistoryData.getHistoryData(artGUI, player);
        MenuHistory menuHistory = historyData.getCurrentMenu();
        if(menuHistory == null) return;
        
        int page = menuHistory.getPage() + 1;
        openPage(player, menuHistory.getMenu(), page, historyData);
    }
    
    public void backPage(Player player){
        HistoryData historyData = HistoryData.getHistoryData(artGUI, player);
        MenuHistory menuHistory = historyData.getCurrentMenu();
        if(menuHistory == null) return;
    
        int page = menuHistory.getPage() - 1;
        openPage(player, menuHistory.getMenu(), page, historyData);
    }

}
