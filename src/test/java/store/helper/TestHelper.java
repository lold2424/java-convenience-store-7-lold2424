package store.helper;

import java.io.*;

public class TestHelper {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream testIn;

    public void setUpOutputCapture() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    public void restoreOutput() {
        System.setOut(originalOut);
    }

    public String getOutput() {
        return outContent.toString();
    }

    public void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    public void restoreInput() {
        System.setIn(originalIn);
    }

    public void tearDown() {
        restoreInput();
        restoreOutput();
    }
}
