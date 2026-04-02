package com.travelDestinationPlanner.fawry.repository;

import com.travelDestinationPlanner.fawry.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);
}
