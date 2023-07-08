package com.github.bea4dev.artgui.button;

import org.bukkit.inventory.ItemStack;

/**
 * 通常のボタン
 */
public class ArtButton {

    protected final ItemStack itemStack;

    private ButtonClickListener buttonClickListener;
    
    /**
     * ボタンのインスタンスを作成
     * @param itemStack ボタンとして表示するアイテム
     */
    public ArtButton(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {return itemStack;}

    public ButtonClickListener getEventListener() {return buttonClickListener;}
    
    /**
     * クリックされたときの動作を設定します
     * @param buttonClickListener クリックされたときの動作
     * @return ArtButton
     */
    public ArtButton listener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
        return this;
    }
}
