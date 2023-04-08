package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AddTaskListCtrlTest {

    public AddTaskListCtrl sut;
    public MainCtrl mainCtrl;
    public ServerUtils sUtils;

    @BeforeEach
    public void setup() {
        mainCtrl = mock(MainCtrl.class);
        sUtils = mock(ServerUtils.class);
        sut = new AddTaskListCtrl(sUtils, mainCtrl);
    }

    @Test
    public void testGetTaskLists() {
        Board boardMock = mock(Board.class);
        TaskList a = new TaskList("a", boardMock);
        TaskList b = new TaskList("b", boardMock);
        TaskList c = new TaskList("c", boardMock);
        List<TaskList> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);
        expected.add(c);
        when(sut.getTaskLists()).thenReturn(expected);
        Assertions.assertEquals(expected, sut.getTaskLists());
    }

}
