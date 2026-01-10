package io.progden.christmaslights;

public class LightGridVer2 implements LightGrid {
    int width = 1000, height = 1000;
    int[][] grid = new int[width][height];

    @Override
    public void open(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                grid[x][y] += 1;
            }
        }
    }

    @Override
    public void close(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if (grid[x][y] > 0) {
                    grid[x][y] -= 1;
                }
            }
        }
    }

    @Override
    public void toggle(int x1, int y1, int x2, int y2) {
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                grid[x][y] += 2;
            }
        }
    }

    @Override
    public int light() {
        int totalBrightness = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                totalBrightness += grid[x][y];
            }
        }
        return totalBrightness;
    }
}
