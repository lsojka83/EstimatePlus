package pl.estimateplus.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private Long numberOfItems;
    @OneToMany
    private List<PriceListItem> priceListItems;

    public PriceList() {
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

    public List<PriceListItem> getPriceListItems() {
        return priceListItems;
    }

    public void setPriceListItems(List<PriceListItem> priceListItems) {
        this.priceListItems = priceListItems;
    }

    @Override
    public String toString() {
        return "PriceList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", priceListItems=" + priceListItems +
                '}';
    }
}
