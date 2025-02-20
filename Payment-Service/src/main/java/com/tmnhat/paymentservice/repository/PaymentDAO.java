package com.tmnhat.paymentservice.repository;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.paymentservice.model.Payment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends BaseDAO{
    public void savePayment(Payment payment) throws SQLException {
        String savePayment="INSERT INTO payment (booking_id,user_id,amount,status,created_at) VALUES (?,?,?,?,?)";
        System.out.println("save payment "+payment);
        executeUpdate(savePayment,stmt->{
            stmt.setLong(1, payment.getBookingId());
            stmt.setLong(2, payment.getUserId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getStatus());
            stmt.setTimestamp(5,payment.getCreatedAt()!=null? Timestamp.valueOf(payment.getCreatedAt()):null);
        });
    }
    public void updatePayment(Long id,Payment payment) throws SQLException {
        String updatePayment="UPDATE payment SET booking_id=?,user_id=?,amount=?,status=?,created_at=? WHERE id=?";
        executeUpdate(updatePayment,stmt->{
            stmt.setLong(1, payment.getBookingId());
            stmt.setLong(2, payment.getUserId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getStatus());
            stmt.setTimestamp(5,payment.getCreatedAt()!=null? Timestamp.valueOf(payment.getCreatedAt()):null);
            stmt.setLong(6, id);

        });
    }
    public List<Payment> getAllPayments() throws SQLException {
        String getAllPayments="SELECT * FROM payment";
        return executeQuery(getAllPayments,stmt->{
            List<Payment> paymentList=new ArrayList<>();
            ResultSet rs= stmt.executeQuery();
            while(rs.next()){
                Payment payment = new Payment.Builder()
                        .id(rs.getLong("id"))
                        .bookingId(rs.getLong("booking_id"))
                        .userId(rs.getLong("user_id"))
                        .amount(rs.getDouble("amount"))
                        .status(rs.getString("status"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
                paymentList.add(payment);
            }
            return paymentList;
        });
    }
    public Payment getPaymentById(Long id) throws SQLException {
        String getPaymentById="SELECT * FROM payment WHERE id=?";
        return executeQuery(getPaymentById,stmt->{
            stmt.setLong(1,id);

            ResultSet rs= stmt.executeQuery();
            if(rs.next()){
                return new Payment.Builder()
                        .id(rs.getLong("id"))
                        .bookingId(rs.getLong("booking_id"))
                        .userId(rs.getLong("user_id"))
                        .amount(rs.getDouble("amount"))
                        .status(rs.getString("status"))
                        .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                        .build();
            }
            return null;
        });
    }

    public Payment getPaymentByBookingId(Long bookingId) throws SQLException {
        String query = "SELECT * FROM payment WHERE booking_id = ?";

        return executeQuery(query, stmt -> {
            stmt.setLong(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Payment.Builder()
                        .id(rs.getLong("id"))
                        .bookingId(rs.getLong("booking_id"))
                        .userId(rs.getLong("user_id"))
                        .amount(rs.getDouble("amount"))
                        .status(rs.getString("status"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
            return null;
        });
    }

}
