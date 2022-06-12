package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.estimateplus.entity.Estimate;
import pl.estimateplus.entity.EstimateItem;

public interface EstimateItemRepository extends JpaRepository<EstimateItem, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "DELETE FROM estimateplus.estimate_estimateitem WHERE estimateItems_id = ?1")
    void deleteFromRelationTableById(Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM estimateplus.estimate_estimateitem WHERE estimateItems_id = ?1")
    EstimateItem findByItemInParentTable(Long id);

//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true,value = "DELETE FROM estimateplus.estimateitem WHERE id = ?1")
//    void deleteById(String id);


//    @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.estimateItems i where i.id = :id")
//    Estimate findByPriceListItemId(@Param("id") Long id);

    @Query("SELECT ei FROM EstimateItem ei LEFT JOIN FETCH ei.priceListItem pi where pi.id = :id")
    EstimateItem findByPriceListItemId(@Param("id") Long id);
}
