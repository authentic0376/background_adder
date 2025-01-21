import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SvgHandler implements FileHandlerStrategy {
    @Override
    public Object[] processFile(File file) {
        return new Object[]{file, addBackground(file)};
    }

    private File addBackground(File originalFile) {

        try {
            // XML Parser Configuration
            // 1. Use Batik's SAXSVGDocumentFactory to load the SVG document
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
            Document doc = factory.createDocument(originalFile.toURI().toString());

            // Retrieve the SVG root element
            Element root = doc.getDocumentElement();
            String svgNamespace = root.getNamespaceURI();

            // Create a <rect> element for the background
            Element backgroundRect = doc.createElementNS(svgNamespace, "rect");
            backgroundRect.setAttribute("x", "0");
            backgroundRect.setAttribute("y", "0");
            backgroundRect.setAttribute("width", "100%");
            backgroundRect.setAttribute("height", "100%");
            backgroundRect.setAttribute("fill", "white");

            // Add the <rect> element to the front (place it behind other elements as the background)
            Node firstChild = root.getFirstChild();
            if (firstChild != null) {
                root.insertBefore(backgroundRect, firstChild);
            } else {
                root.appendChild(backgroundRect);
            }


            // 1. Convert the Document to a string
            StringWriter writer = new StringWriter();
            DOMUtilities.writeDocument(doc, writer);
            String svgContent = writer.toString();

            // 2. Save the string to a temporary file
            File tempFile = File.createTempFile("tempSvg", ".svg");
            System.out.println("임시파일 위치" + System.getProperty("java.io.tmpdir"));
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                fileWriter.write(svgContent);
            }
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(Object processedImage, Path outputPath) {
        File tempFile = (File) processedImage;
        Path targetPath = Paths.get(outputPath.toString() + ".svg");

        try {
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }
            Files.move(tempFile.toPath(), targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
