package com.code.Sales.Stock.Repository;

import com.code.Sales.Stock.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByProduct(String product);
}
