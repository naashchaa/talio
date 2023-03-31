package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class AddTaskCtrlTest {

    AddTaskCtrl sut;
    ServerUtils sUtils;
    MainCtrl mainCtrl;

    @BeforeEach
    public void setup() {
        sUtils = mock(ServerUtils.class);
        mainCtrl = mock(MainCtrl.class);
        sut = new AddTaskCtrl(sUtils, mainCtrl);
    }

    @Test
    public void correctlySetAndGetParentTaskListCtrl() {
        TaskListCtrl ctrlMock = mock(TaskListCtrl.class);
        sut.setParentTaskListCtrl(ctrlMock);
        Assertions.assertEquals(ctrlMock, sut.getParentTaskListCtrl());
    }

    @Test
    public void correctlyReturnsListOfTasks() {
        TaskList mockList = mock(TaskList.class);
        Task a = new Task("a", null, mockList, null);
        Task b = new Task("b", null, mockList, null);
        Task c = new Task("c", null, mockList, null);
        List<Task> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);
        expected.add(c);
        when(sut.getTasks(any(TaskList.class))).thenReturn(expected);
        Assertions.assertEquals(expected, sut.getTasks(mockList));
    }

}