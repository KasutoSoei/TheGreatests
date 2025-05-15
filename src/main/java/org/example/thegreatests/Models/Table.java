package org.example.thegreatests.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
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

    @DatabaseField
    private Integer peopleLenght;

    public Table() {}

    public Table(int size, String location) {
        this.status = "Libre";
        this.size = size;
        this.location = location;
        this.peopleLenght = 0;
    }

    public int getId() {
        return id;
    }

    public Integer getPeopleLenght() {
        return peopleLenght;
    }

    public void setPeopleLenght(Integer peopleLenght) {
        this.peopleLenght = peopleLenght;
        if (peopleLenght > 0) {
            this.status = "Occupé";
        } else {
            this.status = "Libre";
        }
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
