package net.dancier.dancer.images;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.core.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    @Secured(ROLE_USER)
    @PostMapping("")
    public ResponseEntity<DancierImage> upload(@RequestParam("file")MultipartFile multipartFile) {
        log.info("Upload received..." + multipartFile);
        return ResponseEntity.ok(imageService.store(multipartFile));
    }

    @Secured(ROLE_USER)
    @GetMapping(value = "{hash}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getOriginal(@PathVariable("hash") String hash) throws IOException {
        return imageService.load(hash);
    }

    @Secured(ROLE_USER)
    @GetMapping(value = "{hash}/{width}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getScaled(@PathVariable("hash") String hash, @PathVariable("width") Integer width) throws IOException {
        log.info("Getting Scaled Version..." + width);
        return imageService.load(hash, width);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handle(NotFoundException ae) {
        return ae.getLocalizedMessage();
    }

}
