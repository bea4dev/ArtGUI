package be4rjp.artgui.frame;

import be4rjp.artgui.button.ArtButton;

public class ArtFrame {

    private final int rows;

    private final ArtButton[] frameButtons;

    public ArtFrame(int rows){
        this.rows = rows;
        this.frameButtons = new ArtButton[rows * 9];
    }

    public ArtButton[] getFrameButtons() {return frameButtons;}

    public int getRows() {return rows;}

    public ArtFrame putButton(int slot, ArtButton artButton){
        this.frameButtons[slot] = artButton;
        return this;
    }
}
