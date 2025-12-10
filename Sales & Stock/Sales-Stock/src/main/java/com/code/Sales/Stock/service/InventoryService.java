package com.code.Sales.Stock.service;

import com.code.Sales.Stock.Repository.InventoryRepo;
import com.code.Sales.Stock.model.Inventory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


@Service
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    public InventoryService(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

//    Get List of Products
    public ResponseEntity<List<Inventory>> getAllProducts() {
        List<Inventory> products = inventoryRepo.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }


//  Get only Product
    public ResponseEntity<Inventory> getProductsByName(String name) {
        Inventory inventory = inventoryRepo.findByProduct(name.toLowerCase())
                .orElse(null);
        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventory);
    }



    //  add only Product
    public ResponseEntity<Inventory> addProduct(Inventory inventory) {
        inventory.setProduct(inventory.getProduct().toLowerCase());
        Inventory saved = inventoryRepo.save(inventory);
        return ResponseEntity.ok(saved);
    }


    // addproducts
    public ResponseEntity<List<Inventory>> addProducts(List<Inventory> inventoryList) {
        for (Inventory item : inventoryList) {
            item.setProduct(item.getProduct().toLowerCase());
        }
        List<Inventory> saved = inventoryRepo.saveAll(inventoryList);
        return ResponseEntity.ok(saved);
    }

//    update
    public ResponseEntity<Inventory> updateProduct(String product, Inventory updatedData) {

        Inventory existing = inventoryRepo.findByProduct(product.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update fields
        existing.setPrice(updatedData.getPrice());
        existing.setQuantity(updatedData.getQuantity());
        existing.setDate(updatedData.getDate());
        existing.setTime(updatedData.getTime());

        // Rename product only if provided
        if (updatedData.getProduct() != null) {
            existing.setProduct(updatedData.getProduct().toLowerCase());
        }

        return  ResponseEntity.ok(inventoryRepo.save(existing));
    }

//    delete
    public void deleteProduct(String product) {

        Inventory inventory = inventoryRepo.findByProduct(product.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        inventoryRepo.delete(inventory);
    }

    public ResponseEntity<String> modifySale(String product, int qtySold) {

        Inventory existing = inventoryRepo.findByProduct(product.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existing.getQuantity() < qtySold) {
            return ResponseEntity.badRequest().body("Not enough stock");
        }

        int oldQty = existing.getQuantity();
        int price = existing.getPrice();

        existing.setQuantity(oldQty - qtySold);
        inventoryRepo.save(existing);

        writeSaleToCSV(product, qtySold, price);

        return ResponseEntity.ok("Sale recorded successfully");
    }


    //    FileHandling
    private void writeSaleToCSV(String product, int quantity, int price) {
        try {
            // Get user’s Documents folder
            String userHome = System.getProperty("user.home");
            String documentsPath = userHome + "/Documents/SalesRecords";

            // Create folder if not exists
            File folder = new File(documentsPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Create file name based on YEAR-MONTH
            LocalDate today = LocalDate.now();
            String fileName = "sales-" + today.getYear() + "-" + today.getMonthValue() + ".csv";

            File csvFile = new File(folder, fileName);
            int total = price*quantity;

            boolean fileExists = csvFile.exists();

            // Create writer in append mode
            FileWriter writer = new FileWriter(csvFile, true);

            // If file newly created → write header
            if (!fileExists) {
                writer.write("Product,Quantity,Price,Total,Date,Time\n");
            }

            // Get current time
            LocalTime now = LocalTime.now();
            String date = today.toString();
            String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // Write sale record
            writer.write(product.substring(0, 1).toUpperCase() + product.substring(1).toLowerCase() + "," + quantity + "," + price + "," + total +","+ date + "," + time + "\n");

            writer.close();

            System.out.println("Sale record saved to: " + csvFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Error writing sale to CSV: " + e.getMessage());
        }
    }


    public ResponseEntity<List<String>> productAlert() {
        List<Inventory> products = inventoryRepo.findAll();
        List<String> alerts = new ArrayList<>();

        for (Inventory item : products) {
            if (item.getQuantity() < 50) {
                alerts.add(item.getProduct());
            }
        }
        return ResponseEntity.ok(alerts);
    }
}
