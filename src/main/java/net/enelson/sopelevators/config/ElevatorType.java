package net.enelson.sopelevators.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bukkit.Material;

public final class ElevatorType {
    private final String key;
    private final Material material;
    private final int searchRange;
    private final Set<String> allowedCustomBlockIds;

    public ElevatorType(String key, Material material, int searchRange, Set<String> allowedCustomBlockIds) {
        this.key = key;
        this.material = material;
        this.searchRange = searchRange;
        this.allowedCustomBlockIds = Collections.unmodifiableSet(new LinkedHashSet<String>(allowedCustomBlockIds));
    }

    public String getKey() {
        return key;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSearchRange() {
        return searchRange;
    }

    public Set<String> getAllowedCustomBlockIds() {
        return allowedCustomBlockIds;
    }
}
