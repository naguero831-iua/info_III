package rbt;

public class RBNode<K extends Comparable<K>, V> {
    public K key;
    public V value;
    public boolean color; // true = rojo, false = negro
    public RBNode<K, V> left, right, parent;

    public RBNode(K key, V value, boolean color, RBNode<K,V> nil) {
        this.key = key;
        this.value = value;
        this.color = color;
        this.left = nil;
        this.right = nil;
        this.parent = nil;
    }
}
