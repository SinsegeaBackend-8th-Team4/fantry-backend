package com.eneifour.fantry.payment.domain;

import com.eneifour.fantry.payment.exception.PaymentAmountMismatchException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;
    @Column(name = "receipt_id", unique = true)
    private String receiptId;
    @Column(name = "order_id", unique = true)
    private String orderId;
    @Column(name = "price")
    private Integer price;
    @Column(name = "cancelled_price")
    private Integer cancelledPrice;
    @Column(name = "order_name")
    private String orderName;
    @Column(name = "metadata")
    private String metadata;
    @Column(name = "pg")
    private String pg;
    @Column(name = "method")
    private String method;
    @Column(name = "currency")
    private String currency;
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime created_at;
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @Column(name = "requested_at")
    private ZonedDateTime requestedAt;
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @Column(name = "purchased_at")
    private ZonedDateTime purchasedAt;
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE_UTC)
    @Column(name = "cancelled_at")
    private ZonedDateTime cancelledAt;
    @Column(name = "receipt_url")
    private String receiptUrl;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PAYMENT_WAITING;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_info", columnDefinition = "json")
    private Map<String, Object> paymentInfo;
    @Version
    @Column(name = "version")
    Long version = 0L;

    public void validateAmount(Integer price) {
        if (this.price - (int) price != 0) {
            throw new PaymentAmountMismatchException();
        }
    }

    public boolean isPaymentWaiting() {
        return this.status == PaymentStatus.PAYMENT_WAITING;
    }

    public boolean isPaymentApproving() {
        return this.status == PaymentStatus.PAYMENT_APPROVING;
    }
}
