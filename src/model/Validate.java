package model;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Validate {
    public static String validateOption() {
        Scanner scanner = new Scanner(System.in);
        String regex = "^[a-zA-Z0-9_-]+$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        scanner.close();
        return input;
    }

    public static int validateNumber() {
        Scanner scanner = new Scanner(System.in);
        String regex = "^[0-9]*$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        scanner.close();
        return Integer.parseInt(input);
    }

}
