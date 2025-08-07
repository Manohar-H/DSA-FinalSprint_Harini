package com.harini.DSA.controller;

import com.harini.DSA.bst.BinarySearchTree;
import com.harini.DSA.bst.TreeNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TreeController {

    @PostMapping("/process-numbers")
    public ResponseEntity<Map<String, Object>> processNumbers(@RequestBody Map<String, String> request) {
        String numbersString = request.get("numbers");

        if (numbersString == null || numbersString.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Input numbers are required."));
        }

        try {
            List<Integer> numberList = parseNumbers(numbersString);
            BinarySearchTree bst = new BinarySearchTree();
            for (int num : numberList) {
                bst.insert(num);
            }

            TreeNode root = bst.getRoot();
            Map<String, Object> treeAsMap = bst.toMap(root);

            return ResponseEntity.ok(treeAsMap);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please enter valid integers separated by commas."));
        }
    }

    private List<Integer> parseNumbers(String input) throws NumberFormatException {
        String[] parts = input.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            numbers.add(Integer.parseInt(part.trim()));
        }
        return numbers;
    }
}