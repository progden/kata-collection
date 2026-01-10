package io.progden.christmaslights;

public class LightGridCounter implements LightGrid {

    private int openCount;

    private int closeCount;
    private int toggleCount;

    @Override
    public void open(int x1, int y1, int x2, int y2) {
        this.openCount++;
    }

    @Override
    public void close(int x1, int y1, int x2, int y2) {
        this.closeCount++;
    }

    @Override
    public void toggle(int x1, int y1, int x2, int y2) {
        this.toggleCount++;
    }

    @Override
    public int light() {
        return 0;
    }

    public int getOpenCount() {
        return openCount;
    }

    public int getCloseCount() {
        return closeCount;
    }

    public int getToggleCount() {
        return toggleCount;
    }
}
