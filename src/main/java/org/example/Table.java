package org.example;

public class Table {
    private int id;
    private String status;
    private int size;
    private String location;

    public Table(String status, int size, String location) {
        this.status = status;
        this.size = size;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
