package be4rjp.artgui.menu;

import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Menu {

    private final int slots;

    private final int rows;

    private final ArtFrame artFrame;

    public Menu(int rows, int slots, ArtFrame artFrame){
        this.rows = rows;
        this.slots = slots;
        this.artFrame = artFrame;
    }

    private int currentPage = 0;
    
    public int getCurrentPage() {return currentPage;}
    
    private final Map<Integer, Map<Integer, Object>> components = new HashMap<>();
  
    private void createPages(int page){
        for(int index = currentPage; index <= page; index++) {
            createPage(page);
        }
        currentPage = Math.max(page, currentPage);
    }
    
    private void createPage(int page){
        components.computeIfAbsent(page, p -> {
            Map<Integer, Object> map = new HashMap<>();
            for(int slot = 0; slot < slots; slot++){
                map.put(slot, artFrame.getFrameButtons()[slot]);
            }
            return map;
        });
    
        currentPage = Math.max(page, currentPage);
    }


    public void setButton(int page, int slot, ArtButton artButton){
        createPages(page);
        components.get(page).put(slot, artButton);
    }

    private void addComponent(Object object){
        createPage(currentPage);
        for(int page = 0; page <= currentPage; page++){
            Map<Integer, Object> pageComponents = components.get(page);
            for(int slot = 0; slot < slots; slot++){
                Object component = pageComponents.get(slot);
                if(component == null){
                    pageComponents.put(slot, object);
                    return;
                }
            }
        }

        currentPage++;
        createPage(currentPage);
        
        Map<Integer, Object> pageComponents = components.get(currentPage);
        for(int slot = 0; slot < slots; slot++){
            Object component = pageComponents.get(slot);
            if(component == null){
                pageComponents.put(slot, object);
                return;
            }
        }
    }

    public void addButton(ArtButton artButton){
        addComponent(artButton);
    }

    public void addItem(ItemStack itemStack){
        addComponent(itemStack);
    }

    public Map<Integer, Object> getPageComponents(int page){
        return components.get(page);
    }

}
