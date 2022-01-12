package be4rjp.artgui.button;

import org.bukkit.inventory.ItemStack;

public class ArtButton {

    protected final ItemStack itemStack;

    private ButtonClickListener buttonClickListener;

    public ArtButton(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {return itemStack;}

    public ButtonClickListener getEventListener() {return buttonClickListener;}

    public ArtButton listener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
        return this;
    }
}
