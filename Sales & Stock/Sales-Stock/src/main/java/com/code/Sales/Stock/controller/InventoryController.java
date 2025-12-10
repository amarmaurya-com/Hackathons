package com.code.Sales.Stock.controller;

import com.code.Sales.Stock.model.Inventory;
import com.code.Sales.Stock.service.InventoryService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> addProduct(@RequestBody Inventory inventory) {
        return inventoryService.addProduct(inventory);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Inventory>> addProducts(@RequestBody List<Inventory> inventory) {
        return inventoryService.addProducts(inventory);
    }


    @GetMapping
    public ResponseEntity<List<Inventory>> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Inventory> getProductByName(@PathVariable String name) {
        return inventoryService.getProductsByName(name);
    }

    @PutMapping("/{name}/{quantity}")
    public ResponseEntity<String> modifySale(@PathVariable String name, @PathVariable int quantity) {
        return inventoryService.modifySale(name, quantity);
    }

    @PutMapping("/{product}")
    public ResponseEntity<Inventory> updateProduct(
            @PathVariable("product") String product,
            @RequestBody Inventory inventory) {
        return inventoryService.updateProduct(product, inventory);
    }

    @DeleteMapping("/{product}")
    public ResponseEntity<String> deleteProduct(@PathVariable("product") String product) {
        inventoryService.deleteProduct(product);
        return ResponseEntity.ok("Product deleted successfully: " + product);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<String>> productAlert() {
        return inventoryService.productAlert();
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadSalesFile() throws IOException {
        String userHome = System.getProperty("user.home");
        String folder = userHome + "/Documents/SalesRecords/";
        String filename = "sales-" + LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + ".csv";

        File file = new File(folder + filename);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(resource);
    }

}