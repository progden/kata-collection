package io.progden.gameoflife;

/**
 * 遊戲狀態記錄。
 */
public record GameState(int width, int height, String state) {}
