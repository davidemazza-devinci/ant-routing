
public class UnknownSeedTypeException extends Exception {

		public UnknownSeedTypeException(Seed s) {
			this.s = s;
		}
		
		public String toString() {
			return s.toString();
		}
		
		private Seed s;
}
