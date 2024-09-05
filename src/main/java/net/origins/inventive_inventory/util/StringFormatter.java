package net.origins.inventive_inventory.util;

public class StringFormatter {
    public static String convertToCamelCase(String input) {
        String[] words = input.split("[\\s-]+");
        StringBuilder camelCaseString = new StringBuilder();
        for (String word : words) {
            camelCaseString.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
        }
        return camelCaseString.toString();
    }

    public static String replaceSpecialCharacters(String input) {
        return input.replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue");
    }

    public static String placeSpecialCharacters(String input) {
        return input.replace("ae", "ä")
                .replace("oe", "ö")
                .replace("ue", "ü");
    }
}
