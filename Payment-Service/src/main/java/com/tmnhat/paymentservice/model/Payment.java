package com.tmnhat.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Payment {
    private Long id;
    @JsonProperty("booking_id")  // Đảm bảo ánh xạ với JSON

    private Long bookingId;
    @JsonProperty("user_id")
    private Long userId;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;

    private Payment() {

    }

    private Payment(Long id, Long bookingId, Long userId, Double amount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }


    //Getter,Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Payment(Builder builder) {
        this.id = builder.id;
        this.bookingId = builder.bookingId;
        this.userId = builder.userId;
        this.amount = builder.amount;
        this.status = builder.status;
        this.createdAt = builder.createdAt;
    }
    public static class Builder{
        private Long id;
        private Long bookingId;
        private Long userId;
        private Double amount;
        private String status;
        private LocalDateTime createdAt;

        public Builder() {
        }
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Payment build() {
            return new Payment(this);
        }
    }

}
