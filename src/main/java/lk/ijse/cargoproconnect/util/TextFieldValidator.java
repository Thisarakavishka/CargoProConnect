package lk.ijse.cargoproconnect.util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFieldValidator {
    private static final Pattern namePattern = Pattern.compile("[a-zA-Z '.-]+");
    private static final Pattern itemPattern = Pattern.compile("^[a-zA-Z0-9 ]+$");
    private static final Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){5,18}[a-zA-Z0-9]$");
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$");
    private static final Pattern emailPattern = Pattern.compile("(^[a-zA-Z0-9_.-]+)@([a-zA-Z]+)([\\.])([a-zA-Z]+)$");
    private static final Pattern contactNumberPattern = Pattern.compile("\\d{13}");
    private static final Pattern addressPattern = Pattern.compile("^[A-Za-z0-9'\\/\\.\\, ]{5,}$");
    private static final Pattern weightPattern = Pattern.compile("^\\d+(\\.\\d+)?$");
    private static final Pattern intPattern = Pattern.compile("^[1-9][0-9]*$");
    private static final Pattern oldIDPattern = Pattern.compile("^[0-9]{9}[vVxX]$");
    private static final Pattern passportNumber = Pattern.compile("^[A-Z]{2}[0-9]{7}$");
    private static final Pattern drivingNumber = Pattern.compile("^[A-Z]{1,2}\\d{1,6}[A-Z]{0,3}$");

    public static Pattern getNamePattern() {
        return namePattern;
    }

    public static Pattern getItemPattern() {
        return itemPattern;
    }

    public static Pattern getUserNamePattern() {
        return userNamePattern;
    }

    public static Pattern getPasswordPattern() {
        return passwordPattern;
    }

    public static Pattern getEmailPattern() {
        return emailPattern;
    }

    public static Pattern getContactNumberPattern() {
        return contactNumberPattern;
    }

    public static Pattern getAddressPattern() {
        return addressPattern;
    }

    public static Pattern getOldIDPattern() {
        return oldIDPattern;
    }

    public static Pattern getWeightPattern() {
        return weightPattern;
    }

    public static Pattern getIntPattern() {
        return intPattern;
    }

    public static Pattern getPassportNumber() {
        return passportNumber;
    }

    public static Pattern getDrivingNumber() {
        return drivingNumber;
    }

    public static void setFocus(JFXTextField textField, Pattern pattern) {
        textField.setOnKeyReleased(keyEvent -> {
            Matcher matcher = pattern.matcher(textField.getText());
            if (textField.getText().isEmpty() || textField.getText().isBlank() || !matcher.matches()) {
                textField.setFocusColor(Paint.valueOf(Colors.RED));
            } else {
                textField.setFocusColor(Paint.valueOf(Colors.GREEN));
            }
        });
    }

    public static void setValid(JFXTextField textField, Pattern pattern) {
        Matcher matcher = pattern.matcher(textField.getText());
        if (textField.getText().isEmpty() || textField.getText().isBlank() || !matcher.matches()) {
            textField.setFocusColor(Paint.valueOf(Colors.RED));
        } else {
            textField.setFocusColor(Paint.valueOf(Colors.GREEN));
        }
    }
}
