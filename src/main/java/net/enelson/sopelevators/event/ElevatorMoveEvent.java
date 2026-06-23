package net.enelson.sopelevators.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ElevatorMoveEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String direction;

    public ElevatorMoveEvent(Player player, String direction) {
        this.player = player;
        this.direction = direction;
    }

    public Player getPlayer() {
        return player;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
