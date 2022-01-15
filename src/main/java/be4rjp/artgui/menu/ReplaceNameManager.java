package be4rjp.artgui.menu;

/**
 * ボタン名やインベントリ名の特定の文字列を置き換えます。
 * {PreviousDisplayName} 前に開いていたメニューの色コード付き表示名
 * {PreviousName} 前に開いていたメニューの色コード無し表示名
 * {CurrentPage} 今開いているページ番号
 * {MaxPage} 今開いているメニューの最大ページ数
 * {PreviousPage} ひとつ前のページ番号
 * {PreviousPage} 次のページ番号
 */
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
            line = line.replace("{PreviousPage}", String.valueOf(currentMenu.getPage()));
            line = line.replace("{NextPage}", String.valueOf(currentMenu.getPage() + 2));
        }
        
        return line;
    }
    
}
