package com.bulanda.shop.model;

public enum Channel {
    EMAIL("email"),
    PUSH("push");

    private final String routingKey;

    Channel(String routingKey) { this.routingKey = routingKey; }

    public String routingKey() { return routingKey; }
}
