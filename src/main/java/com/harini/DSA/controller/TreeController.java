package com.harini.DSA.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.harini.DSA.bst.BinarySearchTree;
import com.harini.DSA.bst.TreeNode;
import com.harini.DSA.model.TreeRecord;
import com.harini.DSA.repository.TreeRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class TreeController {

    @Autowired
    private TreeRecordRepository treeRecordRepository;

    @GetMapping("/enter-numbers")
    public String enterNumbersForm(Model model) {
        model.addAttribute("treeJson", null);  // ensure it's never undefined
        return "enter-numbers";
    }

    @PostMapping("/submit-numbers")
    public String handleFormSubmit(@RequestParam("numbers") String numbers, Model model) {
        if (numbers == null || numbers.trim().isEmpty()) {
            model.addAttribute("error", "Please enter numbers.");
            return "enter-numbers";
        }

        try {
            List<Integer> numberList = parseNumbers(numbers);
            BinarySearchTree bst = new BinarySearchTree();
            for (int num : numberList) {
                bst.insert(num);
            }

            TreeNode root = bst.getRoot();
            Map<String, Object> treeAsMap = bst.toMap(root);

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String treeJson = mapper.writeValueAsString(treeAsMap);

            // Save to DB
            TreeRecord record = new TreeRecord(numbers, treeJson);
            treeRecordRepository.save(record);

            model.addAttribute("treeJson", treeJson);
            model.addAttribute("numbers", numbers);

        } catch (NumberFormatException e) {
            model.addAttribute("error", "Only valid integers separated by commas are allowed.");
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error: " + e.getMessage());
        }

        return "enter-numbers";
    }

    @GetMapping("/previous-trees")
    public String viewPreviousTrees(Model model) {
        List<TreeRecord> records = treeRecordRepository.findAll();
        model.addAttribute("records", records);
        return "previous-trees"; // Matches previous-trees.html
    }

    private List<Integer> parseNumbers(String input) {
        String[] parts = input.split(",");
        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            numbers.add(Integer.parseInt(part.trim()));
        }
        return numbers;
    }

}