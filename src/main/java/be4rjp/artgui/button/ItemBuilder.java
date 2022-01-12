package be4rjp.artgui.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemBuilder {

    private final Material material;

    public ItemBuilder(Material material){
        this.material = material;
    }


    private String name = null;

    private String[] lore = null;

    public ItemBuilder name(String name){
        this.name = name;
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = lore;
        return this;
    }

    public ItemStack build(){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(name != null){
            Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }

        if(lore != null){
            List<String> loreList = new ArrayList<>();
            for(String line : lore){
                loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            Objects.requireNonNull(itemMeta).setLore(loreList);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
