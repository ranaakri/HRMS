package com.mycompany.hrms.data.dtos.game.response;

public class RequestedByUser {

    public RequestedByUser(long userId, String name, String email){
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    private long userId;

    private String name;

    private String email;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
