# Gossiping Bus Drivers Kata

Gossiping Bus Drivers Kata - TDD 練習，計算公車司機交換八卦所需的時間。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl gossiping-bus-drivers

# 執行特定測試
../mvnw test -Dtest="io.progden.gossipingbusdrivers.GossipingBusDriversTest"
```

## 架構

- `GossipingBusDrivers` - 主要業務邏輯，計算八卦傳播時間
- `Driver` - 司機模型，包含路線和已知八卦
- `Route` - 路線模型，處理循環路線邏輯

## 規則摘要

1. 每位司機有固定路線，循環行駛
2. 每位司機一開始擁有一則獨特的八卦
3. 每站之間行駛需要 1 分鐘
4. 當司機在同一站相遇時，會立即分享所有已知的八卦
5. 工作日上限為 480 分鐘
6. 回傳所有司機知道全部八卦所需的分鐘數，若無法達成則回傳 "never"

## 相關文件

- `../tdd-guide.md` - TDD 測案設計原則
