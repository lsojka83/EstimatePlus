package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.estimateplus.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query(value = "SELECT * FROM estimateplus.estimate e " +
            "JOIN  estimateplus.user_estimate ue ON e.id = ue.estimates_id " +
            "JOIN estimateplus.user u ON ue.User_id = u.id " +
            "WHERE e.name = ?1 AND u.userName = ?2",nativeQuery = true)
    Estimate findByNameAndUserName(String estimateName, String userName);
}
