package com.xalchimes;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyListener implements Listener {
    private final AlchemyGUI guiHandler;
    
    public AlchemyListener() {
        this.guiHandler = new AlchemyGUI(null);
        JavaPlugin.getPlugin(XAlchimes.class).getServer().getPluginManager().registerEvents(guiHandler, JavaPlugin.getPlugin(XAlchimes.class));
    }
}
