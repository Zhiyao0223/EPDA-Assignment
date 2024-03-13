/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.regex.Pattern;

/**
 *
 * @author USER
 */
public class Validation {

    // Validate email using regex
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Validate password strength
    public static boolean isValidPassword(String password) {
        // At least 8 characters, contains at least one digit, one lowercase, one uppercase, and one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    // Validate phone number
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Allows digits, dashes, and optional parenthesis for area code
        String phoneRegex = "^\\(?\\d{3}\\)?[-\\.\\s]?\\d{3}[-\\.\\s]?\\d{4}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phoneNumber).matches();
    }

    // Validate date format (YYYY-MM-DD)
    public static boolean isValidDateFormat(String date) {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(dateRegex);
        return pattern.matcher(date).matches();
    }

    // Validate numeric input
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // Validate if string is empty or null
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Validate if string contains only alphabets
    public static boolean isAlphabetOnly(String str) {
        return str.matches("[a-zA-Z]+");
    }

    // Validate if string contains only digits
    public static boolean isDigitOnly(String str) {
        return str.matches("\\d+");
    }

    // Validate if a string exceed specific character
    public static boolean isExceedMaxCharacter(String str, int maxCharLimit) {
        return str.length() > maxCharLimit;
    }
}
