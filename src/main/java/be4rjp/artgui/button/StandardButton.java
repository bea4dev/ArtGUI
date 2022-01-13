package be4rjp.artgui.button;

import be4rjp.artgui.menu.HistoryData;
import be4rjp.artgui.menu.ReplaceNameManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class StandardButton extends ArtButton{
    
    protected ArtButton alternativeButton = new ArtButton(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("&a").build());
    
    public ArtButton getAlternativeButton() {return alternativeButton;}
    
    public void setAlternativeButton(ArtButton alternativeButton) {this.alternativeButton = alternativeButton;}
    
    
    public StandardButton(ItemStack itemStack) {
        super(itemStack);
    }
    
    public ItemStack getRewriteNameItem(HistoryData historyData){
        ItemStack itemStack = this.itemStack.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return itemStack;
    
        itemMeta.setDisplayName(ReplaceNameManager.replace(itemMeta.getDisplayName(), historyData));
    
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
}
