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
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvgService implements ImageService {

    private static final Logger LOGGER = Logger.getLogger(SvgService.class.getName());

    @Override
    public Image convert(File file) {
        LOGGER.log(Level.FINE, "convert start");
        BufferedImage bufferedImage = null;
        try {
            String svgURI = file.toURI().toString();
            LOGGER.log(Level.FINE, "svgURI: " + svgURI);
            float width = -1;
            float height = -1;
            float[] svgSize = getSvgSize(file);
            width = svgSize[0];
            height = svgSize[1];
            LOGGER.log(Level.FINE, "width: " + width);
            LOGGER.log(Level.FINE, "height: " + height);

            TranscoderInput input = new TranscoderInput(svgURI);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);

            transcoder.transcode(input, output);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            bufferedImage = javax.imageio.ImageIO.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.log(Level.FINE, "convert end");
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static float[] getSvgSize(File file) {
        LOGGER.log(Level.FINE, "Start getSvgSize");
        float width = -1;
        float height = -1;
        try {
            String uri = file.toURI().toString();

            // Batik을 사용하여 SVG 문서 읽기
            Document document = null;
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(null);
            LOGGER.log(Level.FINE, "factory: " + factory);
            document = factory.createDocument(uri);
            LOGGER.log(Level.FINE, "document: " + document);

            // 루트 <svg> 요소 가져오기
            Element svgElement = document.getDocumentElement();

            // width, height 속성 읽기
            String widthStr = svgElement.getAttribute("width");
            String heightStr = svgElement.getAttribute("height");
            String viewBox = svgElement.getAttribute("viewBox");


            if (!widthStr.isEmpty() && !heightStr.isEmpty()) {
                width = Float.parseFloat(widthStr.replaceAll("[^0-9.]", ""));
                height = Float.parseFloat(heightStr.replaceAll("[^0-9.]", ""));
            } else if (!viewBox.isEmpty()) {
                String[] values = viewBox.split(" ");
                if (values.length == 4) {
                    width = Float.parseFloat(values[2]);
                    height = Float.parseFloat(values[3]);
                }
            }

            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("SVG 파일에서 유효한 width/height 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new float[]{width, height};
    }

    @Override
    public File addBackground(File file) {
        LOGGER.log(Level.FINE, "Add Background start");
        File tempFile = null;
        try {
            String uri = file.toURI().toString();

            // Batik을 사용하여 SVG 문서 읽기
            Document document;
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(null);
            document = factory.createDocument(uri);


            // SVG 크기 얻기
            float[] size = getSvgSize(file);
            float width = size[0];
            float height = size[1];

            // <svg> 요소 가져오기
            Element svgElement = document.getDocumentElement();

            // 기존 <svg> 내에서 첫 번째 요소 찾기
            Node firstChild = svgElement.getFirstChild();

            // 흰색 배경 <rect> 추가
            Element backgroundRect = document.createElement("rect");
            backgroundRect.setAttribute("x", "0");
            backgroundRect.setAttribute("y", "0");
            backgroundRect.setAttribute("width", String.valueOf(width));
            backgroundRect.setAttribute("height", String.valueOf(height));
            backgroundRect.setAttribute("fill", "white");

            // <svg> 요소의 가장 앞에 배경 추가
            if (firstChild != null) {
                svgElement.insertBefore(backgroundRect, firstChild);
            } else {
                svgElement.appendChild(backgroundRect);
            }

            // 임시 파일 생성
            tempFile = File.createTempFile("svg_with_background", ".svg");

            // 수정된 SVG 파일 저장 (Transformer 사용)
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(outputStream);
                transformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        LOGGER.log(Level.FINE, "Add Background end");
        return tempFile;
    }


    @Override
    public String getExtension() {
        return "svg";
    }


}
