# Game of Life Kata

Conway's Game of Life TDD 練習。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl game-of-life

# 執行特定測試
../mvnw test -Dtest="io.progden.gameoflife.SampleTest"
```

## 架構

- `Grid` - 細胞網格
- `Cell` - 細胞狀態

## 規則摘要

每個細胞根據周圍 8 個鄰居的狀態決定下一代的生死：

- **存活**: 活細胞有 2 或 3 個活鄰居時存活
- **死亡**: 活細胞有少於 2 個或多於 3 個活鄰居時死亡
- **繁殖**: 死細胞有恰好 3 個活鄰居時復活

## 相關文件

- `../tdd-guide.md` - TDD 測案設計原則
