import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordGuessingGame {

    private static final int MAX_ATTEMPTS = 6;

    public static void main(String[] args) {
        boolean restartGame = true;
        JOptionPane.showMessageDialog(null, "Welcome to Ultimate Word Guesser!");
        JOptionPane.showMessageDialog(null, "In this game, you will have to figure out random words to win.");
        JOptionPane.showMessageDialog(null, "There will be 4 levels of difficulty: Easy, Medium, Hard, Expert.");
        JOptionPane.showMessageDialog(null, "Will you be able to conquer all 4?");

        while (restartGame) {
            int currentLevel = 1;
            restartGame = playGame(currentLevel);

            if (restartGame) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to try again?", "Try Again?", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) {
                    restartGame = false;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Thanks for playing!");
    }

    private static boolean playGame(int currentLevel) {
        while (currentLevel <= 4) {
            boolean levelResult = playLevel(currentLevel);
            if (!levelResult) {
                return false; // Player lost, restart the game
            }
            currentLevel++;
        }
        return true; // Player completed all levels
    }

    private static boolean playLevel(int difficultyLevel) {
        String wordToGuess = chooseWord(difficultyLevel);
        char[] guessedLetters = new char[wordToGuess.length()];
        int attemptsLeft = MAX_ATTEMPTS;
        JOptionPane.showMessageDialog(null, "Level " + difficultyLevel);
        initializeGuessedLetters(guessedLetters);

        while (attemptsLeft > 0) {
            char guess = getValidGuess();

            if (isAlreadyGuessed(guess, guessedLetters)) {
                JOptionPane.showMessageDialog(null, "You've already guessed that letter. Try again.");
                continue;
            }

            updateGuessedLetters(guess, wordToGuess, guessedLetters);

            if (!containsGuess(guess, wordToGuess)) {
                attemptsLeft--;
                JOptionPane.showMessageDialog(null, "Wrong guess! Attempts left: " + attemptsLeft);
            } else {
                JOptionPane.showMessageDialog(null, "Good guess!");
            }

            displayHangman(attemptsLeft);
            displayCurrentStatus(guessedLetters, attemptsLeft);

            if (isWordGuessed(guessedLetters)) {
                JOptionPane.showMessageDialog(null, "Congratulations! You've guessed the word.");
                return true; // Player won the level
            }
        }

        JOptionPane.showMessageDialog(null, "Sorry, you ran out of attempts. The word was: " + wordToGuess);
        return false; // Player lost the level
    }

    private static String chooseWord(int difficultyLevel) {
        String fileName;

        switch (difficultyLevel) {
            case 1:
                fileName = "easywords.txt";
                break;
            case 2:
                fileName = "mediumwords.txt";
                break;
            case 3:
                fileName = "hardwords.txt";
                break;
            case 4:
                fileName = "expertwords.txt";
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }

        return getRandomWordFromFile(fileName);
    }

    private static String getRandomWordFromFile(String fileName) {
        String[] words = readWordsFromFile(fileName);
        return words[(int) (Math.random() * words.length)];
    }

    private static String[] readWordsFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String> wordsList = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                wordsList.add(line.trim());
            }

            return wordsList.toArray(new String[0]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading words from file: " + fileName);
            System.exit(1);
            return null; // Unreachable, but added to satisfy compiler
        }
    }

    private static void initializeGuessedLetters(char[] guessedLetters) {
        for (int i = 0; i < guessedLetters.length; i++) {
            guessedLetters[i] = '_';
        }
    }

    private static void displayHangman(int attemptsLeft) {
        // ASCII art representation of Hangman Harry
        switch (attemptsLeft) {
            case 6:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n");
                break;
            case 5:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n");
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n"
                        + "  |\n");
                break;
            case 3:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n"
                        + " /|\n");
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n"
                        + " /|\\\n");
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n"
                        + " /|\\\n"
                        + " /\n");
                break;
            case 0:
                JOptionPane.showMessageDialog(null, "Hangman Harry:\n\n"
                        + "  O\n"
                        + " /|\\\n"
                        + " / \\\n");
                break;
            default:
                break;
        }
    }

    private static void displayCurrentStatus(char[] guessedLetters, int attemptsLeft) {
        StringBuilder display = new StringBuilder("Current Status: ");
        for (char letter : guessedLetters) {
            display.append(letter).append(" ");
        }
        display.append("\nAttempts left: ").append(attemptsLeft);
        JOptionPane.showMessageDialog(null, display.toString());
    }

    private static char getValidGuess() {
        char guess;
        while (true) {
            String input = JOptionPane.showInputDialog("Guess a letter: ").toLowerCase();
            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                guess = input.charAt(0);
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a single alphabetic character.");
            }
        }
        return guess;
    }

    private static boolean isAlreadyGuessed(char guess, char[] guessedLetters) {
        for (char letter : guessedLetters) {
            if (letter == guess) {
                return true;
            }
        }
        return false;
    }

    private static void updateGuessedLetters(char guess, String wordToGuess, char[] guessedLetters) {
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                guessedLetters[i] = guess;
            }
        }
    }

    private static boolean containsGuess(char guess, String wordToGuess) {
        return wordToGuess.indexOf(guess) != -1;
    }

     private static boolean isWordGuessed(char[] guessedLetters) {
        for (char letter : guessedLetters) {
            if (letter == '_') {
                return false;
            }
        }
        return true;
    }
}

