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
            // YELLOW: Спорно. Может быть лучше бросать исключение (NPE),+
            // но молчаливое игнорирование тоже имеет право на жизнь как защита от дурака.
            // RED: Критичный баг! В блоке `if (size == 1)` есть серьезная ошибка логики.+
            // Если в списке один элемент, но переданный узел `node` ему НЕ равен,
            // метод просто ничего не делает и завершается, хотя должен был либо сломаться,
            // либо проигнорировать запрос. Узел, не принадлежащий списку, не должен обрабатываться молча.
            // Это может маскировать более серьезные ошибки в коде.
            if (node == null) {
                throw new IllegalArgumentException("Node cannot be null");
            }

            if (size == 0) {
                throw new IllegalStateException("Cannot remove from empty list");
            }

            boolean nodeFound = false;
            Node<T> current = head;
            while (current != null) {
                if (current == node) {
                    nodeFound = true;
                    break;
                }
                current = current.next;
            }

            if (!nodeFound) {
                throw new IllegalArgumentException("Node does not belong to this list");
            }

            if (size == 1) {
                if (head == node) {
                    head = null;
                    tail = null;
                    size--;
                    return;
                }
                throw new IllegalArgumentException("Node does not belong to this list");
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
        // YELLOW: Нет проверки на null. Передача null задачи вызовет NPE на следующей строке.+
        // YELLOW: Имя переменной 'Node' с большой буквы+
        if(task == null){
            throw new IllegalArgumentException("Задача не может быть пуста");
        }
        Node<Task> node = tasksHistory.get(task.getId());
        if (node != null) {
            tasks.removeNode(node);
        }
        tasks.linkLast(task);
        tasksHistory.put(task.getId(), tasks.tail);

    }

    @Override
    public void remove(long id) {
        Node<Task> node = tasksHistory.get(id);
        // YELLOW: Если узла с таким id нет в мапе, `node` будет null.+
        // Метод `removeNode` его проигнорирует. В целом поведение корректное.+
        if (node != null) {
            tasks.removeNode(node);
            tasksHistory.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasks.getTasks();
    }

    // YELLOW: Реализация equals и hashCode есть, и это хорошо.++
    // Однако, они основаны на сравнении полей `tasksHistory` и `tasks`.
    // Для `tasks` (CustomLinkedList) методы equals и hashCode не переопределены,
    // поэтому будут использоваться унаследованные от Object (сравнение по ссылкам).
    // Скорее всего, это не то поведение, которое ожидается.
    // Нужно либо переопределить equals/hashCode в CustomLinkedList (сравнивая содержимое),
    // либо пересмотреть необходимость этих методов в целом для данного класса.++

}
