package io.progden.gossipingbusdrivers;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

/**
 * 模擬法實作。
 * <p>
 * 逐分鐘模擬所有司機的移動與相遇，時間複雜度 O(480 * n^2)。
 * </p>
 */
public class GossipingBusDriversSimulation implements GossipingBusDrivers {

    @Override
    public GossipResult calculate(List<Route> routes) {
        // 特例：0 或 1 位司機
        if (routes.size() <= 1) {
            return GossipResult.of(1);
        }

        int totalDrivers = routes.size();

        // 初始化每位司機的已知八卦（每人只知道自己的八卦）
        // 使用司機索引作為八卦 ID
        List<Set<Integer>> knownGossips = routes.zipWithIndex()
                .<Set<Integer>>map(tuple -> HashSet.of(tuple._2))
                .toList();

        // 逐分鐘模擬
        for (int minute = 1; minute <= MAX_MINUTES; minute++) {
            // 依站牌分組司機索引
            final int currentMinute = minute;
            Map<Integer, List<Integer>> driversAtStop = List.range(0, totalDrivers)
                    .groupBy(driverIndex -> routes.get(driverIndex).getStopAt(currentMinute));

            // 同站的司機交換八卦
            knownGossips = exchangeGossipsAtStops(knownGossips, driversAtStop);

            // 檢查是否所有司機都知道全部八卦
            if (allDriversKnowAllGossips(knownGossips, totalDrivers)) {
                return GossipResult.of(minute);
            }
        }

        return GossipResult.never();
    }

    /**
     * 同站的司機交換八卦。
     */
    private List<Set<Integer>> exchangeGossipsAtStops(
            List<Set<Integer>> knownGossips,
            Map<Integer, List<Integer>> driversAtStop) {

        // 建立可變的八卦列表副本
        @SuppressWarnings("unchecked")
        Set<Integer>[] gossipsArray = knownGossips.toJavaArray(Set[]::new);

        // 處理每個站牌的司機群組
        driversAtStop.values()
                .filter(group -> group.size() > 1)
                .forEach(group -> exchangeGossipsInGroup(gossipsArray, group));

        return List.of(gossipsArray);
    }

    /**
     * 群組內所有司機互相交換八卦。
     */
    private void exchangeGossipsInGroup(Set<Integer>[] gossipsArray, List<Integer> group) {
        // 收集群組內所有已知八卦
        Set<Integer> combinedGossips = group
                .flatMap(driverIndex -> gossipsArray[driverIndex])
                .toSet();

        // 每位司機都學習所有八卦
        group.forEach(driverIndex -> gossipsArray[driverIndex] = combinedGossips);
    }

    /**
     * 檢查是否所有司機都知道全部八卦。
     */
    private boolean allDriversKnowAllGossips(List<Set<Integer>> knownGossips, int totalDrivers) {
        return knownGossips.forAll(gossips -> gossips.size() == totalDrivers);
    }
}
