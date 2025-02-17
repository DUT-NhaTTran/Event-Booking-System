package com.tmnhat.userservice.model;

import java.time.LocalDateTime;

public class Users {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

    private Users(Long id, String name, String email, String phoneNumber, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }


    private Users() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private Users(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String phoneNumber;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder() {

        }

        public Builder name(String name) {
            this.name = name;
            return this;

        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Users build() {
            return new Users(this);
        }
    }

}
