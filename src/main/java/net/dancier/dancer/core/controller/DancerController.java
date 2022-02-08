package net.dancier.dancer.core.controller;

import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.security.CurrentUser;
import net.dancier.dancer.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/dancer")
public class DancerController {

    @Autowired
    DancerService dancerService;

    @GetMapping
    public ResponseEntity get(@CurrentUser AuthenticatedUser authenticatedUser) {
        ProfileDto profileDto = dancerService.getProfileByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(profileDto);
    }

}
