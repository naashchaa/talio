package server.api;

import commons.Task;
import commons.TaskList;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
        return this.repo.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

    @GetMapping(path = "/get_by_parent")
    public ResponseEntity<List<Task>> getAllByParentTaskList(@RequestBody TaskList list) {
        return ResponseEntity.ok(this.repo.getAllByParentTaskList(list));
    }

    /** This is a POST endpoint for adding a new Task.
     * @param task The Task sent through in the request body
     * @return The response (whether the creation was successful)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Task> add(@RequestBody Task task) {

        if (task.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        Task saved = this.repo.save(task);
        return ResponseEntity.ok(saved);
    }

    @PostMapping(path = "{id}/update")
    public ResponseEntity<Task> update(@PathVariable("id") long id, @RequestBody Task modifiedTask) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Task task = this.repo.findById(id).get();
        System.out.println(task.id);

        if (task.equals(modifiedTask))
            return ResponseEntity.badRequest().build();

        if (task.getName() != modifiedTask.getName())
            task.setName(modifiedTask.getName());

        if (task.getDescription() != modifiedTask.getDescription())
            task.setDescription(modifiedTask.getDescription());

        if (task.getParentTaskList() != modifiedTask.getParentTaskList())
            task.setParentTaskList(modifiedTask.getParentTaskList());

        if (task.getNextTask() != modifiedTask.getNextTask())
            task.setNextTask(modifiedTask.getNextTask());

        if (task.getPrevTask() != modifiedTask.getPrevTask())
            task.setPrevTask(modifiedTask.getPrevTask());

        if (task.getParentTask() != modifiedTask.getParentTask())
            task.setParentTask(modifiedTask.getParentTask());

        this.repo.save(task);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        this.repo.deleteById(id);
        return ResponseEntity.ok("delete successful");
    }
}
