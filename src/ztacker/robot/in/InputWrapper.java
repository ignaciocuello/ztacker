package ztacker.robot.in;

import java.io.File;
import ztacker.io.GridCapturerReader;
import ztacker.io.ObjectLocatorReader;
import ztacker.io.QueueCapturerReader;

public class InputWrapper {

    private static final int GAME_CORNER_INDEX = 0;
    private static final int START_CORNER_INDEX = 1;
    private static final int RESTART_CORNER_INDEX = 2;
    private static final int PLAY_CORNER_INDEX = 3;

    private final GridCapturer gridCapturer;
    private final ObjectLocator objectLocator;
    private final QueueCapturer queueCapturer;

    public InputWrapper(File gcFile, File olFile, File qcFile) {
        gridCapturer = generateGridCapturer(gcFile);
        objectLocator = generateObjectLocator(olFile);
        queueCapturer = generateQueueCapturer(qcFile);
    }

    private GridCapturer generateGridCapturer(File file) {
        GridCapturerReader reader = new GridCapturerReader(file);
        reader.readData();

        return new GridCapturer(reader.getGridX(), reader.getGridY(),
                reader.getGridUpperY(), reader.getGridXStep(),
                reader.getGridYStep());
    }

    private ObjectLocator generateObjectLocator(File file) {
        ObjectLocatorReader reader = new ObjectLocatorReader(file);

        final int[][] corners = reader.readCorners();

        return new ObjectLocator(
                corners[GAME_CORNER_INDEX],
                corners[START_CORNER_INDEX],
                corners[RESTART_CORNER_INDEX],
                corners[PLAY_CORNER_INDEX]);
    }

    private QueueCapturer generateQueueCapturer(File file) {
        QueueCapturerReader reader = new QueueCapturerReader(file);
        reader.readData();

        return new QueueCapturer(reader.getColorMap(),
                reader.getQueueX(), reader.getQueueY());
    }
    
    public final GridCapturer getGridCapturer() {
        return gridCapturer;
    }
    
    public final ObjectLocator getObjectLocator() {
        return objectLocator;
    }
    
    public final QueueCapturer getQueueCapturer() {
        return queueCapturer;
    }
}
