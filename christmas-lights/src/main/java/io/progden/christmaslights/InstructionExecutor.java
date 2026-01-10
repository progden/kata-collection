package io.progden.christmaslights;

public class InstructionExecutor {
    public void execute(LightGrid lightGrid, String[] instructions) {
        for (String instruction : instructions) {
            String[] parts = instruction.split(" ");
            if (parts[0].equals("turn")) {
                String action = parts[1];
                String[] startCoords = parts[2].split(",");
                String[] endCoords = parts[4].split(",");
                int x1 = Integer.parseInt(startCoords[0]);
                int y1 = Integer.parseInt(startCoords[1]);
                int x2 = Integer.parseInt(endCoords[0]);
                int y2 = Integer.parseInt(endCoords[1]);

                if (action.equals("on")) {
                    lightGrid.open(x1, y1, x2, y2);
                } else if (action.equals("off")) {
                    lightGrid.close(x1, y1, x2, y2);
                }
            } else if (parts[0].equals("toggle")) {
                String[] startCoords = parts[1].split(",");
                String[] endCoords = parts[3].split(",");
                int x1 = Integer.parseInt(startCoords[0]);
                int y1 = Integer.parseInt(startCoords[1]);
                int x2 = Integer.parseInt(endCoords[0]);
                int y2 = Integer.parseInt(endCoords[1]);

                lightGrid.toggle(x1, y1, x2, y2);
            }
        }
    }
}
