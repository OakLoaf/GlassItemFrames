package me.dave.glassitemframes.listener;

import me.dave.chatcolorhandler.ChatColorHandler;
import me.dave.glassitemframes.GlassItemFrames;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemFrameListener implements Listener {
    private static final ItemStack glassItemFrame;

    static {
        glassItemFrame = Bukkit.getServer().getUnsafe().modifyItemStack(new ItemStack(Material.ITEM_FRAME), "{CustomModelData:1,EntityTag:{Invisible:1b}}");

        ItemMeta itemMeta = glassItemFrame.getItemMeta();
        itemMeta.setDisplayName(ChatColorHandler.translateAlternateColorCodes("&fGlass Item Frame"));
        itemMeta.setLore(List.of(ChatColorHandler.translateAlternateColorCodes("&7An item frame but invisible")));

        glassItemFrame.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getEntity() instanceof ItemFrame itemFrame && !itemFrame.isVisible()) {
            event.setCancelled(true);
            itemFrame.remove();

            Location centerPoint = itemFrame.getLocation().add(itemFrame.getFacing().getDirection().multiply(0.3));

            itemFrame.getWorld().playSound(centerPoint, Sound.ENTITY_ITEM_FRAME_BREAK, SoundCategory.BLOCKS, 1f, 1f);

            Item frameDrop = itemFrame.getWorld().dropItem(centerPoint, glassItemFrame);
            Item drop = itemFrame.getWorld().dropItemNaturally(centerPoint, itemFrame.getItem());

            if (!GlassItemFrames.getInstance().callEvent(new EntityDropItemEvent(itemFrame, frameDrop))) {
                frameDrop.remove();
            }
            if (!GlassItemFrames.getInstance().callEvent(new EntityDropItemEvent(itemFrame, drop))) {
                drop.remove();
            }
        }
    }
}
