package assignment;

import java.util.PriorityQueue;

public class Question1b {
    public static int smallestcombination(int[] return1, int[] return2, int k) {
        // Min-heap created to store combined returns
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // For loop to iterate through all possible combinations of return1 and return2
        for (int r1 : return1) {
            for (int r2 : return2) {
                int combinedReturn = r1 * r2; // Calculate the combined return
                minHeap.offer(combinedReturn); // Add the combined return to the min-heap
            }
        }

        // Extract the kth smallest combined return
        int result = 0;
        for (int i = 0; i < k; i++) {
            result = minHeap.poll();
        }

        return result;
    }

    public static void main(String[] args) {

        int[] return1 = {2, 5};
        int[] return2 = {3, 4};
        int k1 = 2;
        System.out.println(smallestcombination(return1, return2, k1));

        int[] return3 = {-4, -2, 0, 3};
        int[] return4 = {2, 4};
        int k2 = 6;
        System.out.println(smallestcombination(return3, return4, k2)); 
    }
}