package org.example.galaxy_trucker;

public class MyPair<K, V> {
    private K key;
    private V value;

    // Costruttore, getter, setter
    public MyPair() { }

    public MyPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public void setKey(K key) { this.key = key; }

    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }
}
