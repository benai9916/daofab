package com.daofab.daofabapp.controller;

import com.daofab.daofabapp.pojo.TransactionDetailResponse;
import com.daofab.daofabapp.pojo.TransactionResponse;
import com.daofab.daofabapp.pojo.common.BaseResponse;
import com.daofab.daofabapp.pojo.common.SuccessResponse;
import com.daofab.daofabapp.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // get transaction detail with total paid amount
    @GetMapping("/transaction")
    public BaseResponse<TransactionResponse> getTransaction(
            @RequestParam(defaultValue = "1") int pageNo) throws IOException, ParseException {
        return new SuccessResponse<>(transactionService.getTransaction(pageNo));
    }

    // get child detail give id, id =  parent id
    @GetMapping("/transaction/{id}")
    public BaseResponse<TransactionDetailResponse> getTransactionDetail(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "2") int pageSize,
            @PathVariable String id
    ) throws ParseException, JsonProcessingException {
        return new SuccessResponse<>(transactionService.getTransactionDetail(id));
    }
}
