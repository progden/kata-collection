package io.progden.christmaslights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LightGridCounterTest {
    private LightGrid lightGrid;
    private InstructionReader reader = new InstructionReader();
    private InstructionExecutor instructionExecutor;

    @BeforeEach
    void setUp() {
        lightGrid = new LightGridCounter();
        reader.load();
        instructionExecutor = new InstructionExecutor();
    }

    @Test
    public void testExecuteInstuctions() {
        instructionExecutor.execute(lightGrid, reader.instructions);
        LightGridCounter counter = (LightGridCounter) lightGrid;
        assertEquals(3, counter.getOpenCount());
        assertEquals(4, counter.getCloseCount());
        assertEquals(2, counter.getToggleCount());
    }
}
