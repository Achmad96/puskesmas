package src.utils;

public class RandomGeneratedId {
    public static String generateRandomID(String prefix) {
        return generateRandomID(15, prefix).toString();
    }

    public static String generateRandomID(int maxLength, String prefix) {
        int length = maxLength - prefix.length();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder(prefix);
        for (int i = 0; i < length; i++) {
            id.append(characters.charAt((int) Math.floor(Math.random() * characters.length())));
        }
        return id.toString();
    }
}
