package com.hehehey.minesweeper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("1) easy;\t2) hard\n");
            String line = reader.readLine();
            int difficulty = Integer.parseInt(line);
            int numMines = 5;
            int width = 10;
            int height = 10;
            switch (difficulty) {
                case 2:
                    numMines = 20;
                    width = 20;
                    height = 20;
                    break;
            }

            line = "";
            MineFiled mineFiled = new MineFiled(numMines, height, width);
            System.out.print(mineFiled.generateHint());
            while (true) {
                try {
                    line = reader.readLine().trim();
                    if (line.equals("exit"))
                        break;

                    if (line.startsWith("hit ") && line.split(" ").length == 3) {
                        String[] parts = line.split(" ");
                        int y = Integer.parseInt(parts[1]);
                        int x = Integer.parseInt(parts[2]);
                        mineFiled.hit(y, x);
                        System.out.print(mineFiled.generateHint());
                    } else if (line.startsWith("mark ") && line.split(" ").length == 3) {
                        String[] parts = line.split(" ");
                        int y = Integer.parseInt(parts[1]);
                        int x = Integer.parseInt(parts[2]);
                        mineFiled.mark(y, x);
                    } else {
                        System.out.println(line + " is not recognised.");
                    }

                    if (mineFiled.isGameOver()) {
                        System.out.println(mineFiled.message);
                        break;
                    }
                } catch (Exception e) {
                    if (line.trim().isEmpty()) {
                        System.out.println("unexpected: " + e.toString().toLowerCase());
                    } else {
                        System.out.println(line + ": " + e.toString().toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(String.format("init failed: %s", e.toString().toLowerCase()));
        }
    }
}
