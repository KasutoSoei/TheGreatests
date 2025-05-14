package org.example.thegreatests.Models;

import com.j256.ormlite.field.DatabaseField;

public class People {

    @DatabaseField(generatedId = true)
    protected int id;

    public People(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
