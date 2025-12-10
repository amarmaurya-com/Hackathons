package com.code.Sales.Stock.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRecord {
    private String product;
    private int quantity;
    private int price;
    private int total;
    private LocalDate date;
    private LocalTime time;
}
