package server.database;

import commons.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository <Task, Long> {

    List<Task> findAllByParentTaskList_Id(long id);
}
