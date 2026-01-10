package io.progden.christmaslights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LightGridTest2 {
    private LightGrid lightGrid;
    private InstructionReader reader = new InstructionReader();
    private InstructionExecutor instructionExecutor;

    @BeforeEach
    void setUp() {
        lightGrid = new LightGridVer2();
        reader.load();
        instructionExecutor = new InstructionExecutor();
    }

    @Test
    @DisplayName("turn on 0,0 through 0,0 would increase the total brightness by 1.")
    public void test1() {
        lightGrid.open(0, 0, 0, 0);
        int lightsOn = lightGrid.light();
        assertThat(lightsOn).isEqualTo(1);
    }

    @Test
    @DisplayName("toggle 0,0 through 999,999 would increase the total brightness by 2000000.")
    public void test2() {
        lightGrid.toggle(0, 0, 999, 999);
        int lightsOn = lightGrid.light();
        assertThat(lightsOn).isEqualTo(2000000);
    }

    @Test
    public void testExecuteInstructions() {
        instructionExecutor.execute(lightGrid, reader.instructions);
        int lightsOn = lightGrid.light();
        assertThat(lightsOn).isEqualTo(539560);
    }
}
