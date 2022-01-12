package be4rjp.artgui.menu;

import be4rjp.artgui.ArtGUI;
import be4rjp.artgui.button.*;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

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
    
    public String getRawName(){return ChatColor.stripColor(displayName);}
    
    
    private Consumer<Menu> syncGUICreator = null;

    private Consumer<Menu> asyncGUICreator = null;

    public void syncCreate(Consumer<Menu> creator){
        this.syncGUICreator = creator;
    }

    public void asyncCreate(Consumer<Menu> creator){
        this.asyncGUICreator = creator;
    }


    private Consumer<InventoryOpenEvent> inventoryOpenEventConsumer;

    private Consumer<InventoryCloseEvent> inventoryCloseEventConsumer;

    public void onOpen(Consumer<InventoryOpenEvent> inventoryOpenEventConsumer){
        this.inventoryOpenEventConsumer = inventoryOpenEventConsumer;
    }

    public void onClose(Consumer<InventoryCloseEvent> inventoryCloseEventConsumer) {
        this.inventoryCloseEventConsumer = inventoryCloseEventConsumer;
    }

    public Consumer<InventoryCloseEvent> getInventoryCloseEventConsumer() {return inventoryCloseEventConsumer;}

    public Consumer<InventoryOpenEvent> getInventoryOpenEventConsumer() {return inventoryOpenEventConsumer;}


    private ArtFrame artFrame;

    public void setArtFrame(ArtFrame artFrame) {this.artFrame = artFrame;}
    
    public ArtFrame getArtFrame() {return artFrame;}
    
    
    private Inventory createInventory(@Nullable ArtGUIHolder artGUIHolder){
        String title = displayName;
        if(artGUIHolder != null) title = ReplaceNameManager.replace(displayName, artGUIHolder);
        
        return Bukkit.createInventory(artGUIHolder, slots, title);
    }

    private Menu menu;
    
    public Menu getMenu() {return menu;}
    
    private CompletableFuture<ArtMenu> createMenu(){
        CompletableFuture<ArtMenu> completableFuture = new CompletableFuture<>();

        if(artFrame == null) artFrame = new ArtFrame(rows);
        menu = new Menu(rows, slots, artFrame);
        
        if(syncGUICreator != null){
            artGUI.runSync(() -> {
                syncGUICreator.accept(menu);
                if(asyncGUICreator != null){
                    artGUI.runAsync(() -> {
                        asyncGUICreator.accept(menu);
                        completableFuture.complete(this);
                    });
                }else{
                    completableFuture.complete(this);
                }
            });
        }else {
            if (asyncGUICreator != null) {
                artGUI.runAsync(() -> {
                    asyncGUICreator.accept(menu);
                    completableFuture.complete(this);
                });
            }
        }

        return completableFuture;
    }
    

    public void open(Player player){
        this.createMenu().thenAccept(artMenu -> {
            artGUI.runSync(() -> {
                ArtGUIHolder artGUIHolder = ArtGUIHolder.getGUIHolder(artGUI, player);
                int page = artGUIHolder.getPage(this);
                openPage(player, page);
            });
        });
    }
    
    public void openPage(Player player, int page){
        if(menu == null) return;
    
        Map<Integer, Object> components = menu.getPageComponents(page);
        if(components == null) return;
        
        ArtGUIHolder artGUIHolder = ArtGUIHolder.getGUIHolder(artGUI, player);
        artGUIHolder.addQueue(this, page);
        Inventory inventory = createInventory(artGUIHolder);
        
        artGUI.runAsync(() -> {
            for(int slot = 0; slot < slots; slot++){
                Object component = components.get(slot);
                
                MenuHistory previousMenu = artGUIHolder.getPreviousMenu();
                if(component instanceof MenuBackButton){
                    if(previousMenu == null) continue;
                }
                if(component instanceof PageBackButton){
                    if(page == 0) continue;
                }
                if(component instanceof PageNextButton){
                    if(page == menu.getCurrentPage()) continue;
                }
                
                if(component instanceof RewriteNameButton){
                    ((RewriteNameButton) component).getRewriteNameItem(artGUIHolder);
                }
                
                if(component instanceof ArtButton){
                    inventory.setItem(slot, ((ArtButton) component).getItemStack());
                }else if(component instanceof ItemStack){
                    inventory.setItem(slot, (ItemStack) component);
                }
            }
        
            artGUI.runSync(() -> player.openInventory(inventory));
        });
    }

    public void nextPage(Player player){
        ArtGUIHolder artGUIHolder = ArtGUIHolder.getGUIHolder(artGUI, player);
        int page = artGUIHolder.getPage(this) + 1;
        openPage(player, page);
    }
    
    public void backPage(Player player){
        ArtGUIHolder artGUIHolder = ArtGUIHolder.getGUIHolder(artGUI, player);
        int page = artGUIHolder.getPage(this) - 1;
        openPage(player, page);
    }

}
