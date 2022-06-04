package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.estimateplus.entity.PriceListItem;

public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {
}
