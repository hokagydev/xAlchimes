package com.xalchimes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AlchemyGUI implements Listener {
    private final Map<UUID, Inventory> openInventories = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public AlchemyGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Alchemy Table");
        
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack result = createPotion(PotionType.SWIFTNESS, 2, true);
        ItemStack ingredient1 = createPotion(PotionType.AWKWARD, 1, false);
        ItemStack ingredient2 = new ItemStack(Material.SUGAR);
        ItemStack ingredient3 = new ItemStack(Material.GLOWSTONE_DUST);
        ItemStack exchange = new ItemStack(Material.EMERALD);
        ItemStack exchangeMeta = exchange.getItemMeta();
        exchangeMeta.setDisplayName(ChatColor.GREEN + "Exchange");
        exchange.setItemMeta(exchangeMeta);

        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, filler);
            }
        }

        inv.setItem(21, ingredient1);
        inv.setItem(22, ingredient2);
        inv.setItem(23, ingredient3);
        inv.setItem(49, exchange);
        inv.setItem(31, result);

        openInventories.put(player.getUniqueId(), inv);
        player.openInventory(inv);
    }

    private ItemStack createPotion(PotionType type, int level, boolean upgraded) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        PotionData data = new PotionData(type, false, false);
        meta.setBasePotionData(data);
        if (upgraded) {
            meta.setDisplayName(ChatColor.AQUA + "Swiftness II");
        } else {
            meta.setDisplayName(ChatColor.GRAY + "Awkward Potion");
        }
        potion.setItemMeta(meta);
        return potion;
    }

    public void open() {
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!openInventories.containsKey(player.getUniqueId())) return;
        
        event.setCancelled(true);
        
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) return;
        
        if (event.getSlot() == 49) {
            exchangePotion(player);
        }
    }

    private void exchangePotion(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long timeLeft = (cooldowns.get(player.getUniqueId()) + 5000) - System.currentTimeMillis();
            if (timeLeft > 0) {
                player.sendMessage("§cWait " + (timeLeft / 1000) + "s before next exchange!");
                return;
            }
        }

        PlayerInventory inv = player.getInventory();
        ItemStack potion = null;
        int slot = -1;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getType() == Material.POTION && item.hasItemMeta()) {
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                if (meta.getBasePotionData().getType() == PotionType.AWKWARD) {
                    potion = item;
                    slot = i;
                    break;
                }
            }
        }

        if (potion == null) {
            player.sendMessage("§cYou need an Awkward Potion!");
            return;
        }

        if (!hasIngredients(player)) {
            player.sendMessage("§cYou need Sugar and Glowstone Dust!");
            return;
        }

        inv.setItem(slot, null);
        removeIngredients(player);
        inv.addItem(createPotion(PotionType.SWIFTNESS, 2, true));
        
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        player.sendMessage("§aPotion exchanged successfully!");
        
        openInventories.remove(player.getUniqueId());
        player.closeInventory();
    }

    private boolean hasIngredients(Player player) {
        PlayerInventory inv = player.getInventory();
        boolean hasSugar = false;
        boolean hasGlowstone = false;

        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            if (item.getType() == Material.SUGAR) hasSugar = true;
            if (item.getType() == Material.GLOWSTONE_DUST) hasGlowstone = true;
        }
        return hasSugar && hasGlowstone;
    }

    private void removeIngredients(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            if (item.getType() == Material.SUGAR || item.getType() == Material.GLOWSTONE_DUST) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    inv.setItem(i, null);
                }
                break;
            }
        }
    }
}
