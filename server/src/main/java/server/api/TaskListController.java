package server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping(path = "/{id}")
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
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        TaskList saved = this.repo.save(taskList);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    /**
     * Modifies the task list that already exists in the database.
     * @param tasklist to be updated
     * @return ResponseEntity
     */
    @PostMapping(path = {"/update"})
    public ResponseEntity<TaskList> update(@RequestBody TaskList tasklist) {
        try {
            Optional<TaskList> o = this.repo.findById(tasklist.getId());
            if(o.isPresent()) {
                TaskList modifiedTaskList = this.repo.save(tasklist);
                return new ResponseEntity<>(modifiedTaskList, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @MessageMapping("/taskList") // /app/quotes
    @SendTo("/topic/taskList")
    public TaskList addTaskList(TaskList taskList) {
        this.add(taskList);
        return taskList;
    }
}
