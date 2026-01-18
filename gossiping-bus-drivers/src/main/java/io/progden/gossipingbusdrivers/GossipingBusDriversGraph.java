package io.progden.gossipingbusdrivers;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

/**
 * 圖論法實作。
 * <p>
 * 預計算首次相遇時間矩陣，使用傳播模擬找出所有司機知道全部八卦的時間。
 * 與模擬法的差異在於只處理有相遇發生的時間點，使用預計算的相遇時間。
 * 時間複雜度 O(n^2 * LCM + M * n)，M = 有相遇的時間點數量。
 * </p>
 */
public class GossipingBusDriversGraph implements GossipingBusDrivers {

    @Override
    public GossipResult calculate(List<Route> routes) {
        int n = routes.size();

        // 特例：0 或 1 位司機
        if (n <= 1) {
            return GossipResult.of(1);
        }

        // 步驟 1: 使用 MAX_MINUTES 作為檢查上限
        // 註：LCM 優化不適用於此問題，因為八卦傳播可能需要多個週期
        int checkUntil = MAX_MINUTES;

        // 步驟 2: 預計算每個時間點的相遇情況
        Map<Integer, List<Set<Integer>>> meetingsAtTime = precomputeMeetings(routes, checkUntil);

        // 步驟 3: 依時間順序模擬八卦傳播
        return simulatePropagation(n, meetingsAtTime, checkUntil);
    }

    /**
     * 預計算每個時間點的相遇群組。
     */
    private Map<Integer, List<Set<Integer>>> precomputeMeetings(List<Route> routes, int checkUntil) {
        int n = routes.size();
        Map<Integer, List<Set<Integer>>> result = io.vavr.collection.HashMap.empty();

        for (int minute = 1; minute <= checkUntil; minute++) {
            // 依站牌分組司機
            final int currentMinute = minute;
            Map<Integer, List<Integer>> driversAtStop = List.range(0, n)
                    .groupBy(driverIndex -> routes.get(driverIndex).getStopAt(currentMinute));

            // 找出有多位司機的站牌
            List<Set<Integer>> meetingGroups = driversAtStop
                    .filter(entry -> entry._2.size() > 1)
                    .map(entry -> entry._2.toSet())
                    .toList();

            if (!meetingGroups.isEmpty()) {
                result = result.put(minute, meetingGroups);
            }
        }

        return result;
    }

    /**
     * 模擬八卦傳播過程。
     */
    private GossipResult simulatePropagation(int n, Map<Integer, List<Set<Integer>>> meetingsAtTime, int checkUntil) {
        // 初始化每位司機的已知八卦
        List<Set<Integer>> knownGossips = List.range(0, n)
                .<Set<Integer>>map(HashSet::of)
                .toList();

        // 依時間順序處理相遇事件
        for (int minute = 1; minute <= checkUntil; minute++) {
            if (meetingsAtTime.containsKey(minute)) {
                List<Set<Integer>> meetingGroups = meetingsAtTime.get(minute).get();

                for (Set<Integer> group : meetingGroups) {
                    // 收集群組內所有已知八卦
                    Set<Integer> combinedGossips = group
                            .flatMap(knownGossips::get);

                    // 更新群組內每位司機的八卦
                    final List<Set<Integer>> currentGossips = knownGossips;
                    knownGossips = knownGossips.zipWithIndex()
                            .map(tuple -> {
                                int index = tuple._2;
                                Set<Integer> gossips = tuple._1;
                                return group.contains(index) ? combinedGossips : gossips;
                            })
                            .toList();
                }

                // 檢查是否完成
                if (allDriversKnowAllGossips(knownGossips, n)) {
                    return GossipResult.of(minute);
                }
            }
        }

        return GossipResult.never();
    }

    /**
     * 檢查是否所有司機都知道全部八卦。
     */
    private boolean allDriversKnowAllGossips(List<Set<Integer>> knownGossips, int totalDrivers) {
        return knownGossips.forAll(gossips -> gossips.size() == totalDrivers);
    }

    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
