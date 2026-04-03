package net.enelson.sopli.elevators.config;

import java.util.Set;
import org.bukkit.Material;

public record ElevatorType(
        String key,
        Material material,
        int searchRange,
        Set<String> allowedCustomBlockIds) {
}
