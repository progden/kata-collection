# Bowling Game Test Cases

## Level 1: 基本計分 (BasicScoring)

| 測試名稱 | 情境 | 預期結果 |
|---------|------|----------|
| gutterGame | 20次投球全部洗溝 | 總分為 0 |
| allOnes | 20次投球每次打1瓶 | 總分為 20 |
| allTwos | 20次投球每次打2瓶 | 總分為 40 |
| mixedRolls | 每局投球3和4瓶 | 總分為 70 |

## Level 2: Spare 規則 (SpareRules)

| 測試名稱 | 情境 | 預期結果 |
|---------|------|----------|
| oneSpareAtFirstFrame | 第一局 Spare (3+7) 後投3瓶，其餘0 | 總分為 16 |
| oneSpareAtMiddleFrame | 第三局 Spare (3+7) 後投3瓶，其餘0 | 總分為 16 |
| twoConsecutiveSpares | 連續兩局 Spare (7+3, 3+7) 後投3瓶，其餘0 | 總分為 31 (10+3+10+3+3) |
| allSpares | 每局都是 5+5 的 Spare，最後補5 (共21球) | 總分為 150 |
| spareDifferentCombination | 第一局 7+3 的 Spare 後投4瓶，其餘0 | 總分為 18 |
| spareFollowedByZero | 第一局 Spare (6+4) 後投0瓶 | 總分為 10 |

## Level 3: Strike 規則 (StrikeRules)

| 測試名稱 | 情境 | 預期結果 |
|---------|------|----------|
| oneStrike | 第一局 Strike 後投3和4瓶，其餘0 | 總分為 24 |
| strikeFollowedByZeros | 第一局 Strike 後投0和0瓶 | 總分為 10 |
| twoConsecutiveStrikes | 連續兩局 Strike 後投3和4瓶，其餘0 | 總分為 47 |
| threeConsecutiveStrikes | 連續三局 Strike 後投3和4瓶，其餘0 | 總分為 77 |
| strikeFollowedBySpare | Strike 後接 Spare (5+5) 再投3瓶，其餘0 | 總分為 36 |

## Level 4: 第十局特殊規則 (TenthFrameRules)

| 測試名稱 | 情境 | 預期結果 |
|---------|------|----------|
| tenthFrameSpare | 前九局0分，第十局 Spare (7+3) 補投5瓶 | 總分為 15 |
| tenthFrameStrike | 前九局0分，第十局 Strike 補投3和4瓶 | 總分為 17 |
| tenthFrameTwoStrikes | 前九局0分，第十局兩個 Strike 補投5瓶 | 總分為 25 |
| tenthFrameThreeStrikes | 前九局0分，第十局三個 Strike | 總分為 30 |

## Level 5: 完整遊戲驗證 (FullGameVerification)

| 測試名稱 | 情境 | 預期結果 |
|---------|------|----------|
| perfectGame | 12次全部 Strike | 總分為 300 |
| allFiveSpares | 21次全部投5瓶 (都是 Spare) | 總分為 150 |

---

## 計分規則說明

- **Spare**: 兩次投球擊倒全部10瓶 → 獎勵分 = 下一球擊倒數
- **Strike**: 第一球擊倒全部10瓶 → 獎勵分 = 接下來兩球擊倒數總和
- **第十局**: 若達成 Strike 或 Spare，最多可投3球
