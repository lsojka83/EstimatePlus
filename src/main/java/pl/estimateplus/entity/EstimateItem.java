package pl.estimateplus.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int individualVatRate;
//    private BigInteger vatAmount;
    private BigDecimal totalNetPrice;
    private int quantity;
    @OneToOne
    private PriceListItem priceListItem;

    public EstimateItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndividualVatRate() {
        return individualVatRate;
    }

    public void setIndividualVatRate(int individualVatRate) {
        this.individualVatRate = individualVatRate;
    }

//    public BigInteger getVatAmount() {
//        return vatAmount;
//    }
//
//    public void setVatAmount(BigInteger vatAmount) {
//        this.vatAmount = vatAmount;
//    }


    public BigDecimal getTotalNetPrice() {
        return totalNetPrice;
    }

    public void setTotalNetPrice(BigDecimal totalNetPrice) {
        this.totalNetPrice = totalNetPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PriceListItem getPriceListItem() {
        return priceListItem;
    }

    public void setPriceListItem(PriceListItem priceListItem) {
        this.priceListItem = priceListItem;
    }

}
