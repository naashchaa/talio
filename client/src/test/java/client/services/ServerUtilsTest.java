package client.services;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerUtilsTest {

    public ServerUtils sut;

    @BeforeEach
    public void setup() {
        sut = new ServerUtils();
    }
    
    @Test
    void registerForTaskListsL() {
        sut.registerForTaskListsL(list -> System.out.println(list.getName()));
    }
}
