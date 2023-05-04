package com.daofab.daofabapp.pojo;

import lombok.*;

import java.util.List;

@Getter
@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse  {
    private List<TransactionDetail> transactionDetail;
}
