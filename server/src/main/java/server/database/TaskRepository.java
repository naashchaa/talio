package server.database;

import commons.Task;
import commons.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository <Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.parentTaskList = :parent")
    List<Task> getAllByParentTaskList(TaskList parent);
}
