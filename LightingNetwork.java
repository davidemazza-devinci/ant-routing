import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class LightingNetwork {
	
	LightingNetwork(int numberOfNodes) {
		nodes = new LinkedList<Node>();
		
		// Create a random graph
		adj_matrix = createAdjMatrix(numberOfNodes);
		
		// Generate the random graph of the network
		generateGraph( adj_matrix );
	}
	
	LightingNetwork(boolean[][] adj_matrix) {
		nodes = new LinkedList<Node>();
		
		this.adj_matrix = adj_matrix;
		
		// Generate the random graph of the network
		generateGraph( adj_matrix );
	}
	
	public void start() {
		// Start nodes as separate threads
		for(Node n : nodes)
			n.start();
		
		System.out.println("LN running...");
	}
	
	public boolean[][] getAdjMatrix() {
		return adj_matrix;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	// Generate a random graph
	private void generateGraph(boolean[][] adj_matrix) {
		
		int numberOfNodes = adj_matrix[0].length;
		
		// Create nodes
		for (int i=0; i<numberOfNodes; i++) {
			nodes.add( new Node() );
		}
		
		for(int i=0; i<numberOfNodes; i++) {
			List<Node> neighbors = new LinkedList<Node>();
			
			for(int j=0;j<numberOfNodes; j++) {
				if( adj_matrix[i][j] )
					neighbors.add( nodes.get(j) );
			}
			
			nodes.get(i).setNeighbors(neighbors);
		}
		
	}
	
	// Create a random adjacency matrix
	private boolean[][] createAdjMatrix(int numberOfNodes) {
		
		Random rand = new Random();
		
		// Create a random adjacency matrix
		boolean[][] adj_matrix = new boolean[numberOfNodes][numberOfNodes];
		
		for(int i=0; i<numberOfNodes-1; i++) {
			
			adj_matrix[i][i] = false; // A node won't be adjacent to itself
			
			for(int j=i+1; j<numberOfNodes; j++) {
				int bit = rand.nextInt(2);
				if( bit == 0 )
					adj_matrix[i][j] = false;
				else
					adj_matrix[i][j] = true;
				
				// Adjacency matrix is symmetric
				adj_matrix[j][i] = adj_matrix[i][j];
			}
		}
		
		return adj_matrix;
	}
		
	private List<Node> nodes;
	
	private boolean[][] adj_matrix;
}
