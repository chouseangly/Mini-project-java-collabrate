package model;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Validate {
    private static final Scanner scanner = new Scanner(System.in);

//    <<<<<<<<<<<<<<<<<<Validate Integer Input>>>>>>>>>>>>>>>>>>>>
    public int validateIntOption() {
        String regex = "^\\d+$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        return Integer.parseInt(input.trim());
    }

//<<<<<<<<<<<<<<<<<<<Validate Char Input>>>>>>>>>>>>>>>>
    public String validateChartOption() {
        String regex = "^[a-zA-Z]+$";
        String input = "";
        boolean valid = false;

        while (!valid) {
            input = scanner.nextLine().trim();
            valid = Pattern.matches(regex, input);
        }
        return input.toLowerCase().trim();
    }

//<<<<<<<<<<<<<<<<<<<<Validate Double Price Inptu>>>>>>>>>>>>>
    public int validatePrice() {
        String regex = "^\\d+(\\.\\d{1,2})?$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        return Integer.parseInt(input.trim());
    }

//    <<<<<<<<<<<<<<<<<<<<<<<<<<Validate File Name Input>>>>>>>>>>>>>>>>>>
    public String validateFileName() {
        String regex = "^[a-zA-Z0-9_-]+$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        return input.trim();
    }

//    <<<<<<<<<<<<<<<<<<<<<<<Validate Product Name Input>>>>>>>>>>>>>>>>>>>
    public String validateProductName() {
        String regex = "^[a-zA-Z0-9_-]+$";
        String input = "";
        boolean valid = false;
        while (!valid) {
            input = scanner.nextLine();
            valid = Pattern.matches(regex, input);
        }
        return input.trim();
    }
}
