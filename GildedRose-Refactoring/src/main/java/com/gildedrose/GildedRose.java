package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }
    
    // updateQuality 介面不可以修改！！！！
    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];

            // AgedBrieItemWorkflow
            if (isAgedBrie(item)) {
                processQuality(item, 1);

                decreaseSellIn(item, -1);

                if (isOutofSellIn(item)) {
                    processQuality(item, 1);
                }
            }

            // isSulfurasItemWorkflow
            if (isSulfuras(item)) {
                processQuality(item, 1);
                decreaseSellIn(item, 0);

                if (isOutofSellIn(item)) {
                    continue;
                }
            }

            // BackstagePassItemWorkflow
            if (isBackstagePass(item)) {
                processQuality(item, 1);
                if (item.sellIn < 11) {
                    if (item.quality < 50) {
                        processQuality(item, 1);
                    }
                }
                if (item.sellIn < 6) {
                    if (item.quality < 50) {
                        processQuality(item, 1);
                    }
                }
                decreaseSellIn(item, -1);
                if (isOutofSellIn(item)) {
                    processQuality(item, -item.quality);
                }
            }

            if (isNormalProduct(item)) {
                processQuality(item, -1);
                decreaseSellIn(item, -1);
                if (isOutofSellIn(item)) {
                    processQuality(item, -1);
                }
            }
        }
    }

    private static boolean isOutofSellIn(Item item) {
        return item.sellIn < 0;
    }

    private static void decreaseSellIn(Item item, int i) {
        item.sellIn = item.sellIn + i;
    }

    private static void processQuality(Item item, int i) {
        if (i < 0 && item.quality > 0) {
            item.quality = item.quality + i;
        }

        if (i > 0 && item.quality < 50) {
            item.quality = item.quality + 1;
        }
    }

    private static boolean isNormalProduct(Item item) {
        return !isAgedBrie(item)
                && !isBackstagePass(item)
                && !isSulfuras(item);
    }

    private static boolean isSulfuras(Item item) {
        return item.name.equals("Sulfuras, Hand of Ragnaros");
    }

    private static boolean isBackstagePass(Item item) {
        return item.name.equals("Backstage passes to a TAFKAL80ETC concert");
    }

    private static boolean isAgedBrie(Item item) {
        return item.name.equals("Aged Brie");
    }
}
