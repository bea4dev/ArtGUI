package be4rjp.artgui.menu;

public class MenuHistory {

    private final ArtMenu artMenu;

    private int page = 0;

    public MenuHistory(ArtMenu artMenu, int page){
        this.artMenu = artMenu;
        this.page = page;
    }

    public int getPage() {return page;}

    public ArtMenu getArtMenu() {return artMenu;}

    public void setPage(int page) {this.page = page;}

}
