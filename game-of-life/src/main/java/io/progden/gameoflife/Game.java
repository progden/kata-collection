package io.progden.gameoflife;

/**
 * Game of Life 遊戲類別。
 */
public class Game {

    private final int width;
    private final int height;
    private String state;

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init(String state) {
        this.state = state;
    }

    public GameState getState() {
        return new GameState(width, height, state);
    }

    public String board() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(state.substring(i * width, (i + 1) * width));
        }
        return sb.toString();
    }

    public void tick() {
        StringBuilder nextState = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int neighbors = countNeighbors(x, y);
                boolean alive = isAlive(x, y);

                if (alive && neighbors < 2) {
                    // 人口不足：活細胞少於 2 個鄰居，死亡
                    nextState.append('.');
                } else if (alive && neighbors > 3) {
                    // 人口過剩：活細胞超過 3 個鄰居，死亡
                    nextState.append('.');
                } else if (alive) {
                    // 延續：活細胞有 2-3 個鄰居，存活
                    nextState.append('X');
                } else if (neighbors == 3) {
                    // 繁殖：死細胞恰好有 3 個鄰居，復活
                    nextState.append('X');
                } else {
                    nextState.append('.');
                }
            }
        }
        state = nextState.toString();
    }

    private boolean isAlive(int x, int y) {
        int index = y * width + x;
        return state.charAt(index) == 'X';
    }

    private int countNeighbors(int x, int y) {
        int count = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (isAlive(nx, ny)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
