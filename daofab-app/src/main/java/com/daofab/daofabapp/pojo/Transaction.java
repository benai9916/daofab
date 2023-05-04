package com.daofab.daofabapp.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private long id;
    private String sender;
    private String receiver;
    private long totalAmount;
    private long totalPaidAmount;
}
