import java.util.Random;
import java.util.Scanner;

public class NumberGuessing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int totalRounds = 0;
        int totalAttempts = 0;
        int maxAttempts = 10;

        boolean playAgain;
        do {
            totalRounds++;
            int numberToGuess = random.nextInt(100) + 1;
            int attempts = 0;
            boolean hasGuessedCorrectly = false;

            System.out.println("Round " + totalRounds + ": Guess the number between 1 and 100. You have " + maxAttempts + " attempts.");

            while (attempts < maxAttempts) {
                attempts++;
                System.out.print("Attempt " + attempts + ": ");
                int userGuess = scanner.nextInt();

                if (userGuess == numberToGuess) {
                    System.out.println("Congratulations! You've guessed the correct number." + userGuess);
                    hasGuessedCorrectly = true;
                    break;
                } else if (userGuess < numberToGuess) {
                    System.out.println("Your guess is too low.");
                } else {
                    System.out.println("Your guess is too high.");
                }
            }

             totalAttempts += attempts;
            if (!hasGuessedCorrectly) {
                System.out.println("Sorry! You've used all your attempts. The correct number was " + numberToGuess + ".");
            }

            System.out.print("Do you want to play another round? (yes/no): ");
            String response = scanner.next();
            playAgain = response.equalsIgnoreCase("yes");
        } while (playAgain);

        System.out.println("Game over. You played " + totalRounds + " rounds with a total of " + totalAttempts + " attempts.");
        scanner.close();
    }
}