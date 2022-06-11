package pl.estimateplus.repository;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.PriceListItem;

import java.util.Optional;

public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {

    Optional<PriceListItem> findByReferenceNumber(String refNumber);

  /*  @Query("SELECT ")
    Optional<PriceListItem> findByReferenceNumber(String refNumber);*/
}
