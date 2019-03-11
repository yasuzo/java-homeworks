package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class UniqueNumbers {

    /**
     * TreeNode struct
     */
    public static class TreeNode {
        private TreeNode left;
        private TreeNode right;
        private int value;
    }

    /**
     * Adds a value to the tree.
     * If the value already exists in a tree, this will do nothing.
     *
     * @param head Head node of the tree
     * @param value Value to add to the tree
     * @return Head node of the tree
     */
    public static TreeNode addNode(TreeNode head, int value) {
        if (head == null) {
            head = new TreeNode();
            head.value = value;
            return head;
        }

        if (value < head.value) {
            head.left = addNode(head.left, value);
        } else if (value > head.value) {
            head.right = addNode(head.right, value);
        }

        return head;
    }

    /**
     * Counts number of values in a tree.
     *
     * @param head Head node of the tree
     * @return Number of values in a tree
     */
    public static int treeSize(TreeNode head) {
        if (head == null) {
            return 0;
        }

        return 1 + treeSize(head.left) + treeSize(head.right);
    }

    /**
     * Checks if a value is in a tree
     *
     * @param head Head of the tree
     * @param value Value to check for
     * @return {@code true} if value exists in a tree, {@code false} otherwise
     */
    public static boolean containsValue(TreeNode head, int value) {
        if(head == null){
            return false;
        }

        if(head.value == value){
            return true;
        }

        if(value > head.value) {
            return containsValue(head.right, value);
        }else {
            return containsValue(head.left, value);
        }
    }


    /**
     * Collects all of tree's values into an array in ascending order
     *
     * @param head Head node of the tree
     * @return Array of values stored in a tree
     */
    public static int[] treeValues(TreeNode head) {
        int[] values = new int[treeSize(head)];

        fillWithTreeValues(head, values, 0);

        return values;
    }

    /**
     * Fills given array with values of a tree in ascending order.
     * Should not be called outside of {@code treeValues()} method.
     *
     * @param head Head node of the tree
     * @param arr Array that should be filled
     * @param startIndex Index which this function should fill the array from
     * @return This value is garbage from outside of this function and should not be used.
     *          It only tells recursive calls where to put next value into an array
     */
    private static int fillWithTreeValues(TreeNode head, int[] arr, int startIndex) {
        if(head == null) {
            return startIndex;
        }

        int index = fillWithTreeValues(head.left, arr, startIndex);
        arr[index] = head.value;
        return fillWithTreeValues(head.right, arr, index + 1);
    }

    public static void main(String[] args) {
        TreeNode head = null;

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("Unesite broj > ");
            String input = sc.next();
            if(input.equals("kraj")) {
                break;
            }

            int val;
            try {
                val = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.format("'%s' nije cijeli broj!%n", input);
                continue;
            }

            if(containsValue(head, val)){
                System.out.println("Broj vec postoji. Preskacem");
                continue;
            }

            head = addNode(head, val);
            System.out.println("Dodano.");
        }

        int[] values = treeValues(head);
        System.out.print("Ispis od najmanjeg:");
        for(int a : values) {
            System.out.format(" %d", a);
        }

        System.out.format("%nIspis od najveceg:");
        for(int i = values.length - 1; i >= 0; i--) {
            System.out.format(" %d", values[i]);
        }


    }
}
