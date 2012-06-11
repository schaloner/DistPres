package utils;

import models.BinaryContent;
import models.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve Chaloner
 */
public class PptImageWriter implements ImageWriter
{
    private final SlideShow slideShow;

    public PptImageWriter(SlideShow slideShow)
    {
        this.slideShow = slideShow;
    }

    public List<Image> writeToImages() throws IOException
    {
        List<Image> images = new ArrayList<Image>();
        Dimension pageSize = slideShow.getPageSize();
        Slide[] slides = slideShow.getSlides();
        for (int i = 0; i < slides.length; i++)
        {
            BufferedImage bufferedImage = new BufferedImage(pageSize.width,
                                                            pageSize.height,
                                                            BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0,
                                                pageSize.width,
                                                pageSize.height));

            slides[i].draw(graphics);

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
