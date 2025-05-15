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

    @DatabaseField
    private Integer age;

    public Employees() {
        super(0);
    }

    public Employees(String name, String job, Integer age) {
        super(0);
        this.name = name;
        this.job = job;
        this.workedHours = 0;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
