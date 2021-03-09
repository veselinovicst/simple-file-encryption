package org.challenge;

public enum OperationType {

    UPLOAD_OPERATION(1),
    DOWNLOAD_OPERATION(2);

    private int id;

    OperationType(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

}
