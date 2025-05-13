package org.example.thegreatests.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Employees")
public class Employees extends People {

    @DatabaseField
    private String name;

    @DatabaseField
    private String job;

    @DatabaseField
    private int workedHours;

    public Employees() {
        super(0);
    }

    public Employees(String name, String job, int workedHours) {
        super(0);
        this.name = name;
        this.job = job;
        this.workedHours = workedHours;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(int workedHours) {
        this.workedHours = workedHours;
    }


}
