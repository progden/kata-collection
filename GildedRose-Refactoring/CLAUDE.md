# Gilded Rose Kata

重構練習 - 在不修改 `Item` 類別的前提下，重構複雜的 `updateQuality()` 方法。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl GildedRose-Refactoring

# 執行特定測試
../mvnw test -Dtest="com.gildedrose.GildedRoseTest"
```

## 架構

- `Item` - 商品類別 (不可修改)
- `GildedRose` - 品質更新邏輯 (重構目標)
- `TexttestFixture` - 文字測試輔助

## 規則摘要

商品品質隨時間變化的規則：

| 商品 | 規則 |
|-----|------|
| 一般商品 | 每天 quality -1，過期後 -2 |
| Aged Brie | 每天 quality +1，越老越值錢 |
| Sulfuras | 傳奇物品，不會過期也不會降質 |
| Backstage passes | 演唱會前品質上升，結束後歸零 |
| Conjured | 品質下降速度為一般的兩倍 |

**限制**：
- `Item` 類別不可修改
- `quality` 永遠在 0-50 之間 (Sulfuras 除外，固定 80)

## 相關文件

- `../tdd-guide.md` - TDD 測案設計原則
