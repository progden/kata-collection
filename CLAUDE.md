# Code Kata 練習專案

此文件為 Claude Code (claude.ai/code) 在此專案中工作時的指引。

## 專案結構

Multi-module Maven 專案，包含多個 Kata 練習：

| 模組 | 說明 |
|-----|------|
| bowling | 保齡球計分 Kata - TDD 練習 |

## 共用建置指令

```bash
# 從根目錄執行所有模組測試
./mvnw test

# 執行特定模組
./mvnw test -pl bowling

# 清除並重新建置
./mvnw clean compile

# 只編譯特定模組
./mvnw compile -pl bowling
```

## 技術堆疊

- **Java 21**
- **Maven** (多模組架構)
- **JUnit 5** (測試框架)
- **AssertJ** (流暢斷言)
- **Mockito** (模擬框架)
- **Vavr** (函數式程式設計函式庫)

## 新增模組

1. 建立模組資料夾
2. 在根目錄 `pom.xml` 的 `<modules>` 加入模組名稱
3. 建立模組 `pom.xml` 並設定 parent
4. 建立模組專屬 `CLAUDE.md`
