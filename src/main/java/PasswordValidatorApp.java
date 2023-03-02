import com.tcm.utilities.PasswordValidatorUtility;

import java.util.Scanner;

public class PasswordValidatorApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean tryAgain = true;

        while (tryAgain) {
            System.out.print("Enter some password to validate: ");
            String input = scanner.nextLine();

            boolean isPasswordOk = Boolean.TRUE.equals(PasswordValidatorUtility.isPasswordValid(input).block());

            System.out.println("Entered password is: " + (isPasswordOk ? "VALID" : "INVALID"));

            System.out.print("Try again? (y/n): ");
            String answer = scanner.nextLine();

            if (!answer.equalsIgnoreCase("y")) {
                tryAgain = false;
            }
        }

        scanner.close();
    }
}
