import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class Seed {
	
	public static enum Type { PHEROMONE, MATCHED, CONFIRMED };

	public Seed() {
		generate(DEFAULT_SIZE);
	}
	
	public Seed(LinkedList<Integer> bitSequence) {
		this.bitSequence = (LinkedList<Integer>) bitSequence.clone();
	}
	
	public Seed.Type getType() throws UnknownSeedTypeException {
		
		if ( getSize() == pheromoneSize )
			return Type.PHEROMONE;
		else if ( getSize() == (pheromoneSize + 1) )
			return Type.MATCHED;
		else if( getSize() == (pheromoneSize + 2) )
			return Type.CONFIRMED;
		else
			throw new UnknownSeedTypeException( this );

	}
	
	public int getSize() {
		return bitSequence.size();
	}
	
	public Seed concatenate(int headBit) {
		LinkedList<Integer> bv = (LinkedList<Integer>) bitSequence.clone();
		bv.addFirst(headBit);
		
		return new Seed( bv );
	}
	
	public Seed concatenate(Seed seed) {
		LinkedList<Integer> bv = (LinkedList<Integer>) bitSequence.clone();
		bv.addAll( seed.getBitSequence() );
		
		return new Seed( bv );
	}
	
	public LinkedList<Integer> getBitSequence() {
		return this.bitSequence;
	}
	
	public Seed getDerived() {
		LinkedList<Integer> bv = (LinkedList<Integer>) bitSequence.clone();
		bv.removeFirst();
		
		Seed s = new Seed(bv);
		
		return s;
	}
	
	public Seed getConjugate() {
		LinkedList<Integer> bv = (LinkedList<Integer>) bitSequence.clone();

		Integer i = bv.removeFirst();
		if(i == 0)
			bv.addFirst(1);
		else
			bv.addFirst(0); 
		
		return new Seed(bv);
	}
	
	@Override
	public int hashCode() { 
		return bitSequence.hashCode();
	}
	
    // Overriding equals() to compare two Complex objects 
    @Override
    public boolean equals(Object o) { 
  
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Seed */
        if ( ! (o instanceof Seed) ) { 
            return false; 
        } 
        
        Seed s = (Seed) o;
		if ( bitSequence.size() != s.getSize() )
			return false;
		
		/*
		LinkedList<Integer> vector = s.getBitSequence();
		for(int i=0; i<bitSequence.size(); i++ )
			if( bitSequence.get(i) != vector.get(i) )
				return false;
		*/
		
		return this.hashCode() == s.hashCode();
	}
	
	public String toString() {
		
		/*
		String str = "";
		
		for(Integer i : bitSequence) {
			str += i.intValue();
		}
		*/
		
		return ((Object) this) + " - Seed length: " + getSize();
	}
	
	private void generate(int numbersOfBit) {
		Random rand = new Random();
		
		bitSequence = new LinkedList<Integer>();
		for(int i=1; i<=numbersOfBit; i++) {
			
			// Obtain a number between [0 - 1].
			int bit = rand.nextInt(2);
			bitSequence.add(bit);
		
		}
	}
	
	private LinkedList<Integer> bitSequence;
	
	private static final int DEFAULT_SIZE = 128;
	private static final int pheromoneSize = (DEFAULT_SIZE * 2) + 1;
}
