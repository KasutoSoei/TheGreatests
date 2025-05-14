package org.example.thegreatests.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "Invoices")
public class Invoices {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String description;

    @DatabaseField
    private String status;

    @DatabaseField
    private float totalPrice;

    @DatabaseField
    private Date date;

    public Invoices() {}

    public Invoices(String description, String status, float totalPrice, Date date) {
        this.description = description;
        this.status = status;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
