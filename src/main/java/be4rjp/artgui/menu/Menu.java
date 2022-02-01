package be4rjp.artgui.menu;

import be4rjp.artgui.button.ArtButton;
import be4rjp.artgui.button.ReplaceableButton;
import be4rjp.artgui.frame.ArtFrame;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * メニュー内のアイテムやボタンは一を管理するためのクラス
 */
public class Menu {

    private final ArtMenu artMenu;
    
    private final int slots;

    private final int rows;

    private final ArtFrame artFrame;
    
    private NamedInventory namedInventory = null;

    public Menu(ArtMenu artMenu, int rows, int slots, ArtFrame artFrame){
        this.artMenu = artMenu;
        this.rows = rows;
        this.slots = slots;
        this.artFrame = artFrame;
        
        createPage(0);
    }
    
    public ArtMenu getArtMenu() {return artMenu;}
    
    public NamedInventory getNamedInventory() {return namedInventory;}
    
    public void setNamedInventory(NamedInventory namedInventory) {this.namedInventory = namedInventory;}
    
    
    private Map<ReplaceableButton, Boolean> altMap = new HashMap<>();
    
    public boolean isAltButton(ReplaceableButton replaceableButton){
        return altMap.get(replaceableButton);
    }
    
    public void setAltButton(ReplaceableButton replaceableButton, boolean is){
        altMap.put(replaceableButton, is);
    }
    
    
    private int currentMaxPage = 0;
    
    public int getCurrentMaxPage() {return currentMaxPage;}
    
    private final Map<Integer, Map<Integer, Object>> components = new HashMap<>();
  
    private void createPages(int page){
        for(int index = currentMaxPage; index <= page; index++) {
            createPage(page);
        }
        currentMaxPage = Math.max(page, currentMaxPage);
    }
    
    private void createPage(int page){
        components.computeIfAbsent(page, p -> {
            Map<Integer, Object> map = new HashMap<>();
            for(int slot = 0; slot < slots; slot++){
                map.put(slot, artFrame.getFrameButtons()[slot]);
            }
            return map;
        });
    
        currentMaxPage = Math.max(page, currentMaxPage);
    }
    
    /**
     * ページを追加します
     */
    public void addPage(){
        currentMaxPage++;
        createPage(currentMaxPage);
    }
    
    /**
     * ボタンを配置します
     * ページフレーム用のボタンと場所が重複する場合は上書きします
     * @param page 設置するページ
     * @param slot 設置するスロット番号
     * @param artButton 設置するボタン
     */
    public void setButton(int page, int slot, ArtButton artButton){
        createPages(page);
        components.get(page).put(slot, artButton);
    }

    private void addComponent(Object object){
        createPage(currentMaxPage);
        for(int page = 0; page <= currentMaxPage; page++){
            Map<Integer, Object> pageComponents = components.get(page);
            for(int slot = 0; slot < slots; slot++){
                Object component = pageComponents.get(slot);
                if(component == null){
                    pageComponents.put(slot, object);
                    return;
                }
            }
        }

        currentMaxPage++;
        createPage(currentMaxPage);
        
        Map<Integer, Object> pageComponents = components.get(currentMaxPage);
        for(int slot = 0; slot < slots; slot++){
            Object component = pageComponents.get(slot);
            if(component == null){
                pageComponents.put(slot, object);
                return;
            }
        }
    }
    
    /**
     * 前の方から詰めてボタンを配置します
     * @param artButton 設置するボタン
     */
    public void addButton(ArtButton artButton){
        addComponent(artButton);
    }
    
    /**
     * 前の方から詰めてアイテムを配置します
     * @param itemStack 設置するアイテム
     */
    public void addItem(ItemStack itemStack){
        addComponent(itemStack);
    }
    
    /**
     * 指定されたページのアイテムやボタンの配置情報を取得します
     * @param page 取得するページ
     * @return Map<Integer, Object> Integerはスロット番号、ObjectにはArtButtonまたはItemStackのインスタンスが格納されています
     */
    public Map<Integer, Object> getPageComponents(int page){
        return components.get(page);
    }
    
    /**
     * 全ページのアイテムやボタンの配置情報を取得します
     * @return Map<Integer, Map<Integer, Object>> 最初のIntegerはページ番号、Integerはスロット番号、
     * ObjectにはArtButtonまたはItemStackのインスタンスが格納されています
     */
    public Map<Integer, Map<Integer, Object>> getComponents() {return components;}
    
    public List<ItemStack> getItemStackContents(){
        List<ItemStack> itemStackList = new ArrayList<>();
        for(int page = 0; page <= currentMaxPage; page++){
            Map<Integer, Object> pageContents = getPageComponents(page);
            if(pageContents == null) continue;
            
            for(Object content : pageContents.values()){
                if(content == null) continue;
                if(content instanceof ItemStack){
                    itemStackList.add((ItemStack) content);
                }
            }
        }
        return itemStackList;
    }
    
    /**
     * メニューの全ページに配置されているボタン以外のアイテムを全て取得してシリアライズします
     * @return String(Base64)
     */
    public String serializeItems(){
    
        ItemStack[] itemStacks = new ItemStack[slots * (currentMaxPage + 1)];
        
        for(int page = 0; page <= currentMaxPage; page++){
            Map<Integer, Object> pageComponents = components.get(page);
        
            for(int slot = 0; slot < slots; slot++){
                Object content = pageComponents.get(slot);
                if(content == null){
                    itemStacks[page * slots + slot] = new ItemStack(Material.AIR);
                }
                if(content instanceof ItemStack){
                    itemStacks[page * slots + slot] = (ItemStack) content;
                }
            }
        }
    
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(itemStacks.length);
    
            for (ItemStack itemStack : itemStacks) {
                dataOutput.writeObject(itemStack);
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * シリアライズされたアイテムのデータからメニュー内にアイテムを復元します
     * @param data String(Base64)
     */
    public void deserializeItems(String data){
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int length = dataInput.readInt();
            
            for (int index = 0; index < length; index++) {
                ItemStack itemStack = (ItemStack) dataInput.readObject();
                if(itemStack != null) addItem(itemStack);
            }
        
            dataInput.close();
    
            for(int page = 0; page <= currentMaxPage; page++){
                Map<Integer, Object> pageComponents = components.get(page);
        
                for(int slot = 0; slot < slots; slot++){
                    Object content = pageComponents.get(slot);
                    if(content instanceof ItemStack){
                        if(((ItemStack) content).getType().isAir()){
                            pageComponents.put(slot, null);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
