package be4rjp.artgui.menu;

import be4rjp.artgui.ArtGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;

public class ArtGUIHolder implements InventoryHolder {

    public static ArtGUIHolder getGUIHolder(ArtGUI artGUI, Player player){
        return artGUI.getPlayerHolderMap().computeIfAbsent(player.getUniqueId(), uuid -> new ArtGUIHolder(uuid, player));
    }

    public static void removeGUIHolder(ArtGUI artGUI, Player player){
        artGUI.getPlayerHolderMap().remove(player.getUniqueId());
    }


    private final UUID uuid;

    private final Player player;

    private final Deque<MenuHistory> historyQueue = new ArrayDeque<>();

    private ArtGUIHolder(UUID uuid, Player player){
        this.uuid = uuid;
        this.player = player;
    }

    public void clearGUIQueue(){this.historyQueue.clear();}

    public void addQueue(ArtMenu artMenu, int page){
        for(MenuHistory menuHistory : historyQueue){
            if(menuHistory.getArtMenu() == artMenu){
                menuHistory.setPage(page);
                return;
            }
        }
        historyQueue.add(new MenuHistory(artMenu, page));
    }

    public MenuHistory getCurrentMenu(){return historyQueue.peekLast();}

    public int getPage(ArtMenu artMenu){
        for(MenuHistory menuHistory : historyQueue){
            if(menuHistory.getArtMenu() == artMenu){
                return menuHistory.getPage();
            }
        }
        return 0;
    }

    public void back(){
        MenuHistory menuHistory = historyQueue.pollLast();
        if(menuHistory == null){
            player.closeInventory();
            return;
        }

        menuHistory = historyQueue.peekLast();
        if(menuHistory == null){
            player.closeInventory();
        }else{
            menuHistory.getArtMenu().open(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
