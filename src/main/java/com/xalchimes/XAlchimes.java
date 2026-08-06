package com.xalchimes;

import org.bukkit.plugin.java.JavaPlugin;

public class XAlchimes extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new AlchemyListener(), this);
        getCommand("alchemy").setExecutor(new AlchemyCommand());
        getServer().getConsoleSender().sendMessage("§aXAlchimes enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§cXAlchimes disabled!");
    }
}
