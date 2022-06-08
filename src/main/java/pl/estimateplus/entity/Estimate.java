package pl.estimateplus.entity;

import pl.estimateplus.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long numberOfItems;
    private BigDecimal totalNetAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalGrossAmount;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime createdOn;
    @OneToMany(fetch = FetchType.EAGER)
    List<EstimateItem> estimateItems = new ArrayList<>();

    public Estimate() {
    }

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
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

    public BigDecimal getTotalNetAmount() {
        return totalNetAmount;
    }

    public void setTotalNetAmount(BigDecimal totalNetAmount) {
        this.totalNetAmount = totalNetAmount;
    }

    public BigDecimal getTotalVatAmount() {
        return totalVatAmount;
    }

    public void setTotalVatAmount(BigDecimal totalVatAmount) {
        this.totalVatAmount = totalVatAmount;
    }

    public BigDecimal getTotalGrossAmount() {
        return totalGrossAmount;
    }

    public void setTotalGrossAmount(BigDecimal totalGrossAmount) {
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
