package utils;

import models.Presentation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @author Steve Chaloner
 */
public class ContentImporter
{
    public boolean importContent(String fileName,
                                 String contentType,
                                 File file)
    {
        boolean ok = true;
        try
        {
            Presentation presentation = null;
            if (contentType.equals("application/pdf"))
            {
                presentation = convertFromPdf(fileName,
                                              file);
            }
            else if (fileName.endsWith(".ppt"))
            {
                presentation = convertFromPpt(fileName,
                                              file);
            }
            if (presentation != null)
            {
                presentation.save();
            }
        }
        catch (IOException e)
        {
            ok = false;
        }

        return ok;
    }

    private static Presentation convertFromPpt(String fileName,
                                               File file) throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        SlideShow slideShow = new SlideShow(fis);
        fis.close();

        Presentation.Builder presentation = new Presentation.Builder().currentPosition(0)
                                                                      .name(fileName)
                                                                      .uploadDate(new Date());

        ImageWriter imageWriter = new PptImageWriter(slideShow);
        presentation.images(imageWriter.writeToImages());

        return presentation.build();
    }

    private static Presentation convertFromPdf(String fileName,
                                               File file) throws IOException
    {
        String color = "rgb";
        int resolution = 96;

        Presentation.Builder presentation = new Presentation.Builder().currentPosition(0)
                                                                      .name(fileName)
                                                                      .uploadDate(new Date());
        PDDocument document = null;
        try
        {
            document = PDDocument.load(file);
            int imageType = 24;
            if ("bilevel".equalsIgnoreCase(color))
            {
                imageType = BufferedImage.TYPE_BYTE_BINARY;
            }
            else if ("indexed".equalsIgnoreCase(color))
            {
                imageType = BufferedImage.TYPE_BYTE_INDEXED;
            }
            else if ("gray".equalsIgnoreCase(color))
            {
                imageType = BufferedImage.TYPE_BYTE_GRAY;
            }
            else if ("rgb".equalsIgnoreCase(color))
            {
                imageType = BufferedImage.TYPE_INT_RGB;
            }
            else if ("rgba".equalsIgnoreCase(color))
            {
                imageType = BufferedImage.TYPE_INT_ARGB;
            }
            else
            {
                System.err.println("Error: the number of bits per pixel must be 1, 8 or 24.");
            }

            //Make the call
            ImageWriter imageWriter = new PdfImageWriter(document,
                                                         imageType,
                                                         resolution);
            presentation.images(imageWriter.writeToImages());
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (document != null)
            {
                document.close();
            }
        }

        return presentation.build();
    }
}
