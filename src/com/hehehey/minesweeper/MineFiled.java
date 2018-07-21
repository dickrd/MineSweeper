package com.hehehey.minesweeper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

class MineFiled {
    private static final int REVEALED = -1;
    private static final int MARKED = -2;
    private static final int EXPLODED = -3;

    String message = "all mines found.";

    private boolean mineExploded = false;
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
            mineExploded = true;
            message = "a mine exploded.";
        } else {
            LinkedList<Candidate> candidates = new LinkedList<>();
            HashSet<Candidate> checked = new HashSet<>();
            if (hint[y][x] == MARKED)
                hint[y][x] = 0;

            candidates.addFirst(new Candidate(y, x));
            while (!candidates.isEmpty()) {
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
        }
        else {
            throw new Exception("cannot mark this place.");
        }
    }

    String generateHint() {
        return generateHint(false);
    }

    String generateHint(boolean answer) {
        StringBuilder hintOutput = new StringBuilder();
        StringBuilder firstLine = new StringBuilder("    ");
        for (int i = 0; i < hint.length; i++) {
            hintOutput.append(String.format("%3s ", i + 1));
            String currentHint;
            for (int j = 0; j < hint[i].length; j++) {
                switch (hint[i][j]) {
                    case REVEALED:
                        currentHint = "  ";
                        break;
                    case MARKED:
                        currentHint = "x ";
                        break;
                    case EXPLODED:
                        currentHint = "@ ";
                        break;
                    case 0:
                        currentHint = "▒ ";
                        break;
                    default:
                        currentHint = hint[i][j] + " ";
                        break;
                }
                if (answer && field[i][j])
                    currentHint = "# ";
                if (i == 0) {
                    firstLine.append(String.format("%-2s", App.IntToAlphabet(j)));
                }
                hintOutput.append(currentHint);
            }
            hintOutput.append("\n");
        }
        hintOutput.insert(0, firstLine + "\n");
        return hintOutput.toString();
    }

    boolean isGameOver() {
        return mineExploded || mineToBeFound == 0;
    }

    private int checkSurrounding(int y, int x) {
        int count = 0;
        for (Candidate candidate : generateSurroundingCandidate(y, x)) {
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
        candidates.addLast(new Candidate(y + 1, x + 1));

        candidates.removeIf(candidate -> candidate.y >= field.length || candidate.x >= field[0].length ||
                candidate.y < 0 || candidate.x < 0);
        return candidates;
    }

    private static class Candidate {
        final int y;
        final int x;

        Candidate(int y, int x) {
            this.y = y;
            this.x = x;
        }

        @Override
        public int hashCode() {
            return y * 101 + x;
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
