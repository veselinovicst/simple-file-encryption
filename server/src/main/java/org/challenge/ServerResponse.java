package org.challenge;

public enum ServerResponse {

    SERVER_READY(1),
    INVALID_OPERATION(2),
    FILE_FOUND_SUCCESSFULLY(3),
    FILE_NOT_FOUND(4);

    private int id;

    ServerResponse(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

}
