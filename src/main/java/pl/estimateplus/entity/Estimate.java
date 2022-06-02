package pl.estimateplus.entity;

import pl.estimateplus.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private Long numberOfItems;
    private BigInteger totalNetAmount;
    private BigInteger totalVatAmount;
    private BigInteger totalGrossAmount;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdOn;
    @OneToMany
    List<EstimateItem> estimateItems;

    public Estimate() {
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

    public Long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public BigInteger getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(BigInteger totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    public BigInteger getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(BigInteger totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }

    public BigInteger getTotalGrossAmount() {
        return totalGrossAmount;
    }

    public void setTotalGrossAmount(BigInteger totalGrossAmount) {
        this.totalGrossAmount = totalGrossAmount;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<EstimateItem> getEstimateItems() {
        return estimateItems;
    }

    public void setEstimateItems(List<EstimateItem> estimateItems) {
        this.estimateItems = estimateItems;
    }

    @Override
    public String toString() {
        return "Estimate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", totalNetAmount=" + totalNetAmount +
                ", totalVatAmount=" + totalVatAmount +
                ", totalGrossAmount=" + totalGrossAmount +
                ", createdOn=" + createdOn +
                ", estimateItems=" + estimateItems +
                '}';
    }
}
