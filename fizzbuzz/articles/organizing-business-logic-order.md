# 組織業務邏輯的順序：從 FizzBuzz 學習規則排序的藝術

## 背景：什麼是 FizzBuzz Kata？

[FizzBuzz Kata](https://kata-log.rocks/fizz-buzz-kata) 是一個經典的程式練習題，常用於 Code Retreat 和 Programming Dojo 等活動。

### 原始題目

> Write a program that prints one line for each number from 1 to 100.
>
> 撰寫一個程式，為 1 到 100 之間的每個數字輸出一行。

輸出規則：
- 一般情況下顯示數字本身
- 3 的倍數輸出 `"Fizz"`
- 5 的倍數輸出 `"Buzz"`
- 同時是 3 和 5 的倍數輸出 `"FizzBuzz"`

### Code Freeze 變體（Arctic Edition）

本文使用的是 FizzBuzz 的北極主題變體版本：

| 條件 | 原版輸出 | Arctic Edition |
|------|----------|----------------|
| 3 的倍數 | `"Fizz"` | `"Kuksa!"` |
| 5 的倍數 | `"Buzz"` | `"Sauna!"` |
| 3 和 5 的公倍數 | `"FizzBuzz"` | `"Dip!"` |

這個變體的特點是：`"Dip!"` 不再是 `"Kuksa!"` + `"Sauna!"` 的組合，而是一個獨立的輸出。這個差異揭示了業務規則組織順序的重要性。

---

## 前言

在軟體開發中，我們經常需要處理多個業務規則。這些規則的**說明順序**和**實作順序**往往是不同的。本文透過 FizzBuzz Kata 的重構過程，探討如何有效地組織業務邏輯。

## 業務規則

FizzBuzz 的規則如下：

| 規則 | 條件 | 輸出 |
|------|------|------|
| Rule 1 | 不是 3 或 5 的倍數 | 數字本身 |
| Rule 2 | 3 的倍數 | `"Kuksa!"` |
| Rule 3 | 5 的倍數 | `"Sauna!"` |
| Rule 4 | 同時是 3 和 5 的倍數 | `"Dip!"` |

## 演進過程

### STEP 0：初始版本 — 按說明順序實作

```java
public String fizzBuzz(int i) {
    var output = new StringBuilder();
    if (i % 3 == 0 || i % 5 == 0) {
        if (i % 3 == 0)
            output.append("Fizz");
        if (i % 5 == 0)
            output.append("Buzz");
    } else {
        output.append(i);
    }
    return output.toString();
}
```

**問題**：邏輯巢狀過深，需要追蹤多個條件分支。

---

### STEP 1：反轉條件式

```java
public String fizzBuzz(int i) {
    var output = new StringBuilder();
    if (i % 3 != 0 && i % 5 != 0) { // !(A || B) => !A && !B
        output.append(i);
    } else {
        if (i % 3 == 0)
            output.append("Fizz");
        if (i % 5 == 0)
            output.append("Buzz");
    }
    return output.toString();
}
```

**改進**：先處理「不符合任何特殊規則」的情況，減少巢狀層級。

---

### STEP 1.1：提早回傳 (Early Return)

```java
public String fizzBuzz(int i) {
    var output = new StringBuilder();
    if (i % 3 != 0 && i % 5 != 0) {
        output.append(i);
        return output.toString();  // 提早回傳 => 減少階層 (移除 else) => 減低認知複雜度
    }

    if (i % 3 == 0)
        output.append("Fizz");
    if (i % 5 == 0)
        output.append("Buzz");

    return output.toString();
}
```

**改進**：使用 Guard Clause，讓主要邏輯更清晰。

---

### STEP 1.2：移除不必要的 StringBuilder

```java
public String fizzBuzz(int i) {
    if (i % 3 != 0 && i % 5 != 0) {
        return "" + i;  // 直接回傳
    }

    var output = new StringBuilder();
    if (i % 3 == 0)
        output.append("Fizz");
    if (i % 5 == 0)
        output.append("Buzz");

    return output.toString();
}
```

**改進**：Rule 1 不需要 StringBuilder，直接回傳即可。

---

### STEP 2：處理 Rule 4 的特殊需求

當規則從 `"Fizz"/"Buzz"` 變成 `"Kuksa!"/"Sauna!"/"Dip!"` 時，原本的字串拼接邏輯失效：

```java
// 測試失敗！15 會輸出 "Kuksa!Sauna!" 而非 "Dip!"
if (i % 3 == 0)
    output.append("Kuksa!");
if (i % 5 == 0)
    output.append("Sauna!");
```

**問題浮現**：Rule 4 需要獨立處理，不能用字串拼接。

---

### STEP 2.1：嘗試提早處理 Rule 4

```java
public String fizzBuzz(int i) {
    if (i % 3 != 0 && i % 5 != 0)
        return "" + i;

    if (i % 3 == 0 && i % 5 == 0)
        return "Dip!";

    if (i % 3 == 0)
        return "Kuksa!";
    if (i % 5 == 0)
        return "Sauna!";

    return "";  // 這行永遠不會執行，但編譯器需要它
}
```

**問題**：最後的 `return ""` 很奇怪，暴露了邏輯順序的問題。

---

### Final Version：從最特殊到最一般

```java
public String fizzBuzz(int i) {
    // Rule 4: 最特殊的情況
    if (i % 3 == 0 && i % 5 == 0)
        return "Dip!";

    // Rule 3
    if (i % 5 == 0)
        return "Sauna!";

    // Rule 2
    if (i % 3 == 0)
        return "Kuksa!";

    // Rule 1: 最一般的情況（預設）
    return "" + i;
}
```

**最終形態**：規則順序完全反轉，程式碼簡潔且無冗餘。

---

## 核心洞察：說明順序 vs 實作順序

### 說明順序：從一般到特殊

當我們**解釋**規則時，習慣從最基本的情況開始：

```
Rule 1: 一般數字 → 輸出數字本身
Rule 2: 3 的倍數 → "Kuksa!"
Rule 3: 5 的倍數 → "Sauna!"
Rule 4: 3 和 5 的公倍數 → "Dip!"
```

這符合人類認知：先建立基礎概念，再逐步加入例外。

### 實作順序：從特殊到一般

當我們**實作**規則時，應該反過來：

```
Rule 4: 3 和 5 的公倍數 → "Dip!"     (最特殊)
Rule 3: 5 的倍數 → "Sauna!"
Rule 2: 3 的倍數 → "Kuksa!"
Rule 1: 一般數字 → 輸出數字本身       (最一般/預設)
```

**原因**：

1. **避免條件遮蔽**：特殊規則如果放在後面，可能被一般規則提前匹配
2. **提升效率**：先排除特殊情況，減少後續判斷
3. **簡化預設情況**：最一般的規則放最後，自然成為 `else` 或預設回傳

---

## 設計原則

### 1. Guard Clause（守衛語句）

將特殊情況提前處理並回傳，避免深層巢狀：

```java
// 不好
if (isSpecialCase) {
    // 特殊處理
} else {
    // 正常處理（可能很長）
}

// 較好
if (isSpecialCase) {
    return handleSpecial();
}
// 正常處理
```

### 2. 規則優先級

```
最特殊的條件（多個條件的交集）
    ↓
較特殊的條件
    ↓
一般條件
    ↓
預設情況（無條件）
```

### 3. 預設值放最後

最後一個 `return` 不需要條件判斷，自然涵蓋所有剩餘情況。

---

## 重構方法清單

| 步驟 | 重構方法 | 說明 |
|------|----------|------|
| STEP 0 → 1 | **Invert Conditional**<br>反轉條件式 | 將 `if (A) { special } else { normal }` 反轉為 `if (!A) { normal } else { special }` |
| STEP 1 → 1.1 | **Replace Nested Conditional with Guard Clauses**<br>以守衛語句取代巢狀條件式 | 使用提早回傳（Early Return）消除 else 區塊 |
| STEP 1.1 → 1.2 | **Remove Dead Code**<br>移除死碼 | 移除不再需要的 StringBuilder 變數 |
| STEP 1.2 → 2 | **Run Tests**<br>執行測試 | 發現測試失敗，揭露隱藏的業務邏輯問題 |
| STEP 2 → 2.1 | **Add Guard Clause**<br>新增守衛語句 | 為 Rule 4 新增獨立的條件判斷 |
| STEP 2.1 → Final | **Reorder Conditionals**<br>重新排序條件式 | 將條件從「特殊→一般」重新排序，消除無用的 `return ""` |

### 使用的重構技巧總覽

| 重構技巧 | 英文名稱 | 出處 |
|----------|----------|------|
| 反轉條件式 | Invert Conditional | IDE 內建 |
| 以守衛語句取代巢狀條件式 | Replace Nested Conditional with Guard Clauses | *Refactoring* (Fowler) |
| 提早回傳 | Early Return | 通用模式 |
| 移除死碼 | Remove Dead Code | *Refactoring* (Fowler) |
| 重新排序條件式 | Reorder Conditionals | 通用模式 |

---

## 結論

| 面向 | 說明規則 | 實作規則 |
|------|----------|----------|
| 順序 | 一般 → 特殊 | 特殊 → 一般 |
| 目的 | 幫助理解 | 正確執行 |
| 風格 | 漸進式 | 排除式 |

在撰寫業務邏輯時，記住：

> **說明時從簡單開始，實作時從複雜開始。**

這個看似矛盾的原則，其實反映了「溝通」與「執行」的不同需求。好的程式碼應該同時考慮兩者 — 透過註解說明規則的邏輯順序，透過程式碼實現正確的執行順序。
