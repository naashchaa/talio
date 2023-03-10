package server.api;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final Random random;
    private final TaskRepository repo;

    public TaskController(Random random, TaskRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public List<Task> getAll() {
        return repo.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Task> add(@RequestBody Task task) {

        if (task.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        Task saved = repo.save(task);
        return ResponseEntity.ok(saved);
    }



}
