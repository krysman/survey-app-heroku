package com.saprykin.surveyapp.model;

import javax.persistence.*;


@Entity
@Table(name = "USERS")
public class User {

    private int id;
    private String email;
    private boolean emailConfirmation;
    private boolean emailNotifications; // should we send to user some useful information
    private String confirmationToken; // token that we send to user's e-mail so he or she could use it to confirm e-mail
    private Role role;

    public User() {
    }

    public User(int id, String email, boolean emailConfirmation, boolean emailNotifications, String confirmationToken, Role role) {
        this.id = id;
        this.email = email;
        this.emailConfirmation = emailConfirmation;
        this.emailNotifications = emailNotifications;
        this.confirmationToken = confirmationToken;
        this.role = role;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(sequenceName = "USER_ID_SEQUENCE", name = "UserIdSequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserIdSequence")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "emailConfirmation",  nullable = false)
    public boolean isEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(boolean emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    @Column(name = "emailNotifications",  nullable = false)
    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    @Column(name = "confirmationToken", unique = true, nullable = true)
    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    @ManyToOne // no cascade type because cascading only (well ALMOST ) makes sense only for Parent – Child associations
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
