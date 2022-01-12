package be4rjp.artgui.button;

import be4rjp.artgui.menu.ArtGUIHolder;
import be4rjp.artgui.menu.ReplaceNameManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuBackButton extends ArtButton implements RewriteNameButton{
    public MenuBackButton(ItemStack itemStack) {
        super(itemStack);
    }
    
    @Override
    public ItemStack getRewriteNameItem(ArtGUIHolder artGUIHolder) {
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return itemStack;
    
        itemMeta.setDisplayName(ReplaceNameManager.replace(itemMeta.getDisplayName(), artGUIHolder));
        
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
