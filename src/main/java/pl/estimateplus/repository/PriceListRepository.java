package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.estimateplus.entity.PriceList;

import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.name = :name")
    PriceList findByName(@Param("name") String name);

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.id = :id")
    PriceList findByIdWithPriceListItems(@Param("id") Long id);

}
