package com.andriod.simpletodoapp;

import java.util.Date;

public class Item {
    private long id;
    private String description;
    private int priority;
    private Date duedate;

    public Item(String description, int priority, Date duedate) {
        this.description = description;
        this.priority = priority;
        this.duedate = duedate;
    }

    public Item(long id, String description, int priority, Date duedate) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.duedate = duedate;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public Date getDuedate() {
        return duedate;
    }

}
