package com.harini.DSA;

import com.harini.DSA.bst.BinarySearchTree;
import com.harini.DSA.bst.TreeNode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TreeServiceTests {

    BinarySearchTree bst = new BinarySearchTree();

    @Test
    void testInsertToTree() {
        TreeNode root = null;
        root = bst.insertToTree(root, 5);
        root = bst.insertToTree(root, 3);
        root = bst.insertToTree(root, 7);

        assertEquals(5, root.value);
        assertEquals(3, root.left.value);
        assertEquals(7, root.right.value);
    }

    @Test
    void testToJson() {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);

        String json = bst.toJson(root);
        assertTrue(json.contains("\"value\" : 10"));
        assertTrue(json.contains("\"left\" : {"));
        assertTrue(json.contains("\"right\" : {"));
    }

    @Test
    void testCreateBalancedBST() {
        int[] sorted = {1, 2, 3};
        TreeNode root = bst.createBalancedBST(sorted);

        // The middle of {1,2,3} is 2
        assertEquals(2, root.value);
        assertEquals(1, root.left.value);
        assertEquals(3, root.right.value);
    }
}