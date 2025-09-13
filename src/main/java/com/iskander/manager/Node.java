package com.iskander.manager;

public class Node<E> {
    public E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, Node<E> next , E data) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

}
