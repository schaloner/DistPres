package utils;

import models.BinaryContent;
import models.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve Chaloner
 */
public class PdfImageWriter implements ImageWriter
{
    private final PDDocument document;
    private final int imageType;
    private final int resolution;

    public PdfImageWriter(PDDocument document,
                          int imageType,
                          int resolution)
    {
        this.document = document;
        this.imageType = imageType;
        this.resolution = resolution;
    }

    public List<Image> writeToImages() throws IOException
    {
        List<Image> images = new ArrayList<Image>();
        List pages = document.getDocumentCatalog().getAllPages();
        for (int i = 0; i < pages.size(); i++)
        {
            PDPage page = (PDPage) pages.get(i);
            BufferedImage bufferedImage = page.convertToImage(imageType,
                                                              resolution);
            String fileName = "page" + (i + 1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,
                          "png",
                          baos);
            images.add(new Image.Builder().name(fileName)
                                          .binaryContent(new BinaryContent.Builder()
                                                                 .bytes(baos.toByteArray())
                                                                 .build())
                                          .build());
        }
        return images;
    }
}
