package com.sprain6628.background_adder.service;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SvgService implements ImageService {

    private static final Logger LOGGER = Logger.getLogger(SvgService.class.getName());

    @Override
    public Image convert(File file) throws Exception {
        LOGGER.log(Level.FINE, "convert start");

        float[] svgSize = new float[0];

        try {
            svgSize = getSvgSize(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while getting size of the svg file");
            throw e;
        }

        BufferedImage bufferedImage = null;

        try {
            bufferedImage = transcodeSvgToPng(file.toURI().toString(), svgSize);
            LOGGER.log(Level.FINE, "convert succeed");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Convert failed");
            throw e;
        }

        LOGGER.log(Level.FINE, "convert end");
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private BufferedImage transcodeSvgToPng(String svgUri, float[] size) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, size[0]);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, size[1]);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            transcoder.transcode(new TranscoderInput(svgUri), new TranscoderOutput(outputStream));
        } catch (TranscoderException e) {
            LOGGER.log(Level.SEVERE, "Error while transcode");
            throw e;
        }

        try {
            return javax.imageio.ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while image read");
            throw e;
        }
    }

    private static float[] getSvgSize(File file) throws Exception {
        LOGGER.log(Level.FINE, "Start getSvgSize");

        Document document = null;
        try {
            document = loadSvgDocument(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while loading svg file");
            throw e;
        }
        Element svgElement = document.getDocumentElement();

        float width = parseDimension(svgElement.getAttribute("width"));
        float height = parseDimension(svgElement.getAttribute("height"));

        if (width <= 0 || height <= 0) {
            String viewBox = svgElement.getAttribute("viewBox");
            String[] values = viewBox.split(" ");
            if (values.length == 4) {
                width = Float.parseFloat(values[2]);
                height = Float.parseFloat(values[3]);
            }
        }

        if (width <= 0 || height <= 0) {
            throw new Exception("Can't find valid 'width/height' information");
        }

        return new float[]{width, height};
    }

    private static Document loadSvgDocument(File file) throws IOException {
        return new SAXSVGDocumentFactory(null).createDocument(file.toURI().toString());
    }

    private static float parseDimension(String dimension) {
        try {
            return Float.parseFloat(dimension.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public File addBackground(File file) throws Exception {
        LOGGER.log(Level.FINE, "Add Background start");

        Document document = null;
        try {
            document = loadSvgDocument(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while load svg document");
            throw e;
        }

        float[] size = null;
        try {
            size = getSvgSize(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while get svg size");
            throw e;
        }

        addWhiteBackground(document, size);
        File tempFile = null;
        try {
            tempFile = saveToTempFile(document);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while save document to temp file");
            throw e;
        }

        LOGGER.log(Level.FINE, "Add Background end");
        return tempFile;
    }

    private void addWhiteBackground(Document document, float[] size) {
        Element svgElement = document.getDocumentElement();
        Element backgroundRect = document.createElement("rect");
        backgroundRect.setAttribute("x", "0");
        backgroundRect.setAttribute("y", "0");
        backgroundRect.setAttribute("width", String.valueOf(size[0]));
        backgroundRect.setAttribute("height", String.valueOf(size[1]));
        backgroundRect.setAttribute("fill", "white");

        Node firstChild = svgElement.getFirstChild();
        if (firstChild != null) {
            svgElement.insertBefore(backgroundRect, firstChild);
        } else {
            svgElement.appendChild(backgroundRect);
        }
    }

    private File saveToTempFile(Document document) throws Exception {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("svg_with_background", "." + getExtension());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while create temp file");
            throw e;
        }

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            Transformer transformer = null;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException e) {
                LOGGER.log(Level.SEVERE, "Exception while create new instance of transformer");
                throw e;
            }
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            try {
                transformer.transform(new DOMSource(document), new StreamResult(outputStream));
            } catch (TransformerException e) {
                LOGGER.log(Level.SEVERE, "Exception while transform");
                throw e;
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Temp file not found");
            throw e;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while reading temp file");
            throw e;
        }
        return tempFile;
    }

    @Override
    public String getExtension() {
        return "svg";
    }
}
