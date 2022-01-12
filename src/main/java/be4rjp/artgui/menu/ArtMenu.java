package be4rjp.artgui.menu;

import be4rjp.artgui.ArtGUI;
import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ArtMenu {

    private final ArtGUI artGUI;

    private final int rows;

    private final int slots;

    public ArtMenu(ArtGUI artGUI, int rows){
        this.artGUI = artGUI;
        this.rows = rows;
        this.slots = rows * 9;
    }


    private Runnable syncGUICreator = null;

    private Runnable asyncGUICreator = null;

    public void syncCreate(Runnable creator){
        this.syncGUICreator = creator;
    }

    public void asyncCreate(Runnable creator){
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

    public void setArtFrame(ArtFrame artFrame) {
        this.artFrame = artFrame;
    }

    private Inventory createInventory(@Nullable ArtGUIHolder artGUIHolder){
        return Bukkit.createInventory(artGUIHolder, slots, "test");
    }

    private void setInventoryComponents(int page){

    }








    public CompletableFuture<ArtMenu> createMenu(){
        CompletableFuture<ArtMenu> completableFuture = new CompletableFuture<>();

        if(syncGUICreator != null){
            artGUI.runSync(() -> {
                syncGUICreator.run();
                if(asyncGUICreator != null){
                    artGUI.runAsync(() -> {
                        asyncGUICreator.run();
                        completableFuture.complete(this);
                    });
                }else{
                    completableFuture.complete(this);
                }
            });
        }else {
            if (asyncGUICreator != null) {
                artGUI.runAsync(()  -> {
                    asyncGUICreator.run();
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
                artGUIHolder.addQueue(this, artGUIHolder.getPage(this));
                Inventory inventory = createInventory(artGUIHolder);
                player.openInventory(inventory);
            });
        });
    }



}
