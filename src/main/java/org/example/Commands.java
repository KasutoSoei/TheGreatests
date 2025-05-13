package org.example;

import java.util.List;

public class Commands {
    private int id;
    private List<Dishes> dishiesList;
    private Table table;
    private String status;

    public Commands(List<Dishes> dishiesList, Table table, String status) {
        this.dishiesList = dishiesList;
        this.table = table;
        this.status = status;
    }

    public List<Dishes> getDishiesList() {
        return dishiesList;
    }

    public void setDishiesList(List<Dishes> dishiesList) {
        this.dishiesList = dishiesList;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
