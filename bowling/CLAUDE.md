# Bowling Kata

保齡球計分 TDD 練習。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl bowling

# 執行特定測試
../mvnw test -Dtest="io.progden.katabowling.GameTest"
```

## 架構

單類別設計：

- `Game.roll(int pins)` - 記錄每次投球擊倒的球瓶數
- `Game.score()` - 計算整場比賽的總分

## 保齡球規則摘要

- 每場比賽 10 局，每局 2 次投球機會
- **Spare**: 兩次投球擊倒 10 瓶 → 獎勵分 = 下一球
- **Strike**: 第一球擊倒 10 瓶 → 獎勵分 = 下兩球總和
- **第十局**: 若達成 Strike 或 Spare，最多可投 3 球

## 相關文件

- `test_case.md` - 測試案例清單
- `../tdd-guide.md` - TDD 測案設計原則
