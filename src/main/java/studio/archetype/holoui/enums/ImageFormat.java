package studio.archetype.holoui.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

@AllArgsConstructor
public enum ImageFormat {
    PNG(new String[]{ "png" }, "image/png"),
    JPEG(new String[]{ "jpg", "jpeg" }, "image/jpeg"),
    WEBP(new String[]{ "webp" }, "image/webp");

    private final String[] extensions;
    private final String mimeType;

    public ImageReader getReader() throws IOException {
        Iterator<ImageReader> r = ImageIO.getImageReadersByMIMEType(mimeType);
        try {
            return r.next();
        } catch(NoSuchElementException e) {
            throw new IOException("Missing reader for image type \"" + mimeType + "\"!");
        }
    }

    public static ImageFormat getFormat(File f) throws IOException {
        return Arrays.stream(ImageFormat.values())
                .filter(format -> FilenameUtils.isExtension(f.getName(), format.extensions))
                .findFirst()
                .orElseThrow(() -> {
                    String extension = FilenameUtils.getExtension(f.getName());
                    if(extension.equalsIgnoreCase("gif"))
                        return new IOException("GIF provided as source for textImage. Please use a \"animatedTextImage\" component instead.");
                    else
                        return new IOException("Unknown image format \"" + FilenameUtils.getExtension(f.getName()) + "\"!");
                });
    }
}
