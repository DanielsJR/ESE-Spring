package nx.ese.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class NxUtilBase64Image {

    private static final Logger logger = LoggerFactory.getLogger(NxUtilBase64Image.class);

    public NxUtilBase64Image() {
        throw new IllegalStateException("Utility class");
    }

    public static String encoder(String imagePath) {
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            String base64Image = "";
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
            return base64Image;
        } catch (FileNotFoundException e) {
            logger.error("Image not found {}", e.getMessage());
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image {}", ioe.getMessage());
        }
        return null;
    }

    public static void decoder(String base64Image, String pathFile) {
        try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
            byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            logger.error("Image not found {}", e.getMessage());
        } catch (IOException ioe) {
            logger.error("Exception while reading the Image {}", ioe.getMessage());
        }
    }
}