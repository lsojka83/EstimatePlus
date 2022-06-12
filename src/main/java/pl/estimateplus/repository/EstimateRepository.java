package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.estimateplus.entity.Estimate;
import pl.estimateplus.entity.User;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

//    List<Estimate> findAllByUser(User user);
//    @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.name")
//    Estimate findByUserIdAndEstimateName(Long userId, String estimateName);
    Estimate findByName(String estimateName);

//    Estimate findByUserIdAndEstimateName(Long userId, String estimateName);

//        @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.name")
//    Estimate findByUserIdAndEstimateName(Long userId, String estimateName);

//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true,value = "DELETE FROM estimateplus.estimate WHERE estimate.id = ?1")
//    void deleteById(Long id);
//
}
