# Christmas Lights Kata

聖誕燈飾 TDD 練習。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl christmas-lights

# 執行特定測試
../mvnw test -Dtest="io.progden.christmaslights.LightGridTest"
```

## 架構

- `Grid` - 燈飾網格控制

## 規則摘要

1000x1000 的燈飾網格，支援以下操作：

- **turn on** (x1,y1) through (x2,y2) - 開啟指定範圍的燈
- **turn off** (x1,y1) through (x2,y2) - 關閉指定範圍的燈
- **toggle** (x1,y1) through (x2,y2) - 切換指定範圍的燈

## 相關文件

- `../tdd-guide.md` - TDD 測案設計原則
