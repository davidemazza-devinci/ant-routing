import java.util.Random;

public class AntRoutingTest {

	public static void main(String[] args) {
	
		boolean randomNetwork = false;
		
		if( ! randomNetwork ) {
			
			//case1();
			//case2();
			//case3();
			case4();
			
		} else
			caseRandom();
	}
		
	public static void case1() {
		
		boolean[][] adj_matrix = { 
				{ false, true, true, false },
				{ true, false, false, false },
				{ true, false, false, true },
				{ false, false, true, false } };
		
		int numNodes = adj_matrix[0].length;
		ln = new LightingNetwork( adj_matrix );
		
		alice = ln.getNodes().get( 0 );
		alice.setAlice();
		
		bob = ln.getNodes().get( 3 );
		bob.setBob();
	
		run();
	}
	
	public static void case2() {
		
		boolean[][] adj_matrix = { 
				{ false, false, true, false },
				{ false, false, false, false },
				{ true, false, false, true },
				{ false, false, true, false } };
				
		int numNodes = adj_matrix[0].length;
		ln = new LightingNetwork( adj_matrix );
		
		alice = ln.getNodes().get( 0 );
		alice.setAlice();
		
		bob = ln.getNodes().get( 2 );
		bob.setBob();
		
		run();
		
	}
	
	public static void case3() {
		
		boolean[][] adj_matrix = { 
				{ false, false, true, true },
				{ false, false, true, false },
				{ true, true, false, true },
				{ true, false, true, false } };
		
		int numNodes = adj_matrix[0].length;
		ln = new LightingNetwork( adj_matrix );
		
		alice = ln.getNodes().get( 0 );
		alice.setAlice();
		
		bob = ln.getNodes().get( 3 );
		bob.setBob();
	
		run();
	}
	
	public static void case4() {
		
		boolean[][] adj_matrix = { 
				{ false, true, true, true },
				{ true, false, false, true },
				{ true, false, false, false },
				{ true, true, false, false } };
		
		int numNodes = adj_matrix[0].length;
		ln = new LightingNetwork( adj_matrix );
		
		alice = ln.getNodes().get( 2 );
		alice.setAlice();
		
		bob = ln.getNodes().get( 0 );
		bob.setBob();
	
		run();
	}
	
	public static void caseRandom() {
		
		// Network initialization
		int numNodes = 4;
		ln = new LightingNetwork( numNodes );
	
		// Randomly choose Alice and Bob
		Random rand = new Random();
		int src = rand.nextInt( numNodes );
		int dst = src;
		while (dst == src )
			dst = rand.nextInt( numNodes );
		
		alice = ln.getNodes().get( src );
		alice.setAlice();
		
		bob = ln.getNodes().get( dst );
		bob.setBob();
		
		run();
		
	}
	
	// Run test
	private static void run() {
		
		ln.start();
		
		while ( ! alice.pathFound() );
		
		System.out.println("Path found!");
		
	}
	
	private static LightingNetwork ln;
	private static Node alice, bob;
	
}