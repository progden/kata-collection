package io.progden.fizzbuzz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FizzBuzz 測試類別。
 * <p>
 * 測試命名格式: given_[場景]_when_[動作]_then_[預期結果]
 */
@DisplayName("FizzBuzzFreeze")
class FizzBuzzFreezeTest {
    FizzBuzz fizzBuzz;

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzzFreeze();
    }

    @Nested
    @DisplayName("RULE 1: 不是 3 或 5 的倍數，應該直接顯示數字本身")
    class BasicFeatures {
        @ParameterizedTest()
        @ValueSource(ints = {1, 2, 4, 7})
        @DisplayName("given_{0}_when_playFizzBuzz_then_return_{0}")
        void test(int input) {
            String result = fizzBuzz.fizzBuzz(input);
            assertThat(result).isEqualTo("" + input);
        }
    }

    @Nested
    @DisplayName("RULE 2: 3 的倍數，應該直接顯示Kuksa!")
    class MultipleOfThree {
        @ParameterizedTest()
        @ValueSource(ints = {3, 6, 9, 12})
        @DisplayName("given_{0}_when_playFizzBuzz_then_return_Kuksa!")
        void test(int input) {
            String result = fizzBuzz.fizzBuzz(input);
            assertThat(result).isEqualTo("Kuksa!");
        }
    }

    @Nested
    @DisplayName("RULE 3: 5 的倍數，應該直接顯示Sauna!")
    class MultipleOfFive {
        @ParameterizedTest()
        @ValueSource(ints = {5, 10, 20, 25})
        @DisplayName("given_{0}_when_playFizzBuzz_then_return_Sauna!")
        void test(int input) {
            String result = fizzBuzz.fizzBuzz(input);
            assertThat(result).isEqualTo("Sauna!");
        }
    }

    @Nested
    @DisplayName("同時是 3 和 5 的倍數，應該直接顯示Dip!")
    class MultipleOfThreeAndFive {
        @ParameterizedTest()
        @ValueSource(ints = {15, 30, 45, 60})
        @DisplayName("given_{0}_when_playFizzBuzz_then_return_Dip!")
        void test(int input) {
            String result = fizzBuzz.fizzBuzz(input);
            assertThat(result).isEqualTo("Dip!");
        }
    }
}
