package be4rjp.artgui.frame;

import be4rjp.artgui.ArtGUI;
import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.menu.ArtMenu;

import java.util.function.Supplier;

public class Artist {
    
    private final Supplier<ArtButton[]> art;
    
    public Artist(Supplier<ArtButton[]> art){
        this.art = art;
    }
    
    public ArtMenu createMenu(ArtGUI artGUI, String displayName){
        ArtButton[] frameButtons = art.get();
        if(frameButtons.length % 9 != 0) throw new IllegalArgumentException("The length of the array must be a multiple of 9.");
        
        ArtFrame artFrame = new ArtFrame(frameButtons);
        ArtMenu artMenu = new ArtMenu(artGUI, artFrame.getRows(), displayName);
        artMenu.setArtFrame(artFrame);
        
        return artMenu;
    }
    
}
