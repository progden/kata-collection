package io.progden.christmaslights;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InstructionReader {

    public String[] instructions;

    public InstructionReader() {
    }

    public void load() {
        // load instruction.txt file at the resource path
        try (java.io.InputStream is = InstructionReader.class.getResourceAsStream("/instruction.txt")) {
            if (is == null) {
                throw new RuntimeException("Resource `instruction.txt` not found on classpath");
            }
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
                java.util.List<String> lines = new java.util.ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                instructions = lines.toArray(new String[0]);
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
