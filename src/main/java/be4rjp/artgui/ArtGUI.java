package be4rjp.artgui;

import be4rjp.artgui.menu.HistoryData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ArtGUI extends JavaPlugin {

    private static ArtGUI artGUI;

    private Plugin plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        artGUI = this;
        plugin = this;
        
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ArtGUI getArtGUI() {
        return artGUI;
    }

    public Plugin getPlugin() {return plugin;}

    public void runSync(Runnable runnable){Bukkit.getScheduler().runTask(plugin, runnable);}

    public void runAsync(Runnable runnable){Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);}
    
    public void runSyncDelayed(Runnable runnable, long delay){Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);}


    private final Map<UUID, HistoryData> playerHolderMap = new HashMap<>();

    public Map<UUID, HistoryData> getPlayerHolderMap() {return playerHolderMap;}
    
    private void registerListener(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new EventListener(this), plugin);
    }
}
