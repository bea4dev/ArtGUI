package be4rjp.artgui.menu;

public class ReplaceNameManager {
    
    public static String replace(String line, HistoryData historyData){
        MenuHistory previousMenu = historyData.getPreviousMenu();
        if(previousMenu != null) {
            NamedInventory namedInventory = previousMenu.getMenu().getNamedInventory();
            if(namedInventory != null) {
                line = line.replace("{PreviousDisplayName}", namedInventory.displayName);
                line = line.replace("{PreviousName}", namedInventory.replacedName);
            }
        }
    
        MenuHistory currentMenu = historyData.getCurrentMenu();
        if(currentMenu != null){
            line = line.replace("{CurrentPage}", String.valueOf(currentMenu.getPage() + 1));
            line = line.replace("{MaxPage}", String.valueOf(currentMenu.getMenu().getCurrentMaxPage() + 1));
        }
        
        return line;
    }
    
}
