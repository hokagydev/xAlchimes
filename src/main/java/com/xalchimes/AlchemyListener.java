package com.xalchimes;

import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyListener implements org.bukkit.event.Listener {
    private final AlchemyGUI guiHandler = new AlchemyGUI(null);
    
    public AlchemyListener() {
        JavaPlugin.getPlugin(XAlchimes.class).getServer().getPluginManager().registerEvents(guiHandler, JavaPlugin.getPlugin(XAlchimes.class));
    }
}
