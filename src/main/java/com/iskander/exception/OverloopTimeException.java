package com.iskander.exception;

import java.util.Random;

public class OverloopTimeException extends RuntimeException {
    public OverloopTimeException() {
        super("Задачи не должны пересекатсья по времени");
    }
}
