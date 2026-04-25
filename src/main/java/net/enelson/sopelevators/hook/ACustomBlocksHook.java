package net.enelson.sopelevators.hook;

import java.util.Locale;
import java.util.Set;
import net.enelson.astract.customblocks.ACustomBlocks;
import net.enelson.astract.customblocks.managers.blocks.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public final class ACustomBlocksHook {
    public boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("ACustomBlocks") != null && ACustomBlocks.getInstance() != null;
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
            return ACustomBlocks.getInstance().getBlockManager().getBlock(location.getBlock().getLocation());
        } catch (Throwable ignored) {
            return null;
        }
    }
}
