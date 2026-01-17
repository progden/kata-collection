package io.progden.gameoflife.web;

import io.progden.gameoflife.Game;
import io.progden.gameoflife.GameState;
import org.springframework.stereotype.Service;

/**
 * Game of Life 遊戲服務，管理當前遊戲實例。
 */
@Service
public class GameService {

    private Game currentGame;

    /**
     * 建立新遊戲。
     */
    public synchronized GameStateResponse createGame(CreateGameRequest request) {
        currentGame = new Game(request.width(), request.height());
        String state = request.state();
        if (state == null || state.isEmpty()) {
            state = ".".repeat(request.width() * request.height());
        }
        currentGame.init(state);
        return toResponse();
    }

    /**
     * 取得當前遊戲狀態。
     */
    public synchronized GameStateResponse getState() {
        if (currentGame == null) {
            throw new IllegalStateException("遊戲尚未建立");
        }
        return toResponse();
    }

    /**
     * 執行一代演化。
     */
    public synchronized GameStateResponse tick() {
        if (currentGame == null) {
            throw new IllegalStateException("遊戲尚未建立");
        }
        currentGame.tick();
        return toResponse();
    }

    private GameStateResponse toResponse() {
        GameState state = currentGame.getState();
        return new GameStateResponse(
            state.width(),
            state.height(),
            state.state(),
            currentGame.board()
        );
    }
}
