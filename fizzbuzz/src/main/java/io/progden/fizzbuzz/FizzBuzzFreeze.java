package io.progden.fizzbuzz;

public class FizzBuzzFreeze implements FizzBuzz {
    public String fizzBuzz2(int i) {
        // rule 4
        if (i % 3 == 0 && i % 5 == 0) {
            return "Dip!";
        }

        // rule 2 & 3
        if (i % 3 == 0)
            return "Kuksa!";

        if (i % 5 == 0)
            return "Sauna!";

        // rule 1
        return "" + i;
    }

//    // STEP 0: 初始版本
//    public String fizzBuzz(int i) {
//        var output = new StringBuilder();
//        if (i % 3 == 0 || i % 5 == 0) {
//            if (i % 3 == 0)
//                output.append("Fizz");
//            if (i % 5 == 0)
//                output.append("Buzz");
//        } else {
//            output.append(i);
//        }
//        return output.toString();
//    }

    public String fizzBuzz(int i) {
        // rule 4
        if (i % 3 == 0 && i % 5 == 0)
            return "Dip!";

        // rule 3
        if (i % 5 == 0)
            return "Sauna!";

        // rule 2
        if (i % 3 == 0)
            return "Kuksa!";

        // rule 1
        return "" + i;
    }

    // Final Version: 把規則調整順序之後 會發現比較合理，只是順序反過來了
    // WHY？ 說明的順序是從最基本的規則開始說起 (Rule 1 -> Rule 4)，但是寫程式的時候如果從最簡單的規則開始寫，其實等於要把所有規則都檢查一次
    // 才能確定符合最基本的規則，這樣會導致程式碼效率較低且不易閱讀
    // 因此在實作時，通常會從最複雜或最特殊的規則開始寫起，這樣可以更快地排除不符合條件的情況，提升程式碼的效率和可讀性
    // 因此在這個例子中，先檢查 Rule 4，再檢查 Rule 3 和 Rule 2，最後才是 Rule 1，這樣的順序更符合程式設計的邏輯
//    public String fizzBuzz(int i) {
//        // rule 4
//        if (i % 3 == 0 && i % 5 == 0)
//            return "Dip!";
//
//        // rule 3
//        if (i % 5 == 0)
//            return "Sauna!";
//
//        // rule 2
//        if (i % 3 == 0)
//            return "Kuksa!";
//
//        // rule 1
//        return "" + i;
//    }

    // STEP 2.1: 提早回傳移除沒有用的 StringBuilder，但是最後的 return 空白很奇怪 跑出了之前沒有的邏輯
//    public String fizzBuzz(int i) {
//        // rule 1
//        if (i % 3 != 0 && i % 5 != 0) {
//            return "" + i;
//        }
//
//        // rule 4
//        if (i % 3 == 0 && i % 5 == 0) {
//            return "Dip!";
//        }
//
//        // rule 2 & 3
//        if (i % 3 == 0)
//            return "Kuksa!";
//        if (i % 5 == 0)
//            return "Sauna!";
//
//        return ""; // ??
//    }

    // STEP 2: 處理 Rule 4: 3 和 5 的公倍數
//    public String fizzBuzz(int i) {
//        // rule 1
//        if (i % 3 != 0 && i % 5 != 0) {
//            return "" + i;
//        }
//
//        // rule 4
//        if (i % 3 == 0 && i % 5 == 0) {
//            return "Dip!";
//        }
//
//        // rule 2 & 3
//        var output = new StringBuilder();
//        if (i % 3 == 0)
//            output.append("Kuksa!");
//        if (i % 5 == 0)
//            output.append("Sauna!");
//
//        return output.toString();
//    }


    // Run Test -> Case 4 on FizzBuzzFreezeTest will fail
//    public String fizzBuzz(int i) {
//        // rule 1
//        if (i % 3 != 0 && i % 5 != 0) {
//            return "" + i;
//        }
//
//        var output = new StringBuilder();
//        // rule 2 & 3 & 4?
//        if (i % 3 == 0)
//            output.append("Kuksa!");
//        if (i % 5 == 0)
//            output.append("Sauna!");
//
//        return output.toString();
//    }

    // STEP 1.2: 移除不必要的 StringBuilder
//    public String fizzBuzz(int i) {
//        // rule 1
//        if (i % 3 != 0 && i % 5 != 0) {
//            return "" + i;
//        }
//
//        var output = new StringBuilder();
//        if (i % 3 == 0)
//            output.append("Fizz");
//        if (i % 5 == 0)
//            output.append("Buzz");
//
//        return output.toString();
//    }

    // STEP 1.1: 提早回傳
//    public String fizzBuzz(int i) {
//        var output = new StringBuilder();
//        if (i % 3 != 0 && i % 5 != 0) {
//            output.append(i);
//            return output.toString();
//        }
//
//        if (i % 3 == 0)
//            output.append("Fizz");
//        if (i % 5 == 0)
//            output.append("Buzz");
//
//        return output.toString();
//    }

//    // STEP 1: 反轉條件式
//    public String fizzBuzz(int i) {
//        var output = new StringBuilder();
//        if (i % 3 != 0 && i % 5 != 0) {
//            output.append(i);
//        } else {
//            if (i % 3 == 0)
//                output.append("Fizz");
//            if (i % 5 == 0)
//                output.append("Buzz");
//        }
//        return output.toString();
//    }
}
