# TDD 測案設計指南

## 核心理念

> 這些原則都是為了讓「改變」變得安全且可控。

---

## 1. TDD 三法則 (Uncle Bob)

```
1. 只寫剛好讓測試失敗的測試程式碼
2. 只寫剛好讓測試通過的生產程式碼
3. 重構，保持測試綠燈
```

**Red → Green → Refactor** 循環：

- **Red**: 寫一個失敗的測試
- **Green**: 用最簡單的方式讓測試通過
- **Refactor**: 清理程式碼，保持測試綠燈

**為什麼漸進複雜度？** → 每次只解決一個小問題，降低認知負擔。

---

## 2. Transformation Priority Premise (TPP)

Uncle Bob 提出的「轉換優先順序」，指導我們按照什麼順序演進程式碼：

```
({}→nil) → (nil→constant) → (constant→variable) →
(statement→statements) → (unconditional→conditional) →
(scalar→collection) → (statement→recursion)
```

### 測案順序對應

| 測案         | 轉換類型                        |
|------------|-----------------------------|
| 全部洗溝 → 0   | nil → constant              |
| 每次打1瓶 → 20 | constant → scalar           |
| Spare 計算   | unconditional → conditional |
| 多局累加       | scalar → collection         |

**原則**：優先使用列表前面的轉換，避免跳躍式的複雜度增加。

https://blog.cleancoder.com/uncle-bob/2013/05/27/TheTransformationPriorityPremise.html

---

## 3. ZOMBIES 測試啟發法

James Grenning 提出的測試順序助記法：

| 字母    | 意義               | 說明      | 對應測案範例               |
|-------|------------------|---------|----------------------|
| **Z** | Zero             | 零值、空值情境 | 全部洗溝 (0分)            |
| **O** | One              | 單一元素情境  | 每次打1瓶                |
| **M** | Many             | 多個元素情境  | 多局、多次投球              |
| **B** | Boundary         | 邊界條件    | 第十局特殊規則              |
| **I** | Interface        | API 設計  | roll() / score()     |
| **E** | Exception        | 異常處理    | 非法輸入 (此 Kata 未涵蓋)    |
| **S** | Simple Scenarios | 簡單情境優先  | 先基本計分，後 Spare/Strike |

### 應用順序

```
Z → O → M → B → I → E → S
零 → 一 → 多 → 邊界 → 介面 → 例外 → 簡單優先
```

這個原則旨在：

由簡入繁：先專注最簡單的行為，再逐步處理複雜情境。

量化邊界與介面：測試同時驅動 API 設計 及 邊界條件 行為。

持續維持正確性：每一行行為都有對應測試，保證系統整體始終通過測試。

https://blog.wingman-sw.com/tdd-guided-by-zombies

---

## 4. 三角測量 (Triangulation)

Kent Beck 的原則：**用多個測試案例「逼出」通用解法**

### 範例

```java
// 測案1: 可以用 hardcode 通過
Spare 3+7,下一球3 → 16

// 測案2: 開始需要通用邏輯
Spare 7+3,下一球4 → 18

// 測案3: 確保邏輯正確
Spare 6+4,下一球0 → 10
```

**為什麼？**

- 一個測案可以用 hardcode 通過
- 多個測案迫使你寫出真正的演算法
- 避免過早泛化，也避免寫死答案

---

## 5. 小步前進 (Baby Steps)

### 錯誤示範

```
❌ 一次寫完所有 Strike 邏輯
```

### 正確示範

```
✅ 步驟分解：
   1. 單一 Strike → 通過
   2. 連續兩個 Strike → 通過
   3. 連續三個 Strike → 通過
   4. 完美比賽 (12 Strikes) → 通過
```

**為什麼？**

- 每一步都有安全網
- 出錯時容易定位問題
- 快速回饋循環
- 建立信心與節奏

---

## 6. 測試即文件 (Tests as Documentation)

### Given-When-Then 格式

```java
@DisplayName("Strike規則 - Given 連續兩局Strike後投3和4瓶 - When 計算分數 - Then 總分為47")
```

### 結構說明

| 部分        | 說明        |
|-----------|-----------|
| **Given** | 前置條件、初始狀態 |
| **When**  | 執行的動作     |
| **Then**  | 預期結果      |

**好處**：

- 新人看測案就能理解業務規則
- 測試名稱 = 規格說明書
- 重構時不怕遺漏需求

---

## 7. 邊界值測試 (Boundary Testing)

測試系統的極端情況：

| 類型  | 範例          |
|-----|-------------|
| 最小值 | 全部洗溝 (0分)   |
| 最大值 | 完美比賽 (300分) |
| 臨界點 | 第十局額外投球規則   |
| 空值  | 無投球紀錄       |

---

## 8. 測案分層結構

使用 `@Nested` 將相關測試分組：

```java

@Nested
@DisplayName("Level 1: 基本計分")
class BasicScoring { ...
}

@Nested
@DisplayName("Level 2: Spare 規則")
class SpareRules { ...
}
```

**好處**：

- 提高可讀性
- 方便導航
- 清楚呈現測試覆蓋範圍

---

## 9. 輔助方法提升可讀性

```java
// 重複投球
private void rollTimes(int pins, int times) {
    for (int i = 0; i < times; i++) {
        game.roll(pins);
    }
}

// 斷言包裝
private void scoreShouldBe(int expected) {
    assertThat(game.score()).isEqualTo(expected);
}
```

**原則**：測試程式碼也要保持 DRY (Don't Repeat Yourself)

---

## 總結：核心原則對照表

| 原則      | 目的       | 應用方式                      |
|---------|----------|---------------------------|
| TDD 三法則 | 快速回饋循環   | Red → Green → Refactor    |
| TPP     | 控制複雜度增長  | 按優先順序轉換程式碼                |
| ZOMBIES | 測試順序指引   | Z → O → M → B → I → E → S |
| 三角測量    | 逼出通用解法   | 多個測案覆蓋同一規則                |
| 小步前進    | 降低風險     | 每次只增加一點複雜度                |
| 測試即文件   | 可讀性      | Given-When-Then 命名        |
| 邊界值測試   | 確保極端情況正確 | 測試最小、最大、臨界值               |

---

## 參考資料

- **Uncle Bob** - Clean Code, TDD 三法則, Transformation Priority Premise
- **Kent Beck** - Test-Driven Development: By Example, 三角測量
- **James Grenning** - ZOMBIES 測試啟發法
- **Martin Fowler** - Refactoring, Given-When-Then

---

## 一句話總結

> **從「最簡單能失敗的測試」開始，逐步驅動出完整實作。**
