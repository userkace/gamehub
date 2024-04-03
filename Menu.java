import java.io.*;
import java.util.*;

public class Menu {
    private String email;
    private String username;

    // This is a constructor for the `Menu` class that takes in two parameters, `email` and `username`,
    // and assigns them to the corresponding instance variables `this.email` and `this.username`. This
    // allows the `Menu` object to have access to the email and username of the user who is currently
    // logged in.
    public Menu(String email, String username) {
        this.email = email;
        this.username = username;
    }

    /**
     * The function displays a menu with options for managing an account or playing games, and allows
     * the user to log out.
     */
    public void run() {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n[----------------------]");
            System.out.print("  [1] Account\n" +
                    "  [2] Games\n\n" +
                    "  [3] Log Out\n" +
                    "[----------------------]\n\n");
            int choice = Main.readInt("  > ");
            Game game = new Game(username);
            if (choice == 1) {
                System.out.println("\n[----------------------]\n");
                System.out.println("<< Username > " + username);
                System.out.println("<< Email    > " + email + "\n");
                currentUser();
            } else if (choice == 2) {
                game.run();
            } else if (choice == 3) {
                exit = true;
            } else {
                System.out.println("\n{Error!}\nInvalid input.");
            }
        }
    }
    
    /**
     * The function reads user information from a CSV file and presents it in a formatted way.
     */
    public void currentUser() {
        // Read userInfo.csv file
        List<String> userInfo = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username)) {
                    // User has entered information for this game
                    userInfo.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Present user info
        List<String> games = new ArrayList<>();
        List<Integer> maxAchievements = new ArrayList<>();
        // This code block is reading data from a CSV file named "games.csv" and storing the game names
        // and their maximum achievements in two separate lists named `games` and `maxAchievements`. It
        // then prints a formatted table of the user's game information, including the game name, the
        // time played, and the number of achievements earned out of the maximum possible for that
        // game. The `try` block is used to handle any potential `IOException` that may occur while
        // reading the file.
        try (BufferedReader br = new BufferedReader(new FileReader("games.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                games.add(values[0]);
                maxAchievements.add(Integer.parseInt(values[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[ Game | Time | Achievements ]\n");
        for (String line : userInfo) {
            String[] values = line.split(",");
            System.out.print(values[1] + " | ");
            System.out.print(values[2] + " hrs. | ");
            System.out.print(values[3] + "/" + maxAchievements.get(games.indexOf(values[1])) + "\n");
            System.out.println();
        }
    }
}