package net.enelson.sopelevators.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElevatorSettings {
    private final long cooldownMillis;
    private final boolean useJumpToGoUp;
    private final boolean useSneakToGoDown;
    private final List<ElevatorType> elevatorTypes;

    public ElevatorSettings(long cooldownMillis, boolean useJumpToGoUp, boolean useSneakToGoDown, List<ElevatorType> elevatorTypes) {
        this.cooldownMillis = cooldownMillis;
        this.useJumpToGoUp = useJumpToGoUp;
        this.useSneakToGoDown = useSneakToGoDown;
        this.elevatorTypes = Collections.unmodifiableList(new ArrayList<ElevatorType>(elevatorTypes));
    }

    public static ElevatorSettings fromConfig(JavaPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        List<ElevatorType> elevatorTypes = new ArrayList<ElevatorType>();
        ConfigurationSection elevatorsSection = config.getConfigurationSection("elevators");

        if (elevatorsSection != null) {
            for (String key : elevatorsSection.getKeys(false)) {
                ConfigurationSection elevatorSection = elevatorsSection.getConfigurationSection(key);
                if (elevatorSection == null) {
                    continue;
                }

                Material material = null;
                String materialName = elevatorSection.getString("material");
                if (materialName != null && !materialName.trim().isEmpty()) {
                    material = Material.matchMaterial(materialName);
                }

                Set<String> ids = new LinkedHashSet<String>();
                List<String> configuredIds = elevatorSection.getStringList("custom-block-ids");
                if (configuredIds.isEmpty()) {
                    configuredIds = elevatorSection.getStringList("acustomblocks-ids");
                }
                for (String value : configuredIds) {
                    ids.add(value.toLowerCase(Locale.ROOT));
                }

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
            elevatorTypes.add(new ElevatorType("default", Material.DAYLIGHT_DETECTOR, 16, Collections.<String>emptySet()));
        }

        return new ElevatorSettings(
                Math.max(0L, config.getLong("settings.cooldown-ms", 750L)),
                config.getBoolean("settings.use-jump-to-go-up", true),
                config.getBoolean("settings.use-sneak-to-go-down", true),
                elevatorTypes);
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public boolean isUseJumpToGoUp() {
        return useJumpToGoUp;
    }

    public boolean isUseSneakToGoDown() {
        return useSneakToGoDown;
    }

    public List<ElevatorType> getElevatorTypes() {
        return elevatorTypes;
    }
}
