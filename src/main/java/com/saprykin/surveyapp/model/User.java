package com.saprykin.surveyapp.model;

/**
 * Created by Ivan on 3/12/2015.
 */
public class User {

    int id;
    String email;
    boolean emailConfirmation;
    boolean emailNotifications; // should we send to user some useful information
    String confirmationToken; // token that we send to user's e-mail so he or she could use it to confirm e-mail
    //Role role;

    public User() {
    }

    public User(int id, String email, boolean emailConfirmation, boolean emailNotifications, String confirmationToken, Role role) {
        this.id = id;
        this.email = email;
        this.emailConfirmation = emailConfirmation;
        this.emailNotifications = emailNotifications;
        this.confirmationToken = confirmationToken;
        //this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(boolean emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
}
