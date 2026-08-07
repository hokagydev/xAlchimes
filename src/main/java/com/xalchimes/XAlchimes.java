package com.xalchimes;

import org.bukkit.plugin.java.JavaPlugin;

public class XAlchimes extends JavaPlugin {
    private AlchemyGUI guiHandler;
    
    @Override
    public void onEnable() {
        this.guiHandler = new AlchemyGUI();
        getServer().getPluginManager().registerEvents(guiHandler, this);
        getCommand("alchemy").setExecutor(new AlchemyCommand(guiHandler));
        getServer().getConsoleSender().sendMessage("§aXAlchimes enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cXAlchimes disabled!");
    }
}
