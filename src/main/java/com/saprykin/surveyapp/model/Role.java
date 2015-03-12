package com.saprykin.surveyapp.model;

/**
 * Created by Ivan on 3/12/2015.
 *
 * Roles are: User, Manager, Admin
 * Manager has all rights of User plus some more
 * Admin has all rights of Manager (and User) and some more
 */
public class Role {

    int id;
    String role;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
