# Game of Life 規則案例

以下用 `X` 表示活細胞，`.` 表示死細胞。

---

## API 介面

```java
// 建立指定大小的網格
Game game = new Game(width, height);

// 用字串初始化網格狀態（. 為死細胞，X 為活細胞）
game.init("...XXX...");

// 進行下一代演化
game.tick();

// 取得當前狀態（回傳 GameState 結構）
GameState state = game.getState();

// 取得格式化棋盤（含換行字元）
String board = game.board();
```

### GameState 結構

```java
record GameState(int width, int height, String state) {}

// 範例
// { width: 3, height: 3, state: ".X..X..X." }
```

### 初始化範例

```java
// 3x3 網格，中間一列為活細胞（Blinker 垂直狀態）
Game game = new Game(3, 3);
game.init(
    ".X." +
    ".X." +
    ".X."
);
```

### getState() vs board() 差異

```java
Game game = new Game(3, 3);
game.init(".X." + ".X." + ".X.");

// getState() - 回傳 GameState 結構
GameState state = game.getState();
// state.width()  -> 3
// state.height() -> 3
// state.state()  -> ".X..X..X."

// board() - 回傳格式化棋盤，每列以換行分隔
game.board();
// ".X.\n" +
// ".X.\n" +
// ".X."
```

`board()` 輸出效果：
```
.X.
.X.
.X.
```

---

## 1. 人口不足（Underpopulation）

**規則：活細胞少於 2 個活鄰居 → 死亡**

```
案例 A：0 個活鄰居        案例 B：1 個活鄰居
. . .                    . X .
. X .  → 死亡            . X .  → 死亡
. . .                    . . .
```

中間的活細胞因為鄰居太少，孤獨而死。

```java
// 案例 A：0 個活鄰居
Game game = new Game(3, 3);
game.init(
    "..." +
    ".X." +
    "..."
);
game.tick();
assertThat(game.getState().state()).isEqualTo(
    "..." +
    "..." +
    "..."
);

// 案例 B：1 個活鄰居
Game game = new Game(3, 3);
game.init(
    ".X." +
    ".X." +
    "..."
);
game.tick();
// 中間細胞死亡（只有 1 個鄰居）
```

---

## 2. 延續（Survival）

**規則：活細胞有 2 或 3 個活鄰居 → 存活**

```
案例 A：2 個活鄰居        案例 B：3 個活鄰居
. X .                    . X .
. X .  → 存活            X X .  → 存活
. X .                    . X .
```

中間的活細胞有適當數量的鄰居，繼續存活。

```java
// 案例 A：2 個活鄰居
Game game = new Game(3, 3);
game.init(
    ".X." +
    ".X." +
    ".X."
);
game.tick();
// 中間細胞存活（有 2 個鄰居）

// 案例 B：3 個活鄰居
Game game = new Game(3, 3);
game.init(
    ".X." +
    "XX." +
    ".X."
);
game.tick();
// 中間細胞存活（有 3 個鄰居）
```

---

## 3. 人口過剩（Overcrowding）

**規則：活細胞超過 3 個活鄰居 → 死亡**

```
案例 A：4 個活鄰居        案例 B：5 個活鄰居
. X .                    X X .
X X X  → 死亡            X X X  → 死亡
. X .                    . X .
```

中間的活細胞因為鄰居過多，資源競爭而死。

```java
// 案例 A：4 個活鄰居
Game game = new Game(3, 3);
game.init(
    ".X." +
    "XXX" +
    ".X."
);
game.tick();
// 中間細胞死亡（有 4 個鄰居）

// 案例 B：5 個活鄰居
Game game = new Game(3, 3);
game.init(
    "XX." +
    "XXX" +
    ".X."
);
game.tick();
// 中間細胞死亡（有 5 個鄰居）
```

---

## 4. 繁殖（Reproduction）

**規則：死細胞恰好有 3 個活鄰居 → 復活**

```
案例 A：                  案例 B：
. X .                    X X .
X . .  → 復活            . . X  → 復活
. X .                    . . .
```

中間的死細胞恰好有 3 個活鄰居，新生命誕生。

```java
// 案例 A：3 個活鄰居
Game game = new Game(3, 3);
game.init(
    ".X." +
    "X.." +
    ".X."
);
game.tick();
// 中間細胞復活（有 3 個鄰居）

// 案例 B：3 個活鄰居
Game game = new Game(3, 3);
game.init(
    "XX." +
    "..X" +
    "..."
);
game.tick();
// 中間細胞復活（有 3 個鄰居）
```

**注意：** 死細胞有 2 個或 4 個活鄰居時**不會**復活：

```
2 個活鄰居：不復活        4 個活鄰居：不復活
. X .                    X X .
X . .  → 維持死亡        X . X  → 維持死亡
. . .                    . . .
```

```java
// 2 個活鄰居：不復活
Game game = new Game(3, 3);
game.init(
    ".X." +
    "X.." +
    "..."
);
game.tick();
// 中間細胞維持死亡（只有 2 個鄰居）

// 4 個活鄰居：不復活
Game game = new Game(3, 3);
game.init(
    "XX." +
    "X.X" +
    "..."
);
game.tick();
// 中間細胞維持死亡（有 4 個鄰居）
```

---

## 綜合範例：Blinker 演化

```
第 0 代:          第 1 代:
. . . . .         . . . . .
. . X . .         . . . . .
. . X . .    →    . X X X .
. . X . .         . . . . .
. . . . .         . . . . .
```

**分析中間那列三個活細胞：**

| 細胞位置 | 活鄰居數 | 套用規則 | 結果 |
|----------|----------|----------|------|
| 上方 X | 1 | 人口不足 | 死亡 |
| 中間 X | 2 | 延續 | 存活 |
| 下方 X | 1 | 人口不足 | 死亡 |

**分析中間那列左右的死細胞：**

| 細胞位置 | 活鄰居數 | 套用規則 | 結果 |
|----------|----------|----------|------|
| 左側 . | 3 | 繁殖 | 復活 |
| 右側 . | 3 | 繁殖 | 復活 |

```java
// Blinker 演化測試
Game game = new Game(5, 5);
game.init(
    "....." +
    "..X.." +
    "..X.." +
    "..X.." +
    "....."
);

game.tick();

assertThat(game.getState().state()).isEqualTo(
    "....." +
    "....." +
    ".XXX." +
    "....." +
    "....."
);

// 再 tick 一次會回到原狀態（週期 2）
game.tick();

assertThat(game.getState().state()).isEqualTo(
    "....." +
    "..X.." +
    "..X.." +
    "..X.." +
    "....."
);
```
