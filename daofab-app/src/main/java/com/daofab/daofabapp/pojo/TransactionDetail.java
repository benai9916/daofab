package com.daofab.daofabapp.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetail {
    private long id;
    private String sender;
    private String receiver;
    private long totalAmount;
    private long paidAmount;
}
