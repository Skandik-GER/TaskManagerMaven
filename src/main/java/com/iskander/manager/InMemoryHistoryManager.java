package com.iskander.manager;

import com.iskander.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    protected final Map<Long, Node<Task>> tasksHistory = new HashMap<>();
    protected final CustomLinkedList<Task> tasks = new CustomLinkedList<>();

    public static class CustomLinkedList<T> {

        private Node<T> head;

        private Node<T> tail;

        private int size = 0;


        public List<T> getTasks() {
            List<T> tasks = new ArrayList<>();
            Node<T> cur = head;
            while (cur != null) {
                tasks.add(cur.data);
                cur = cur.next;
            }
            return tasks;
        }

        public void linkLast(T task) {
            final Node<T> oldtail = tail;
            final Node<T> newNode = new Node<>(oldtail, null, task);
            tail = newNode;
            if (oldtail != null) {
                oldtail.next = newNode;
            }
            if (size == 0) {
                head = newNode;
            }
            size++;
        }

        public void removeNode(Node<T> node) {
            if(node == null){
                return;
            }
            if (size == 1) {
                if (head == node) {
                    head = null;
                    tail = null;
                    size--;
                    return;
                }
            }
            if (node == head) {
                head = node.next;
                if (head != null) {
                    head.prev = null;
                }
                size--;
                return;
            }
            if (node == tail) {
                tail = node.prev;
                if (tail != null) {
                    tail.next = null;
                }
                size--;
                return;
            }
            Node<T> nodePrev = node.prev;
            nodePrev.next = node.next;

            Node<T> nodeNext = node.next;
            nodeNext.prev = node.prev;

            size--;
        }

        public int size() {
            return this.size;
        }
    }

    @Override
    public void add(Task task) {
        Node<Task> Node = tasksHistory.get(task.getId());
        if (Node != null) {
            tasks.removeNode(Node);
        }
        tasks.linkLast(task);
        tasksHistory.put(task.getId(), tasks.tail);

    }

    @Override
    public void remove(long id) {
        Node<Task> node = tasksHistory.get(id);
        tasks.removeNode(node);
        tasksHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return tasks.getTasks();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(tasksHistory, that.tasksHistory) && Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasksHistory, tasks);
    }
}
