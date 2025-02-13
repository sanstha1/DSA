
/* You have a material with n temperature levels. You know that there exists a critical temperature f where
0 <= f <= n such that the material will react or change its properties at temperatures higher than f but
remain unchanged at or below f.
Rules:
 You can measure the material's properties at any temperature level once.
 If the material reacts or changes its properties, you can no longer use it for further measurements.
 If the material remains unchanged, you can reuse it for further measurements.
Goal:
Determine the minimum number of measurements required to find the critical temperature.
Input:
 k: The number of identical samples of the material.
 n: The number of temperature levels.
Output:
 The minimum number of measurements required to find the critical temperature */



package assignment;

public class Question1a {
    //It is a function which is created to calculate the minimum number of mesurements required
    public static int minimum_measurement_finding(int k,int n){  
        int low = 1, high = n;
        while(low<high){
            int mid = low + (high-low) / 2;
            if(determine(k,n,mid)){
                high = mid;
            }else{
                low = mid + 1;
            }
        }
        return low;
    }
    //helper function to check if m measurements are sufficient
    private static boolean determine(int k,int n,int m){
        int total = 0;
        int term = 1;
        for(int i=1; i <= k; i++){
            term = term * (m-i+1) / i;
            total = total + term;
            if(total >= n){
                return true;
            }
        }
        return total >= n;
    }
    public static void main(String[] args){
        System.out.println(minimum_measurement_finding(1, 2));
        System.out.println(minimum_measurement_finding(2, 6));
        System.out.println(minimum_measurement_finding(3, 14));
    }
}
