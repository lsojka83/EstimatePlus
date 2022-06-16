package pl.estimateplus.repository;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.estimateplus.entity.PriceList;

import java.util.List;
import java.util.Optional;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.name = :name")
    PriceList findByName(@Param("name") String name);

    @Query("SELECT p FROM PriceList p LEFT JOIN FETCH p.priceListItems WHERE p.id = :id")
    PriceList findByIdWithPriceListItems(@Param("id") Long id);

    @Query(nativeQuery = true,value = "SELECT * FROM estimateplus.pricelist p LEFT JOIN estimateplus.user u ON p.id=u.userPriceList_id WHERE (u.id is null OR u.id = ?1)")
    List<PriceList> findAllByUserAndAllGeneral(Long id);


}
