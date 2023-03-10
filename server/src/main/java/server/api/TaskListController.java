package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import commons.TaskList;
import server.database.TaskListRepository;

@RestController
@RequestMapping("/api/taskList")
public class TaskListController {

    private final TaskListRepository repo;

    public TaskListController(TaskListRepository repo) {
        this.repo = repo;
    }

    /**
     * @return a list of all Task Lists in the repository
     */
    @GetMapping(path = {"", "/" })
    public List<TaskList> getAll() {
        return this.repo.findAll();
    }

    /**
     * @param id of the Task List to be gotten
     * @return the ResponseEntity with the board
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

    /**
     * Adds a TaskList to the repository.
     * @param taskList to be added
     * @return ResponseEntity
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList) {

        if (taskList.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = this.repo.save(taskList);
        return ResponseEntity.ok(saved);
    }
}


