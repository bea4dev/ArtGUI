package be4rjp.artgui.frame;

import be4rjp.artgui.ArtGUI;
import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.menu.ArtMenu;

import java.util.function.Supplier;

/**
 * メニューの作成を視覚的に分かりやすく表現できるようにするためのユーティリティクラス
 */
public class Artist {
    
    private final Supplier<ArtButton[]> art;
    
    /**
     * インスタンス作成
     * @param art ボタンの配列
     *            渡された配列からメニューとページフレームを作成します
     */
    public Artist(Supplier<ArtButton[]> art){
        this.art = art;
    }
    
    /**
     * メニューとページフレームを作成します
     * @param artGUI ArtGUIのインスタンス
     * @param displayName GUIの表示名
     * @return ArtMenu
     */
    public ArtMenu createMenu(ArtGUI artGUI, String displayName){
        ArtButton[] frameButtons = art.get();
        if(frameButtons.length % 9 != 0) throw new IllegalArgumentException("The length of the array must be a multiple of 9.");
        
        ArtFrame artFrame = new ArtFrame(frameButtons);
        ArtMenu artMenu = new ArtMenu(artGUI, artFrame.getRows(), displayName);
        artMenu.setArtFrame(artFrame);
        
        return artMenu;
    }
    
}
