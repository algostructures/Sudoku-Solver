import java.util.HashMap;
public class MyCustomHashMap<K,V> extends HashMap<K,V> {
    /**
     * a custom HashMap with overridden get function
     **/
    private static final long serialVersionUID = 1L;
    protected V val;
    public MyCustomHashMap(V val) {
        this.val = val;
    }
    
    /**
     * returns default value if value is null other wise the value if key is not there
     * @param k : object for key
     * @return : value
     **/
    @Override
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? this.val : v;
    }
}