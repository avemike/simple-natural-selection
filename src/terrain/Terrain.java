package terrain;

import interfaces.Paintable;
import services.Config;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Terrain implements Paintable {
    private final int field_size = Integer.parseInt(Config.get("terrain_field_size"));
    private final int height = Integer.parseInt(Config.get("terrain_map_height"));
    private final int width = Integer.parseInt(Config.get("terrain_map_width"));
    private char[][] board;

    public Terrain() {
        board = new char[height][width];
        load();
    }

    @Override
    public void paint(Graphics graphics) {
        final char water_symbol = Config.get("water").charAt(0);
        final char ground_symbol = Config.get("ground").charAt(0);

        for (int row = 0; row < board.length; row++)
            for (int column = 0; column < board[row].length; column++) {

                // 0. pick color
                var color = Color.black;
                if (board[row][column] == water_symbol)
                    color = Color.blue;
                else if (board[row][column] == ground_symbol)
                    color = Color.green;
                else
                    System.err.println("Unknown terrain symbol - " + board[row][column]);

                // 1. draw component
                graphics.setColor(color);
                graphics.fillRect(column * field_size, row * field_size, field_size, field_size);

                // 2. set color to black
                graphics.setColor(Color.black);
            }
    }

    /**
     * Parse terrain map to `board` array from file specified in `terrain.properties` as `terrain_path`
     */
    private void load() {
        String path = Config.assets_path + "/" + Config.get("terrain_path");

        try {
            File obj = new File(path);
            Scanner reader = new Scanner(obj);

            StringBuilder data = new StringBuilder();
            while (reader.hasNextLine()) {
                data.append(reader.nextLine());
            }

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
