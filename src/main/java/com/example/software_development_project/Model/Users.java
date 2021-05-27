package com.example.software_development_project.Model;

public class Users {
    private String Name, Phone, Password;

    public Users(){

    }
    public Users(String name, String phone, String password){
        this.Password = password;
        this.Phone = phone;
        this.Name = name;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getPhone(){
        return Phone;
    }

    public void setPhone(String phone){
        this.Phone = phone;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(){
        this.Password = Password;
    }

}
