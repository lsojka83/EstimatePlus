package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;

import java.util.Optional;

public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {

    Optional<PriceListItem> findByReferenceNumber(String refNumber);
}
