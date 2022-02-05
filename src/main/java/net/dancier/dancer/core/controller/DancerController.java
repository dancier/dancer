package net.dancier.dancer.core.controller;

import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.security.CurrentUser;
import net.dancier.dancer.security.UserPrincipal;
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
    public ResponseEntity get(@CurrentUser UserPrincipal userPrincipal) {
        ProfileDto profileDto = dancerService.getProfileByUserId(userPrincipal.getId());
        return ResponseEntity.ok(profileDto);
    }

}
