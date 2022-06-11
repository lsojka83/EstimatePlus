package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.estimateplus.entity.EstimateItem;

public interface EstimateItemRepository extends JpaRepository<EstimateItem, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM estimateplus.estimate_estimateitem WHERE estimateItems_id = ?1")
    void deleteFromRelationTableById(String id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM estimateplus.estimateitem WHERE id = ?1")
    void deleteById(String id);


}
