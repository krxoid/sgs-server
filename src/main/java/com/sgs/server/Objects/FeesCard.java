package com.sgs.server.Objects;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fees_cards")
public class FeesCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFees = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paidFees = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dueFees = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean paid = false;

    private LocalDate lastPaymentDate;

    protected FeesCard() {
        // Required by JPA
    }

    public FeesCard(UUID studentId, BigDecimal totalFees) {
        this.studentId = studentId;
        this.totalFees = totalFees;
        this.dueFees = totalFees;
    }

    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public BigDecimal getTotalFees() {
        return totalFees;
    }

    public BigDecimal getPaidFees() {
        return paidFees;
    }

    public BigDecimal getDueFees() {
        return dueFees;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void recordPayment(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment must be greater than zero");
        }

        if (amount.compareTo(dueFees) > 0) {
            throw new IllegalArgumentException("Payment exceeds outstanding fees");
        }

        paidFees = paidFees.add(amount);
        dueFees = totalFees.subtract(paidFees);
        paid = dueFees.signum() == 0;
        lastPaymentDate = LocalDate.now();
    }
}