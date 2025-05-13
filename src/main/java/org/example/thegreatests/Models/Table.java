package org.example.thegreatests.Models;

import java.util.List;

public class Table {
    private int id;
    private String status;
    private int size;
    private String location;
    private List<People> peopleList;

    public Table(String status, int size, String location, List<People> peopleList) {
        this.status = status;
        this.size = size;
        this.location = location;
        this.peopleList = peopleList;
    }

    public int getId() {
        return id;
    }

    public List<People> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<People> peopleList) {
        this.peopleList = peopleList;
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
