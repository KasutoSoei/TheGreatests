package org.example.thegreatests.Models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "Commands")
public class Commands {

    @DatabaseField(generatedId = true)
    private int id;

    private List<Dishes> dishiesList;

    @DatabaseField
    private Integer idTable;

    @DatabaseField
    private String status;

    @DatabaseField
    private String date;

    public Commands() {}

    public Commands(List<Dishes> dishiesList, Integer idTable, String status, String date) {
        this.dishiesList = dishiesList;
        this.idTable = idTable;
        this.status = status;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public List<Dishes> getDishiesList() {
        return dishiesList;
    }

    public void setDishiesList(List<Dishes> dishiesList) {
        this.dishiesList = dishiesList;
    }

    public Integer getIdTable() {
        return idTable;
    }

    public void setIdTable(Integer idTable) {
        this.idTable = idTable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
