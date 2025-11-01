package sk.tuke.backend.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.tuke.backend.entity.Competitor;
import sk.tuke.backend.service.CompetitorException;
import sk.tuke.backend.service.CompetitorService;

@Controller
@RequestMapping("/api/competitors/")
public class CompetitorJPAController {

    @Autowired
    private CompetitorService competitorService;


    @GetMapping("findBy/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try {
            Competitor competitor = competitorService.findById(Integer.parseInt(id));
            //System.out.println(competitor.getName());
            return ResponseEntity.ok(competitor);
        } catch (CompetitorException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    } @PostMapping("/register")
    public ResponseEntity<Competitor>  register(@RequestBody Competitor competitor) {

        Competitor savedCompetitor = competitorService.register(competitor);
        return ResponseEntity.ok(savedCompetitor);

    }

}
