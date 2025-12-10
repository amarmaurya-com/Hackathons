package com.code.Sales.Stock.controller;

import com.code.Sales.Stock.model.Bill;
import com.code.Sales.Stock.service.QrService;
import com.code.Sales.Stock.util.QRGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/qr")
public class QRController {

    private final QrService qrService;

    public QRController(QrService qrService) {
        this.qrService = qrService;
    }

    @GetMapping(value = "/product/{name}", produces = "image/png")
    public ResponseEntity<byte[]> generateProductQR(@PathVariable String name) {
        return qrService.generateProductQRFromDB(name);
    }


    @GetMapping("/save")
    public ResponseEntity<String> saveQR(@RequestParam String text) {
        try {
            String userHome = System.getProperty("user.home");
            String folder = userHome + "/Documents/QRcodes/";
            new File(folder).mkdirs();

            String filePath = folder + text.toLowerCase() + ".png";

            QRGenerator.saveQRCode(text, 300, 300, filePath);

            return ResponseEntity.ok("QR saved at: " + filePath);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/bill", produces = "image/png")
    public ResponseEntity<byte[]> generateBillQR(@RequestBody Bill bill) {
        byte[] qr = qrService.generateBillQR(bill);

        if (qr == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(qr);
    }

}
