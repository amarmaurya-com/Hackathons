package com.code.Sales.Stock.service;

import com.code.Sales.Stock.Repository.InventoryRepo;
import com.code.Sales.Stock.model.Bill;
import com.code.Sales.Stock.model.Inventory;
import com.code.Sales.Stock.util.QRGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class QrService {

    private final InventoryRepo inventoryRepo;

    public QrService(InventoryRepo inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    public ResponseEntity<byte[]> generateProductQRFromDB(String name) {
        try {
            // Fetch product from database
            Inventory item = inventoryRepo.findByProduct(name.toLowerCase())
                    .orElse(null);

            if (item == null) {
                return ResponseEntity.status(404).body(null);
            }

            // Prepare QR content
            String qrData =
                    "Product: " + capitalize(item.getProduct()) +
                            "; Price: " + item.getPrice() +
                            "; Quantity: " + item.getQuantity();

            // Generate QR image
            byte[] qrImage = QRGenerator.generateQRCode(qrData, 300, 300);

            return ResponseEntity.ok(qrImage);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

//    Billing System
    public byte[] generateBillQR(Bill bill) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String qrData = mapper.writeValueAsString(bill);  // Convert Bill → JSON

            return QRGenerator.generateQRCode(qrData, 350, 350);

        } catch (Exception e) {
            return null;
        }
    }

}
