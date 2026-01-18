package io.progden.gossipingbusdrivers;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

/**
 * 事件驅動法實作。
 * <p>
 * 預計算所有相遇事件，只處理有意義的時間點。
 * 時間複雜度 O(E * n)，E = 相遇事件數。
 * </p>
 */
public class GossipingBusDriversEventDriven implements GossipingBusDrivers {

    /**
     * 相遇事件記錄。
     */
    private record MeetingEvent(int minute, Set<Integer> driverIndices)
            implements Comparable<MeetingEvent> {

        @Override
        public int compareTo(MeetingEvent other) {
            return Integer.compare(this.minute, other.minute);
        }
    }

    @Override
    public GossipResult calculate(List<Route> routes) {
        // 特例：0 或 1 位司機
        if (routes.size() <= 1) {
            return GossipResult.of(1);
        }

        int totalDrivers = routes.size();

        // 使用 MAX_MINUTES 作為檢查上限
        // 註：LCM 優化不適用於此問題，因為八卦傳播可能需要多個週期
        int checkUntil = MAX_MINUTES;

        // 預計算所有相遇事件
        List<MeetingEvent> events = calculateMeetingEvents(routes, checkUntil);

        // 若無相遇事件，則永遠無法完成
        if (events.isEmpty()) {
            return GossipResult.never();
        }

        // 初始化每位司機的已知八卦
        List<Set<Integer>> knownGossips = List.range(0, totalDrivers)
                .<Set<Integer>>map(HashSet::of)
                .toList();

        // 按時間順序處理事件
        for (MeetingEvent event : events) {
            // 交換八卦
            knownGossips = exchangeGossips(knownGossips, event.driverIndices);

            // 檢查是否完成
            if (allDriversKnowAllGossips(knownGossips, totalDrivers)) {
                return GossipResult.of(event.minute);
            }
        }

        return GossipResult.never();
    }

    /**
     * 計算所有相遇事件。
     */
    private List<MeetingEvent> calculateMeetingEvents(List<Route> routes, int checkUntil) {
        int totalDrivers = routes.size();
        List<MeetingEvent> events = List.empty();

        for (int minute = 1; minute <= checkUntil; minute++) {
            // 依站牌分組
            final int currentMinute = minute;
            Map<Integer, List<Integer>> driversAtStop = List.range(0, totalDrivers)
                    .groupBy(driverIndex -> routes.get(driverIndex).getStopAt(currentMinute));

            // 找出有多位司機的站牌，建立事件
            final int eventMinute = minute;
            List<MeetingEvent> minuteEvents = driversAtStop
                    .filter(entry -> entry._2.size() > 1)
                    .map(entry -> new MeetingEvent(eventMinute, entry._2.toSet()))
                    .toList();

            events = events.appendAll(minuteEvents);
        }

        return events.sorted();
    }

    /**
     * 交換八卦。
     */
    private List<Set<Integer>> exchangeGossips(List<Set<Integer>> knownGossips, Set<Integer> driverIndices) {
        // 收集參與相遇的司機的所有八卦
        Set<Integer> combinedGossips = driverIndices
                .flatMap(knownGossips::get);

        // 更新每位參與司機的八卦
        return knownGossips.zipWithIndex()
                .map(tuple -> {
                    int index = tuple._2;
                    Set<Integer> currentGossips = tuple._1;
                    return driverIndices.contains(index) ? combinedGossips : currentGossips;
                })
                .toList();
    }

    /**
     * 計算多個路線長度的最小公倍數。
     */
    private int calculateLCM(List<Route> routes) {
        return routes.map(Route::length)
                .reduce(this::lcm);
    }

    private int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 檢查是否所有司機都知道全部八卦。
     */
    private boolean allDriversKnowAllGossips(List<Set<Integer>> knownGossips, int totalDrivers) {
        return knownGossips.forAll(gossips -> gossips.size() == totalDrivers);
    }
}
