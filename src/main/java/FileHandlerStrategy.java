import java.io.File;
import java.io.IOException;

interface FileHandlerStrategy {
    Object[] processFile(File file) throws IOException;

    void write(Object processedImage, File outputFile) throws IOException;
}
