package com.github.bea4dev.artgui.menu;

import com.github.bea4dev.artgui.ArtGUI;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * プレイヤーのメニュー閲覧履歴
 * 内部動作用
 */
public class HistoryData {

    public static HistoryData getHistoryData(ArtGUI artGUI, Player player){
        return artGUI.getPlayerHolderMap().computeIfAbsent(player.getUniqueId(), uuid -> new HistoryData(uuid, player));
    }

    public static void removeHistoryData(ArtGUI artGUI, Player player){
        artGUI.getPlayerHolderMap().remove(player.getUniqueId());
    }


    private final UUID uuid;

    private final Player player;

    private final Deque<MenuHistory> historyQueue = new ConcurrentLinkedDeque<>();
    
    private Map<Integer, Object> currentPageContent = null;
    
    private ArtMenu nowOpeningMenu = null;
    
    private int nowOpeningPage = 0;
    
    public boolean clearOnClose = true;
    
    public ArtMenu getNowOpeningMenu() {return nowOpeningMenu;}
    
    public void setNowOpeningMenu(ArtMenu nowOpeningMenu) {this.nowOpeningMenu = nowOpeningMenu;}
    
    public int getNowOpeningPage() {return nowOpeningPage;}
    
    public void setNowOpeningPage(int nowOpeningPage) {this.nowOpeningPage = nowOpeningPage;}
    
    
    public Map<Integer, Object> getCurrentPageContent() {return currentPageContent;}
    
    public void setCurrentPageContent(Map<Integer, Object> currentPageContent) {this.currentPageContent = currentPageContent;}
    
    private HistoryData(UUID uuid, Player player){
        this.uuid = uuid;
        this.player = player;
    }

    public void clearGUIQueue(){this.historyQueue.clear();}

    public void addQueue(ArtMenu artMenu, Menu menu, int page){
        List<MenuHistory> historyList = new ArrayList<>(historyQueue);
        for (ListIterator<MenuHistory> i = historyList.listIterator(historyList.size()); i.hasPrevious();) {
            MenuHistory menuHistory = i.previous();
            if(menuHistory.getArtMenu() == artMenu){
                menuHistory.setPage(page);
                menuHistory.setMenu(menu);
                return;
            }
        }
        historyQueue.add(new MenuHistory(artMenu, menu, page));
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
    
    public MenuHistory getPreviousMenu(){
        Deque<MenuHistory> temp = new ArrayDeque<>(historyQueue);
        temp.pollLast();
        return temp.peekLast();
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
    
}
