package io.progden.fizzbuzz;

public class FizzBuzzFreeze implements FizzBuzz {

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
}
