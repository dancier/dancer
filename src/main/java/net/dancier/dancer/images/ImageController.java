package net.dancier.dancer.images;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    @PostMapping("")
    public ResponseEntity upload(@RequestParam("file")MultipartFile multipartFile) {
        log.info("Upload received..." + multipartFile);
        imageService.store(multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{hash}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getOriginal(@PathVariable("hash") String hash) throws IOException {
        return imageService.load(hash);
    }

    @GetMapping(value = "{hash}/{width}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getScaled(@PathVariable("hash") String hash, @PathVariable("width") Integer width) throws IOException {
        log.info("Getting Scaled Version..." + width);
        return imageService.load(hash, width);
    }
}
