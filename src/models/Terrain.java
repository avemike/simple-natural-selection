package models;

import services.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Terrain {
    private int initial_resize = 10;
    private char[][] board;

    public Terrain(String path) {
        load(System.getProperty("user.dir") + "/" + path);
    }

    private void load(String path) {
        try {
            File obj = new File(path);
            Scanner reader = new Scanner(obj);

            StringBuilder data = new StringBuilder();
            while (reader.hasNextLine()) {
                data.append(reader.nextLine());
            }

            int height = Integer.parseInt(Config.get("terrain_height"));
            int width = Integer.parseInt(Config.get("terrain_width"));

            board = new char[height][width];

            String tmp_board = data.toString();
            for (int row = 0; row < height; row++)
                for (int col = 0; col < width; col++)
                    board[row][col] = tmp_board.charAt(row * width + col);

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
