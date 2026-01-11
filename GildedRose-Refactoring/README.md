https://kata-log.rocks/gilded-rose-kata

# Gilded Rose Kata

## 鳴謝

靈感來自 Emily Bache

## 介紹

您好，歡迎加入鍍金玫瑰團隊。如你所知，我們是一家位於繁華都市黃金地段的小旅館，由一位名叫艾莉森的友善旅館老闆娘經營。我們只買賣最優質的商品。可惜的是，隨著商品接近保質期，它們的品質會不斷下降。我們目前有一套系統可以自動更新庫存。這套系統是由一位名叫利羅伊的務實派開發者開發的，他現在已經踏上了新的冒險之旅。您的任務是為我們的系統添加新功能，以便我們能夠開始銷售新的商品類別。首先，讓我們來了解一下我們的系統：

所有商品都有一個售賣期限，表示我們有多少天時間來售出該商品。
所有物品都有一個品質值，該值表示物品的價值。
每天結束時，我們的系統會降低每個項目的這兩個值。
很簡單，對吧？但有趣的地方就在這裡：

- 一旦過了保質期，產品品質下降的速度會加倍。
- 商品的品質永遠不會是負面的。
- 「陳年布里起司」(Aged Brie)的品質實際上會隨著時間的推移而提高。
- 商品的品質永遠不會超過50%。
- 作為一件傳說級物品，「薩弗拉斯」(Sulfuras)永遠不會被出售，品質也不會降低。
- 「後台通行證」(Backstage passes)就像陳年布里起司一樣，隨著其銷售價值的提高，品質也會提高；
    - 當時間在 10 天或更短時，品質提高 2 倍；當時間在 5 天或更短時，品質提高 3 倍。
    - 演唱會結束後，品質降至0。

## 你的任務

我們最近與一家魔法物品供應商簽訂了合約。這需要對我們的系統進行更新：

- 「召喚」(Conjured)物品的品質下降速度是一般物品的兩倍。

## 規則

您可以隨意修改 UpdateQuality 方法並添加任何新程式碼，只要一切運作正常即可。但是，請勿更改 Item 類別或 Items
屬性，因為它們屬於角落裡的哥布林，他會立刻暴怒並一擊秒殺您，因為他不相信程式碼共享（如果您願意，可以將 UpdateQuality 方法和
Items 屬性設為靜態，我們會為您處理）。

需要澄清的是，物品的品質永遠不可能超過 50，但是「薩弗拉斯」是一件傳說物品，因此它的品質為 80，並且永遠不會改變。

## 重構策略

採用 **責任鍊模式 (Chain of Responsibility)** 來區分各種商品的處理邏輯。

### 類別結構

```
ItemWorkflow (abstract)
├── ConjuredItemWorkflow
├── AgedBrieItemWorkflow
├── SulfurasItemWorkflow
├── BackstagePassItemWorkflow
└── NormalItemWorkflow
```

### 處理鏈順序

```
Item → Conjured → AgedBrie → Sulfuras → BackstagePass → NormalItem
```

### 設計優點

- **單一職責**：每個 Workflow 只負責一種商品類型的邏輯
- **開放封閉**：新增商品類型只需加入新的 Workflow，不需修改現有程式碼
- **易於測試**：各 Workflow 可獨立單元測試

### Workflow 職責

| Workflow | 商品 | 邏輯 |
|----------|------|------|
| ConjuredItemWorkflow | Conjured* | 品質下降速度 x2 |
| AgedBrieItemWorkflow | Aged Brie | 品質隨時間增加 |
| SulfurasItemWorkflow | Sulfuras | 傳奇物品，不變化 |
| BackstagePassItemWorkflow | Backstage passes | 品質依剩餘天數變化，過期歸零 |
| NormalItemWorkflow | 其他商品 | 預設處理邏輯（兜底）|

### 共用方法 (ItemWorkflow)

| 方法 | 說明 |
|------|------|
| `isOutofSellIn(item)` | 判斷是否已過期 (sellIn < 0) |
| `decreaseSellIn(item, i)` | 減少售賣期限 |
| `processQuality(item, i)` | 處理品質變化 (0-50 限制) |
