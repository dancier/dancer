package net.dancier.dancer.images;

import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.core.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Optional;

@Service
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private static final String ORIGINAL_FILE_NAME = "original.png";
    private final Path imageLocation;

    @Autowired
    public ImageService(ImageProperties imageProperties) {
        this.imageLocation = Paths.get(imageProperties.getDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageLocation);
        } catch (Exception ex) {
            throw new AppliationException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public DancierImage store(MultipartFile multipartFile) {
        String sha256String = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] allBytes = multipartFile.getInputStream().readAllBytes();
            byte[] sha256byteArray = md.digest(allBytes);
            sha256String =  new String(Hex.encode(sha256byteArray));
            Path targetLocation = this.imageLocation.resolve(sha256String);
            Files.createDirectory(targetLocation);
            Files.write(targetLocation.resolve(ORIGINAL_FILE_NAME), allBytes);
        } catch (Exception noSuchAlgorithmException) {
            new AppliationException("Unable to save ...");
        }
        return new DancierImage(sha256String);
    }
    public byte[] load(String hash) throws IOException {
        Path path = imageLocation.resolve(hash).resolve(ORIGINAL_FILE_NAME);
        try {
            return Files.readAllBytes(path);
        } catch (NoSuchFileException noSuchFileException) {
            throw new NotFoundException("This file could not be resolved" , noSuchFileException);
        }
    }

    public byte[] load(String hash, Integer width) throws IOException {
        scaleIfNeccessary(hash, width);
        Path path = imageLocation.resolve(hash).resolve(width.toString() +".png");
        try {
            return Files.readAllBytes(path);
        } catch (NoSuchFileException noSuchFileException) {
            throw new NotFoundException("This file could not be resolved" , noSuchFileException);
        }
    }

    void scaleIfNeccessary(String hash, Integer width) throws IOException {
        Path targetPath = imageLocation.resolve(hash).resolve(width.toString() + ".png");
        if (!Files.exists(targetPath)) {

            Path original = imageLocation.resolve(hash).resolve(ORIGINAL_FILE_NAME);
            BufferedImage originalImage = ImageIO.read(original.toFile());
            Integer originalWidth = originalImage.getWidth();
            Integer originalHeight = originalImage.getHeight();
            Double ratio = new Double( originalHeight) / new Double(originalWidth);

            Integer targetHeight = new Double(width * ratio).intValue();

            Image resultingImage = originalImage.getScaledInstance(
                    width, targetHeight, Image.SCALE_DEFAULT
            );
            BufferedImage targetBufferedImage = new BufferedImage(width, targetHeight, BufferedImage.TYPE_INT_RGB);
            targetBufferedImage.getGraphics().drawImage(resultingImage, 0,0,null);
            ImageIO.write(targetBufferedImage,"png", targetPath.toFile());
        }
    }
}
