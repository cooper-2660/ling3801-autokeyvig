import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AutoKeyVig {

    private final static Set<Character> ALPHABET = Set.of(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

    private final static Character j = Character.valueOf('J');

    private final static Map<Character, Integer> ALPHABET_TO_VALUE = new HashMap<>();
    private final static Map<Integer, Character> VALUE_TO_ALPHABET = new HashMap<>();

    private static void initializeAlphabet() {
        ALPHABET_TO_VALUE.put('A', 0);
        ALPHABET_TO_VALUE.put('B', 1);
        ALPHABET_TO_VALUE.put('C', 2);
        ALPHABET_TO_VALUE.put('D', 3);
        ALPHABET_TO_VALUE.put('E', 4);
        ALPHABET_TO_VALUE.put('F', 5);
        ALPHABET_TO_VALUE.put('G', 6);
        ALPHABET_TO_VALUE.put('H', 7);
        ALPHABET_TO_VALUE.put('I', 8);
        ALPHABET_TO_VALUE.put('J', 9);
        ALPHABET_TO_VALUE.put('K', 10);
        ALPHABET_TO_VALUE.put('L', 11);
        ALPHABET_TO_VALUE.put('M', 12);
        ALPHABET_TO_VALUE.put('N', 13);
        ALPHABET_TO_VALUE.put('O', 14);
        ALPHABET_TO_VALUE.put('P', 15);
        ALPHABET_TO_VALUE.put('Q', 16);
        ALPHABET_TO_VALUE.put('R', 17);
        ALPHABET_TO_VALUE.put('S', 18);
        ALPHABET_TO_VALUE.put('T', 19);
        ALPHABET_TO_VALUE.put('U', 20);
        ALPHABET_TO_VALUE.put('V', 21);
        ALPHABET_TO_VALUE.put('W', 22);
        ALPHABET_TO_VALUE.put('X', 23);
        ALPHABET_TO_VALUE.put('Y', 24);
        ALPHABET_TO_VALUE.put('Z', 25);

        VALUE_TO_ALPHABET.put(0, 'A');
        VALUE_TO_ALPHABET.put(1, 'B');
        VALUE_TO_ALPHABET.put(2, 'C');
        VALUE_TO_ALPHABET.put(3, 'D');
        VALUE_TO_ALPHABET.put(4, 'E');
        VALUE_TO_ALPHABET.put(5, 'F');
        VALUE_TO_ALPHABET.put(6, 'G');
        VALUE_TO_ALPHABET.put(7, 'H');
        VALUE_TO_ALPHABET.put(8, 'I');
        VALUE_TO_ALPHABET.put(9, 'J');
        VALUE_TO_ALPHABET.put(10, 'K');
        VALUE_TO_ALPHABET.put(11, 'L');
        VALUE_TO_ALPHABET.put(12, 'M');
        VALUE_TO_ALPHABET.put(13, 'N');
        VALUE_TO_ALPHABET.put(14, 'O');
        VALUE_TO_ALPHABET.put(15, 'P');
        VALUE_TO_ALPHABET.put(16, 'Q');
        VALUE_TO_ALPHABET.put(17, 'R');
        VALUE_TO_ALPHABET.put(18, 'S');
        VALUE_TO_ALPHABET.put(19, 'T');
        VALUE_TO_ALPHABET.put(20, 'U');
        VALUE_TO_ALPHABET.put(21, 'V');
        VALUE_TO_ALPHABET.put(22, 'W');
        VALUE_TO_ALPHABET.put(23, 'X');
        VALUE_TO_ALPHABET.put(24, 'Y');
        VALUE_TO_ALPHABET.put(25, 'Z');
    }

    public static void main(String[] args) {
        final int numBuckets = Integer.parseInt(args[0]);

        final StringBuilder ct = new StringBuilder(args[1]);

        initializeAlphabet();

        // Create and initialize array for missing characters
        final Set<Character>[] missingLetters = new HashSet[numBuckets];

        // Create and initialize array for shifted characters
        final Set<Character>[] shiftedLetters = new HashSet[numBuckets];

        // Create and initialize array to check for unique characters
        final Set<Character>[] lettersFoundSoFar = new HashSet[numBuckets];
        for (int i = 0; i < lettersFoundSoFar.length; i++) {
            lettersFoundSoFar[i] = new HashSet<>();

            shiftedLetters[i] = new HashSet<>();
        }

        // Create an initalize array to hold bucketed strings
        final StringBuilder[] buckets = new StringBuilder[numBuckets];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new StringBuilder();
        }

        // Put characters from ciphertext into correct bucket
        for (int i = 0; i < ct.length(); i++) {
            buckets[i % numBuckets].append(ct.charAt(i));

            lettersFoundSoFar[i % numBuckets].add(ct.charAt(i));

            // If one of our buckets contains all of the letters, then we know our key
            // length guess is wrong.
            if (lettersFoundSoFar[i % numBuckets].size() == 26) {
                System.out.println("\n\n--------FAIL--------\n");
                System.exit(1);
            }
        }

        // Output buckets
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", false))) {
            for (int i = 0; i < buckets.length; i++) {
                final int index = i;

                // Collect the missing letters from each bucket
                missingLetters[i] = ALPHABET.stream().filter(letter -> !lettersFoundSoFar[index].contains(letter))
                        .collect(Collectors.toSet());

                for (Character character : missingLetters[i]) {
                    int guess = ALPHABET_TO_VALUE.get(character) - ALPHABET_TO_VALUE.get(j);
                    if (guess < 0) {
                        guess += 26;
                    }
                    shiftedLetters[i].add(VALUE_TO_ALPHABET.get(guess));
                }

                writer.write("Bucket #" + i + ": \n" + buckets[index] + "\n");
                writer.write("Missing Letter(s): " + missingLetters[i]
                        + "\nKey Guess(es): " + shiftedLetters[i]
                        + "\n\n\n");
            }
        } catch (IOException e) {
            System.out.println("\n\n--------ERROR--------\n");
        }

        System.out.println("\n\n--------SUCCESS--------\n");
    }

}