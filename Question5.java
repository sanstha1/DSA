/* Optimizing a Network with Multiple Objectives
Problem:
Suppose you are hired as software developer for certain organization and you are tasked with creating a
GUI application that helps network administrators design a network topology that is both cost-effective
and efficient for data transmission. The application needs to visually represent servers and clients as
nodes in a graph, with potential network connections between them, each having associated costs and
bandwidths. The goal is to enable the user to find a network topology that minimizes both the total cost
and the latency of data transmission.
Approach:
1. Visual Representation of the Network:
o Design the GUI to allow users to create and visualize a network graph where each node
represents a server or client, and each edge represents a potential network connection. The
edges should display associated costs and bandwidths.
2. Interactive Optimization:
o Implement tools within the GUI that enable users to apply algorithms or heuristics to
optimize the network. The application should provide options to find the best combination
of connections that minimizes the total cost while ensuring all nodes are connected.
3. Dynamic Path Calculation:
o Include a feature where the user can calculate the shortest path between any pair of nodes
within the selected network topology. The GUI should display these paths, taking into
account the bandwidths as weights.
4. Real-time Evaluation:
o Provide real-time analysis within the GUI that displays the total cost and latency of the
current network topology. If the user is not satisfied with the results, they should be able
to adjust the topology and explore alternative solutions interactively. */



package assignment;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Node class representing a server or client
class Node {
    String name;
    int x, y; // Position on GUI

    public Node(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

// Edge class representing a connection between nodes
class Edge {
    Node from, to;
    int cost, bandwidth;

    public Edge(Node from, Node to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

// Main Network GUI class (renamed to Question5)
public class Question5 extends JFrame {
    private List<Node> nodes = new ArrayList<>();
    private List<Edge> edges = new ArrayList<>();

    public Question5() {
        setTitle("Network Optimization Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton addNodeButton = new JButton("Add Node");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton findPathButton = new JButton("Find Shortest Path");

        addNodeButton.setBounds(50, 20, 120, 30);
        addEdgeButton.setBounds(200, 20, 120, 30);
        findPathButton.setBounds(350, 20, 150, 30);

        add(addNodeButton);
        add(addEdgeButton);
        add(findPathButton);

        addNodeButton.addActionListener(e -> addNode());
        addEdgeButton.addActionListener(e -> addEdge());
        findPathButton.addActionListener(e -> findShortestPath());
    }

    // Method to add a node (server/client)
    private void addNode() {
        String name = JOptionPane.showInputDialog("Enter Node Name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Node name cannot be empty!");
            return;
        }
        int x = new Random().nextInt(600) + 100;
        int y = new Random().nextInt(400) + 100;
        nodes.add(new Node(name, x, y));
        revalidate();
        repaint();
    }

    // Method to add an edge (network connection)
    private void addEdge() {
        if (nodes.size() < 2) {
            JOptionPane.showMessageDialog(this, "At least two nodes are required!");
            return;
        }
        String fromNodeName = JOptionPane.showInputDialog("Enter From Node Name:");
        String toNodeName = JOptionPane.showInputDialog("Enter To Node Name:");
        if (fromNodeName == null || toNodeName == null || fromNodeName.trim().isEmpty() || toNodeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Node names cannot be empty!");
            return;
        }

        int cost = 0, bandwidth = 0;
        try {
            cost = Integer.parseInt(JOptionPane.showInputDialog("Enter Connection Cost:"));
            bandwidth = Integer.parseInt(JOptionPane.showInputDialog("Enter Bandwidth:"));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric input for cost or bandwidth!");
            return;
        }

        Node from = null, to = null;
        for (Node node : nodes) {
            if (node.name.equals(fromNodeName)) from = node;
            if (node.name.equals(toNodeName)) to = node;
        }
        if (from != null && to != null) {
            edges.add(new Edge(from, to, cost, bandwidth));
            revalidate();
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid node names!");
        }
    }

    // Method to find shortest path using Dijkstra's Algorithm
    private void findShortestPath() {
        if (nodes.isEmpty() || edges.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No paths available!");
            return;
        }

        String startNodeName = JOptionPane.showInputDialog("Enter Start Node:");
        String endNodeName = JOptionPane.showInputDialog("Enter End Node:");
        if (startNodeName == null || endNodeName == null || startNodeName.trim().isEmpty() || endNodeName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Node names cannot be empty!");
            return;
        }

        Node startNode = null, endNode = null;
        for (Node node : nodes) {
            if (node.name.equals(startNodeName)) startNode = node;
            if (node.name.equals(endNodeName)) endNode = node;
        }
        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(this, "Invalid node names!");
            return;
        }

        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Node node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(startNode, 0);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            for (Edge edge : edges) {
                if (edge.from.equals(current)) {
                    Node neighbor = edge.to;
                    int newDist = distances.get(current) + edge.cost;
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previous.put(neighbor, current);
                        queue.remove(neighbor); // Remove to update priority
                        queue.add(neighbor);
                    }
                }
            }
        }

        if (distances.get(endNode) == Integer.MAX_VALUE) {
            JOptionPane.showMessageDialog(this, "No path found!");
            return;
        }

        // Reconstruct path
        List<Node> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = previous.get(node)) {
            path.add(node);
        }
        Collections.reverse(path);

        StringBuilder pathString = new StringBuilder("Shortest Path: ");
        for (int i = 0; i < path.size(); i++) {
            pathString.append(path.get(i).name);
            if (i < path.size() - 1) pathString.append(" -> ");
        }
        pathString.append("\nTotal Cost: ").append(distances.get(endNode));

        JOptionPane.showMessageDialog(this, pathString.toString());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Edge edge : edges) {
            g.drawLine(edge.from.x, edge.from.y, edge.to.x, edge.to.y);
            g.drawString("Cost: " + edge.cost + " BW: " + edge.bandwidth,
                    (edge.from.x + edge.to.x) / 2, (edge.from.y + edge.to.y) / 2);
        }
        for (Node node : nodes) {
            g.fillOval(node.x - 15, node.y - 15, 30, 30);
            g.drawString(node.name, node.x - 10, node.y - 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Question5 optimizer = new Question5(); 
            optimizer.setVisible(true);
        });
    }
}