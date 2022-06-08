package pl.estimateplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.estimateplus.entity.PriceList;
import pl.estimateplus.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.estimates WHERE u.userName = :name")
    User findByUserName(@Param("name") String name);

}

//    LEFT JOIN FETCH u.userPriceList