package in.aklabs.kv.core;

public interface Store {
    void put(String key, Value value);

    boolean contains(String key);
}
