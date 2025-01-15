import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface FileHandlerStrategy {
    BufferedImage addWhiteBackground(BufferedImage originalImage);

    BufferedImage readImage(File file) throws IOException;

    void write(File outputFile) throws IOException;
}
