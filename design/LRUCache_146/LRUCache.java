package LeetCode.design.LRUCache_146;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private Map<Integer, CacheItem> map = new HashMap<>();
    private CacheItem head; // first fake node
    private CacheItem tail; // last node
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head = new CacheItem(-1, -1); // fake head
        tail = head;
    }

    public int get(int key) {
        if (map.get(key) == null) return -1;
        int value = map.get(key).value;
        update(key, value);
        return value;
    }

    private void update(int key, int value) {
        remove(key);
        putNewest(key, value);
    }

    public int remove(int key) {
        CacheItem item = map.get(key);
        removeFromLinkedList(item);

        map.remove(key);
        return item.value;
    }

    private void removeFromLinkedList(CacheItem item) {
        if (item == tail) {
            tail = item.prev;
        } else {
            CacheItem prev = item.prev;
            CacheItem next = item.next;
            prev.next = next; // update forward link
            if (next != null) next.prev = prev; // update backward link
        }
    }

    private void removeOldest() {
        CacheItem oldest = head.next;
        removeFromLinkedList(oldest);
        map.remove(oldest.key);
    }

    private void putNewest(int key, int val) {
        CacheItem item = new CacheItem(key, val);

        tail.next = item; // tail -> next - forward link
        item.prev = tail; // tail <- next - backward link

        tail = item; // move tail to the last item in the list
        map.put(key, item);
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            update(key, value);
        } else {
            if (map.size() == capacity) removeOldest();
            putNewest(key, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        CacheItem current = head;
        while (current != null) {
            sb.append("key: " + current.key + "; value: " + current.value + " -> ");
            current = current.next;
        }
        sb.append("; map size: " + map.size());
        return sb.toString();
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */