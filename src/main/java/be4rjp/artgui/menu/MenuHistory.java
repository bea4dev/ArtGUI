package be4rjp.artgui.menu;

public class MenuHistory {

    private final ArtMenu artMenu;
    
    private Menu menu;

    private int page = 0;

    public MenuHistory(ArtMenu artMenu, Menu menu, int page){
        this.artMenu = artMenu;
        this.menu = menu;
        this.page = page;
    }

    public int getPage() {return page;}

    public ArtMenu getArtMenu() {return artMenu;}
    
    public Menu getMenu() {return menu;}
    
    public void setPage(int page) {this.page = page;}
    
    public void setMenu(Menu menu) {this.menu = menu;}
    
}
