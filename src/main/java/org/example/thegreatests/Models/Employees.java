package org.example.thegreatests.Models;

public class Employees extends People {
    private int id;
    private String name;
    private String job;
    private int workedHours;

    public Employees(String name, String job, int workedHours) {
        this.name = name;
        this.job = job;
        this.workedHours = workedHours;
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
