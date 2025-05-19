package org.example.galaxy_trucker.Commands;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {

    static InputReader inputReader;
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);
        inputReader = new InputReader(queue);
    }
    @Test
    public void terminal(){

        System.out.println("Daje roma");

    }

}