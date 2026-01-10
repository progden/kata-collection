package io.progden.christmaslights;

public interface LightGrid {
    void open(int x1, int y1, int x2, int y2);

    void close(int x1, int y1, int x2, int y2);

    void toggle(int x1, int y1, int x2, int y2);

    int light();
}
