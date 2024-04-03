import java.io.*;
import java.util.*;

public class Game {
    private String username;

    public Game(String username) {
        this.username = username;
    }

    /**
     * This function displays a menu with options to add games, view the leaderboard, or go back, and
     * executes the corresponding function based on user input.
     */
    public void run() {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n[----------------------]");
            System.out.print("  [1] Add Games\n" +
                    "  [2] Leaderboard\n\n" +
                    "  [3] Back" +
                    "\n[----------------------]\n\n");
            int choice = Main.readInt("  > ");
            if (choice == 1) {
                addGames();
            } else if (choice == 2) {
                leaderboard();
            } else if (choice == 3) {
                exit = true;
            } else {
                System.out.println("\n{Error!}\nInvalid input.");
            }
        }
    }

    /**
     * This function reads game information from a CSV file, prompts the user to input information
     * about a selected game, checks if the user has already entered information for the selected game,
     * and stores the information in a CSV file.
     */
    public void addGames() {
        // Read games.csv file
        List<String> games = new ArrayList<>();
        List<Integer> maxAchievements = new ArrayList<>();
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

        // Print all games with their indices
        System.out.println("\n[----------------------]\n");
        System.out.println("[ Games ]\n");
        for (int i = 0; i < games.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + games.get(i));
        }

        // Get user input
        Scanner input = new Scanner(System.in);
        System.out.print("\n");
        int gameOpt = Main.readInt("  > ");
        int gameIndex = (gameOpt - 1);
        // Check if game index is valid
        if (gameIndex < 0 || gameIndex >= games.size()) {
            System.out.println("\n{Error!}\nInvalid game index.");
            return;
        }
        System.out.println("\nChosen game [" + games.get(gameIndex) + "]");
        System.out.print("\nEnter hours played\n\n");
        int timePlayed = Main.readInt("  > ");
        System.out.print("\nEnter achievements obtained (out of " + maxAchievements.get(gameIndex) + ")\n\n ");
        int achievementsObtained = Main.readInt("  > ");
        // Check if achievements obtained is not greater than max achievements
        if (achievementsObtained > maxAchievements.get(gameIndex)) {
            System.out.println("Achievements obtained cannot be greater than " + maxAchievements.get(gameIndex));
            return;
        }
        // Read userInfo.csv file and check if user has already entered information for the selected game
        List<String> userInfo = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username) && values[1].equals(games.get(gameIndex))) {
                    // User has already entered information for the selected game
                    // Update time played and achievements obtained
                    userInfo.add(username + "," + games.get(gameIndex) + "," + timePlayed + "," + achievementsObtained);
                    found = true;
                } else {
                    userInfo.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    // Store information in userInfo.csv file
        try (FileWriter fw = new FileWriter("userInfo.csv")) {
            for (String line : userInfo) {
                fw.append(line + "\n");
            }
            if (!found) {
                // User has not entered information for the selected game
                // Append a new line
                fw.append(username + "," + games.get(gameIndex) + "," + timePlayed + "," + achievementsObtained + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function displays a menu for the user to choose between different leaderboards and presents
     * the corresponding leaderboard based on the user's choice.
     */
    public void leaderboard() {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n[----------------------]");
            System.out.print("    [ LEADERBOARD ]\n" +
                    "  [1] GameHub user hours\n" +
                    "  [2] Most played games\n" +
                    "  [3] Hour played per game\n" +
                    "  [4] Achievements per game\n\n" +
                    "  [5] Back\n" +
                    "[----------------------]\n\n");
            int option = Main.readInt("  > ");
            List<String> games = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("games.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    games.add(values[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            int gameOpt;
            int gameIndex;
            switch (option) {
                case 1:
                    // Read userInfo.csv file
                    Map<String, Integer> totalTimePlayedByUser = new HashMap<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            String user = values[0];
                            int timePlayed = Integer.parseInt(values[2]);
                            totalTimePlayedByUser.put(user, totalTimePlayedByUser.getOrDefault(user, 0) + timePlayed);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Sort users by total time played
                    List<Map.Entry<String, Integer>> sortedUsers = new ArrayList<>(totalTimePlayedByUser.entrySet());
                    sortedUsers.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                    // Present user leaderboard
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ GameHub user hours ]\n");
                    System.out.println("@=@=@=@=@=@=@=@");
                    System.out.println("| Leaderboard |");
                    System.out.println("@=@=@=@=@=@=@=@\n");
                    for (int i = 0; i < sortedUsers.size(); i++) {
                        Map.Entry<String, Integer> user = sortedUsers.get(i);
                        System.out.println((i + 1) + ": " + user.getKey() + " (" + user.getValue() + " hrs.)");
                    }
                    break;
                case 2:
                    // Read userInfo.csv file
                    Map<String, Integer> totalTimePlayedByGame = new HashMap<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            String game2 = values[1];
                            int timePlayed = Integer.parseInt(values[2]);
                            totalTimePlayedByGame.put(game2, totalTimePlayedByGame.getOrDefault(game2, 0) + timePlayed);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Sort games by total time played
                    List<Map.Entry<String, Integer>> sortedGames = new ArrayList<>(totalTimePlayedByGame.entrySet());
                    sortedGames.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                    // Present game leaderboard
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ Most played games ]\n");
                    System.out.println("@=@=@=@=@=@=@=@");
                    System.out.println("| Leaderboard |");
                    System.out.println("@=@=@=@=@=@=@=@\n");
                    for (int i = 0; i < sortedGames.size(); i++) {
                        Map.Entry<String, Integer> game = sortedGames.get(i);
                        System.out.println((i + 1) + ": " + game.getKey() + " (" + game.getValue() + " hrs.)");
                    }
                    break;
                case 3:

                    // Print all games with their indices
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ Games ]\n");
                    for (int i = 0; i < games.size(); i++) {
                        System.out.println("  [" + (i + 1) + "] " + games.get(i));
                    }

                    // Get user input
                    System.out.print("\n");
                    gameOpt = Main.readInt("  > ");
                    gameIndex = (gameOpt - 1);

                    // Check if game index is valid
                    if (gameIndex < 0 || gameIndex >= games.size()) {
                        System.out.println("\n{Error!}\nInvalid game index.");
                        break;
                    }

                    String game3 = games.get(gameIndex);

                    // Read userInfo.csv file
                    Map<String, Integer> timeObtained = new HashMap<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            if (values[1].equals(game3)) {
                                String user = values[0];
                                int time = Integer.parseInt(values[2]);
                                timeObtained.put(user, time);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Sort users by achievements obtained
                    List<Map.Entry<String, Integer>> sortedUsers3 = new ArrayList<>(timeObtained.entrySet());
                    sortedUsers3.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                    // Present leaderboard
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ "+game3+"'s Hours ]\n");
                    System.out.println("@=@=@=@=@=@=@=@");
                    System.out.println("| Leaderboard |");
                    System.out.println("@=@=@=@=@=@=@=@\n");
                    for (int i = 0; i < sortedUsers3.size(); i++) {
                        Map.Entry<String, Integer> user = sortedUsers3.get(i);
                        System.out.println((i + 1) + ": " + user.getKey() + " (" + user.getValue() + " hrs.)");
                    }
                    break;
                case 4:

                    // Print all games with their indices
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ Games ]\n");
                    for (int i = 0; i < games.size(); i++) {
                        System.out.println("  [" + (i + 1) + "] " + games.get(i));
                    }

                    // Get user input
                    System.out.print("\n");
                    gameOpt = Main.readInt("  > ");
                    gameIndex = (gameOpt - 1);

                    // Check if game index is valid
                    if (gameIndex < 0 || gameIndex >= games.size()) {
                        System.out.println("\n{Error!}\nInvalid game index.");
                        break;
                    }

                    String game = games.get(gameIndex);

                    // Read userInfo.csv file
                    Map<String, Integer> achievementsObtained = new HashMap<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("userInfo.csv"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] values = line.split(",");
                            if (values[1].equals(game)) {
                                String user = values[0];
                                int achievements = Integer.parseInt(values[3]);
                                achievementsObtained.put(user, achievements);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Sort users by achievements obtained
                    List<Map.Entry<String, Integer>> sortedUsers2 = new ArrayList<>(achievementsObtained.entrySet());
                    sortedUsers2.sort((a, b) -> b.getValue().compareTo(a.getValue()));

                    // Present leaderboard
                    System.out.println("\n[----------------------]\n");
                    System.out.println("[ "+game+"'s Achievements ]\n");
                    System.out.println("@=@=@=@=@=@=@=@");
                    System.out.println("| Leaderboard |");
                    System.out.println("@=@=@=@=@=@=@=@\n");
                    for (int i = 0; i < sortedUsers2.size(); i++) {
                        Map.Entry<String, Integer> user = sortedUsers2.get(i);
                        System.out.println((i + 1) + ": " + user.getKey() + " (" + user.getValue() + ")");
                    }
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("\n{Error!}\nInvalid input.");
            }
        }
    }
}