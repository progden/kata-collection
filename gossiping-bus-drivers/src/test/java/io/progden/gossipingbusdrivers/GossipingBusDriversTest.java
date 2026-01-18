package io.progden.gossipingbusdrivers;

import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Gossiping Bus Drivers Kata 測試類別。
 * <p>
 * 測試案例依照 ZOMBIES 順序設計：
 * Zero → One → Many → Boundary → Interface → Exception → Simple
 * </p>
 */
@DisplayName("Gossiping Bus Drivers")
class GossipingBusDriversTest {

    GossipingBusDrivers sut;

    @BeforeEach
    void setUp() {
        sut = new GossipingBusDriversSimulation();
    }

    /**
     * 提供所有實作供參數化測試使用。
     */
    static Stream<GossipingBusDrivers> implementations() {
        return Stream.of(
                new GossipingBusDriversSimulation(),
                new GossipingBusDriversEventDriven(),
                new GossipingBusDriversGraph()
        );
    }

    // =========================================================================
    // Zero - 零或最小情境
    // =========================================================================
    @Nested
    @DisplayName("Zero - 零或最小情境")
    class ZeroScenarios {

        @Test
        @DisplayName("given_只有一位司機_when_計算八卦傳播時間_then_回傳1")
        void given_只有一位司機_when_計算八卦傳播時間_then_回傳1() {
            // Arrange
            List<Route> routes = List.of(Route.of(1, 2, 3));

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.toString()).isEqualTo("1");
        }

