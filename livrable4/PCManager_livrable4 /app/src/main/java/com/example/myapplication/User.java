package com.example.myapplication;
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long createdAt;
    private long modifiedAt;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
        this.modifiedAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public long getCreatedAt() { return createdAt; }
    public long getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(long modifiedAt) { this.modifiedAt = modifiedAt; }
    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }

}
