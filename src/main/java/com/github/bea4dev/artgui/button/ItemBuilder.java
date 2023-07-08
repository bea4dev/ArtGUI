package com.github.bea4dev.artgui.button;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * アイテムを作成するためのユーティリティクラス
 */
public class ItemBuilder {

    private final Material material;
    
    /**
     * アイテムビルダーを作成
     * @param material アイテムに設定するマテリアル
     */
    public ItemBuilder(Material material){
        this.material = material;
    }


    private String name = null;

    private String[] lore = null;
    
    /**
     * アイテムに名前を設定する
     * @param name 名前
     * @return ItemBuilder
     */
    public ItemBuilder name(String name){
        this.name = name;
        return this;
    }
    
    /**
     * アイテムのloreを設定する
     * @param lore 説明文等
     * @return ItemBuilder
     */
    public ItemBuilder lore(String... lore) {
        this.lore = lore;
        return this;
    }
    
    /**
     * 設定された情報からBukkitのItemStackを作成します
     * @return ItemStack
     */
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
