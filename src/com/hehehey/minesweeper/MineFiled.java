package com.hehehey.minesweeper;

import java.util.LinkedList;
import java.util.Random;

class MineFiled {
    private static final int REVEALED = -1;
    private static final int MARKED = -2;
    private static final int EXPLODED = -3;

    String message = "all mines found.";

    private boolean gameOver = false;
    private int mineToBeFound = 0;

    private boolean[][] field;
    private int[][] hint;

    MineFiled(int numMines, int height, int width) {
        field = new boolean[height][width];
        hint = new int[height][width];

        Random random = new Random();
        for (int i = 0; i < numMines; i++) {
            int y = Math.abs(random.nextInt()) % height;
            int x = Math.abs(random.nextInt()) % width;

            if (!field[y][x]) {
                field[y][x] = true;
                mineToBeFound += 1;
            }
        }
    }

    void hit(int y, int x) {
        if (field[y][x]) {
            hint[y][x] = EXPLODED;
            gameOver = true;
            message = "a mine exploded.";
        } else {
            LinkedList<Candidate> candidates = new LinkedList<>();
            LinkedList<Candidate> checked = new LinkedList<>();
            if (hint[y][x] == MARKED)
                hint[y][x] = 0;

            candidates.addLast(new Candidate(y, x));
            while (candidates.size() != 0) {
                Candidate candidate = candidates.getFirst();
                int numSurroundedMine = checkSurrounding(candidate.y, candidate.x);
                if (numSurroundedMine > 0 && hint[candidate.y][candidate.x] != MARKED)
                    hint[candidate.y][candidate.x] = numSurroundedMine;
                else {
                    hint[candidate.y][candidate.x] = REVEALED;
                    candidates.addAll(generateSurroundingCandidate(candidate.y, candidate.x));
                }
                checked.add(candidate);
                candidates.removeAll(checked);
            }
        }
    }

    void mark(int y, int x) throws Exception {
        if (hint[y][x] == 0) {
            hint[y][x] = MARKED;
            if (field[y][x])
                mineToBeFound--;
            if (mineToBeFound == 0)
                gameOver = true;
        }
        else {
            throw new Exception("cannot mark this place.");
        }
    }

    String generateHint() {
        StringBuilder currentHint = new StringBuilder();
        for (int[] line : hint) {
            currentHint.append("    ");
            for (int aHint : line) {
                switch (aHint) {
                    case REVEALED:
                        currentHint.append(" ");
                        break;
                    case MARKED:
                        currentHint.append("#");
                        break;
                    case EXPLODED:
                        currentHint.append("%");
                        break;
                    case 0:
                        currentHint.append("-");
                        break;
                    default:
                        currentHint.append(aHint);
                        break;
                }
            }
            currentHint.append("\n");
        }
        return currentHint.toString();
    }

    boolean isGameOver() {
        return gameOver;
    }

    private int checkSurrounding(int y, int x) {
        int count = 0;
        for (Candidate candidate :
                generateSurroundingCandidate(y, x)) {
            count += field[candidate.y][candidate.x] ? 1 : 0;
        }
        return count;
    }

    private LinkedList<Candidate> generateSurroundingCandidate(int y, int x) {
        LinkedList<Candidate> candidates = new LinkedList<>();
        candidates.addLast(new Candidate(y - 1, x - 1));
        candidates.addLast(new Candidate(y - 1, x));
        candidates.addLast(new Candidate(y - 1, x + 1));
        candidates.addLast(new Candidate(y, x - 1));
        candidates.addLast(new Candidate(y, x + 1));
        candidates.addLast(new Candidate(y + 1, x - 1));
        candidates.addLast(new Candidate(y + 1, x));
        candidates.addLast(new Candidate(y + 1, x - 1));

        candidates.removeIf(candidate -> candidate.y >= field.length || candidate.x >= field[0].length ||
                candidate.y < 0 || candidate.x < 0);
        return candidates;
    }

    private static class Candidate {
        int y;
        int x;

        Candidate(int y, int x) {
            this.y = y;
            this.x = x;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Candidate) {
                Candidate candidate = (Candidate) obj;
                return candidate.y == this.y && candidate.x == this.x;
            }
            else {
                return super.equals(obj);
            }
        }
    }
}
