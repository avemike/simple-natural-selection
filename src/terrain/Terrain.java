package terrain;

import interfaces.Paintable;
import services.Config;
import utils.Position;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Terrain implements Paintable {
    private final int field_size = Integer.parseInt(Config.get("terrain_field_size"));
    private final int map_height = Integer.parseInt(Config.get("terrain_map_height"));
    private final int map_width = Integer.parseInt(Config.get("terrain_map_width"));
    private final double width = map_width * field_size;
    private final double height = map_height * field_size;
    private final char water_symbol = Config.get("water").charAt(0);
    private final char ground_symbol = Config.get("ground").charAt(0);

    private char[][] board;

    public Terrain() {
        board = new char[map_height][map_width];

        load();
    }

    @Override
    public void paintComponent(Graphics2D graphics) {
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

    public char getField(int field_x, int field_y) {
        if (field_x < 0) field_x = 0;
        if (field_y < 0) field_y = 0;
        if (field_x >= map_width) field_x = map_width - 1;
        if (field_y >= map_height) field_y = map_height - 1;

        return board[field_y][field_x];
    }

    public boolean isCollidingWithWater(final Position pos, final double collision_range) {
        // 0. check most direct position
        int field_x = ((int) pos.x) / field_size;
        int field_y = ((int) pos.y) / field_size;

        if (getField(field_x, field_y) == water_symbol) return true;

        // 1. check 4 nearby positions
        // 1.1 left
        var field = ((int) (pos.x - collision_range)) / field_size;
        if (field != field_x) {
            if (getField(field, field_y) == water_symbol) return true;
        }
        // 1.2 right
        field = ((int) (pos.x + collision_range)) / field_size;
        if (field != field_x) {
            if (getField(field, field_y) == water_symbol) return true;
        }
        // 1.3 up
        field = ((int) (pos.y - collision_range)) / field_size;
        if (field != field_y) {
            if (getField(field_x, field) == water_symbol) return true;
        }
        // 1.4 down
        field = ((int) (pos.y + collision_range)) / field_size;
        if (field != field_y) {
            if (getField(field_x, field) == water_symbol) return true;
        }

        return false;
    }


    public Position searchForWater(final Position src, final double range) {
        int field_x = ((int) src.x) / field_size;
        int field_y = ((int) src.y) / field_size;
        int field_range = (int) (range / field_size);

        // from n to n+1 gives n+1
        if (((double) field_range * field_size) < range) field_range++;

        return new Position(0, 0);
        // @todo: finish implementation
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

            board = new char[map_height][map_width];

            String tmp_board = data.toString();
            for (int row = 0; row < map_height; row++)
                for (int col = 0; col < map_width; col++)
                    board[row][col] = tmp_board.charAt(row * map_width + col);

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
