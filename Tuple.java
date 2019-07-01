
public class Tuple<T,V> {
	
	public Tuple(T t, V v) {
		this.t = t;
		this.v = v;
	}
	
	public T getFirstElement() {
		return t;
	}
	
	public V getSecondElement() {
		return v;
	}
	
	public void setFirstElement(T t) {
		this.t = t;
	}
	
	public void setSecondElement(V v) {
		this.v = v;
	}
	
	private T t;
	private V v;

}
