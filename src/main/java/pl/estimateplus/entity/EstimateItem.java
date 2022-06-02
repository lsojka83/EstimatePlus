package pl.estimateplus.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int individualVatRate;
    private BigInteger vatAmount;
    private BigInteger unitGrossPrice;
    @OneToMany
    private List<PriceListItem> priceListItems;

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

    public BigInteger getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigInteger vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigInteger getUnitGrossPrice() {
        return unitGrossPrice;
    }

    public void setUnitGrossPrice(BigInteger unitGrossPrice) {
        this.unitGrossPrice = unitGrossPrice;
    }

    public List<PriceListItem> getPriceListItems() {
        return priceListItems;
    }

    public void setPriceListItems(List<PriceListItem> priceListItems) {
        this.priceListItems = priceListItems;
    }

    @Override
    public String toString() {
        return "EstimateItem{" +
                "id=" + id +
                ", individualVatRate=" + individualVatRate +
                ", vatAmount=" + vatAmount +
                ", unitGrossPrice=" + unitGrossPrice +
                ", priceListItems=" + priceListItems +
                '}';
    }
}
