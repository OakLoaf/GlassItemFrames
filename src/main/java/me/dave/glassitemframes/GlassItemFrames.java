package me.dave.glassitemframes;

import me.dave.glassitemframes.listener.ItemFrameListener;
import me.dave.glassitemframes.tasks.ItemFrameDisplayTask;
import me.dave.glassitemframes.utils.GlowingBlocks;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class GlassItemFrames extends JavaPlugin {
    private static GlassItemFrames plugin;
    private static BukkitTask heartBeatTask;
    private static GlowingBlocks glowingBlocks;

    @Override
    public void onEnable() {
        plugin = this;

        glowingBlocks = new GlowingBlocks(this);

        ItemFrameDisplayTask itemFrameDisplayTask = new ItemFrameDisplayTask();
        heartBeatTask = Bukkit.getScheduler().runTaskTimer(this, itemFrameDisplayTask::tick, 20, 20);

        getServer().getPluginManager().registerEvents(new ItemFrameListener(), this);
    }

    @Override
    public void onDisable() {
        if (heartBeatTask != null) {
            heartBeatTask.cancel();
            heartBeatTask = null;
        }

        glowingBlocks.disable();
    }

    public boolean callEvent(Event event) {
        getServer().getPluginManager().callEvent(event);
        if (event instanceof Cancellable cancellable) {
            return !cancellable.isCancelled();
        } else {
            return true;
        }
    }

    public static GlassItemFrames getInstance() {
        return plugin;
    }

    public static GlowingBlocks getGlowingBlocks() {
        return glowingBlocks;
    }
}
