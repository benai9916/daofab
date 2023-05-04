package com.daofab.daofabapp.pojo;

import lombok.*;

import java.util.List;

@Getter
@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private List<Transaction> transaction;
    private int pageContext;
}
