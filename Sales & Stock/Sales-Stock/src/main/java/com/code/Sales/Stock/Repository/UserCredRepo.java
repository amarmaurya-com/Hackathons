package com.code.Sales.Stock.Repository;

import com.code.Sales.Stock.model.UserCred;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredRepo extends JpaRepository<UserCred, Integer> {
    Optional<UserCred> findByUsername(String username);
    boolean existsByUsername(String username);
}

