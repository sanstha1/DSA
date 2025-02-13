
/* You have two points in a 2D plane, represented by the arrays x_coords and y_coords. The goal is to find
the lexicographically pair i.e. (i, j) of points (one from each array) that are closest to each other.
Goal:
Determine the lexicographically pair of points with the smallest distance and smallest distance calculated
using
| x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]|
Note that
|x| denotes the absolute value of x.
A pair of indices (i1, j1) is lexicographically smaller than (i2, j2) if i1 < i2 or i1 == i2 and j1 < j2.
Input:
x_coords: The array of x-coordinates of the points.
y_coords: The array of y-coordinates of the points.
Output:
The indices of the closest pair of points.
Input: x_coords = [1, 2, 3, 2, 4], y_coords = [2, 3, 1, 2, 3]
Output: [0, 3]
Explanation: Consider index 0 and index 3. The value of | x_coords [i]- x_coords [j]| + | y_coords [i]-
y_coords [j]| is 1, which is the smallest value we can achieve. */


package assignment;

import java.util.Arrays;


public class Question2b {
     public static int[] find_closest_pair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minimum_distance = Integer.MAX_VALUE;
        int min_i = -1, min_j = -1;

        // Brute-force approach is used here where all pairs (i, j) are compared
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Update if a smaller distance is found or if same distance but lexicographically smaller pair
                if (distance < minimum_distance || (distance == minimum_distance && (i < min_i || (i == min_i && j < min_j)))) {
                    minimum_distance = distance;
                    min_i = i;
                    min_j = j;
                }
            }
        }

        return new int[]{min_i, min_j};
    }

    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        int[] result = find_closest_pair(x_coords, y_coords);
        System.out.println(Arrays.toString(result));
    }
}
