package io.progden.gameoflife.web;

/**
 * 建立遊戲請求 DTO。
 *
 * @param width  網格寬度
 * @param height 網格高度
 * @param state  初始狀態（可選，若為空則產生全死狀態）
 */
public record CreateGameRequest(
    int width,
    int height,
    String state
) {}