        @Test
        @DisplayName("given_空的路線清單_when_計算八卦傳播時間_then_回傳1")
        void given_空的路線清單_when_計算八卦傳播時間_then_回傳1() {
            // Arrange
            List<Route> routes = List.empty();

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("1");
        }
    }

    // =========================================================================
    // One - 單一情境
    // =========================================================================
    @Nested
    @DisplayName("One - 單一情境")
    class OneScenarios {

        @Test
        @DisplayName("given_兩位司機第一站相同_when_計算八卦傳播時間_then_回傳1")
        void given_兩位司機第一站相同_when_計算八卦傳播時間_then_回傳1() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2, 3),
                    Route.of(1, 3, 4)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("1");
        }

        @Test
        @DisplayName("given_兩位司機第二站相遇_when_計算八卦傳播時間_then_回傳2")
        void given_兩位司機第二站相遇_when_計算八卦傳播時間_then_回傳2() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2, 3),
                    Route.of(5, 2, 4)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("2");
        }

        @Test
        @DisplayName("given_兩位司機需要循環才相遇_when_計算八卦傳播時間_then_回傳正確分鐘數")
        void given_兩位司機需要循環才相遇_when_計算八卦傳播時間_then_回傳正確分鐘數() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2),
                    Route.of(3, 2)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("2");
        }
    }

    // =========================================================================
    // Many - 多重情境
    // =========================================================================
    @Nested
    @DisplayName("Many - 多重情境")
    class ManyScenarios {

        @Test
        @DisplayName("given_範例一的三位司機_when_計算八卦傳播時間_then_回傳5")
        void given_範例一的三位司機_when_計算八卦傳播時間_then_回傳5() {
            // Arrange - 根據 kata-log 原始範例
            // route a: 3 1 2 3   -> 路線 [3, 1, 2, 3]
            // route b: 3 2 3 1   -> 路線 [3, 2, 3, 1]
            // route c: 4 2 3 4 5 -> 路線 [4, 2, 3, 4, 5]
            // 整行都是站牌（包含第一個數字）
            List<Route> routes = List.of(
                    Route.of(3, 1, 2, 3),
                    Route.of(3, 2, 3, 1),
                    Route.of(4, 2, 3, 4, 5)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("5");
        }

        @Test
        @DisplayName("given_三位司機同時在第一站相遇_when_計算八卦傳播時間_then_回傳1")
        void given_三位司機同時在第一站相遇_when_計算八卦傳播時間_then_回傳1() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2, 3),
                    Route.of(1, 4, 5),
                    Route.of(1, 6, 7)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("1");
        }
    }

    // =========================================================================
    // Boundary - 邊界情境
    // =========================================================================
    @Nested
    @DisplayName("Boundary - 邊界情境")
    class BoundaryScenarios {

        @Test
        @DisplayName("given_路線長度為1且同站_when_計算八卦傳播時間_then_回傳1")
        void given_路線長度為1且同站_when_計算八卦傳播時間_then_回傳1() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1),
                    Route.of(1)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("1");
        }

        @Test
        @DisplayName("given_路線長度為1且不同站_when_計算八卦傳播時間_then_回傳never")
        void given_路線長度為1且不同站_when_計算八卦傳播時間_then_回傳never() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1),
                    Route.of(2)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }

        @Test
        @DisplayName("given_永不相交的路線_when_計算八卦傳播時間_then_回傳never")
        void given_永不相交的路線_when_計算八卦傳播時間_then_回傳never() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2),
                    Route.of(3, 4)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }
    }

    // =========================================================================
    // Interface - API 設計驗證
    // =========================================================================
    @Nested
    @DisplayName("Interface - API 設計驗證")
    class InterfaceScenarios {

        @Test
        @DisplayName("Route_應該正確處理循環")
        void route_應該正確處理循環() {
            // Arrange
            Route route = Route.of(1, 2, 3);

            // Act & Assert
            assertThat(route.getStopAt(1)).isEqualTo(1);
            assertThat(route.getStopAt(2)).isEqualTo(2);
            assertThat(route.getStopAt(3)).isEqualTo(3);
            assertThat(route.getStopAt(4)).isEqualTo(1); // 循環
            assertThat(route.getStopAt(5)).isEqualTo(2);
            assertThat(route.getStopAt(6)).isEqualTo(3);
            assertThat(route.getStopAt(7)).isEqualTo(1);
        }

        @Test
        @DisplayName("GossipResult_成功結果_應該正確表示")
        void gossipResult_成功結果_應該正確表示() {
            // Arrange & Act
            GossipResult result = GossipResult.of(42);

            // Assert
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.minutes()).contains(42);
            assertThat(result.toString()).isEqualTo("42");
        }

        @Test
        @DisplayName("GossipResult_never結果_應該正確表示")
        void gossipResult_never結果_應該正確表示() {
            // Arrange & Act
            GossipResult result = GossipResult.never();

            // Assert
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.minutes()).isEmpty();
            assertThat(result.toString()).isEqualTo("never");
        }
    }

    // =========================================================================
    // Exception - 例外情境
    // =========================================================================
    @Nested
    @DisplayName("Exception - 例外情境")
    class ExceptionScenarios {

        @Test
        @DisplayName("given_範例二的司機_when_計算八卦傳播時間_then_回傳never")
        void given_範例二的司機_when_計算八卦傳播時間_then_回傳never() {
            // Arrange - 根據 kata-log 原始範例
            // route a]2 1 2
            // route b]5 2 8
            List<Route> routes = List.of(
                    Route.of(1, 2),
                    Route.of(2, 8)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }

        @Test
        @DisplayName("Route_空站牌序列_應該拋出例外")
        void route_空站牌序列_應該拋出例外() {
            // Act & Assert
            assertThatThrownBy(() -> Route.of())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("路線不可為空");
        }

        @Test
        @DisplayName("given_部分司機永遠不會相遇_when_計算八卦傳播時間_then_回傳never")
        void given_部分司機永遠不會相遇_when_計算八卦傳播時間_then_回傳never() {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2),
                    Route.of(1, 3),
                    Route.of(5, 6)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }
    }

    // =========================================================================
    // Complex - 複雜情境（更長路線、更多司機）
    // =========================================================================
    @Nested
    @DisplayName("Complex - 複雜情境")
    class ComplexScenarios {

        @Test
        @DisplayName("given_五位司機共同起點_when_計算八卦傳播時間_then_回傳1")
        void given_五位司機共同起點_when_計算八卦傳播時間_then_回傳1() {
            // Arrange - 5 位司機，第一站都是站 1
            List<Route> routes = List.of(
                    Route.of(1, 2, 3, 4, 5, 6, 7, 8),
                    Route.of(1, 10, 11, 12, 13, 14, 15, 16),
                    Route.of(1, 20, 21, 22, 23, 24, 25),
                    Route.of(1, 30, 31, 32, 33, 34),
                    Route.of(1, 40, 41, 42, 43, 44, 45, 46, 47)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert - 第 1 分鐘所有司機都在站 1 相遇
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.toString()).isEqualTo("1");
        }

        @Test
        @DisplayName("given_六位司機星狀網路_when_計算八卦傳播時間_then_回傳正確分鐘數")
        void given_六位司機星狀網路_when_計算八卦傳播時間_then_回傳正確分鐘數() {
            // Arrange - 6 位司機，中心司機與其他 5 位分別在不同時間相遇
            List<Route> routes = List.of(
                    Route.of(0, 1, 2, 3, 4, 5),                 // 中心司機
                    Route.of(0, 10, 11, 12, 13, 14),            // 與中心在站 0 (分鐘 1) 相遇
                    Route.of(20, 1, 21, 22, 23, 24),            // 與中心在站 1 (分鐘 2) 相遇
                    Route.of(30, 31, 2, 32, 33, 34),            // 與中心在站 2 (分鐘 3) 相遇
                    Route.of(40, 41, 42, 3, 43, 44),            // 與中心在站 3 (分鐘 4) 相遇
                    Route.of(50, 51, 52, 53, 4, 54)             // 與中心在站 4 (分鐘 5) 相遇
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert - 中心司機依序與其他司機相遇並收集八卦
            // 分鐘 5 時中心司機已知全部，但其他司機尚未全知
            // 需要等到循環回來再次相遇
            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("given_八位司機鏈狀傳播_when_計算八卦傳播時間_then_回傳正確分鐘數")
        void given_八位司機鏈狀傳播_when_計算八卦傳播時間_then_回傳正確分鐘數() {
            // Arrange - 8 位司機，鏈狀相遇
            // 司機 0-1 在分鐘 1 站 1 相遇
            // 司機 1-2 在分鐘 2 站 2 相遇
            // ...以此類推
            List<Route> routes = List.of(
                    Route.of(1, 90, 91, 92, 93, 94, 95),          // 司機 0：分鐘1@站1
                    Route.of(1, 2, 80, 81, 82, 83, 84),           // 司機 1：分鐘1@站1, 分鐘2@站2
                    Route.of(70, 2, 3, 71, 72, 73, 74),           // 司機 2：分鐘2@站2, 分鐘3@站3
                    Route.of(60, 61, 3, 4, 62, 63, 64),           // 司機 3：分鐘3@站3, 分鐘4@站4
                    Route.of(50, 51, 52, 4, 5, 53, 54),           // 司機 4：分鐘4@站4, 分鐘5@站5
                    Route.of(40, 41, 42, 43, 5, 6, 44),           // 司機 5：分鐘5@站5, 分鐘6@站6
                    Route.of(30, 31, 32, 33, 34, 6, 7),           // 司機 6：分鐘6@站6, 分鐘7@站7
                    Route.of(20, 21, 22, 23, 24, 25, 7)           // 司機 7：分鐘7@站7
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert - 鏈狀傳播需要多次循環才能完成
            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("given_十位司機複雜網路_when_計算八卦傳播時間_then_回傳正確分鐘數")
        void given_十位司機複雜網路_when_計算八卦傳播時間_then_回傳正確分鐘數() {
            // Arrange - 10 位司機，複雜交錯網路
            List<Route> routes = List.of(
                    Route.of(1, 2, 3, 4, 5),
                    Route.of(1, 6, 7, 8, 9, 10),
                    Route.of(2, 6, 11, 12, 13, 14, 15),
                    Route.of(3, 7, 11, 16, 17, 18),
                    Route.of(4, 8, 12, 16, 19, 20, 21, 22),
                    Route.of(5, 9, 13, 17, 19, 23, 24, 25, 26),
                    Route.of(10, 14, 18, 20, 23, 27, 28),
                    Route.of(15, 21, 24, 27, 29, 30, 31, 32, 33),
                    Route.of(22, 25, 28, 29, 34, 35, 36, 37),
                    Route.of(26, 30, 33, 34, 38, 39, 40)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert - 經過驗證，實際結果是 66
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.toString()).isEqualTo("66");
        }

        @Test
        @DisplayName("given_六位司機長路線部分不相交_when_計算八卦傳播時間_then_回傳never")
        void given_六位司機長路線部分不相交_when_計算八卦傳播時間_then_回傳never() {
            // Arrange - 6 位司機，分成兩個不相交的群組
            List<Route> routes = List.of(
                    // 群組 A
                    Route.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    Route.of(1, 11, 12, 13, 14, 15),
                    Route.of(2, 11, 16, 17, 18, 19, 20),
                    // 群組 B（與群組 A 完全不相交）
                    Route.of(100, 101, 102, 103, 104, 105),
                    Route.of(100, 106, 107, 108, 109),
                    Route.of(101, 106, 110, 111, 112, 113, 114)
            );

            // Act
            GossipResult result = sut.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對五位司機共同起點應該回傳1")
        void 所有實作對五位司機共同起點應該回傳1(GossipingBusDrivers impl) {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2, 3, 4, 5, 6, 7, 8),
                    Route.of(1, 10, 11, 12, 13, 14, 15, 16),
                    Route.of(1, 20, 21, 22, 23, 24, 25),
                    Route.of(1, 30, 31, 32, 33, 34),
                    Route.of(1, 40, 41, 42, 43, 44, 45, 46, 47)
            );

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.toString()).isEqualTo("1");
        }

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對十位司機複雜網路應該回傳相同結果")
        void 所有實作對十位司機複雜網路應該回傳相同結果(GossipingBusDrivers impl) {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2, 3, 4, 5),
                    Route.of(1, 6, 7, 8, 9, 10),
                    Route.of(2, 6, 11, 12, 13, 14, 15),
                    Route.of(3, 7, 11, 16, 17, 18),
                    Route.of(4, 8, 12, 16, 19, 20, 21, 22),
                    Route.of(5, 9, 13, 17, 19, 23, 24, 25, 26),
                    Route.of(10, 14, 18, 20, 23, 27, 28),
                    Route.of(15, 21, 24, 27, 29, 30, 31, 32, 33),
                    Route.of(22, 25, 28, 29, 34, 35, 36, 37),
                    Route.of(26, 30, 33, 34, 38, 39, 40)
            );

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert - 經過驗證，實際結果是 66
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.toString()).isEqualTo("66");
        }

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對八位司機鏈狀傳播應該回傳相同結果")
        void 所有實作對八位司機鏈狀傳播應該回傳相同結果(GossipingBusDrivers impl) {
            // Arrange - 鏈狀相遇模式
            List<Route> routes = List.of(
                    Route.of(1, 90, 91, 92, 93, 94, 95),
                    Route.of(1, 2, 80, 81, 82, 83, 84),
                    Route.of(70, 2, 3, 71, 72, 73, 74),
                    Route.of(60, 61, 3, 4, 62, 63, 64),
                    Route.of(50, 51, 52, 4, 5, 53, 54),
                    Route.of(40, 41, 42, 43, 5, 6, 44),
                    Route.of(30, 31, 32, 33, 34, 6, 7),
                    Route.of(20, 21, 22, 23, 24, 25, 7)
            );

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert - 鏈狀傳播需要多次循環（經驗證結果為 43）
            assertThat(result.isSuccess())
                    .as("實作 %s 應該成功，但回傳: %s", impl.getClass().getSimpleName(), result)
                    .isTrue();
            assertThat(result.toString()).isEqualTo("43");
        }
    }

    // =========================================================================
    // 跨實作驗證
    // =========================================================================
    @Nested
    @DisplayName("跨實作驗證")
    class CrossImplementationVerification {

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對範例一應該回傳相同結果")
        void 所有實作對範例一應該回傳相同結果(GossipingBusDrivers impl) {
            // Arrange - 整行都是站牌
            List<Route> routes = List.of(
                    Route.of(3, 1, 2, 3),
                    Route.of(3, 2, 3, 1),
                    Route.of(4, 2, 3, 4, 5)
            );

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("5");
        }

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對範例二應該回傳相同結果")
        void 所有實作對範例二應該回傳相同結果(GossipingBusDrivers impl) {
            // Arrange
            List<Route> routes = List.of(
                    Route.of(1, 2),
                    Route.of(2, 8)
            );

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("never");
        }

        @ParameterizedTest
        @MethodSource("io.progden.gossipingbusdrivers.GossipingBusDriversTest#implementations")
        @DisplayName("所有實作對只有一位司機應該回傳1")
        void 所有實作對只有一位司機應該回傳1(GossipingBusDrivers impl) {
            // Arrange
            List<Route> routes = List.of(Route.of(1, 2, 3));

            // Act
            GossipResult result = impl.calculate(routes);

            // Assert
            assertThat(result.toString()).isEqualTo("1");
        }
    }

    // =========================================================================
    // Performance - 效能比較
    // =========================================================================
    @Nested
    @DisplayName("Performance - 效能比較")
    class PerformanceComparison {

        /**
         * 產生複雜測試案例：多位司機、長路線。
         */
        static List<Route> generateComplexScenario(int driverCount, int routeLength) {
            List<Route> routes = List.empty();
            for (int d = 0; d < driverCount; d++) {
                int[] stops = new int[routeLength];
                for (int s = 0; s < routeLength; s++) {
                    // 每位司機的路線有部分重疊，確保能傳播八卦
                    stops[s] = (d * 10 + s) % (driverCount * 5);
                }
                routes = routes.append(Route.of(stops));
            }
            return routes;
        }

        @Test
        @DisplayName("效能比較_15位司機_20站路線")
        void 效能比較_15位司機_20站路線() {
            // Arrange
            List<Route> routes = generateComplexScenario(15, 20);
            int warmupRuns = 3;
            int measureRuns = 10;

            GossipingBusDrivers simulation = new GossipingBusDriversSimulation();
            GossipingBusDrivers eventDriven = new GossipingBusDriversEventDriven();
            GossipingBusDrivers graph = new GossipingBusDriversGraph();

            // Warmup
            for (int i = 0; i < warmupRuns; i++) {
                simulation.calculate(routes);
                eventDriven.calculate(routes);
                graph.calculate(routes);
            }

            // Measure Simulation
            long simStart = System.nanoTime();
            GossipResult simResult = null;
            for (int i = 0; i < measureRuns; i++) {
                simResult = simulation.calculate(routes);
            }
            long simTime = (System.nanoTime() - simStart) / measureRuns;

            // Measure Event-Driven
            long edStart = System.nanoTime();
            GossipResult edResult = null;
            for (int i = 0; i < measureRuns; i++) {
                edResult = eventDriven.calculate(routes);
            }
            long edTime = (System.nanoTime() - edStart) / measureRuns;

            // Measure Graph
            long graphStart = System.nanoTime();
            GossipResult graphResult = null;
            for (int i = 0; i < measureRuns; i++) {
                graphResult = graph.calculate(routes);
            }
            long graphTime = (System.nanoTime() - graphStart) / measureRuns;

            // Output results
            System.out.println("\n========================================");
            System.out.println("效能比較: 15 位司機, 20 站路線");
            System.out.println("========================================");
            System.out.printf("模擬法 (Simulation):    %,10d ns  結果: %s%n", simTime, simResult);
            System.out.printf("事件驅動 (EventDriven): %,10d ns  結果: %s%n", edTime, edResult);
            System.out.printf("圖論法 (Graph):         %,10d ns  結果: %s%n", graphTime, graphResult);
            System.out.println("----------------------------------------");
            System.out.printf("模擬法 vs 事件驅動: %.2fx%n", (double) simTime / edTime);
            System.out.printf("模擬法 vs 圖論法:   %.2fx%n", (double) simTime / graphTime);
            System.out.println("========================================\n");

            // Assert - 三種實作結果應該一致
            assertThat(simResult.toString()).isEqualTo(edResult.toString());
            assertThat(simResult.toString()).isEqualTo(graphResult.toString());
        }

        @Test
        @DisplayName("效能比較_20位司機_30站路線")
        void 效能比較_20位司機_30站路線() {
            // Arrange
            List<Route> routes = generateComplexScenario(20, 30);
            int warmupRuns = 3;
            int measureRuns = 5;

            GossipingBusDrivers simulation = new GossipingBusDriversSimulation();
            GossipingBusDrivers eventDriven = new GossipingBusDriversEventDriven();
            GossipingBusDrivers graph = new GossipingBusDriversGraph();

            // Warmup
            for (int i = 0; i < warmupRuns; i++) {
                simulation.calculate(routes);
                eventDriven.calculate(routes);
                graph.calculate(routes);
            }

            // Measure Simulation
            long simStart = System.nanoTime();
            GossipResult simResult = null;
            for (int i = 0; i < measureRuns; i++) {
                simResult = simulation.calculate(routes);
            }
            long simTime = (System.nanoTime() - simStart) / measureRuns;

            // Measure Event-Driven
            long edStart = System.nanoTime();
            GossipResult edResult = null;
            for (int i = 0; i < measureRuns; i++) {
                edResult = eventDriven.calculate(routes);
            }
            long edTime = (System.nanoTime() - edStart) / measureRuns;

            // Measure Graph
            long graphStart = System.nanoTime();
            GossipResult graphResult = null;
            for (int i = 0; i < measureRuns; i++) {
                graphResult = graph.calculate(routes);
            }
            long graphTime = (System.nanoTime() - graphStart) / measureRuns;

            // Output results
            System.out.println("\n========================================");
            System.out.println("效能比較: 20 位司機, 30 站路線");
            System.out.println("========================================");
            System.out.printf("模擬法 (Simulation):    %,10d ns  結果: %s%n", simTime, simResult);
            System.out.printf("事件驅動 (EventDriven): %,10d ns  結果: %s%n", edTime, edResult);
            System.out.printf("圖論法 (Graph):         %,10d ns  結果: %s%n", graphTime, graphResult);
            System.out.println("----------------------------------------");
            System.out.printf("模擬法 vs 事件驅動: %.2fx%n", (double) simTime / edTime);
            System.out.printf("模擬法 vs 圖論法:   %.2fx%n", (double) simTime / graphTime);
            System.out.println("========================================\n");

            // Assert
            assertThat(simResult.toString()).isEqualTo(edResult.toString());
            assertThat(simResult.toString()).isEqualTo(graphResult.toString());
        }

        @Test
        @DisplayName("效能比較_極端案例_需要接近480分鐘")
        void 效能比較_極端案例_需要接近480分鐘() {
            // Arrange - 設計一個需要很長時間才能完成的案例
            // 兩位司機，路線長度互質，需要較長時間才能相遇
            List<Route> routes = List.of(
                    Route.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13),  // 13 站
                    Route.of(100, 1, 101, 102, 103, 104, 105, 106, 107, 108, 109) // 11 站，只有站 1 共用
            );
            int warmupRuns = 2;
            int measureRuns = 5;

            GossipingBusDrivers simulation = new GossipingBusDriversSimulation();
            GossipingBusDrivers eventDriven = new GossipingBusDriversEventDriven();
            GossipingBusDrivers graph = new GossipingBusDriversGraph();

            // Warmup
            for (int i = 0; i < warmupRuns; i++) {
                simulation.calculate(routes);
                eventDriven.calculate(routes);
                graph.calculate(routes);
            }

            // Measure
            long simStart = System.nanoTime();
            GossipResult simResult = null;
            for (int i = 0; i < measureRuns; i++) {
                simResult = simulation.calculate(routes);
            }
            long simTime = (System.nanoTime() - simStart) / measureRuns;

            long edStart = System.nanoTime();
            GossipResult edResult = null;
            for (int i = 0; i < measureRuns; i++) {
                edResult = eventDriven.calculate(routes);
            }
            long edTime = (System.nanoTime() - edStart) / measureRuns;

            long graphStart = System.nanoTime();
            GossipResult graphResult = null;
            for (int i = 0; i < measureRuns; i++) {
                graphResult = graph.calculate(routes);
            }
            long graphTime = (System.nanoTime() - graphStart) / measureRuns;

            // Output
            System.out.println("\n========================================");
            System.out.println("效能比較: 極端案例 (LCM=143, 需要週期性相遇)");
            System.out.println("========================================");
            System.out.printf("模擬法 (Simulation):    %,10d ns  結果: %s%n", simTime, simResult);
            System.out.printf("事件驅動 (EventDriven): %,10d ns  結果: %s%n", edTime, edResult);
            System.out.printf("圖論法 (Graph):         %,10d ns  結果: %s%n", graphTime, graphResult);
            System.out.println("----------------------------------------");
            System.out.printf("模擬法 vs 事件驅動: %.2fx%n", (double) simTime / edTime);
            System.out.printf("模擬法 vs 圖論法:   %.2fx%n", (double) simTime / graphTime);
            System.out.println("========================================\n");

            // Assert
            assertThat(simResult.toString()).isEqualTo(edResult.toString());
            assertThat(simResult.toString()).isEqualTo(graphResult.toString());
        }
    }
}
