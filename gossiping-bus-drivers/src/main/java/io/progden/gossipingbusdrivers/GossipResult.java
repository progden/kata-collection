package io.progden.gossipingbusdrivers;

import java.util.Optional;

/**
 * 八卦傳播結果封裝。
 * <p>
 * 使用 Optional 表示可能無法在時間內完成的情況。
 * </p>
 */
public record GossipResult(Optional<Integer> minutes) {

    /**
     * 建立成功結果。
     *
     * @param minutes 完成所需分鐘數
     * @return 成功結果
     */
    public static GossipResult of(int minutes) {
        return new GossipResult(Optional.of(minutes));
    }

    /**
     * 建立永遠無法完成的結果。
     *
     * @return 失敗結果
     */
    public static GossipResult never() {
        return new GossipResult(Optional.empty());
    }

    /**
     * 是否成功完成八卦傳播。
     *
     * @return true 表示成功，false 表示無法完成
     */
    public boolean isSuccess() {
        return minutes.isPresent();
    }

    /**
     * 取得結果字串表示。
     *
     * @return 分鐘數或 "never"
     */
    @Override
    public String toString() {
        return minutes.map(String::valueOf).orElse("never");
    }
}
