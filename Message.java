public class Message<Seed,Node> extends Tuple<Seed,Node> {

	public Message(Seed s, Node n) {
		super(s,n);
	}
	
	public Seed getSeed() {
		return getFirstElement();
	}
	
	public Node getNode() {
		return getSecondElement();
	}
	
	public void setSeed(Seed s) {
		setFirstElement(s);
	}
	
	public void setNode(Node n) {
		setSecondElement(n);
	}
}
