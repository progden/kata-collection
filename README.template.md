# Kata 專案範本使用說明

本範本提供一個標準化的 TDD Kata 練習模組結構。

## 快速開始

### 1. 解壓縮範本

```bash
# 在 kata-collection 根目錄解壓縮
unzip template.zip -d my-kata-name
```

### 2. 重新命名目錄和套件

將解壓縮後的目錄重新命名為您的 Kata 名稱：

```bash
mv my-kata-name fizzbuzz  # 例如
```

### 3. 修改必要檔案

#### pom.xml

修改以下欄位：
- `<artifactId>` - 改為 `kata-your-module-name`
- `<name>` - 改為 `kata-your-module-name`
- `<description>` - 改為模組描述

#### 套件名稱

重新命名 Java 套件目錄：

```bash
# 主程式碼
mv src/main/java/io/progden/katamodule src/main/java/io/progden/yourmodule

# 測試程式碼
mv src/test/java/io/progden/katamodule src/test/java/io/progden/yourmodule
```

並更新所有 Java 檔案中的 `package` 宣告。

#### CLAUDE.md

更新模組名稱、描述和架構說明。

#### README.md

貼上 Kata 的完整題目描述。

### 4. 註冊到父專案

編輯根目錄的 `pom.xml`，在 `<modules>` 區塊加入新模組：

```xml
<modules>
    <module>bowling</module>
    <module>christmas-lights</module>
    <module>GildedRose-Refactoring</module>
    <module>your-module-name</module>  <!-- 新增這行 -->
</modules>
```

### 5. 驗證設定

```bash
# 從根目錄執行
./mvnw test -pl your-module-name
```

## 範本結構

```
kata-template/
├── pom.xml                              # Maven 專案設定
├── CLAUDE.md                            # Claude Code 模組說明
├── README.md                            # Kata 題目描述
└── src/
    ├── main/
    │   ├── java/io/progden/katamodule/
    │   │   └── package-info.java        # 套件說明
    │   └── resources/
    │       └── .gitkeep
    └── test/
        └── java/io/progden/katamodule/
            └── SampleTest.java          # 範例測試
```

## TDD 開發流程

1. **Red**: 撰寫一個失敗的測試
2. **Green**: 實作最少的程式碼使測試通過
3. **Refactor**: 重構程式碼，確保測試仍然通過

詳細指南請參考 `tdd-guide.md`。

## 推薦的 Kata 資源

- [Kata-Log](https://kata-log.rocks/) - Kata 題目集合
- [Coding Dojo](https://codingdojo.org/kata/) - 經典 Kata 列表
