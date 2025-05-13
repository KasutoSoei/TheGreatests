package org.example.thegreatests.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "Table")
public class Table {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String status;

    @DatabaseField
    private int size;

    @DatabaseField
    private String location;

    private List<People> peopleList;

    public Table() {}

    public Table(String status, int size, String location) {
        this.status = status;
        this.size = size;
        this.location = location;
        this.peopleList = null;
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
