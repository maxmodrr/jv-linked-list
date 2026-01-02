package core.basesyntax;

import java.util.List;
import java.util.Objects;

public class MyLinkedList<T> implements MyLinkedListInterface<T> {
    private static final int DEFAULT_SIZE = 0;
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = DEFAULT_SIZE;

    @Override
    public void add(T value) {
        if (head == null) {
            setHead(value);
        } else {
            setTail(value);
        }
        size++;
    }

    @Override
    public void add(T value, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index here: " + index);
        }

        if (size == 0) {
            Node<T> node = new Node<>(null, value, null);
            head = node;
            tail = node;
            size++;
            return;
        }

        if (index == 0) {
            Node<T> node = new Node<>(null, value, head);
            head.prev = node;
            head = node;
            size++;
            return;
        }

        if (index == size) {
            Node<T> node = new Node<>(tail, value, null);
            tail.next = node;
            tail = node;
            size++;
            return;
        }

        Node<T> current = getNodeByIndex(index);
        Node<T> node = new Node<>(current.prev, value, current);
        current.prev.next = node;
        current.prev = node;

        size++;
    }

    @Override
    public void addAll(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }

    @Override
    public T get(int index) {
        if (isInvalidIndex(index)) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node<T> current = getNodeByIndex(index);
        return current.value;
    }

    @Override
    public T set(T value, int index) {
        if (isInvalidIndex(index)) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node<T> currentNode = getNodeByIndex(index);
        T oldValue = currentNode.value;
        currentNode.value = value;

        return oldValue;
    }

    @Override
    public T remove(int index) {
        if (isInvalidIndex(index)) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        T removedValue;

        if (size == 1) {
            removedValue = head.value;
            head = null;
            tail = null;
        } else if (index == 0) {
            removedValue = head.value;
            head.next.prev = null;
            head = head.next;
        } else if (index == size - 1) {
            removedValue = tail.value;
            tail = tail.prev;
            tail.next = null;
        } else {
            Node<T> currentNode = getNodeByIndex(index);
            removedValue = currentNode.value;
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
        }
        size--;
        return removedValue;
    }

    @Override
    public boolean remove(T object) {
        if (head == null) {
            return false;
        }

        Node<T> current = head;

        while (current != null) {
            if (Objects.equals(current.value, object)) {
                if (current == head) {
                    head = current.next;
                    if (head != null) {
                        head.prev = null;
                    } else {
                        tail = null;
                    }
                } else if (current == tail) {
                    tail = current.prev;
                    tail.next = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isInvalidIndex(int index) {
        return index < 0 || index >= size;
    }

    private boolean isCloseToHead(int index) {
        return index < (size / 2);
    }

    private void setHead(T value) {
        Node<T> node = new Node<>(null, value, null);
        head = node;
        tail = node;
    }

    private void setTail(T value) {
        tail.next = new Node<>(tail, value, null);
        tail = tail.next;
    }

    private Node<T> getNodeByIndex(int index) {
        boolean isCloseToHead = isCloseToHead(index);
        Node<T> current = isCloseToHead ? head : tail;

        if (isCloseToHead) {
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            for (int i = size - 1; i != index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private static class Node<T> {
        private Node<T> next;
        private Node<T> prev;
        private T value;

        public Node(Node<T> prev, T value, Node<T> next) {
            this.next = next;
            this.value = value;
            this.prev = prev;
        }
    }
}
