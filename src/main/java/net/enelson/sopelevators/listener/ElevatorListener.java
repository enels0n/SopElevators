package net.enelson.sopelevators.listener;

import net.enelson.sopelevators.SopElevatorsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public final class ElevatorListener implements Listener {
    private final SopElevatorsPlugin plugin;

    public ElevatorListener(SopElevatorsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getSettings().isUseJumpToGoUp() || event.getTo() == null) {
            return;
        }
        if (event.getTo().getY() <= event.getFrom().getY()) {
            return;
        }
        plugin.getElevatorService().tryTeleportUp(event.getPlayer(), event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!plugin.getSettings().isUseSneakToGoDown() || !event.isSneaking()) {
            return;
        }

        plugin.getElevatorService().tryTeleportDown(event.getPlayer());
    }
}
