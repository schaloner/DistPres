package utils;

import models.Image;

import java.io.IOException;
import java.util.List;

/**
 * @author Steve Chaloner
 */
public interface ImageWriter
{
    List<Image> writeToImages() throws IOException;
}
