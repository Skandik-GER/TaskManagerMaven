package com.iskander.exception;

public class EpicIdException extends RuntimeException{
    public EpicIdException() {
        super("Такого EpicID не существует");
    }
}
