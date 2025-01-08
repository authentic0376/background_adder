import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// ImageProcessor 클래스: 이미지 처리 분리
class ImageProcessor {
    public static void addWhiteBackgroundToPng(File file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file);
        BufferedImage newImage = addWhiteBackgroundToImage(originalImage);

        File output = new File(file.getParent(), "processed_" + file.getName());
        ImageIO.write(newImage, "png", output);
    }

    public static void addWhiteBackgroundToSvg(File file) throws IOException {
        String content = Files.readString(file.toPath());
        String wrappedContent = "<svg xmlns=\"http://www.w3.org/2000/svg\">" +
                "<rect width=\"100%\" height=\"100%\" fill=\"white\"/>" +
                content.replace("<svg", "<g").replace("</svg>", "</g>") +
                "</svg>";

        File output = new File(file.getParent(), "processed_" + file.getName());
        Files.writeString(output.toPath(), wrappedContent);
    }

    public static BufferedImage addWhiteBackgroundToImage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int imageType = originalImage.getType();

        BufferedImage newImage = new BufferedImage(width, height, imageType == 0 ? BufferedImage.TYPE_INT_ARGB : imageType);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        return newImage;
    }
}