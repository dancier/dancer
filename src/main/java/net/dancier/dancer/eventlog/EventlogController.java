package net.dancier.dancer.eventlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eventlog")
public class EventlogController {

    @Autowired
    EventlogDAO eventlogDAO;

    @GetMapping
    public ResponseEntity getCount() {
        return ResponseEntity.ok(eventlogDAO.getCountOfEventlogEntries());
    }
}
