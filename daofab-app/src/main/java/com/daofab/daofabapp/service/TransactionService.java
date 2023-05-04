package com.daofab.daofabapp.service;

import com.daofab.daofabapp.pojo.Transaction;
import com.daofab.daofabapp.pojo.TransactionDetail;
import com.daofab.daofabapp.pojo.TransactionDetailResponse;
import com.daofab.daofabapp.pojo.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class TransactionService {

    @Value("classpath:data/Parent.json")
    Resource parentData;
    @Value("classpath:data/Child.json")
    Resource childData;
    public TransactionResponse getTransaction(int PageNo) throws IOException, ParseException {
        /*
         * Read child and parent json data and convert to JSONobject
        * */
        List<JSONObject> parent = readFile(parentData);
        List<JSONObject> child = readFile(childData);
        // get total paid ammount
        List<JSONObject> parentWithTotalPaidAmount = getTotalPaidAmount(parent, child);
        Collections.sort(parentWithTotalPaidAmount, new MyJSONComparator());

        List<Integer> pages =  getPages(parentWithTotalPaidAmount, PageNo);

        return TransactionResponse.builder()
                .transaction(parentWithTotalPaidAmount.subList(pages.get(0) - 1, pages.get(1)).stream().map(e ->
                    Transaction.builder()
                            .id((Long) e.get("id"))
                            .totalPaidAmount((Long) e.get("totalPaidAmount"))
                            .sender(e.get("sender").toString())
                            .receiver(e.get("receiver").toString())
                            .totalAmount((Long) e.get("totalAmount"))
                            .build()
                ).toList())
                .pageContext(parentWithTotalPaidAmount.size())
                .build();
    }

    public TransactionDetailResponse getTransactionDetail(String id) throws ParseException, JsonProcessingException {
         // Read child and parent json data and convert to JSONobject
        List<JSONObject> parent = readFile(parentData);
        List<JSONObject> child = readFile(childData);
        // get child detail give the parent id and merge child and parent with the match id
        List<JSONObject> parentWithPaidAmount = getChildWithParent(parent, child, id);

        return TransactionDetailResponse.builder()
                .transactionDetail(parentWithPaidAmount.stream().map(e ->
                        TransactionDetail.builder()
                                .id((Long) e.get("id"))
                                .paidAmount((Long) e.get("paidAmount"))
                                .sender(e.get("sender").toString())
                                .receiver(e.get("receiver").toString())
                                .totalAmount((Long) e.get("totalAmount"))
                                .build()
                ).toList())
                .build();
    }

    private ArrayList<JSONObject> readFile(Resource fileResource) {
        JSONParser parser = new JSONParser();
        ArrayList<JSONObject> list = new ArrayList<>();
        try {
            JSONObject o = (JSONObject) parser.parse(new FileReader(fileResource.getFile()));
            // read file and convert to Json object
            JSONArray array = (JSONArray) o.get("data");
            for (int i = 0; i < array.size(); i++) {
                list.add((JSONObject) array.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private List<JSONObject> getTotalPaidAmount(List<JSONObject> parent, List<JSONObject> child) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser=new JSONParser();
        List<JSONObject> parentWithTotalAmount = new ArrayList<>();
        Integer totalAmount;
        JsonNode parentNode = null;
        // iterate over parent and child and parent
        for(JSONObject p: parent) {
            totalAmount = 0;
            for(JSONObject c: child) {
                // convert jsonObject to object
                parentNode = mapper.readTree(String.valueOf(p));
                JsonNode childNode = mapper.readTree(String.valueOf(c));
                // if parent id match the parentId field in child
                if(parentNode.get("id").equals(childNode.get("parentId"))) {
                    // add up the total paid amount
                    totalAmount +=  childNode.get("paidAmount").asInt();
                }
            }
            ObjectNode node = (ObjectNode) parentNode;
            // add new field totalPaidAmount in the object
            Object obj = parser.parse(String.valueOf(node.putRawValue("totalPaidAmount", new RawValue(String.valueOf(totalAmount)))));
            JSONObject jsonObject= (JSONObject)obj;
            // save to file
            parentWithTotalAmount.add(jsonObject);
        }
        return parentWithTotalAmount;
    }

    private List<JSONObject> getChildWithParent(List<JSONObject> parent, List<JSONObject> child, String id) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser=new JSONParser();
        List<JSONObject> parentWithTotalAmount = new ArrayList<>();
        // get the object with give id from parent
        List<JSONObject> selectedParent = parent.stream().filter(el -> el.get("id").toString().equals(id)).collect(Collectors.toList());
            for(JSONObject c: child) {
                JsonNode childNode = mapper.readTree(String.valueOf(c));
                if(childNode.get("parentId").toString().equals(id)) {
                    ObjectNode node = (ObjectNode)  mapper.readTree(String.valueOf(selectedParent.get(0)));
                    // get paidAmount from child
                    Object obj = parser.parse(String.valueOf(node.putRawValue("paidAmount", new RawValue(childNode.get("paidAmount")))));
                    Object ids = parser.parse(String.valueOf(node.putRawValue("id", new RawValue(childNode.get("id")))));
                    JSONObject jsonObject= (JSONObject) obj;
                    jsonObject = (JSONObject)ids;
                    parentWithTotalAmount.add(jsonObject);
                }
            }
        return parentWithTotalAmount;
    }

    class MyJSONComparator implements Comparator<JSONObject> {
        // use comparator to sort the list
        public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
            int compare = 0;
            long keyA = (long) jsonObjectA.get("id");
            long keyB = (long) jsonObjectB.get("id");
            compare = Long.compare(keyA, keyB);
            return compare;
        }
    }

    public static List<Integer> getPages(List<JSONObject> sourceList, int pageNo) {
        int totalPages = sourceList.size();
        int offset = 2;
        int start = pageNo - (offset / 2);
        start = Math.max(start, 1);
        int end = start + offset - 1;
        if (end > totalPages) {
            end = totalPages;
            start = end - offset + 1;
        }
        return  IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
