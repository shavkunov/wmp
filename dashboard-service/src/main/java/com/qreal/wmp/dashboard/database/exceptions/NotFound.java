package com.qreal.wmp.dashboard.database.exceptions;

public class NotFound extends Exception {
    private final String id;

    public NotFound(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return  id;
    }
}
