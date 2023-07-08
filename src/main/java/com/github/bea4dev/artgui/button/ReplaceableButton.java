package com.github.bea4dev.artgui.button;

import com.github.bea4dev.artgui.menu.HistoryData;
import com.github.bea4dev.artgui.menu.ReplaceNameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 名前やアイテムを自動で置き換えることが可能なボタン。
 * 置き換えられる名前は ReplaceNameManager を参照
 */
public class ReplaceableButton extends ArtButton{
    
    protected ArtButton alternativeButton = new ArtButton(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("&a").build());
    
    public ArtButton getAlternativeButton() {return alternativeButton;}
    
    public void setAlternativeButton(ArtButton alternativeButton) {this.alternativeButton = alternativeButton;}
    
    
    public ReplaceableButton(ItemStack itemStack) {
        super(itemStack);
    }
    
    public ItemStack getRewriteNameItem(HistoryData historyData){
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return itemStack;

        Component displayName = itemMeta.displayName();
        boolean hasTextDisplayName = false;
        if (displayName != null) {
            if (displayName instanceof TextComponent) {
                hasTextDisplayName = true;
            }
        }

        if (hasTextDisplayName) {
            itemMeta.setDisplayName(ReplaceNameManager.replace(itemMeta.getDisplayName(), historyData));
        }
    
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
}
