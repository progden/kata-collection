package io.progden.gameoflife.web;

/**
 * 遊戲狀態回應 DTO。
 *
 * @param width  網格寬度
 * @param height 網格高度
 * @param state  扁平狀態字串（如 "..X.X..."）
 * @param board  格式化棋盤（含換行）
 */
public record GameStateResponse(
    int width,
    int height,
    String state,
    String board
) {}
