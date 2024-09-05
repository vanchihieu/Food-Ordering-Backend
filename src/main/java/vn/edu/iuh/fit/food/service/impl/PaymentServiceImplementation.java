package vn.edu.iuh.fit.food.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.food.model.Order;
import vn.edu.iuh.fit.food.response.PaymentResponse;
import vn.edu.iuh.fit.food.service.PaymentService;

@Service
public class PaymentServiceImplementation implements PaymentService {
    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentResponse generatePaymentLink(Order order) {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success/" + order.getId())
                .setCancelUrl("https://zosh-food.vercel.app/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) order.getTotalAmount() * 100) // Specify the order amount in cents
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("hieu food")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        System.out.println("session _____ " + session);

        PaymentResponse res = new PaymentResponse();
        res.setPayment_url(session.getUrl());

        return res;
    }
}
