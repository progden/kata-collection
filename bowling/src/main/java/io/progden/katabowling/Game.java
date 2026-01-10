package io.progden.katabowling;

/**
 * @author Dennis.Chang
 * @description 保齡球規則
 * 遊戲共10局。每局玩家有兩次投擲機會，目標是擊倒10個球瓶。該局得分是擊倒的球瓶總數，加上全中和補中獎勵分數。
 * <p>
 * 補中是指玩家在兩次投擲中擊倒全部10個球瓶。該局的獎勵分數是下一輪投擲擊倒的球瓶數量。
 * <p>
 * 全中是指玩家第一次投擲就擊倒全部10個球瓶。之後只需再投一次即可完成該局。該局的獎勵分數等於接下來兩次投擲的總分。
 * <p>
 * 在第十局中，如果一名球員打出補中或全中，他可以投出剩餘的球來完成該局。但是，在第十局中，最多只能投出三球。
 * @date 2026/1/10
 */
public class Game {
    int rolls[] = new int[21];
    private int rollIndex = 0;

    /**
     * @param pins number of pins knocked down
     * @description void roll(int) is called each time the player rolls a ball. The argument is the number of pins knocked down.
     * @author Dennis.Chang
     * @date 2026/1/10
     */
    public void roll(int pins) {
        rolls[rollIndex++] = pins;
    }

    /**
     * @return the total score for that game.
     * @description int score() returns the total score for that game.
     * @author Dennis.Chang
     * @date 2026/1/10
     */
    public int score() {
        int score = 0;
        int frameIndex = 0;
        for (int frame = 0; frame < 10; frame++) {
            if (rolls[frameIndex] == 10) {
                score += rolls[frameIndex]; // strike
                score += rolls[frameIndex + 1] + rolls[frameIndex + 2]; // bonus
                frameIndex += 1;
            } else if (rolls[frameIndex] + rolls[frameIndex + 1] == 10) {
                score += rolls[frameIndex] + rolls[frameIndex + 1]; // spare
                score += rolls[frameIndex + 2]; // bonus
                frameIndex += 2;
            } else {
                score += rolls[frameIndex] + rolls[frameIndex + 1];
                frameIndex += 2;
            }
        }
        return score;
    }
}
