package io.progden.fizzbuzz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FizzBuzz 測試類別。
 * <p>
 * 測試命名格式: given_[場景]_when_[動作]_then_[預期結果]
 */
@DisplayName("FizzBuzz")
class FizzBuzzTest {

    @Nested
    @DisplayName("基本功能")
    class BasicFeatures {

        @Test
        @DisplayName("範例測試 - 請修改或刪除")
        void given_初始狀態_when_執行操作_then_預期結果() {
            // Arrange
            // TODO: 準備測試資料

            // Act
            // TODO: 執行待測方法
            var result = true;

            // Assert
            assertThat(result).isTrue();
        }
    }
}
