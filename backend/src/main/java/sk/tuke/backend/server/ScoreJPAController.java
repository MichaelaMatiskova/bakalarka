package sk.tuke.backend.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tuke.backend.entity.Competitor;
import sk.tuke.backend.entity.Score;
import sk.tuke.backend.entity.Station;
import sk.tuke.backend.service.CompetitorService;
import sk.tuke.backend.service.ScoreException;
import sk.tuke.backend.service.ScoreService;
import sk.tuke.backend.service.StationService;

@RestController
@RequestMapping("/api/score/")
public class ScoreJPAController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private StationService stationService;
    @Autowired
    private CompetitorService competitorService;

    @PostMapping("/add")
    public ResponseEntity<?> addScore(@RequestBody Score score) {
        try {
            Station station = stationService.findById(Math.toIntExact(score.getStation().getId()));
            score.setStation(station);
            Competitor competitor = competitorService.findById(Math.toIntExact(score.getCompetitor().getId()));
            score.setCompetitor(competitor);

            Score savedScore = scoreService.addScore(score);
            return ResponseEntity.ok().body(savedScore);

        } catch (ScoreException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while adding score");
        }
    }
}
