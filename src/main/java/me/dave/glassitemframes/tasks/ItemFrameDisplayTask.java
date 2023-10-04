package me.dave.glassitemframes.tasks;

import com.google.common.collect.HashMultimap;
import me.dave.glassitemframes.GlassItemFrames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class ItemFrameDisplayTask {
    private HashMultimap<Player, Location> glowMap = HashMultimap.create();

    public void tick() {
        HashMultimap<Player, Location> newGlowMap = HashMultimap.create();

        Bukkit.getOnlinePlayers().forEach(player -> {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();

            if (mainHand.getType().equals(Material.ITEM_FRAME) || offHand.getType().equals(Material.ITEM_FRAME)) {
                for (ItemFrame frame : getNearbyInvisibleFrames(player)) {
                    try {
                        Block frameBlock = frame.getLocation().getBlock().getRelative(frame.getAttachedFace());
                        GlassItemFrames.getGlowingBlocks().setGlowing(frameBlock, player, ChatColor.WHITE);
                        newGlowMap.put(player, frameBlock.getLocation());
                        glowMap.remove(player, frameBlock.getLocation());
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
            }

            glowMap.get(player).forEach(location -> {
                try {
                    GlassItemFrames.getGlowingBlocks().unsetGlowing(location, player);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        glowMap = newGlowMap;
    }

//    public boolean isInvisibleItemFrame(ItemStack itemStack) {
//        if (!itemStack.getType().equals(Material.ITEM_FRAME)) {
//            return false;
//        }
//    }

    public Collection<ItemFrame> getNearbyInvisibleFrames(Player player) {
        return player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5, (entity) -> entity.getType().equals(EntityType.ITEM_FRAME)).stream().map(entity -> (ItemFrame) entity).filter(frame -> !frame.isVisible()).toList();
    }
}
