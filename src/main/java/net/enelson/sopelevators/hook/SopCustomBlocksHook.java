package net.enelson.sopelevators.hook;

import java.util.Locale;
import java.util.Set;
import net.enelson.sopcustomblocks.SopCustomBlocks;
import net.enelson.sopcustomblocks.managers.blocks.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class SopCustomBlocksHook {
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("SopCustomBlocks") != null && SopCustomBlocks.getInstance() != null;
    }

    public boolean isElevatorBlock(Block block, Set<String> allowedIds) {
        String customBlockId = getCustomBlockId(block);
        if (customBlockId == null) {
            return false;
        }
        return allowedIds.isEmpty() || allowedIds.contains(customBlockId);
    }

    public String getCustomBlockId(Block block) {
        CustomBlock customBlock = getCustomBlock(block.getLocation());
        return customBlock != null ? customBlock.getId().toLowerCase(Locale.ROOT) : null;
    }

    private CustomBlock getCustomBlock(Location location) {
        if (!isAvailable()) {
            return null;
        }

        try {
            return SopCustomBlocks.getInstance().getBlockManager().getBlock(location.getBlock().getLocation());
        } catch (Throwable ignored) {
            return null;
        }
    }
}
