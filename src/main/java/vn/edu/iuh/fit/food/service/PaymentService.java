package vn.edu.iuh.fit.food.service;

import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.response.PaymentResponse;

public interface PaymentService {
    public PaymentResponse generatePaymentLink(Order order);
}