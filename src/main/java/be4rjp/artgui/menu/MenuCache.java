package be4rjp.artgui.menu;

import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCache {

    private final int slots;

    private final int rows;

    private final ArtFrame artFrame;

    public MenuCache(int rows, int slots, ArtFrame artFrame){
        this.rows = rows;
        this.slots = slots;
        this.artFrame = artFrame;
    }

    private int currentPage = 0;

    private int componentCount = 0;

    private final Map<Integer, Object[]> components = new HashMap<>();

    private void createPages(int page){
        for(int index = currentPage; index <= page; index++) {
            components.computeIfAbsent(index, p -> new Object[slots]);
        }
        currentPage = page;
    }


    public void setButton(int page, int slot, ArtButton artButton){
        createPages(page);
        components.get(page)[slot] = artButton;
    }

    private void addComponent(Object object){
        for(int page = 0; page < currentPage; page++){
            for(int slot = 0; slot < slots; slot++){
                Object[] pageComponents = components.get(page);
                Object component = pageComponents[slot];
                if(component == null){
                    pageComponents[slot] = object;
                    return;
                }
            }
        }

        currentPage++;
        components.computeIfAbsent(currentPage, p -> new Object[slots]);
    }

    public void addButton(ArtButton artButton){
        addComponent(artButton);
    }

    public void addItem(ItemStack itemStack){
        addComponent(itemStack);
    }

    public Object[] getPageComponents(int page){
        return components.get(page);
    }

}
