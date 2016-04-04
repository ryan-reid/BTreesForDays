package implementation;

public class Main {

	public static void main(String[] args) {
		long startTime;
		long finishTime;
		System.out.println("Program starting");
		
		for(int testNum = 2; testNum < 200; testNum++) {
			startTime = System.nanoTime();
			BPlusTree tree = new BPlusTree(2);
			
			for(int i = 0; i < 500000; i++) {
				tree.insertItem(i);
			}
			
			
			for(int i = 0; i < 500000; i++) {
				int result = tree.getItem(i);
				if(result == -1) {
					System.out.println("Couldn't find item: " + i);
				}
			}	
			
			finishTime = System.nanoTime();
			System.out.println("B+ Tree of order: " + testNum + " Has finished running in: " + ((finishTime - startTime)/1000000000.0) + " Seconds");
		}
	

		
		System.out.println("\n\nDone processing\n\n");
	}
}
