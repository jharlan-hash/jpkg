package com.jacksovern.jpkg;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] a) throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter 's' to start the server or 'c' to start the client: ");
        String inputstring = scan.nextLine();
        String[] input = inputstring.split(" ");
        String[] args = Arrays.copyOfRange(input, 1, input.length);
        if (input[0].equals("s")) {
            try {
                Server.main(args);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (input[0].equals("c")) {
            try {
                Client.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid input.");
            scan.close();
        }
        scan.close();
    }
}
