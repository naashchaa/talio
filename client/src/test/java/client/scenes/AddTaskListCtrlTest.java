package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;

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
}
