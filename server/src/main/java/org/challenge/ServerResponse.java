package org.challenge;

public enum ServerResponse {

    SERVER_READY(1),
    INVALID_OPERATION(2);

    private int id;

    ServerResponse(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

}
