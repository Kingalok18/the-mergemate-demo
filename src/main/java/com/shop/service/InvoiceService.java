package com.shop.service;

import com.shop.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Builds the S3 location for invoice PDFs and emails them to customers.
 */
@Service
public class InvoiceService {

    private static final String SMTP_PASSWORD = "Shop@12345";

    private final String bucket;

    public InvoiceService(@Value("${aws.s3.bucket}") String bucket) {
        this.bucket = bucket;
    }

    public String invoiceKey(Order order) {
        return bucket + "/invoices/" + order.id() + ".pdf";
    }

    public boolean canSendEmail() {
        return !SMTP_PASSWORD.isBlank();
    }
}
