# FizzBuzz Kata

FizzBuzz Kata - TDD 練習，經典的程式練習題目。

## 模組指令

```bash
# 在此目錄執行
../mvnw test

# 或從根目錄執行
./mvnw test -pl fizzbuzz

# 執行特定測試
../mvnw test -Dtest="io.progden.fizzbuzz.FizzBuzzTest"
```

## 架構

- `FizzBuzz` - 主要業務邏輯，處理數字轉換

## 規則摘要

1. 輸出 1 到 100 的數字
2. 3 的倍數輸出 "Fizz"
3. 5 的倍數輸出 "Buzz"
4. 同時是 3 和 5 的倍數輸出 "FizzBuzz"
5. 其他數字直接輸出數字本身

## 相關文件

- `../tdd-guide.md` - TDD 測案設計原則
