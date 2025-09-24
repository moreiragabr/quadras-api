package app.quadras.controller;

import app.quadras.entity.Time;
import app.quadras.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TimeController {

    private final TimeService timeService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Time>> findAll() {
        var result = timeService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Time> findById(@PathVariable Long id) {
        var result = timeService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Time> save(@RequestBody Time time) {
        var result = timeService.save(time);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Time> update(@PathVariable Long id, @RequestBody Time time) {
        var result = timeService.update(id, time);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
