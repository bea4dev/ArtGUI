package be4rjp.artgui.menu;

public class ReplaceNameManager {
    
    public static String replace(String line, ArtGUIHolder artGUIHolder){
        MenuHistory previousMenu = artGUIHolder.getPreviousMenu();
        if(previousMenu != null) {
            line = line.replace("%PreviousDisplayName", previousMenu.getArtMenu().getDisplayName());
            line = line.replace("%PreviousName", previousMenu.getArtMenu().getRawName());
        }
    
        MenuHistory currentMenu = artGUIHolder.getCurrentMenu();
        if(currentMenu != null){
            line = line.replace("%CurrentPage", String.valueOf(currentMenu.getPage() + 1));
            line = line.replace("%MaxPage", String.valueOf(currentMenu.getArtMenu().getMenu().getCurrentPage() + 1));
        }
        
        return line;
    }
    
}
