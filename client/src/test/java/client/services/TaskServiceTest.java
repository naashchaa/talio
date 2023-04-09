package client.services;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TaskServiceTest {

    public TaskService sut;

    public MainCtrl mainCtrl;

    public ServerUtils sUtils;

    @BeforeEach
    void setUp() {
        mainCtrl = mock(MainCtrl.class);
        sUtils = mock(ServerUtils.class);
        sut = new TaskService(sUtils);
    }

    @Test
    void getNextTask() {
    }

    @Test
    void moveTaskTo() {
    }

    @Test
    void refreshTask() {
    }

    @Test
    void deleteTask() {
    }
}