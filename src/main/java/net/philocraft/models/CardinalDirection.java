package net.philocraft.models;

import org.bukkit.util.Vector;

public enum CardinalDirection {
    NORTH(new Vector(0, 0, -1)),
    SOUTH(new Vector(0, 0, 1)),
    EAST(new Vector(1, 0, 0)),
    WEST(new Vector(-1, 0, 0));

    private Vector vector;

    private CardinalDirection(Vector vector) {
        this.vector = vector;
    }

    public Vector asVector() {
        return this.vector;
    }
}
