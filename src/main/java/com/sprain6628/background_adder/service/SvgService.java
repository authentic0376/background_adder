package com.sprain6628.background_adder.service;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SvgService implements ImageService {

    private static final Logger LOGGER = Logger.getLogger(SvgService.class.getName());

    @Override
    public Image convert(File file) {
        LOGGER.log(Level.FINE, "convert start");
        try {
            float[] svgSize = getSvgSize(file);
            BufferedImage bufferedImage = transcodeSvgToPng(file.toURI().toString(), svgSize);
            LOGGER.log(Level.FINE, "convert end");
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SVG 변환 중 오류 발생", e);
            return null;
        }
    }

    private BufferedImage transcodeSvgToPng(String svgUri, float[] size) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, size[0]);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, size[1]);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transcoder.transcode(new TranscoderInput(svgUri), new TranscoderOutput(outputStream));

        return javax.imageio.ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    private static float[] getSvgSize(File file) {
        LOGGER.log(Level.FINE, "Start getSvgSize");
        try {
            Document document = loadSvgDocument(file);
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
                throw new IllegalArgumentException("유효한 width/height 정보를 찾을 수 없습니다.");
            }
            return new float[]{width, height};
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SVG 크기 추출 중 오류 발생", e);
        }
        return null;
    }

    private static Document loadSvgDocument(File file) throws Exception {
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
    public File addBackground(File file) {
        LOGGER.log(Level.FINE, "Add Background start");
        try {
            Document document = loadSvgDocument(file);
            float[] size = getSvgSize(file);
            assert size != null;
            addWhiteBackground(document, size);
            File tempFile = saveToTempFile(document);
            LOGGER.log(Level.FINE, "Add Background end");
            return tempFile;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "SVG 배경 추가 중 오류 발생", e);
            return null;
        }
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
        File tempFile = File.createTempFile("svg_with_background", "." + getExtension());
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        }
        return tempFile;
    }

    @Override
    public String getExtension() {
        return "svg";
    }
}
