package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.estimateplus.entity.EstimateItem;

public interface EstimateItemRepository extends JpaRepository<EstimateItem, Long> {
}
