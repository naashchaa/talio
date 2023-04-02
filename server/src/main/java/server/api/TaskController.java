package server.api;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.TaskRepository;
import java.util.List;

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
        if (id < 0 || !this.repo.existsById(id)) {
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
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Task task = this.repo.findById(id).get();
        System.out.println(task.id);

        if (task.equals(modifiedTask))
            return ResponseEntity.badRequest().build();

        if (!task.getName().equals(modifiedTask.getName()))
            task.setName(modifiedTask.getName());

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

    /** This is a DELETE mapping that deletes a task by ID.
     * @param id ID of the task to delete
     * @return message if delete was successful, error otherwise
     */
    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        this.repo.deleteById(id);
        return ResponseEntity.ok("delete successful");
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
        this.add(task);
        return task;
    }

    // might need a delete and update tasks as well


}
