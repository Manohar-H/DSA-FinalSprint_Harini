package com.harini.DSA.bst;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BinarySearchTree {
    private TreeNode root;

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private TreeNode insertRecursive(TreeNode node, int value) {
        if (node == null) return new TreeNode(value);

        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
        } else {
            node.right = insertRecursive(node.right, value);
        }
        return node;
    }

    public TreeNode getRoot() {
        return root;
    }

    public Map<String, Object> toMap(TreeNode node) {
        if (node == null) return null;

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("value", node.value);
        map.put("left", toMap(node.left));
        map.put("right", toMap(node.right));
        return map;
    }

    public TreeNode insertToTree(TreeNode root, int value) {
        if (root == null) return new TreeNode(value);
        if (value < root.value) root.left = insertToTree(root.left, value);
        else root.right = insertToTree(root.right, value);
        return root;
    }

    public TreeNode createBalancedBST(int[] nums) {
        return buildBalancedBST(nums, 0, nums.length - 1);
    }

    private TreeNode buildBalancedBST(int[] nums, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode node = new TreeNode(nums[mid]);
        node.left = buildBalancedBST(nums, start, mid - 1);
        node.right = buildBalancedBST(nums, mid + 1, end);
        return node;
    }

    public String toJson(TreeNode root) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

}