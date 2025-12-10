package com.code.Sales.Stock.model;

import lombok.Data;
import java.util.List;

@Data
public class Bill {
    private String billId;
    private String date;
    private List<BillItem> items;
    private int total;

    @Data
    public static class BillItem {
        private String product;
        private int qty;
        private int price;
    }
}
