import java.util.HashMap;
import java.util.Map;

public class LRUCacheImpl<T, U> implements LRUCache<T, U> {
    private class Node {
        Node next;
        Node prev;
        T key;
        U value;

        public Node(T key, U value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<T, Node> values = new HashMap<>();
    private Node tail = null;
    private Node head = null;
    int size = 0;
    int maxSize;

    public LRUCacheImpl(int maxSize) {
        assert maxSize > 0;
        this.maxSize = maxSize;
    }

    private void checkInvariant() {
        assert size <= maxSize;
        assert size == values.size();
    }

    private void removeOldestIfNeeded() {
        checkInvariant();

        if (size == maxSize) {
            assert tail.next != null;
            assert values.containsKey(tail.key);
            size--;

            values.remove(tail.key);
            tail = tail.next;
            tail.prev = null;
        }
    }

    private void moveToTop(T key) {
        checkInvariant();

        Node node = values.get(key);
        if (node == head || node == null) {
            return;
        }

        assert node.next != null;

        node.next.prev = node.prev;
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        node.prev = head;

        if (tail == node) {
            tail = node.next;
        }

        node.next = null;
        head.next = node;
        head = node;

        assert head.key == key;
    }

    @Override
    public void put(T key, U value) {
        checkInvariant();

        removeOldestIfNeeded();

        if (values.get(key) != null) {
            values.get(key).value = value;
        } else {
            size++;
            Node newNode = new Node(key, value);
            values.put(key, newNode);

            if (tail == head) {
                if (tail == null) {
                    head = newNode;
                    tail = newNode;
                } else {
                    head = newNode;
                    newNode.prev = tail;
                    tail.next = newNode;
                }
            } else {
                head.next = newNode;
                newNode.prev = head;
                head = newNode;
            }
        }

        assert values.containsKey(key);
        assert head.key == key;
        assert head.value == value;
    }

    @Override
    public U get(T key) {
        moveToTop(key);
        Node keyValue = values.get(key);

        return keyValue == null ? null : keyValue.value;
    }

    @Override
    public int size() {
        return size;
    }
}
