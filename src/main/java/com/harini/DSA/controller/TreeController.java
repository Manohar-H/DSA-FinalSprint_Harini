package com.harini.DSA.controller;

import com.harini.DSA.bst.BinarySearchTree;
import com.harini.DSA.bst.TreeNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.*;

@Controller
public class TreeController {

    @GetMapping("/enter-numbers")
    public String enterNumbersForm() {
        return "enter-numbers";  // maps to enter-numbers.html under templates/
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

            model.addAttribute("treeJson", treeJson);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Please enter only valid integers separated by commas.");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "enter-numbers";
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