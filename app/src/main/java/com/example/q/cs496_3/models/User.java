package com.example.q.cs496_3.models;

public class User {
    private String image;
    private String name;
    private String gender;
    private String age;
    private String residence;
    private String contact;
    private String job;
    private String hobby;
    private String photo;
    private String id;

    //TODO : Image

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {return gender;}
    public void setgender(String gender) {this.gender = gender;}

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getResidence() { return age; }
    public void setResidence(String age) { this.age = age; }

    public String getHobby() { return hobby; }
    public void setHobby(String hobby) { this.hobby = hobby; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public String getId() {return id;}
    public void setId(String id){this.id = id;}
}
