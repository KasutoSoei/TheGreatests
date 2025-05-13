package org.example.thegreatests.Models;

import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private List<Table> tableList;
    private List<Commands> commandsList;

    public Restaurant(String name, List<Table> tableList, List<Commands> commandsList) {
        this.name = name;
        this.tableList = tableList;
        this.commandsList = commandsList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    public List<Commands> getCommandsList() {
        return commandsList;
    }

    public void setCommandsList(List<Commands> commandsList) {
        this.commandsList = commandsList;
    }
}
