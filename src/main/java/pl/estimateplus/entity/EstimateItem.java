package pl.estimateplus.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(0)
    private int individualVatRate;
    private BigDecimal totalNetPrice;
    @Min(1)
    private int quantity;
    @OneToOne
    private PriceListItem priceListItem;

    public EstimateItem() {
    }

    public void calculateAmounts(int quantity)
    {
        this.totalNetPrice = this.priceListItem.getUnitNetPrice().multiply(BigDecimal.valueOf(quantity));
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

    @Override
    public String toString() {
        return "EstimateItem{" +
                "id=" + id +
                ", individualVatRate=" + individualVatRate +
                ", totalNetPrice=" + totalNetPrice +
                ", quantity=" + quantity +
                ", priceListItem=" + priceListItem +
                '}';
    }
}
