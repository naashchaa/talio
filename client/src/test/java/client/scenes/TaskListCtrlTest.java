package client.scenes;

import client.services.TaskListService;
import client.services.TaskService;
import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;

class TaskListCtrlTest {

    public TaskListCtrl sut;
    public MainCtrl mainCtrl;
    public ServerUtils sUtils;
    public TaskListService lService;
    public TaskService tService;

    @BeforeEach
    public void setup() {
        mainCtrl = mock(MainCtrl.class);
        sUtils = mock(ServerUtils.class);
        sut = new TaskListCtrl(sUtils, mainCtrl, tService, lService);
    }

    @Test
    public void addTaskCallsMainCtrlCorrectly() {
        sut.addTask();
        verify(mainCtrl, times(1)).showAddTask(sut);
    }

    @Test
    public void getTaskListReturnsTaskList() {
        TaskList expected = new TaskList("testTaskList", new Board("testBoard"));
        TaskListCtrl sutSpy = spy(sut);
        when(sutSpy.getTaskList()).thenReturn(expected);
        Assertions.assertEquals(expected, sutSpy.getTaskList());
    }


}