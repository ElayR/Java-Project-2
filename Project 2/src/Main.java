import java.util.Scanner;
import java.util.Random;
public class Main {

    static int size;
    static int mines;
    static char[][] board;
    static boolean[][] minepositions;
    static boolean[][] bigreveal;

    public static void main(String[] args) {
        System.out.println("Minesweeper!");
        choosingDifficulty();
        placingMines();
        displayBoard();
        playGame();
    }

    public static void choosingDifficulty() {
        Scanner sc = new Scanner(System.in);
        //size is not being initialised :9 ?????????? ahhh

        System.out.println("Enter the difficulty: ");
        System.out.println("1) Easy (9 * 9 cells, 10 mines)");
        System.out.println("2) Medium (16 * 16 Cells and 40 Mines)");
        System.out.println("3) Hard (24 * 24 Cells and 99 Mines)");

        int n = sc.nextInt();

        if (n == 1) {
            size = 9;
            mines = 10;
        } else if (n == 2) {
            size = 16;
            mines = 40;
        } else if (n == 3) {
            size = 24;
            mines = 99;
        }
        // depending on the difficulty will place size on the things
        board = new char[size][size];
        minepositions = new boolean[size][size];
        bigreveal = new boolean[size][size];

        // making blank places
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = '.';
                bigreveal[i][j] = false;
            }
        }
    }

    public static void placingMines() {
        Random rand = new Random();
        int placedMines = 0;

        while (placedMines < mines) {
            int row = rand.nextInt(size);
            int col = rand.nextInt(size);

            // Place a mine if there isn't already one at this position
            if (!minepositions[row][col]) {
                minepositions[row][col] = true;
                board[row][col] = '*';
                placedMines++; // Count how many mines have been placed
            }
        }
    }

    public static void displayBoard() {
        System.out.println("Minesweeper!:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (bigreveal[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public static void playGame() {
        Scanner sc = new Scanner(System.in);
        boolean gameOver = false;

        while (!gameOver) {
            displayBoard();

            System.out.println("Enter a command (u for uncover, f for flag): ");
            System.out.println("Example: To uncover row 2, column 3, enter 'u 2 3'");
            System.out.println("To flag row 5, column 6, enter 'f 5 6'");

            String command = sc.nextLine();
            String[] parts = command.split(" ");

            if (parts.length != 3) {
                System.out.println("Invalid input type 'u *row* *col*' or 'f *row* *col*'.");
                continue;
            }

            char action = parts[0].charAt(0);
            int row = Integer.parseInt(parts[1]) - 1;
            int col = Integer.parseInt(parts[2]) - 1;

            if (row < 0 || row >= size || col < 0 || col >= size) {
                System.out.println("Invalid row or column.");
                continue;
            }

            if (action == 'u') {
                // Digging a cell
                if (minepositions[row][col]) {
                    System.out.println("BOOM!!! Say bye to breathing.");
                    System.out.println( "Game over");
                    //I want to make it a big size
                    gameOver = true;
                } else {
                    uncoverCell(row, col);
                    // Checking if all non mine cells have been dug
                    if (checkWin()) {
                        System.out.println("Congratulations! You are now officially a winner and my favourite user!");
                        gameOver = true;
                    }
                }

            } else if (action == 'f') {
                // Flag a cell
                if (!bigreveal[row][col]) {
                    System.out.println("Cell flagged at (" + (row + 1) + ", " + (col + 1) + ")");

                    } else {
                        System.out.println("You cannot flag an uncovered cell.");
                    }
                        } else {
                            System.out.println("Invalid action. Use 'u' to uncover or 'f' to flag.");
            }

        }
    }
    public static void uncoverCell(int row, int col) {
        if (bigreveal[row][col]) {
            return;
        }

        bigreveal[row][col] = true;
        int adjacentMines = countAdjacentMines(row, col);
        if (adjacentMines == 0) {
            board[row][col] = ' ';
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (row + i >= 0 && row + i < size && col + j >= 0 && col + j < size) {
                        uncoverCell(row + i, col + j);
                    }
                }
            }
        } else {
            board[row][col] = (char) (adjacentMines + '0'); // Show number of adjacent mines
        }
    }
    public static int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < size && c >= 0 && c < size && minepositions[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Checking if user won
    public static boolean checkWin() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!minepositions[i][j] && !bigreveal[i][j]) {
                    return false; // There is still a non mined cell!
                }
            }
        }
        return true; // Everything was revealed, good job!
    }
}