package net.enelson.sopelevators.config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public record ElevatorSettings(
        long cooldownMillis,
        boolean useJumpToGoUp,
        boolean useSneakToGoDown,
        List<ElevatorType> elevatorTypes) {

    public static ElevatorSettings fromConfig(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        List<ElevatorType> elevatorTypes = new ArrayList<>();
        ConfigurationSection elevatorsSection = config.getConfigurationSection("elevators");

        if (elevatorsSection != null) {
            for (String key : elevatorsSection.getKeys(false)) {
                ConfigurationSection elevatorSection = elevatorsSection.getConfigurationSection(key);
                if (elevatorSection == null) {
                    continue;
                }

                Material material = null;
                String materialName = elevatorSection.getString("material");
                if (materialName != null && !materialName.isBlank()) {
                    material = Material.matchMaterial(materialName);
                }

                Set<String> ids = elevatorSection.getStringList("acustomblocks-ids").stream()
                        .map(value -> value.toLowerCase(Locale.ROOT))
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                if (material == null && ids.isEmpty()) {
                    continue;
                }

                elevatorTypes.add(new ElevatorType(
                        key,
                        material,
                        Math.max(2, elevatorSection.getInt("search-range", 16)),
                        ids));
            }
        }

        if (elevatorTypes.isEmpty()) {
            elevatorTypes.add(new ElevatorType("default", Material.DAYLIGHT_DETECTOR, 16, Set.of()));
        }

        return new ElevatorSettings(
                Math.max(0L, config.getLong("settings.cooldown-ms", 750L)),
                config.getBoolean("settings.use-jump-to-go-up", true),
                config.getBoolean("settings.use-sneak-to-go-down", true),
                List.copyOf(elevatorTypes));
    }
}
