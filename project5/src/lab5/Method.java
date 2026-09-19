package lab5;

public class Method {

    public static String Initials(String name) {
        int i;
        String initial = "";
        for (i = 0; i < name.length(); i++) {
            if (i == 0) {
                initial = name.substring(0, 1);

            }
            if (i != 0 && name.charAt(i - 1) == ' ') {
                initial += name.charAt(i);
            }
        }
        return initial;
    }

    public static int Vowel(String text) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if ("aeiouAEIOU".indexOf(ch) >= 0) {
                return i;
            }
        }
        return -1;
    }

     public static void main(String[] args) {
        System.out.println(Initials("Edna del Humboldt von der Schooch"));
        System.out.println(Vowel("Goat"));
    }
}




