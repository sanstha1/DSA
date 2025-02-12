package assignment;

public class Question1_a {
    //It is a function which is created to calculate the minimum number of mesurements required
    public static int MinMeasurementfinding(int k,int n){  
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
            if(total >= m){
                return true;
            }
        }
        return total >= n;
    }
    public static void main(String[] args){
        System.out.println(MinMeasurementfinding(1, 2));
        System.out.println(MinMeasurementfinding(2, 6));
        System.out.println(MinMeasurementfinding(3, 14));
    }
}
