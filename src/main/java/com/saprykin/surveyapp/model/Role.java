package com.saprykin.surveyapp.model;


import javax.persistence.*;

/**
 * Created by Ivan on 3/12/2015.
 *
 * Roles are: User, Manager, Admin
 * Manager has all rights of User plus some more
 * Admin has all rights of Manager (and User) and some more
 */

@Entity
@Table(name = "ROLES")
public class Role {

    private int id;
    private String role;

    public Role() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)

    @SequenceGenerator(sequenceName = "ROLE_ID_SEQUENCE", name = "RoleIdSequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleIdSequence")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "role", unique = true, nullable = false, length = 15)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
