package io.progden.gameoflife;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;

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

        private final String[] EMPTY_3X3 = {
                "...",
                "...",
                "..."
        };
        private final String[] BLINKER_VERTICAL = {
                ".X.",
                ".X.",
                ".X."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(3, 3);
        }

        @Test
        @DisplayName("建立 3x3 空白棋盤")
        void given_3x3網格_when_初始化空白_then_回傳正確狀態() {
            // Arrange & Act
            game.init(joinString(EMPTY_3X3));

            // Assert
            assertThat(game.getState().width()).isEqualTo(3);
            assertThat(game.getState().height()).isEqualTo(3);
            assertThat(game.getState().state()).isEqualTo(joinString(EMPTY_3X3));
        }

        @Test
        @DisplayName("board() 回傳格式化棋盤")
        void given_3x3網格_when_呼叫board_then_回傳含換行的棋盤() {
            // Arrange
            game.init(joinString(BLINKER_VERTICAL));

            // Act
            String board = game.board();

            // Assert
            assertThat(board).isEqualTo(joinStringNewLine(BLINKER_VERTICAL));
        }
    }

    @NonNull
    private static String joinString(String formatedMapString) {
        return formatedMapString.replaceAll("\n", "");
    }

    @NonNull
    private static String joinString(String... stringArray) {
        // join stringArrayh
        return Arrays.stream(stringArray).collect(Collectors.joining());
    }

    @NonNull
    private static String joinStringNewLine(String... stringArray) {
        // join stringArrayh
        return Arrays.stream(stringArray).collect(Collectors.joining("\n"));
    }

    @Test
    public void testCreateMap() {
        String map = joinString(
                "...",
                ".X.",
                "X.."
        );
        assertThat(map).isEqualTo("....X.X..");
    }

    @Test
    public void testCreateMap2() {
        String map = joinStringNewLine(
                "...",
                ".X.",
                "X.."
        );
        assertThat(map).isEqualTo("...\n.X.\nX..");
    }

    @Nested
    @DisplayName("人口不足（Underpopulation）")
    class Underpopulation {

        private final String[] ZERO_NEIGHBOR = {
                "...",
                ".X.",
                "..."
        };
        private final String[] ONE_NEIGHBOR = {
                ".X.",
                ".X.",
                "..."
        };
        private final String[] EMPTY_BOARD = {
                "...",
                "...",
                "..."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(3, 3);
        }

        @Test
        @DisplayName("0 個活鄰居 - 活細胞死亡")
        void given_孤獨的活細胞_when_tick_then_細胞死亡() {
            // Arrange
            game.init(joinString(ZERO_NEIGHBOR));

            // Act
            game.tick();

            // Assert
            assertThat(game.getState().state()).isEqualTo(joinString(EMPTY_BOARD));
        }

        @Test
        @DisplayName("1 個活鄰居 - 活細胞死亡")
        void given_只有1個鄰居的活細胞_when_tick_then_細胞死亡() {
            // Arrange
            game.init(joinString(ONE_NEIGHBOR));

            // Act
            game.tick();

            // Assert
            // 兩個細胞都只有 1 個鄰居，都會死亡
            assertThat(game.getState().state()).isEqualTo(joinString(EMPTY_BOARD));
        }
    }

    @Nested
    @DisplayName("延續（Survival）")
    class Survival {

        private String[] TWO_ALIVE_NEIGHBOR = new String[]{
                ".X.",
                ".X.",
                ".X."
        };
        private String[] THREE_ALIVE_NEIGHBOR = new String[]{
                ".X.",
                "XX.",
                ".X."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(3, 3);
        }

        @Test
        @DisplayName("2 個活鄰居 - 活細胞存活")
        void given_有2個鄰居的活細胞_when_tick_then_細胞存活() {
            // Arrange - Blinker 垂直狀態，中間細胞有 2 個鄰居
            game.init(joinString(TWO_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞存活
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }

        @Test
        @DisplayName("3 個活鄰居 - 活細胞存活")
        void given_有3個鄰居的活細胞_when_tick_then_細胞存活() {
            // Arrange - 中間細胞有 3 個鄰居
            game.init(joinString(THREE_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 存活
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }
    }

    @Nested
    @DisplayName("人口過剩（Overcrowding）")
    class Overcrowding {
        private final String[] FOUR_ALIVE_NEIGHBOR = new String[]{
                ".X.",
                "XXX",
                ".X."
        };
        private final String[] FIVE_ALIVE_NEIGHBOR = new String[]{
                "XX.",
                "XXX",
                ".X."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(3, 3);
        }

        @Test
        @DisplayName("4 個活鄰居 - 活細胞死亡")
        void given_有4個鄰居的活細胞_when_tick_then_細胞死亡() {
            game.init(joinString(FOUR_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }

        @Test
        @DisplayName("5 個活鄰居 - 活細胞死亡")
        void given_有5個鄰居的活細胞_when_tick_then_細胞死亡() {
            // Arrange - 中間細胞有 5 個鄰居
            game.init(joinString(FIVE_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 死亡
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }
    }

    @Nested
    @DisplayName("繁殖（Reproduction）")
    class Reproduction {

        private final String[] THREE_ALIVE_NEIGHBOR = new String[]{
                ".X.",
                "X..",
                ".X."
        };
        private final String[] TWO_ALIVE_NEIGHBOR = new String[]{
                ".X.",
                "X..",
                "..."
        };
        private final String[] FOUR_ALIVE_NEIGHBOR = new String[]{
                "XX.",
                "X.X",
                "..."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(3, 3);
        }

        @Test
        @DisplayName("3 個活鄰居 - 死細胞復活")
        void given_有3個鄰居的死細胞_when_tick_then_細胞復活() {
            // Arrange - 中間死細胞有 3 個鄰居
            game.init(joinString(THREE_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 復活
            shouldBeAlive();
        }

        @Test
        @DisplayName("2 個活鄰居 - 死細胞維持死亡")
        void given_有2個鄰居的死細胞_when_tick_then_細胞維持死亡() {
            // Arrange - 中間死細胞只有 2 個鄰居
            game.init(joinString(TWO_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 維持死亡
            shouldBeKeepDead();
        }

        @Test
        @DisplayName("4 個活鄰居 - 死細胞維持死亡")
        void given_有4個鄰居的死細胞_when_tick_then_細胞維持死亡() {
            // Arrange - 中間死細胞有 4 個鄰居
            game.init(joinString(FOUR_ALIVE_NEIGHBOR));
            // Act
            game.tick();
            // Assert - 中間細胞 (1,1) 維持死亡
            shouldBeKeepDead();
        }

        private void shouldBeAlive() {
            assertThat(game.getState().state().charAt(4)).isEqualTo('X');
        }

        private void shouldBeKeepDead() {
            assertThat(game.getState().state().charAt(4)).isEqualTo('.');
        }
    }

    @Nested
    @DisplayName("綜合測試")
    class Integration {

        private final String[] BLINKER_VERTICAL = {
                ".....",
                "..X..",
                "..X..",
                "..X..",
                "....."
        };
        private final String[] BLINKER_HORIZONTAL = {
                ".....",
                ".....",
                ".XXX.",
                ".....",
                "....."
        };
        private Game game;

        @BeforeEach
        void setUp() {
            game = new Game(5, 5);
        }

        @Test
        @DisplayName("Blinker 從垂直變水平")
        void given_垂直Blinker_when_tick_then_變成水平() {
            // Arrange
            game.init(joinString(BLINKER_VERTICAL));

            // Act
            game.tick();

            // Assert
            assertThat(game.getState().state()).isEqualTo(joinString(BLINKER_HORIZONTAL));
        }

        @Test
        @DisplayName("Blinker 週期 2 回到原狀態")
        void given_垂直Blinker_when_tick兩次_then_回到原狀態() {
            // Arrange
            game.init(joinString(BLINKER_VERTICAL));

            // Act
            game.tick();
            game.tick();

            // Assert
            assertThat(game.getState().state()).isEqualTo(joinString(BLINKER_VERTICAL));
        }
    }
}
