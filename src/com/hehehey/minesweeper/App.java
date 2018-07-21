package com.hehehey.minesweeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        // initialize mine field.
        System.out.println("---- minesweeper ----");
        System.out.println("==> init mine field...");
        BufferedReader reader;
        MineFiled mineFiled;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("difficulty:\n" +
                    "    1) beginner;\n" +
                    "    2) standard;\n" +
                    "    3) challenge;\n" +
                    "    4) dark souls.\n");

            int difficulty = 2;
            parseDifficulty:
            while (true) {
                System.out.print("--> ");
                String line = reader.readLine().trim();
                if (line.isEmpty())
                    break;

                for (String word : line.split(" ")) {
                    switch (word) {
                        case "beginner":
                            difficulty = 1;
                            break parseDifficulty;
                        case "standard":
                            difficulty = 2;
                            break parseDifficulty;
                        case "challenge":
                            difficulty = 3;
                            break parseDifficulty;
                        case "dark":
                        case "souls":
                            difficulty = 4;
                            break parseDifficulty;
                        default:
                            try {
                                difficulty = Integer.parseInt(String.valueOf(line.charAt(0)));
                                break parseDifficulty;
                            } catch (NumberFormatException ignored) {
                            }
                            break;
                    }
                }

                System.out.println("invalid difficulty: " + line);
            }

            int numMines, width, height;
            switch (difficulty) {
                case 1:
                    numMines = 5;
                    width = 5;
                    height = 5;
                    break;
                case 3:
                    numMines = 100;
                    width = 26;
                    height = 26;
                    break;
                case 4:
                    numMines = 3000;
                    width = 100;
                    height = 100;
                    break;
                case 2:
                default:
                    numMines = 30;
                    width = 13;
                    height = 13;
                    break;
            }
            mineFiled = new MineFiled(numMines, height, width);
            System.out.print(mineFiled.generateHint());
        } catch (Exception e) {
            System.out.println(String.format("init failed: %s", e.toString().toLowerCase()));
            return;
        }

        // begin game.
        String line;
        Pattern pattern1 = Pattern.compile("([a-zA-Z]+)(\\d+)");
        Pattern pattern2 = Pattern.compile("(\\d+)([a-zA-Z]+)");
        while (true) {
            try {
                System.out.print("--> ");
                line = reader.readLine().trim();
                if (line.equals("exit"))
                    break;

                if (line.startsWith("hit ")) {
                    int y, x;
                    Matcher matcher1 = pattern1.matcher(line);
                    Matcher matcher2 = pattern2.matcher(line);
                    if (matcher1.find()) {
                        x = AlphabetToInt(matcher1.group(1));
                        y = Integer.parseInt(matcher1.group(2)) - 1;
                        mineFiled.hit(y, x);
                    } else if (matcher2.find()) {
                        x = AlphabetToInt(matcher2.group(2));
                        y = Integer.parseInt(matcher2.group(1)) - 1;
                        mineFiled.hit(y, x);
                    } else {
                        System.out.println(line + ": syntax error.");
                        continue;
                    }
                } else if (line.startsWith("mark ")) {
                    int y, x;
                    Matcher matcher1 = pattern1.matcher(line);
                    Matcher matcher2 = pattern2.matcher(line);
                    if (matcher1.find()) {
                        x = AlphabetToInt(matcher1.group(1));
                        y = Integer.parseInt(matcher1.group(2)) - 1;
                        mineFiled.mark(y, x);
                    } else if (matcher2.find()) {
                        x = AlphabetToInt(matcher2.group(2));
                        y = Integer.parseInt(matcher2.group(1)) - 1;
                        mineFiled.mark(y, x);
                    } else {
                        System.out.println(line + ": syntax error.");
                        continue;
                    }
                } else {
                    System.out.println(line + " is not recognised.");
                    continue;
                }

                if (mineFiled.isGameOver()) {
                    System.out.print(mineFiled.generateHint(true));
                    System.out.println(mineFiled.message);
                    break;
                }

                System.out.print(mineFiled.generateHint());
            } catch (Exception e) {
                System.out.println(e.toString().toLowerCase());
            }
        }
    }

    private static int AlphabetToInt(String alphabet) {
        int result = 0;
        for (int i = 0; i < alphabet.length(); i++) {
            int base = alphabet.charAt(i) - 'a';
            result += base * Math.pow(26, alphabet.length() - i - 1);
        }
        return result;
    }

    static String IntToAlphabet(int value) {
        StringBuilder result = new StringBuilder();
        do {
            result.insert(0, (char) (value % 26 + 'a'));
            value = value / 26;
        } while (value != 0);
        return result.toString();
    }
}
