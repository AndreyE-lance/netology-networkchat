package Server;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Scanner sc = new Scanner(System.in);
        final String[] string = {""};

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    string[0] = sc.nextLine();
                }
            }
        }.start();
        while (true) {
            System.out.println(string[0]);
            Thread.sleep(2000);
        }
    }
}
