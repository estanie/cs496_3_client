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
    private String uId;
    private String date_of_birth;
    private int like_me;
    private String token;
    private Boolean likeMe = false;

    public User() {}
    public User(String image, String name, String gender, String age, String residence, String contact,
         String job, String hobby, String photo, String uId, String date_of_birth, int like_me, String token) {
        this.image = image;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.residence = residence;
        this.contact = contact;
        this.job = job;
        this.hobby = hobby;
        this.photo = photo;
        this.uId = uId;
        this.date_of_birth = date_of_birth;
        this.like_me = like_me;
        this.token = token;
    }

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

    public String getResidence() { return residence; }
    public void setResidence(String age) { this.residence= residence; }

    public String getHobby() { return hobby; }
    public void setHobby(String hobby) { this.hobby = hobby; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public String getUId() {return uId;}
    public void setUId(String uId){this.uId = uId;}

    public String getDate_of_birth() {return date_of_birth;}
    public void setDate_of_birth(String date_of_birth) {this.date_of_birth = date_of_birth;}

    public int getLike_me() {return like_me;}
    public void setLike_me(int like_me) {this.like_me = like_me;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

}
