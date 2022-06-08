package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.estimateplus.entity.Estimate;
import pl.estimateplus.entity.User;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

//    List<Estimate> findAllByUser(User user);
//    @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.name")
//    Estimate findByUserIdAndEstimateName(Long userId, String estimateName);
    Estimate findByName(String estimateName);

//    Estimate findByUserIdAndEstimateName(Long userId, String estimateName);

}
