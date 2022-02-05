package net.dancier.dancer.core;

import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.security.CurrentUser;
import net.dancier.dancer.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private DancerService dancerService;

    @GetMapping
    public ResponseEntity get(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(
                dancerService.getProfileByUserId(userPrincipal.getId())
        );
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        System.out.println("");
        return ResponseEntity.notFound().build();
    }
}
