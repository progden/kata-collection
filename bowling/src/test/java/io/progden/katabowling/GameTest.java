package io.progden.katabowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Nested
    @DisplayName("Level 1: 基本計分")
    class BasicScoring {

        @Test
        @DisplayName("基本計分 - Given 20次投球全部洗溝 - When 計算分數 - Then 總分為0")
        void gutterGame() {
            rollTimes(0, 20);
            scoreShouldBe(0);
        }

        @Test
        @DisplayName("基本計分 - Given 20次投球每次打1瓶 - When 計算分數 - Then 總分為20")
        void allOnes() {
            rollTimes(1, 20);
            scoreShouldBe(20);
        }

        @Test
        @DisplayName("基本計分 - Given 20次投球每次打2瓶 - When 計算分數 - Then 總分為40")
        void allTwos() {
            rollTimes(2, 20);
            scoreShouldBe(40);
        }

        @Test
        @DisplayName("基本計分 - Given 每局投球3和4瓶 - When 計算分數 - Then 總分為70")
        void mixedRolls() {
            for (int i = 0; i < 10; i++) {
                game.roll(3);
                game.roll(4);
            }
            scoreShouldBe(70);
        }
    }

    @Nested
    @DisplayName("Level 2: Spare 規則")
    class SpareRules {

        @Test
        @DisplayName("Spare規則 - Given 第一局Spare後投3瓶其餘0 - When 計算分數 - Then 總分為16")
        void oneSpareAtFirstFrame() {
            // spare
            game.roll(3);
            game.roll(7);
            // next roll 3
            game.roll(3);
            scoreShouldBe(16);
        }

        @Test
        @DisplayName("Spare規則 - Given 第三局Spare後投3瓶其餘0 - When 計算分數 - Then 總分為16")
        void oneSpareAtMiddleFrame() {
            rollTimes(0, 4);
            // 3rd frame spare
            game.roll(3);
            game.roll(7);
            // next roll 3
            game.roll(3);
            scoreShouldBe(16);
        }

        @Test
        @DisplayName("Spare規則 - Given 連續兩局Spare後投3瓶其餘0 - When 計算分數 - Then 總分為31")
        void twoConsecutiveSpares() {
            // 1st frame spare
            game.roll(7);
            game.roll(3);
            // 2nd frame spare
            game.roll(3);
            game.roll(7);
            // next roll 3
            game.roll(3);
            scoreShouldBe(10 + 3 + 10 + 3 + 3);
        }

        @Test
        @DisplayName("Spare規則 - Given 每局都是5加5的Spare最後補5 - When 計算分數 - Then 總分為150")
        void allSpares() {
            rollTimes(5, 21);
            scoreShouldBe(150);
        }

        @Test
        @DisplayName("Spare規則 - Given 第一局7加3的Spare後投4瓶其餘0 - When 計算分數 - Then 總分為18")
        void spareDifferentCombination() {
            game.roll(7);
            game.roll(3);
            game.roll(4);
            scoreShouldBe(18);
        }

        @Test
        @DisplayName("Spare規則 - Given 第一局Spare後投0瓶 - When 計算分數 - Then 總分為10")
        void spareFollowedByZero() {
            game.roll(6);
            game.roll(4);
            game.roll(0);
            scoreShouldBe(10);
        }

    }

    @Nested
    @DisplayName("Level 3: Strike 規則")
    class StrikeRules {

        @Test
        @DisplayName("Strike規則 - Given 第一局Strike後投3和4瓶其餘0 - When 計算分數 - Then 總分為24")
        void oneStrike() {
            game.roll(10);
            game.roll(3);
            game.roll(4);
            scoreShouldBe(24);
        }

        @Test
        @DisplayName("Strike規則 - Given 第一局Strike後投0和0瓶 - When 計算分數 - Then 總分為10")
        void strikeFollowedByZeros() {
            game.roll(10);
            game.roll(0);
            game.roll(0);
            scoreShouldBe(10);
        }

        @Test
        @DisplayName("Strike規則 - Given 連續兩局Strike後投3和4瓶其餘0 - When 計算分數 - Then 總分為47")
        void twoConsecutiveStrikes() {
            game.roll(10);
            game.roll(10);
            game.roll(3);
            game.roll(4);
            scoreShouldBe(47);
        }

        @Test
        @DisplayName("Strike規則 - Given 連續三局Strike後投3和4瓶其餘0 - When 計算分數 - Then 總分為77")
        void threeConsecutiveStrikes() {
            game.roll(10);
            game.roll(10);
            game.roll(10);
            game.roll(3);
            game.roll(4);
            scoreShouldBe(77);
        }

        @Test
        @DisplayName("Strike規則 - Given Strike後接Spare再投3瓶其餘0 - When 計算分數 - Then 總分為36")
        void strikeFollowedBySpare() {
            game.roll(10);
            game.roll(5);
            game.roll(5);
            game.roll(3);
            scoreShouldBe(36);
        }

    }

    @Nested
    @DisplayName("Level 4: 第十局特殊規則")
    class TenthFrameRules {

        @Test
        @DisplayName("第十局規則 - Given 前九局0分第十局Spare補投5瓶 - When 計算分數 - Then 總分為15")
        void tenthFrameSpare() {
            rollTimes(0, 18);
            game.roll(7);
            game.roll(3);
            game.roll(5);
            scoreShouldBe(15);
        }

        @Test
        @DisplayName("第十局規則 - Given 前九局0分第十局Strike補投3和4瓶 - When 計算分數 - Then 總分為17")
        void tenthFrameStrike() {
            rollTimes(0, 18);
            game.roll(10);
            game.roll(3);
            game.roll(4);
            scoreShouldBe(17);
        }

        @Test
        @DisplayName("第十局規則 - Given 前九局0分第十局兩個Strike補投5瓶 - When 計算分數 - Then 總分為25")
        void tenthFrameTwoStrikes() {
            rollTimes(0, 18);
            game.roll(10);
            game.roll(10);
            game.roll(5);
            scoreShouldBe(25);
        }

        @Test
        @DisplayName("第十局規則 - Given 前九局0分第十局三個Strike - When 計算分數 - Then 總分為30")
        void tenthFrameThreeStrikes() {
            rollTimes(0, 18);
            game.roll(10);
            game.roll(10);
            game.roll(10);
            scoreShouldBe(30);
        }

    }

    @Nested
    @DisplayName("Level 5: 完整遊戲驗證")
    class FullGameVerification {

        @Test
        @DisplayName("完整遊戲 - Given 12次全部Strike - When 計算分數 - Then 總分為300")
        void perfectGame() {
            rollTimes(10, 12);
            scoreShouldBe(300);
        }

        @Test
        @DisplayName("完整遊戲 - Given 21次全部投5瓶都是Spare - When 計算分數 - Then 總分為150")
        void allFiveSpares() {
            rollTimes(5, 21);
            scoreShouldBe(150);
        }

    }

    private void scoreShouldBe(int expected) {
        assertThat(game.score()).isEqualTo(expected);
    }

    private void rollTimes(int pins, int times) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }
}
