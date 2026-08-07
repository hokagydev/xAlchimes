package com.xalchimes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlchemyCommand implements CommandExecutor {
    private final AlchemyGUI guiHandler;
    
    public AlchemyCommand(AlchemyGUI guiHandler) {
        this.guiHandler = guiHandler;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        guiHandler.open(player);
        return true;
    }
}
