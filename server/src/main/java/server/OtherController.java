package server;

import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;

@Service
public class OtherController {
    private int num = 0;

    public void increase() {
        num++;
    }

    public int value() {
        return num;
    }
}
