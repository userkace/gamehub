import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern; //Email RegEx

public class Main {
    /**
     * This function is the main menu for a game hub program that allows users to sign up, log in, and
     * exit the program.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        String email;
        String username;
        String password;

        System.out.println("                 __");
        System.out.println("     ..======.. |==|");
        System.out.println("     || GAME || |= |");
        System.out.println("  _  || HUB  || |^*| _");
        System.out.println(" |=| o=,====,=o |__||=|");
        System.out.println(" |_|  ________)~`)  |_|");
        System.out.println("     [========] (')");

        // This code block is the main loop of the program that displays a menu with options to sign
        // up, log in, or exit. It reads the user's input and performs the corresponding action. If the
        // user chooses to sign up, it prompts the user to enter their email, username, and password,
        // checks if the email and username are not duplicates, and writes the information to a CSV
        // file. If the user chooses to log in, it prompts the user to enter their username and
        // password, checks if the account is valid, and displays a menu for the user. If the user
        // chooses to exit, it shuts down the program.
        while (!exit) {
            System.out.println("\n[----------------------]");
            System.out.print("  [1] Sign up\n" +
                    "  [2] Log in\n\n" +
                    "  [3] Exit" +
                    "\n[----------------------]\n\n");

            int choice = readInt("  > ");
            if (choice == 1) {
                do {
                    System.out.print("\n[ Enter your email ]\n  > ");
                    email = input.nextLine();
                } while (hasSpaceOrEmpty(email) || !(isEmail(email)));
                do {
                    System.out.print("\n[ Enter your username ]\n  > ");
                    username = input.nextLine();
                } while (hasSpaceOrEmpty(username));
                do {
                    System.out.print("\n[ Enter your password ]\n  > ");
                    password = input.nextLine();
                } while (hasSpaceOrEmpty(password));
                if (isDuplicate(email, username)) {
                    System.out.println("\n{Error!}\nEmail or username already exists.");
                } else {
                    try {
                        FileWriter writer = new FileWriter("users.csv", true);
                        writer.append(email);
                        writer.append(',');
                        writer.append(username);
                        writer.append(',');
                        writer.append(password);
                        writer.append('\n');
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (choice == 2) {
                do {
                    System.out.print("\n[ Enter your username ]\n  > ");
                    username = input.nextLine();
                } while (hasSpaceOrEmpty(username));
                do {
                    System.out.print("\n[ Enter your password ]\n  > ");
                    password = input.nextLine();
                } while (hasSpaceOrEmpty(password));
                if (isValidAccount(username, password)) {
                    System.out.println("\n<< Sign In successful!");
                    email = getEmail(username);
                    Menu menu = new Menu(email, username);
                    menu.run();
                } else {
                    System.out.println("\n{Error!}\nInvalid username or password.");
                }
            } else if (choice == 3) {
                System.out.print("\n<< Shutting down.");
                delayNext(500);
                System.out.print(".");
                delayNext(500);
                System.out.print(".\n\n");
                delayNext(500);
                exit = true;
            } else {
                System.out.println("\n{Error!}\nInvalid input.");
            }
        }
    }

    /**
     * The function delays the execution of the next statement by a specified number of milliseconds.
     * 
     * @param ms The parameter "ms" is an integer value representing the number of milliseconds to
     * delay the execution of the current thread. The method uses the Thread.sleep() method to pause
     * the execution of the current thread for the specified number of milliseconds.
     */
    public static void delayNext(int ms){
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * The function checks if a given input string contains a space or is empty and returns a boolean
     * value accordingly.
     * 
     * @param input A string that represents the input to be checked for spaces or emptiness.
     * @return The method `hasSpaceOrEmpty` returns a boolean value. It returns `true` if the input
     * string contains a space or is empty, and `false` otherwise.
     */
    public static boolean hasSpaceOrEmpty(String input) {
        if (input.contains(" ")) {
            System.out.println("\n{Error!}\nInput cannot have space.");
            return true;
        }
        if (input.isEmpty()) {
            System.out.println("\n{Error!}\nInput cannot be empty.");
            return true;
        }
        return false;
    }

    /**
     * This function reads an integer input from the user with error handling for non-integer and
     * negative inputs.
     * 
     * @param prompt a String that will be printed to prompt the user for input.
     * @return An integer value is being returned.
     */
    public static int readInt(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        boolean validInput = false;
        while (!validInput) {
            do {
                try {
                    System.out.print(prompt);
                    num = scanner.nextInt();
                    validInput = true;
                    if (num < 0) System.out.println("\n{Error!}\nInput must be positive.\n");
                } catch (InputMismatchException e) {
                    System.out.println("\n{Error!}\nInput must be an integer.\n");
                    scanner.nextLine();
                }
            } while (num < 0);
        }
        return num;
    }

    /**
     * The function checks if an email is valid by matching it against a regular expression and
     * checking if it ends with any of the valid email endings read from a CSV file.
     * 
     * @param email The email address that needs to be checked for validity.
     * @return The method is returning a boolean value indicating whether the input email is valid or
     * not. If the email is valid, the method returns true. If the email is not valid, the method
     * returns false and prints an error message.
     */
    public static boolean isEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@" + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"; //
        List<String> validEmailEndings = new ArrayList<>();
        // Read valid email endings from csv file
        try (BufferedReader br = new BufferedReader(new FileReader("validEmail.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                validEmailEndings.add(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Check if email ends with any of the valid email endings
        for (String ending : validEmailEndings) {
            if (email.endsWith(ending)) {
                if (Pattern.compile(regexPattern).matcher(email).matches() == true) {
                    return true;
                }
            }
        }
        System.out.println("\n{Error!}\nEmail is not valid.");
        return false;
    }

    /**
     * This function checks if a given email or username already exists in a CSV file containing user
     * data.
     * 
     * @param email A string representing the email address of a user.
     * @param username The username to check for duplicates in the CSV file.
     * @return The method is returning a boolean value. It returns true if either the email or username
     * already exists in the "users.csv" file, and false otherwise.
     */
    public static boolean isDuplicate(String email, String username) {
        List<String[]> users = new ArrayList<>();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.csv"));
            while ((line = br.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length == 3) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String[] user : users) {
            if (user[0].equals(email) || user[1].equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function checks if a given username and password match any of the user accounts stored in a
     * CSV file.
     * 
     * @param username A string representing the username of the account being checked for validity.
     * @param password The password parameter is a String variable that represents the password entered
     * by the user trying to log in.
     * @return The method is returning a boolean value. It returns true if the provided username and
     * password match with any of the user accounts stored in the "users.csv" file, and false
     * otherwise.
     */
    public static boolean isValidAccount(String username, String password) {
        List<String[]> users = new ArrayList<>();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.csv"));
            while ((line = br.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length == 3) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String[] user : users) {
            if (user[1].equals(username) && user[2].equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This Java function retrieves the email address associated with a given username from a CSV file.
     * 
     * @param username The username of the user whose email is to be retrieved from the "users.csv"
     * file.
     * @return The method is returning a String value, which is the email address associated with the
     * given username. If the username is not found in the "users.csv" file, the method returns null.
     */
    public static String getEmail(String username) {
        List<String[]> users = new ArrayList<>();
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("users.csv"));
            while ((line = br.readLine()) != null) {
                String[] user = line.split(",");
                if (user.length == 3) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String[] user : users) {
            if (user[1].equals(username)) {
                return user[0];
            }
        }
        return null;
    }
}