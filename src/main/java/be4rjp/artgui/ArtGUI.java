package be4rjp.artgui;

import be4rjp.artgui.menu.ArtGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ArtGUI getArtGUI() {
        return artGUI;
    }

    public Plugin getPlugin() {return plugin;}

    public void runSync(Runnable runnable){
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public void runAsync(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }


    private final Map<UUID, ArtGUIHolder> playerHolderMap = new HashMap<>();

    public Map<UUID, ArtGUIHolder> getPlayerHolderMap() {return playerHolderMap;}
}
