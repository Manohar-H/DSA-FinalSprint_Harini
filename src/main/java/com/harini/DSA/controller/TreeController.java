package com.harini.DSA.controller;

import com.harini.DSA.bst.BinarySearchTree;
import com.harini.DSA.bst.TreeNode;
import com.harini.DSA.model.TreeRecord;
import com.harini.DSA.repository.TreeRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class TreeController {

    private final TreeRecordRepository treeRecordRepository;

    public TreeController(TreeRecordRepository treeRecordRepository) {
        this.treeRecordRepository = treeRecordRepository;
    }

    @GetMapping("/enter-numbers")
    public String enterNumbersForm(Model model) {
        // Keep attributes defined so Thymeleaf conditionals don't explode
        model.addAttribute("treeJson", null);
        model.addAttribute("error", null);
        model.addAttribute("input", null);
        model.addAttribute("balanced", false);
        return "enter-numbers";
    }

    @PostMapping("/submit-numbers")
    public String submitNumbers(@RequestParam("numbers") String numbers,
                                @RequestParam(value = "balanced", defaultValue = "false") boolean balanced,
                                Model model) {
        try {
            if (numbers == null || numbers.trim().isEmpty()) {
                model.addAttribute("error", "Please enter at least one number.");
                return "enter-numbers";
            }

            int[] values = Arrays.stream(numbers.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .toArray();

            if (values.length == 0) {
                model.addAttribute("error", "Please enter at least one number.");
                return "enter-numbers";
            }

            BinarySearchTree bst = new BinarySearchTree();
            TreeNode root;

            if (balanced) {
                int[] sorted = Arrays.stream(values).sorted().toArray();
                root = bst.createBalancedBST(sorted);
            } else {
                root = null;
                for (int v : values) {
                    root = bst.insertToTree(root, v);
                }
            }

            String treeJson = bst.toJson(root);

            TreeRecord record = new TreeRecord(numbers, treeJson);
            treeRecordRepository.save(record);

            model.addAttribute("treeJson", treeJson);
            model.addAttribute("balanced", balanced);
            model.addAttribute("input", numbers);

        } catch (NumberFormatException nfe) {
            model.addAttribute("error", "Please enter only valid integers separated by commas.");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "enter-numbers";
    }

    @GetMapping("/previous-trees")
    public String viewPreviousTrees(Model model) {
        List<TreeRecord> records = treeRecordRepository.findAllByOrderByIdDesc();
        model.addAttribute("records", records);
        return "previous-trees";
    }

    @PostMapping(value = "/process-numbers", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> processNumbers(@RequestBody NumbersRequest req) {
        try {
            if (req == null || req.numbers() == null || req.numbers().isBlank()) {
                return ResponseEntity.badRequest().body("{\"error\":\"numbers is required\"}");
            }

            boolean balanced = Boolean.TRUE.equals(req.balanced());

            int[] values = Arrays.stream(req.numbers().split("[,\\s]+"))
                    .filter(s -> !s.isBlank())
                    .mapToInt(Integer::parseInt)
                    .toArray();

            BinarySearchTree bst = new BinarySearchTree();
            TreeNode root = balanced
                    ? bst.createBalancedBST(Arrays.stream(values).sorted().toArray())
                    : Arrays.stream(values).boxed().reduce(null, (r, v) -> bst.insertToTree(r, v), (a,b)->a);

            String json = bst.toJson(root);

            TreeRecord rec = new TreeRecord(req.numbers(), json);
            treeRecordRepository.save(rec);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);

        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body("{\"error\":\"numbers must be integers\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"unexpected error\"}");
        }
    }
}