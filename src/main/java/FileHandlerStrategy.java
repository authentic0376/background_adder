import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

interface FileHandlerStrategy {
    Object[] processFile(File file) throws IOException;

    void write(Object processedImage, Path outputPath) throws IOException, TransformerException;
}
