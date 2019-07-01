
public class SeedTest {
	
	public static void main(String[] args) {
		Seed a = new Seed();
		
		System.out.println(a.getSize());
		System.out.println(a);
		
		Seed b = a.concatenate(a);
		
		System.out.println(b.getSize());
		System.out.println(b);
	}
	
}
