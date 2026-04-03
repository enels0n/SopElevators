package net.enelson.sopli.elevators.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.enelson.sopli.elevators.SElevatorsPlugin;
import net.enelson.sopli.elevators.service.ElevatorService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public final class ElevatorListener implements Listener {
    private final SElevatorsPlugin plugin;

    public ElevatorListener(SElevatorsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJump(PlayerJumpEvent event) {
        if (!plugin.getSettings().useJumpToGoUp()) {
            return;
        }

        plugin.getElevatorService().tryTeleportUp(event.getPlayer(), event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!plugin.getSettings().useSneakToGoDown() || !event.isSneaking()) {
            return;
        }

        plugin.getElevatorService().tryTeleportDown(event.getPlayer());
    }
}
