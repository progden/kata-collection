package com.gildedrose;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    private GildedRose app;
    private Item[] items;

    @BeforeEach
    public void setUp() {
        initItem("foo", 2, 5);
    }

    @Test
    @DisplayName("每天結束時，我們的系統會降低每個項目的這兩個值。")
    void foo() {
        sellInAndQualityShouldBe(2, 5);

        app.updateQuality();
        sellInAndQualityShouldBe(1, 4);

        app.updateQuality(); // sellIn 變成 0, qulity 變成 3
        sellInAndQualityShouldBe(0, 3);
        // 只處理到不碰觸到其他規則的最簡單情境
    }

    private void initItem(String foo, int sellIn, int quality) {
        items = new Item[]{new Item(foo, sellIn, quality)};
        app = new GildedRose(items);
    }

    private void sellInAndQualityShouldBe(int expectedSellIn, int expectedQuality) {
        assertEquals(expectedQuality, app.items[0].quality);
        assertEquals(expectedSellIn, app.items[0].sellIn);
    }

    @Nested
    @DisplayName("一旦過了保質期，產品品質下降的速度會加倍")
    class QualityDegradesTwiceAsFastAfterSellByDate {

        @Test
        @DisplayName("正常商品在過期後品質下降速度加倍")
        void normalItemQualityDegradesTwiceAsFast() {
            initItem("Normal Item", 0, 10);

            app.updateQuality();
            sellInAndQualityShouldBe(-1, 8);
        }

        @Test
        @DisplayName("正常商品在過期後從較高品質開始下降")
        void normalItemQualityDegradesTwiceAsFastFromHigherQuality() {
            initItem("Normal Item", -1, 20);

            app.updateQuality();
            sellInAndQualityShouldBe(-2, 18);
        }

    }

    @Nested
    @DisplayName("商品的品質永遠不會是負面的")
    class QualityNeverNegative {

        @Test
        @DisplayName("正常商品品質降到零後不再下降")
        void normalItemQualityStopsAtZero() {
            initItem("Normal Item", 5, 1);

            app.updateQuality();
            sellInAndQualityShouldBe(4, 0);

            app.updateQuality();
            sellInAndQualityShouldBe(3, 0);
        }

        @Test
        @DisplayName("品質為零的商品保持為零")
        void itemWithZeroQualityRemainsZero() {
            initItem("Normal Item", 10, 0);

            app.updateQuality();
            sellInAndQualityShouldBe(9, 0);
        }

        @Test
        @DisplayName("過期商品品質降到零後不再下降")
        void expiredItemQualityStopsAtZero() {
            initItem("Normal Item", 0, 1);

            app.updateQuality();
            sellInAndQualityShouldBe(-1, 0);

            app.updateQuality();
            sellInAndQualityShouldBe(-2, 0);
        }

    }

    @Nested
    @DisplayName("「陳年布里起司」的品質實際上會隨著時間的推移而提高")
    class AgedBrieQualityIncreasesOverTime {

        @Test
        @DisplayName("陳年布里起司品質隨時間增加")
        void agedBrieQualityIncreasesOverTime() {
            initItem("Aged Brie", 10, 5);

            app.updateQuality();
            sellInAndQualityShouldBe(9, 6);

            app.updateQuality();
            sellInAndQualityShouldBe(8, 7);
        }

        // 規則沒有寫到的特殊規則！！！
        @Test
        @DisplayName("陳年布里起司在過期後品質增加更快")
        void agedBrieQualityIncreasesAfterSellByDate() {
            initItem("Aged Brie", 0, 10);

            app.updateQuality();
            sellInAndQualityShouldBe(-1, 12);
        }

    }

    @Nested
    @DisplayName("商品的品質永遠不會超過50")
    class QualityNeverExceedsFifty {

        @Test
        @DisplayName("正常商品品質不會超過50")
        void normalItemQualityDoesNotExceedFifty() {
            initItem("Normal Item", 10, 50);

            app.updateQuality();
            sellInAndQualityShouldBe(9, 49);
        }

        @Test
        @DisplayName("陳年布里起司品質在到達50後不再增加")
        void agedBrieQualityStopsAtFifty() {
            initItem("Aged Brie", 10, 50);

            app.updateQuality();
            sellInAndQualityShouldBe(9, 50);

            app.updateQuality();
            sellInAndQualityShouldBe(8, 50);
        }

        @Test
        @DisplayName("陳年布里起司在過期後品質也不會超過50")
        void agedBrieQualityStopsAtFiftyAfterExpiration() {
            initItem("Aged Brie", 0, 49);

            app.updateQuality();
            sellInAndQualityShouldBe(-1, 50);

            app.updateQuality();
            sellInAndQualityShouldBe(-2, 50);
        }
    }

    @Nested
    @DisplayName("作為一件傳說級物品，「薩弗拉斯」永遠不會被出售，品質也不會降低")
    class SulfurasNeverChangesSellInOrQuality {

        @Test
        @DisplayName("薩弗拉斯的品質和銷售期限永遠不變")
        void sulfurasNeverChanges() {
            initItem("Sulfuras, Hand of Ragnaros", 10, 80);

            app.updateQuality();
            sellInAndQualityShouldBe(10, 80);
        }

        @Test
        @DisplayName("薩弗拉斯在多次更新後仍保持不變")
        void sulfurasRemainsUnchangedAfterMultipleUpdates() {
            initItem("Sulfuras, Hand of Ragnaros", 5, 80);

            app.updateQuality();
            sellInAndQualityShouldBe(5, 80);

            app.updateQuality();
            sellInAndQualityShouldBe(5, 80);

            app.updateQuality();
            sellInAndQualityShouldBe(5, 80);
        }
    }

    @Nested
    @DisplayName("「後台通行證」就像陳年布里起司一樣，隨著其銷售價值的提高，品質也會提高")
    class BackstagePassesQualityIncreasesOverTime {

        @Test
        @DisplayName("後台通行證品質隨時間增加")
        void backstagePassesQualityIncreasesOverTime() {
            initItem("Backstage passes to a TAFKAL80ETC concert", 15, 20);

            app.updateQuality();
            sellInAndQualityShouldBe(14, 21);

            app.updateQuality();
            sellInAndQualityShouldBe(13, 22);
        }

        @Nested
        @DisplayName("當時間在 10 天或更短時，品質提高 2 倍;當時間在 5 天或更短時，品質提高 3 倍")
        class QualityIncreasesByTwoOrThreeWhenSellInIsTenOrLess {

            @Test
            @DisplayName("後台通行證在剩餘10天時品質增加2")
            void backstagePassesQualityIncreasesByTwoAtTenDays() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 10, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(9, 22);
            }

            @Test
            @DisplayName("後台通行證在剩餘6到10天時品質增加2")
            void backstagePassesQualityIncreasesByTwoBetweenSixAndTenDays() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 8, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(7, 22);

                app.updateQuality();
                sellInAndQualityShouldBe(6, 24);
            }

            @Test
            @DisplayName("後台通行證在剩餘5天時品質增加3")
            void backstagePassesQualityIncreasesByThreeAtFiveDays() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 5, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(4, 23);
            }

            @Test
            @DisplayName("後台通行證在剩餘1到5天時品質增加3")
            void backstagePassesQualityIncreasesByThreeBetweenOneAndFiveDays() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 3, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(2, 23);

                app.updateQuality();
                sellInAndQualityShouldBe(1, 26);
            }
        }

        @Nested
        @DisplayName("演唱會結束後，音質降至0")
        class QualityDropsToZeroAfterConcert {

            @Test
            @DisplayName("後台通行證在演唱會當天結束後品質降至0")
            void backstagePassesQualityDropsToZeroAfterConcert() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 1, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(0, 23);

                app.updateQuality();
                sellInAndQualityShouldBe(-1, 0);
            }

            @Test
            @DisplayName("後台通行證在演唱會結束後品質保持為0")
            void backstagePassesQualityRemainsZeroAfterConcert() {
                initItem("Backstage passes to a TAFKAL80ETC concert", 0, 20);

                app.updateQuality();
                sellInAndQualityShouldBe(-1, 0);

                app.updateQuality();
                sellInAndQualityShouldBe(-2, 0);
            }

        }

    }

    @Nested
    @DisplayName("「召喚」(Conjured)物品的品質下降速度是一般物品的兩倍")
    class ConjuredItemsQualityDegradesTwiceAsFast {

        @Test
        @DisplayName("Conjured物品在保質期內品質下降速度是一般物品的兩倍")
        void conjuredItemQualityDegradesTwiceAsFast() {
            initItem("Conjured Mana Cake", 10, 20);

            app.updateQuality();
            sellInAndQualityShouldBe(9, 18);

            app.updateQuality();
            sellInAndQualityShouldBe(8, 16);
        }

        @Test
        @DisplayName("Conjured物品在過期後品質下降速度是一般物品的四倍")
        void conjuredItemQualityDegradesFourTimesAsFastAfterSellByDate() {
            initItem("Conjured Mana Cake", 0, 20);

            app.updateQuality();
            sellInAndQualityShouldBe(-1, 16);

            app.updateQuality();
            sellInAndQualityShouldBe(-2, 12);
        }

        @Test
        @DisplayName("Conjured物品品質降到零後不再下降")
        void conjuredItemQualityStopsAtZero() {
            initItem("Conjured Mana Cake", 5, 2);

            app.updateQuality();
            sellInAndQualityShouldBe(4, 0);

            app.updateQuality();
            sellInAndQualityShouldBe(3, 0);
        }

    }

}
