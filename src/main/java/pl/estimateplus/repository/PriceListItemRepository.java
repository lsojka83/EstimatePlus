package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.estimateplus.entity.PriceListItem;

import java.util.List;
import java.util.Optional;

public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {

    Optional<PriceListItem> findByReferenceNumber(String refNumber);

    @Query(nativeQuery = true,
            value = "SELECT * FROM pricelistitem LEFT JOIN pricelist_pricelistitem on pricelistitem.id = priceListItems_id left join user on PriceList_id = userPriceList_id where (userPriceList_id is null OR user.id = ?1) AND pricelistitem.referenceNumber LIKE ?2")
    Optional<PriceListItem> findByUserIdAndReferenceNumber(Long userId, String refNo);

    @Query(nativeQuery = true,
            value = "SELECT * FROM pricelistitem LEFT JOIN pricelist_pricelistitem on pricelistitem.id = priceListItems_id left join user on PriceList_id = userPriceList_id where (userPriceList_id is null OR user.id = ?1) AND pricelistitem.referenceNumber LIKE ?2")
    List<PriceListItem> findAllByUserIdAndReferenceNumber(Long userId, String refNo);

}

