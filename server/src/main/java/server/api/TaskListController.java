package server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import commons.TaskList;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.TaskListRepository;

@RestController
@RequestMapping("/api/tasklists")
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
        if (id < 0 || this.repo.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

    @GetMapping(path = "/get_by_parent/{id}")
    public ResponseEntity<List<TaskList>> getAllByParentBoard(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.repo.findAllByParentBoard_Id(id));
    }

    /**
     * Adds a TaskList to the repository.
     * @param taskList to be added
     * @return ResponseEntity
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList) {

        // long polling
        this.listeners.forEach((k, l) -> l.accept(taskList));

        TaskList saved = this.repo.save(taskList);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    /**
     * Mapping for deleting a task list.
     * @param id the id of the tasks list to be found in the database
     * @return the response
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<TaskList> delete(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        TaskList removed = this.repo.getById(id);
        this.repo.deleteById(id);
        return ResponseEntity.ok(removed);
    }

    /**
     * Modifies the task list that already exists in the database.
     * @param tasklist to be updated
     * @return ResponseEntity
     */
    @PostMapping(path = {"/update"})
    public ResponseEntity<TaskList> update(@RequestBody TaskList tasklist) {
        if (tasklist.getId() < 0 || this.repo.findById(tasklist.getId()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TaskList saved = this.repo.findById(tasklist.getId()).get();
        saved.setName(tasklist.getName());

        this.repo.save(saved);
        return ResponseEntity.ok(saved);
    }

    @MessageMapping("/tasklists") // /app/quotes
    @SendTo("/topic/tasklists")
    public TaskList addTaskList(TaskList taskList) {
        this.add(taskList);
        return taskList;
    }

    private final Map<Object, Consumer<TaskList>> listeners = new HashMap<>();

    /**
     * This is an update endpoint for long polling.
     * @return A DeferredResult with the TaskList ResponseEntity in it
     */
    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<TaskList>> getUpdates() {

        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<TaskList>>(5000L, noContent);

        var key = new Object();
        this.listeners.put(key, taskList -> res.setResult(ResponseEntity.ok(taskList)));
        res.onCompletion(() -> this.listeners.remove(key));
        return res;
    }

    @MessageMapping("/tasklists/add")
    @SendTo("/topic/tasklists/add")
    public TaskList addMessage(TaskList taskList) {
        return this.add(taskList).getBody();
    }

    @MessageMapping("/tasklists/edit")
    @SendTo("/topic/tasklists/edit")
    public TaskList editMessage(TaskList taskList) {
        return this.update(taskList).getBody();
    }

    @MessageMapping("/tasklists/delete")
    @SendTo("/topic/tasklists/delete")
    public TaskList deleteMessage(TaskList taskList) {
        System.out.println("tryna delete");
        this.delete(taskList.getId());
        return taskList;
    }

}
