public interface LRUCache<U, T> {
    void put(U key, T value);

    T get(U key);

    int size();
}
