package io.progden.christmaslights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LightGridTest {
    private LightGrid lightGrid;
    private InstructionReader reader = new InstructionReader();
    private InstructionExecutor instructionExecutor;

    @BeforeEach
    void setUp() {
        lightGrid = new LightGridVer1();
        reader.load();
        instructionExecutor = new InstructionExecutor();
    }

    @Test
    public void readInstructions() {
        assertThat(reader.instructions.length).isEqualTo(9);
    }

    /**
     * ex.
     * turn on 887,9 through 959,629
     * turn off 539,243 through 559,965
     * toggle 831,394 through 904,860
     *
     * @description
     * @author Dennis.Chang
     * @date 2026/1/11
     */
    @Test
    public void testExecuteInstuctions() {
        instructionExecutor.execute(lightGrid, reader.instructions);
        int lightsOn = lightGrid.light();
        assertThat(lightsOn).isEqualTo(230022);
    }
}
