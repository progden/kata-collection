package io.progden.gameoflife;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Game of Life 測試類別。
 * 測試命名格式: given_[場景]_when_[動作]_then_[預期結果]
 */
@DisplayName("Game of Life")
class GameTest {

    @Nested
    @DisplayName("建立與初始化")
    class Creation {

        @Test
        @DisplayName("建立 3x3 空白棋盤")
        void given_3x3網格_when_初始化空白_then_回傳正確狀態() {
            // Arrange
            Game game = new Game(3, 3);

            // Act
            game.init(".........");

            // Assert
            GameState state = game.getState();
            assertThat(state.width()).isEqualTo(3);
            assertThat(state.height()).isEqualTo(3);
            assertThat(state.state()).isEqualTo(".........");
        }

        @Test
        @DisplayName("board() 回傳格式化棋盤")
        void given_3x3網格_when_呼叫board_then_回傳含換行的棋盤() {
            // Arrange
            Game game = new Game(3, 3);
            game.init(".X." + ".X." + ".X.");

            // Act
            String board = game.board();

            // Assert
            assertThat(board).isEqualTo(".X.\n.X.\n.X.");
        }
    }

    @Nested
    @DisplayName("人口不足（Underpopulation）")
    class Underpopulation {

        @Test
        @DisplayName("0 個活鄰居 - 活細胞死亡")
        void given_孤獨的活細胞_when_tick_then_細胞死亡() {
            // Arrange
            Game game = new Game(3, 3);
            game.init(
                "..." +
                ".X." +
                "..."
            );

            // Act
            game.tick();

            // Assert
            assertThat(game.getState().state()).isEqualTo(
                "..." +
                "..." +
                "..."
            );
        }

        @Test
        @DisplayName("1 個活鄰居 - 活細胞死亡")
        void given_只有1個鄰居的活細胞_when_tick_then_細胞死亡() {
            // Arrange
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                ".X." +
                "..."
            );

            // Act
            game.tick();

            // Assert
            // 兩個細胞都只有 1 個鄰居，都會死亡
            assertThat(game.getState().state()).isEqualTo(
                "..." +
                "..." +
                "..."
            );
        }
    }

    @Nested
    @DisplayName("延續（Survival）")
    class Survival {

        @Test
        @DisplayName("2 個活鄰居 - 活細胞存活")
        void given_有2個鄰居的活細胞_when_tick_then_細胞存活() {
            // Arrange - Blinker 垂直狀態，中間細胞有 2 個鄰居
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                ".X." +
                ".X."
            );

            // Act
            game.tick();

            // Assert - 中間細胞存活
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }

        @Test
        @DisplayName("3 個活鄰居 - 活細胞存活")
        void given_有3個鄰居的活細胞_when_tick_then_細胞存活() {
            // Arrange - 中間細胞有 3 個鄰居
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                "XX." +
                ".X."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 存活
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }
    }

    @Nested
    @DisplayName("人口過剩（Overcrowding）")
    class Overcrowding {

        @Test
        @DisplayName("4 個活鄰居 - 活細胞死亡")
        void given_有4個鄰居的活細胞_when_tick_then_細胞死亡() {
            // Arrange - 中間細胞有 4 個鄰居（上下左右）
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                "XXX" +
                ".X."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }

        @Test
        @DisplayName("5 個活鄰居 - 活細胞死亡")
        void given_有5個鄰居的活細胞_when_tick_then_細胞死亡() {
            // Arrange - 中間細胞有 5 個鄰居
            Game game = new Game(3, 3);
            game.init(
                "XX." +
                "XXX" +
                ".X."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }
    }

    @Nested
    @DisplayName("繁殖（Reproduction）")
    class Reproduction {

        @Test
        @DisplayName("3 個活鄰居 - 死細胞復活")
        void given_有3個鄰居的死細胞_when_tick_then_細胞復活() {
            // Arrange - 中間死細胞有 3 個鄰居
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                "X.." +
                ".X."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 復活
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }

        @Test
        @DisplayName("2 個活鄰居 - 死細胞維持死亡")
        void given_有2個鄰居的死細胞_when_tick_then_細胞維持死亡() {
            // Arrange - 中間死細胞只有 2 個鄰居
            Game game = new Game(3, 3);
            game.init(
                ".X." +
                "X.." +
                "..."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 維持死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }

        @Test
        @DisplayName("4 個活鄰居 - 死細胞維持死亡")
        void given_有4個鄰居的死細胞_when_tick_then_細胞維持死亡() {
            // Arrange - 中間死細胞有 4 個鄰居
            Game game = new Game(3, 3);
            game.init(
                "XX." +
                "X.X" +
                "..."
            );

            // Act
            game.tick();

            // Assert - 中間細胞 (1,1) 維持死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }
    }

    @Nested
    @DisplayName("綜合測試")
    class Integration {

        @Test
        @DisplayName("Blinker 從垂直變水平")
        void given_垂直Blinker_when_tick_then_變成水平() {
            // Arrange - Blinker 垂直狀態
            Game game = new Game(5, 5);
            game.init(
                "....." +
                "..X.." +
                "..X.." +
                "..X.." +
                "....."
            );

            // Act
            game.tick();

            // Assert - 變成水平狀態
            assertThat(game.getState().state()).isEqualTo(
                "....." +
                "....." +
                ".XXX." +
                "....." +
                "....."
            );
        }

        @Test
        @DisplayName("Blinker 週期 2 回到原狀態")
        void given_垂直Blinker_when_tick兩次_then_回到原狀態() {
            // Arrange - Blinker 垂直狀態
            Game game = new Game(5, 5);
            String initial =
                "....." +
                "..X.." +
                "..X.." +
                "..X.." +
                ".....";
            game.init(initial);

            // Act
            game.tick();
            game.tick();

            // Assert - 回到原狀態
            assertThat(game.getState().state()).isEqualTo(initial);
        }
    }
}
