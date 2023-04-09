package server.database;

import commons.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository <TaskList, Long> {

    List<TaskList> findAllByParentBoard_Id(long id);
}
