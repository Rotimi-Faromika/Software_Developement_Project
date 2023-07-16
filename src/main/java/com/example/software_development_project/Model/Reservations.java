package com.example.software_development_project.Model;

public class Reservations {
    private String pid, pname, quantity,date, time,name, phone;

    public Reservations() {
    }

    public Reservations(String pid, String pname, String quantity, String date, String time, String name, String phone) {
        this.pid = pid;
        this.pname = pname;
        this.quantity = quantity;
        this.date = date;
        this.time = time;
        this.name = name;
        this.phone = phone;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
