
/*You have a network of n devices. Each device can have its own communication module installed at a
cost of modules [i - 1]. Alternatively, devices can communicate with each other using direct connections.
The cost of connecting two devices is given by the array connections where each connections[j] =
[device1j, device2j, costj] represents the cost to connect devices device1j and device2j. Connections are
bidirectional, and there could be multiple valid connections between the same two devices with different
costs.
Goal:
Determine the minimum total cost to connect all devices in the network.
Input:
n: The number of devices.
modules: An array of costs to install communication modules on each device.
connections: An array of connections, where each connection is represented as a triplet [device1j,
device2j, costj].
Output:
The minimum total cost to connect all devices.
 */


 package assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
 
 public class Question3a {
 
     static class UnionFind {
         int[] parent, rank;
 
         UnionFind(int n) {
             parent = new int[n];
             rank = new int[n];
             for (int i = 0; i < n; i++) {
                 parent[i] = i;
                 rank[i] = 1;
             }
         }
 
         int find(int x) {
             if (parent[x] != x) {
                 parent[x] = find(parent[x]); // Path compression
             }
             return parent[x];
         }
 
         boolean union(int x, int y) {
             int rootX = find(x);
             int rootY = find(y);
 
             if (rootX == rootY) return false; // Already connected
 
             // Union by rank
             if (rank[rootX] > rank[rootY]) {
                 parent[rootY] = rootX;
             } else if (rank[rootX] < rank[rootY]) {
                 parent[rootX] = rootY;
             } else {
                 parent[rootY] = rootX;
                 rank[rootX]++;
             }
             return true;
         }
     }
 
     public static int minimum_cost_to_connect_device(int n, int[] modules, int[][] connections) {
         List<int[]> edges = new ArrayList<>();
 
         // Add module installation edges (0 -> device_i) with cost modules[i-1]
         for (int i = 1; i <= n; i++) {
             edges.add(new int[]{0, i, modules[i - 1]});
         }
 
         // direct connection edges added
         Collections.addAll(edges, connections);
 
         // Sort edges by cost as it is used in Greedy approach
         edges.sort(Comparator.comparingInt(a -> a[2]));
 
         // Kruskal’s Algorithm using Union-Find
         UnionFind uf = new UnionFind(n + 1);
         int totalCost = 0, edgesUsed = 0;
 
         for (int[] edge : edges) {
             if (uf.union(edge[0], edge[1])) { // If no cycle, add edge
                 totalCost += edge[2];
                 edgesUsed++;
                 if (edgesUsed == n) break; // MST complete (n edges for n+1 nodes)
             }
         }
 
         return totalCost;
     }
 
     public static void main(String[] args) {
         int n = 3;
         int[] modules = {1, 2, 2};
         int[][] connections = {{1, 2, 1}, {2, 3, 1}};
 
         System.out.println(minimum_cost_to_connect_device(n, modules, connections)); 
     }
 }
 