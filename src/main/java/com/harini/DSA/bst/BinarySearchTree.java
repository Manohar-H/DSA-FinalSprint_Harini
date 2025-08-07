package com.harini.DSA.bst;

import java.util.*;

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

    // Optional: Convert to Map for JSON response
    public Map<String, Object> toMap(TreeNode node) {
        if (node == null) return null;

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("value", node.value);
        map.put("left", toMap(node.left));
        map.put("right", toMap(node.right));
        return map;
    }
}