package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    abstract class ItemWorkflow {
        ItemWorkflow next;

        ItemWorkflow(ItemWorkflow next) {
            this.next = next;
        }

        protected static boolean isOutofSellIn(Item item) {
            return item.sellIn < 0;
        }

        protected static void decreaseSellIn(Item item, int i) {
            item.sellIn = item.sellIn + i;
        }

        protected static void processQuality(Item item, int i) {
            if (i < 0 && item.quality > 0) {
                item.quality = item.quality + i;
            }

            if (i > 0 && item.quality < 50) {
                item.quality = item.quality + 1;
            }
        }

        abstract void update(Item item);
    }

    class AgedBrieItemWorkflow extends ItemWorkflow {
        AgedBrieItemWorkflow(ItemWorkflow next) {
            super(next);
        }

        private static boolean isAgedBrie(Item item) {
            return item.name.equals("Aged Brie");
        }

        @Override
        void update(Item item) {
            if (isAgedBrie(item)) {
                processQuality(item, 1);

                decreaseSellIn(item, -1);

                if (isOutofSellIn(item)) {
                    processQuality(item, 1);
                }
            } else {
                if (super.next != null) {
                    next.update(item);
                }
            }
        }
    }

    class SulfurasItemWorkflow extends ItemWorkflow {
        SulfurasItemWorkflow(ItemWorkflow next) {
            super(next);
        }

        private static boolean isSulfuras(Item item) {
            return item.name.equals("Sulfuras, Hand of Ragnaros");
        }

        @Override
        void update(Item item) {
            if (isSulfuras(item)) {
                processQuality(item, 1);
                decreaseSellIn(item, 0);

                if (isOutofSellIn(item)) {
                    // do nothing
                }
            } else {
                if (super.next != null) {
                    next.update(item);
                }
            }
        }
    }

    class BackstagePassItemWorkflow extends ItemWorkflow {
        BackstagePassItemWorkflow(ItemWorkflow next) {
            super(next);
        }

        private static boolean isBackstagePass(Item item) {
            return item.name.equals("Backstage passes to a TAFKAL80ETC concert");
        }

        @Override
        void update(Item item) {
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
            } else {
                if (super.next != null) {
                    next.update(item);
                }
            }
        }
    }

    class NormalItemWorkflow extends ItemWorkflow {
        NormalItemWorkflow(ItemWorkflow next) {
            super(next);
        }

        @Override
        void update(Item item) {
            processQuality(item, -1);
            decreaseSellIn(item, -1);
            if (isOutofSellIn(item)) {
                processQuality(item, -1);
            }
        }
    }

    // updateQuality 介面不可以修改！！！！
    public void updateQuality() {
        ItemWorkflow workflow = new AgedBrieItemWorkflow(new SulfurasItemWorkflow(new BackstagePassItemWorkflow(new NormalItemWorkflow(null))));
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            workflow.update(item);
        }
    }
}
