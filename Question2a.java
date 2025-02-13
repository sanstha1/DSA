
/*You have a team of n employees, and each employee is assigned a performance rating given in the
integer array ratings. You want to assign rewards to these employees based on the following rules:
Every employee must receive at least one reward.
Employees with a higher rating must receive more rewards than their adjacent colleagues.
Goal:
Determine the minimum number of rewards you need to distribute to the employees.
Input:
ratings: The array of employee performance ratings.
Output:
The minimum number of rewards needed to distribute.  */


package assignment;

public class Question2a {
    //  calculate minimum rewards needed
    public static int minimum_rewards(int[] ratings) {
        // Input validation
        if (ratings == null || ratings.length == 0) {
            return 0;
        }
        if (ratings.length == 1) {
            return 1;
        }
        
        // Initializing rewards array - every employee are given 1 reward at first
        int[] rewards = new int[ratings.length];
        for (int i = 0; i < rewards.length; i++) {
            rewards[i] = 1;
        }
        
        // Forward pass: ensure higher rated employees get more than their left neighbor
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        // Backward pass: ensure higher rated employees get more than their right neighbor
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        // Sum up total rewards
        int total_rewards = 0;
        for (int reward : rewards) {
            total_rewards += reward;
        }
        return total_rewards;
    }

    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2};
        System.out.println("Test case 1: " + minimum_rewards(ratings1));
        
        int[] ratings2 = {1, 2, 2};
        System.out.println("Test case 2: " + minimum_rewards(ratings2)); 
    }
}