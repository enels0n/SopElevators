package net.enelson.sopelevators.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.enelson.sopelevators.SopElevatorsPlugin;
import net.enelson.sopelevators.config.ElevatorSettings;
import net.enelson.sopelevators.config.ElevatorType;
import net.enelson.sopelevators.event.ElevatorMoveEvent;
import net.enelson.sopelevators.hook.SopCustomBlocksHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class ElevatorService {
    private final SopElevatorsPlugin plugin;
    private final ElevatorSettings settings;
    private final SopCustomBlocksHook customBlocksHook;
    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public ElevatorService(SopElevatorsPlugin plugin, ElevatorSettings settings, SopCustomBlocksHook customBlocksHook) {
        this.plugin = plugin;
        this.settings = settings;
        this.customBlocksHook = customBlocksHook;
    }

    public boolean tryTeleportUp(Player player, Location referenceLocation) {
        Block baseBlock = getStandingBlock(referenceLocation);
        ElevatorType elevatorType = getElevatorType(baseBlock);
        if (elevatorType == null) {
            return false;
        }

        return teleport(player, baseBlock, elevatorType, 1);
    }

    public boolean tryTeleportDown(Player player) {
        Block baseBlock = getStandingBlock(player.getLocation());
        ElevatorType elevatorType = getElevatorType(baseBlock);
        if (elevatorType == null) {
            return false;
        }

        return teleport(player, baseBlock, elevatorType, -1);
    }

    private boolean teleport(Player player, Block origin, ElevatorType elevatorType, int direction) {
        if (isOnCooldown(player)) {
            return false;
        }

        Block destination = findDestination(origin, elevatorType, direction);
        if (destination == null) {
            return false;
        }

        Location target = destination.getLocation().add(0.5D, 1.01D, 0.5D);
        Vector velocity = new Vector(0.0D, 0.0D, 0.0D);

        Location facing = player.getLocation();
        target.setYaw(facing.getYaw());
        target.setPitch(facing.getPitch());

        player.teleport(target);
        player.setVelocity(velocity);
        plugin.getServer().getScheduler().runTask(plugin, () -> player.setVelocity(new Vector(0.0D, 0.0D, 0.0D)));
        player.playSound(target, Sound.BLOCK_NOTE_BLOCK_PLING, 0.7F, direction > 0 ? 1.35F : 0.85F);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        Bukkit.getPluginManager().callEvent(new ElevatorMoveEvent(player, direction > 0 ? "UP" : "DOWN"));
        return true;
    }

    private Block findDestination(Block origin, ElevatorType elevatorType, int direction) {
        for (int offset = direction; Math.abs(offset) <= elevatorType.getSearchRange(); offset += direction) {
            Block candidate = origin.getWorld().getBlockAt(origin.getX(), origin.getY() + offset, origin.getZ());
            if (matchesElevatorType(candidate, elevatorType) && isSafeDestination(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private boolean isSafeDestination(Block baseBlock) {
        return baseBlock.getRelative(0, 1, 0).isPassable() && baseBlock.getRelative(0, 2, 0).isPassable();
    }

    private ElevatorType getElevatorType(Block block) {
        if (block == null) {
            return null;
        }

        for (ElevatorType elevatorType : settings.getElevatorTypes()) {
            if (matchesElevatorType(block, elevatorType)) {
                return elevatorType;
            }
        }

        return null;
    }

    private boolean matchesElevatorType(Block block, ElevatorType elevatorType) {
        if (block == null) {
            return false;
        }

        if (elevatorType.getMaterial() != null && block.getType() == elevatorType.getMaterial()) {
            return true;
        }

        return customBlocksHook.isAvailable()
                && !elevatorType.getAllowedCustomBlockIds().isEmpty()
                && customBlocksHook.isElevatorBlock(block, elevatorType.getAllowedCustomBlockIds());
    }

    private Block getStandingBlock(Location location) {
        return location.clone().subtract(0.0D, 0.2D, 0.0D).getBlock();
    }

    private boolean isOnCooldown(Player player) {
        long lastUse = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        return System.currentTimeMillis() - lastUse < settings.getCooldownMillis();
    }
}
