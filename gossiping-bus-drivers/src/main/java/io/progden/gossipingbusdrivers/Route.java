package io.progden.gossipingbusdrivers;

import io.vavr.collection.List;

/**
 * 公車路線模型。
 * <p>
 * 路線以站牌序列表示，循環行駛。
 * </p>
 */
public record Route(List<Integer> stops) {

    /**
     * 建立路線。
     *
     * @param stops 站牌序列
     * @throws IllegalArgumentException 如果站牌序列為空
     */
    public Route {
        if (stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("路線不可為空");
        }
    }

    /**
     * 便利建構方法。
     *
     * @param stops 站牌編號
     * @return 路線
     */
    public static Route of(int... stops) {
        if (stops == null || stops.length == 0) {
            throw new IllegalArgumentException("路線不可為空");
        }
        return new Route(List.ofAll(stops));
    }

    /**
     * 取得在指定分鐘時所在的站牌。
     * <p>
     * 分鐘從 1 開始計算，使用模運算處理循環。
     * </p>
     *
     * @param minute 分鐘數（1-based）
     * @return 站牌編號
     */
    public int getStopAt(int minute) {
        int index = (minute - 1) % stops.length();
        return stops.get(index);
    }

    /**
     * 路線長度（站數）。
     *
     * @return 站數
     */
    public int length() {
        return stops.length();
    }
}
