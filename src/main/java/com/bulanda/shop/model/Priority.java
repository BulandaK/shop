package com.bulanda.shop.model;

public enum Priority {
    LOW(1),
    HIGH(9);

    private final int rabbitPriority;

    Priority(int rabbitPriority) { this.rabbitPriority = rabbitPriority; }

    public int rabbitPriority() { return rabbitPriority; }
}
