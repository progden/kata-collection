package io.progden.gameoflife.web;

import org.springframework.web.bind.annotation.*;

/**
 * Game of Life REST 控制器。
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * 建立新遊戲。
     * POST /api/game
     */
    @PostMapping
    public GameStateResponse createGame(@RequestBody CreateGameRequest request) {
        return gameService.createGame(request);
    }

    /**
     * 取得當前遊戲狀態。
     * GET /api/game
     */
    @GetMapping
    public GameStateResponse getState() {
        return gameService.getState();
    }

    /**
     * 執行一代演化。
     * POST /api/game/tick
     */
    @PostMapping("/tick")
    public GameStateResponse tick() {
        return gameService.tick();
    }
}
