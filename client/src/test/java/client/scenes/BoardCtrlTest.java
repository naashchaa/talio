package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import static org.mockito.Mockito.*;

public class BoardCtrlTest {

    public BoardCtrl sut;
    public MainCtrl mainCtrl;
    public ServerUtils sUtils;

    @BeforeEach
    public void setup() {
        mainCtrl = mock(MainCtrl.class);
        sUtils = mock(ServerUtils.class);
        sut = new BoardCtrl(sUtils, mainCtrl);
    }

    @Test
    public void test1() {
        Board expected = new Board("testBoard");
        BoardCtrl sutSpy = spy(sut);
        doReturn(expected).when(sutSpy).getBoard();
        Assertions.assertEquals(expected, sutSpy.getBoard());
    }

    @Test
    public void test2() {
        sut.addTaskList();
        verify(mainCtrl, times(1)).showAddTaskList();
    }

    @Test
    public void test3() {
        TaskList taskList = mock(TaskList.class);
        TaskListCtrl expected = new TaskListCtrl(sUtils, mainCtrl, "test");
//        when(sut.addTaskListToBoard(any(TaskList.class))).thenReturn(expected);
        BoardCtrl sutSpy = spy(sut);
        doReturn(expected).when(sutSpy).addTaskListToBoard(any(TaskList.class));
        Assertions.assertEquals(expected, sutSpy.addTaskListToBoard(taskList));
    }
}
