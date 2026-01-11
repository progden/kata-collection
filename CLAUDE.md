# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 專案概述

Multi-module Maven 專案，用於 TDD 方式的 Code Kata 練習。

| 模組 | 說明 |
|-----|------|
| bowling | 保齡球計分 Kata |
| christmas-lights | 聖誕燈飾 Kata |
| GildedRose-Refactoring | Gilded Rose 重構 Kata |

## 建置指令

```bash
# 執行所有模組測試
./mvnw test

# 執行特定模組
./mvnw test -pl bowling
./mvnw test -pl christmas-lights

# 執行特定測試類別
./mvnw test -pl bowling -Dtest="io.progden.katabowling.GameTest"

# 清除並重新建置
./mvnw clean compile
```

## 技術堆疊

- Java 21
- Maven (多模組)
- JUnit 5 + AssertJ + Mockito
- Vavr

## 架構

```
kata-bowling/
├── pom.xml              # Parent POM (dependencyManagement)
├── tdd-guide.md         # TDD 測案設計原則
├── bowling/             # 各模組有獨立 CLAUDE.md
├── christmas-lights/
└── GildedRose-Refactoring/
```

## TDD 開發方法

本專案遵循 TDD 原則，詳見 `tdd-guide.md`：

- **Red → Green → Refactor** 循環
- **ZOMBIES** 測試順序：Zero → One → Many → Boundary → Interface → Exception → Simple
- **Given-When-Then** 測試命名格式
- 使用 `@Nested` 分層組織測試

## 新增模組

1. 建立模組資料夾
2. 根目錄 `pom.xml` 的 `<modules>` 加入模組名稱
3. 建立模組 `pom.xml` (繼承 parent)
4. 建立模組 `CLAUDE.md`
