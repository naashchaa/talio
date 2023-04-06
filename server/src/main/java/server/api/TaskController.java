package server.api;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public List<Task> getAll() {
        return this.repo.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id <= 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

    @GetMapping(path = "/get_by_parent/{id}")
    public ResponseEntity<List<Task>> getAllByParentTaskList(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.repo.findAllByParentTaskList_Id(id));
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

    /** This is a POST mapping that updates an existing Task with the given ID
     * to match the supplied object.
     * @param id The ID of the task to modify
     * @param modifiedTask The modified task object
     * @return The modified task if successful, an error otherwise
     */
    @PostMapping(path = "{id}/update")
    public ResponseEntity<Task> update(@PathVariable("id") long id,
                                       @RequestBody Task modifiedTask) {
        if (id <= 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Task task = this.repo.findById(id).get();

        if (task.equals(modifiedTask))
            return ResponseEntity.badRequest().build();

//        if (!task.getName().equals(modifiedTask.getName()))
        task.setName(modifiedTask.getName());

        task.setDescription(modifiedTask.getDescription());

//        if (!task.getParentTaskList().equals(modifiedTask.getParentTaskList()))
        task.setParentTaskList(modifiedTask.getParentTaskList());

//        if (!task.getNextTask().equals(modifiedTask.getNextTask()))
//            task.setNextTask(modifiedTask.getNextTask());

//        if (!task.getPrevTask().equals(modifiedTask.getPrevTask()))
        task.setPrevTask(modifiedTask.getPrevTask());

//        if (!task.getParentTask().equals(modifiedTask.getParentTask()))
        task.setParentTask(modifiedTask.getParentTask());

        this.repo.save(task);
        return ResponseEntity.ok(task);
    }

    /** This is a DELETE mapping that deletes a task by ID.
     * @param id ID of the task to delete
     * @return message if delete was successful, error otherwise
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Task> delete(@PathVariable("id") long id) {
        if (id <= 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Task removed = this.repo.getById(id);
        removed.id = 0;
        this.repo.deleteById(id);
        return ResponseEntity.ok(removed);
    }

    /**
     * Gets all the tasks associated to a task list and deleted them one by one
     * from the repository.
     * @param id the id of the parent task list.
     * @return return a response entity with the status and body of the operation.
     */
    @DeleteMapping(path = "/delete_by_parent/{id}")
    public ResponseEntity<String> deleteByParentId(@PathVariable("id") long id) {
        List<Task> tasks = this.repo.findAllByParentTaskList_Id(id);
        if (tasks.size() == 0) {
            return ResponseEntity.ok("no tasks deleted");
        }
        for (Task t : tasks) {
            this.repo.deleteById(t.getId());
        }
        return ResponseEntity.ok("delete successful");
    }

    @MessageMapping("/tasks/add") // app/tasks
    @SendTo("/topic/tasks/add")
    public Task addMessage(Task task) {
        return this.add(task).getBody();
    }

    @MessageMapping("/tasks/edit") // app/tasks
    @SendTo("/topic/tasks/edit")
    public Task editMessage(Task task) {
        return this.update(task.id, task).getBody();
    }

    /** This is a websocket delete mapping.
     * @param task task to delete
     * @return deleted task
     */
    @MessageMapping("/tasks/delete") // app/tasks
    @SendTo("/topic/tasks/delete")
    public Task deleteMessage(Task task) {
        // find next task and clear the potential prev reference
        Optional<Task> nextTask = this.getAll()
            .stream()
            .filter(next -> task.id == next.getPrevTask())
            .findFirst();

        if (nextTask.isPresent()) {
            Task next = nextTask.get();
            next.setPrevTask(0);
            this.update(next.id, next);
        }

        this.delete(task.id);
        return task;
    }
}
