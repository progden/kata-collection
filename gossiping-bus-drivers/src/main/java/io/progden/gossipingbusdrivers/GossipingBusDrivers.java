package io.progden.gossipingbusdrivers;

import io.vavr.collection.List;

/**
 * Gossiping Bus Drivers 問題的解決方案介面。
 * <p>
 * 計算所有司機知道全部八卦所需的最少分鐘數。
 * </p>
 */
public interface GossipingBusDrivers {

    /**
     * 工作日上限（分鐘）。
     */
    int MAX_MINUTES = 480;

    /**
     * 計算所有司機知道全部八卦所需的分鐘數。
     *
     * @param routes 每位司機的路線清單
     * @return 計算結果，包含分鐘數或 "never"
     */
    GossipResult calculate(List<Route> routes);
}
