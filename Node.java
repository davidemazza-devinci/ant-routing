import java.util.List;
import java.util.Queue;
import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Node extends Thread {
		
	// Constructors
	public Node() {
		neighbors = new LinkedList<Node>();
		LNmempool = new ConcurrentHashMap< Seed, List<Node> >(); 
		messages = new ConcurrentLinkedQueue< Message<Seed,Node> >();
	}
	
	public Node(List<Node> neighbors) {
		this.neighbors = neighbors;
		LNmempool = new ConcurrentHashMap< Seed, List<Node> >();
		messages = new ConcurrentLinkedQueue< Message<Seed,Node> >();
	}
	
	// Public methods
	public void setAlice() {
		alice = true;
	}
	
	public void setBob() {
		bob = true;
	}
	
	public void setNeighbors(List<Node> neighbors) {
		this.neighbors = neighbors;
	}
	
	public void run() {
		
		if( isAlice() || isBob() ) {
			Seed R = RA.concatenate( RB );
			Seed S;
			
			if( isAlice() )
				S = R.concatenate( 0 );
			else
				S = R.concatenate( 1 );
			
			for(Node n : neighbors)
				n.notify( new Message(S, this) );
		}
		
		
		while( ! pathFound ) {
			
			while( messages.isEmpty() && ! pathFound );
			
			while( ! messages.isEmpty() && ! pathFound ) {
				Message<Seed,Node> m = messages.remove();
				
				try {
					switch( m.getSeed().getType() ) {
					
						case PHEROMONE :
							pheromoneReceived( m.getSeed(), m.getNode() );
							break;
							
						case MATCHED :
							matchedSeedReceived( m.getSeed(), m.getNode() );
							break;
							
						case CONFIRMED :
							confirmedSeedReceived( m.getSeed(), m.getNode() );
							break;
							
					}
				} catch(UnknownSeedTypeException e) {
					
					System.out.println( e );
					
				}
			}
		}
	}
	
	public boolean isAlice() {
		return alice;
	}
	
	public boolean isBob() {
		return bob;
	}
	
	public void notify( Message<Seed,Node> m) {
		messages.add(m);
	}
	
	// Manage the reception of a pheromone seed
	private void pheromoneReceived(Seed s, Node sender) {
		
		Seed derived1 = s.getDerived();
		
		boolean found = false;
		for( Seed s1 : LNmempool.keySet() ) {
			
			Seed derived2 = s1.getDerived();
			
			if( derived1.equals(derived2) ) {
				// S derived is found
				found = true;
				
				Seed foundSeed;
				if( isInPool( s ) ) {
					foundSeed = s; 	// S is stored
					
					// Update the routing table with the received seed
					updateLNmempool( s, sender );
				}
				else {
					foundSeed = s.getConjugate();	// S conjugate was stored
					
					// A match has occurred: create a matched seed
					matchNode = true;
					
					// Update the routing table with the received seed
					updateLNmempool( s, sender );
					
					Seed Sm = s.concatenate(0);
					Seed Sm_conj = foundSeed.concatenate(0);
					
					// Update the routing table with the matched seed
					updateLNmempool( Sm_conj, sender );
					
					// The matched seed is transmitted to the neighbors from which it received S or S conjugate
					broadcastTo( LNmempool.get( s ), Sm );
					broadcastTo( LNmempool.get( foundSeed ), Sm_conj );

				}
			}
		}
		
		
		// S derived has not been found
		if( ! found ) {
			
			broadcastAllButSender( s, sender );
			
			// Update the routing table with the received seed
			updateLNmempool( s, sender );
		}
	}
		
	// Manage the reception of a matched seed
	private void matchedSeedReceived(Seed matched, Node sender) {
		
		// Alice
		if( isAlice() && ! matchedSeedAlreadyReceived ) {

			// Flag is used to be sure to select only one matched seed among all the possible received by Alice
			matchedSeedAlreadyReceived = true;
			
			Seed confirmed = matched.concatenate(0);
			sender.notify( new Message(confirmed, this) );
			
			// Update the routing table with the matched seed
			updateLNmempool( matched, sender );

		}
		
		// Other nodes
		else {
			
			Seed unmatched = matched.getDerived();
			
			if( isInPool( unmatched ) )
				broadcastToButSender( LNmempool.get( unmatched ), matched, sender );
			
			updateLNmempool( unmatched, sender );
			
			// Update the routing table with the matched seed
			updateLNmempool( matched, sender );
		}
	}
	
	// Manage the reception of a confirmed seed
	private void confirmedSeedReceived(Seed confirmed, Node sender) {
		
		// Bob
		if( isBob() ) {
			
			pathFound = true;
			
		} 
		
		// Other nodes
		else {
			
			if ( isMatchNode() ) {
				
				Seed matched = confirmed.getDerived();				
				if( isInPool( matched ) )
					broadcastToButSender( LNmempool.get( matched ), confirmed, sender );
				
			} else
				broadcastAllButSender( confirmed, sender );
		}
	}
	
	public boolean isMatchNode() {
		return matchNode;
	}
	
	public boolean pathFound() {
		return pathFound;
	}

	private boolean isInPool(Seed seed) {
		return LNmempool.keySet().contains( seed );
	}
	
	// Broadcast seed to a given list of nodes
	private void broadcastTo(List<Node> nodes, Seed seed) {
		for(Node n : nodes )
				n.notify( new Message( seed, this ) );
	}
	
	// Broadcast seed to all neighbors
	private void broadcastAll(Seed seed) {
		for(Node n : neighbors )
				n.notify( new Message( seed, this ) );
	}
	
	// Broadcast seed to a given list of nodes but sender
	private void broadcastToButSender(List<Node> nodes, Seed seed, Node sender) {
		
		for(Node n : nodes )
			if( ! n.equals(sender) )
				n.notify( new Message( seed, this ) );
		
	}
	
	// Broadcast seed to all neighbors but sender
	private void broadcastAllButSender(Seed seed, Node sender) {
		for(Node n : neighbors )
			if( ! n.equals(sender) )
				n.notify( new Message( seed, this ) );
	}
	
	// Update the seed routing table
	private void updateLNmempool(Seed s, Node n) {
		if( ! isInPool( s ) ) {
			
			List<Node> l = new LinkedList<Node>();
			l.add( n );
			
			LNmempool.put(s, l);
		} else {
			
			LNmempool.get(s).add( n );			
		}
	}
		
	private List<Node> neighbors;
	private Map< Seed, List<Node> > LNmempool;
	
	private boolean alice = false; 	// Identify sender (Alice)
	private boolean bob = false; 	// Identify receiver (Bob)
	private boolean matchedSeedAlreadyReceived = false;
	private boolean matchNode = false;
	
	private Queue< Message<Seed,Node> > messages;
	
	private static boolean pathFound = false;
	private static Seed RA, RB;
	
	static {
		RA = new Seed();
		RB = new Seed();
	}
}