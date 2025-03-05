/* You are given a class NumberPrinter with three methods: printZero, printEven, and printOdd.
These methods are designed to print the numbers 0, even numbers, and odd numbers, respectively.
Task:
Create a ThreadController class that coordinates three threads:
5. ZeroThread: Calls printZero to print 0s.
6. EvenThread: Calls printEven to print even numbers.
7. OddThread: Calls printOdd to print odd numbers.
These threads should work together to print the sequence "0102030405..." up to a specified number n.
The output should be interleaved, ensuring that the numbers are printed in the correct order.
Example:
If n = 5, the output should be "0102030405".
Constraints:
 The threads should be synchronized to prevent race conditions and ensure correct output.
 The NumberPrinter class is already provided and cannot be modified.
 */

package assignment;

public class Question6a {
    static class NumberPrinter {
        public void printZero() {
            System.out.print(0);
        }

        public void printEven(int num) {
            System.out.print(num);
        }

        public void printOdd(int num) {
            System.out.print(num);
        }
    }

    static class ThreadController {
        private final int n; 
        private int counter = 1; // Tracks the current step
        private final Object lock = new Object();

        public ThreadController(int n) {
            this.n = n;
        }

        public void zero(NumberPrinter printer) {
            synchronized (lock) {
                for (int i = 1; i <= n; i++) {
                    while (counter % 2 != 1) { // Wait if it's not zero's turn
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printZero(); 
                    counter++;
                    lock.notifyAll(); // Notify other threads
                }
            }
        }

        public void even(NumberPrinter printer) {
            synchronized (lock) {
                for (int i = 2; i <= n; i += 2) {
                    while (counter % 2 != 0 || counter / 2 % 2 != 0) { // Wait if it's not even's turn
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printEven(i); // Print even number
                    counter++;
                    lock.notifyAll(); // Notify other threads
                }
            }
        }

        public void odd(NumberPrinter printer) {
            synchronized (lock) {
                for (int i = 1; i <= n; i += 2) {
                    while (counter % 2 != 0 || counter / 2 % 2 != 1) { // Wait if it's not odd's turn
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printOdd(i); // Print odd number
                    counter++;
                    lock.notifyAll(); // Notify other threads
                }
            }
        }
    }

    public static class NumberPrintingApp {
        public static void main(String[] args) {
            int n = 5; 
            NumberPrinter printer = new NumberPrinter();
            ThreadController controller = new ThreadController(n);

            Thread zeroThread = new Thread(() -> controller.zero(printer));
            Thread evenThread = new Thread(() -> controller.even(printer));
            Thread oddThread = new Thread(() -> controller.odd(printer));

            zeroThread.start();
            evenThread.start();
            oddThread.start();
        }
    }
}