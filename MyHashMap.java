
// Custom implementation of a HashMap data structure using open addressing with linear probing.
public class MyHashMap<K,V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private Entry<K,V>[] table;
    private int size;
    private int capacity;

    //Represents a key-value pair stored in hashMap
    private static class Entry<K,V>{
        K key;
        V value;
        boolean active; // Flag to indicate if the entry is currently active
        Entry(K key, V value){
            this.key = key;
            this.value = value;
            this.active = true;
        }
    }

    public MyHashMap(){
        this.capacity = INITIAL_CAPACITY;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    private int hash(Object key){
        return Math.abs(key.hashCode()%capacity);

    }

    public void put(K key, V value){
        double loadFactor = (double) size/capacity;
        if(loadFactor>=LOAD_FACTOR_THRESHOLD){ //checks whether loadFactor exceeds the threshold
            rehash();
        }

        int index = hash(key);

        //Linear probing to find an empty or inactive slot
        while(table[index] != null && table[index].active){
            if(table[index].key.equals(key)){
                return; //If the key is already in map, returns
            }
            index = (index+1)%capacity; //With linear probing, looking for an available slot
        }
        table[index] = new Entry<>(key,value);
        size++;
    }

    public V get(K key){
        int index = hash(key);

        //Linear Probing to find the key
        while(table[index]!=null){
            if(table[index].key.equals(key)&&table[index].active){
                return table[index].value;
            }
            index = (index+1)%capacity;
        }
        return null; // Key not found
    }

    public V remove(K key) {
        int index = hash(key);

        // Linear probing to find the key
        while (table[index] != null) {
            if (table[index].key.equals(key) && table[index].active) {
                table[index].active = false;  // Marked as inactive instead of deleted
                size--;
                return table[index].value;
            }
            index = (index + 1) % capacity;  // Move to the next slot
        }

        return null;  // Key not found
    }

    private void rehash() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] oldTable = table;

        table = new Entry[newCapacity];
        capacity = newCapacity;
        size = 0;

        // Rehash all existing active entries into the new table
        for (Entry<K, V> entry : oldTable) {
            if (entry != null && entry.active) {
                put(entry.key, entry.value);
            }
        }
    }

    public boolean containsKey(K key) {
        int index = hash(key);

        while (table[index] != null) {
            if (table[index].key.equals(key) && table[index].active) {
                return true;
            }
            index = (index + 1) % capacity;  // Linear probing to the next slot
        }

        return false;  // Key not found
    }

}
