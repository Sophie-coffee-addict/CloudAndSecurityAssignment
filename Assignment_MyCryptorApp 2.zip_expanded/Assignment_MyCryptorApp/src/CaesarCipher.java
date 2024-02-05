public class CaesarCipher {

    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char encryptedChar = (char) ('A' + (Character.toUpperCase(character) - 'A' + key) % 26);
                result.append(Character.isLowerCase(character) ? Character.toLowerCase(encryptedChar) : encryptedChar);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char decryptedChar = (char) ('A' + (Character.toUpperCase(character) - 'A' - key + 26) % 26);
                result.append(Character.isLowerCase(character) ? Character.toLowerCase(decryptedChar) : decryptedChar);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }
}
