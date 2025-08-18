package app.quadras.controller;

import app.quadras.entity.Time;
import app.quadras.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/times")
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping
    public ResponseEntity<List<Time>> findAll() {
        return ResponseEntity.ok(timeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Time> findById(@PathVariable Long id) {
        return ResponseEntity.ok(timeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Time> save(@RequestBody Time time) {
        return new ResponseEntity<>(timeService.save(time), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Time> update(@PathVariable Long id, @RequestBody Time time) {
        return ResponseEntity.ok(timeService.update(id, time));
    }
}
