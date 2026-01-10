package io.progden.christmaslights;

public class LightGrid {
    int width = 1000, height = 1000;
    boolean[][] grid = new boolean[width][height];

    void open(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                grid[x][y] = true;
            }
        }
    }

    void close(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                grid[x][y] = false;
            }
        }
    }

    void toggle(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                grid[x][y] = !grid[x][y];
            }
        }
    }

    public int light() {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y]) {
                    count++;
                }
            }
        }
        return count;
    }
}
