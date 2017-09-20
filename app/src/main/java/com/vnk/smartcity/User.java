package com.vnk.smartcity;

/**
 * Created by root on 20/9/17.
 */

public class User {
  private  String name;

    public User(String name, String email, String address, String mobile_no) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    private String email;
    private String address;
    private String mobile_no;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
