package com.example.software_development_project.Model;

@SuppressWarnings("unused")
public class Users {
    private String Name;
    private String Phone;
    private String Password;
    private String Image;
    private String Address;

    public Users() {

    }

    public Users(String name, String phone, String password, String image, String address) {
        this.Password = password;
        this.Phone = phone;
        this.Name = name;
        this.Image = image;
        this.Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    public String getPhone(){
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


}
