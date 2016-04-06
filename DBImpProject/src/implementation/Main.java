package implementation;

public class Main {

	public static void main(String[] args) {
		long startTime;
		long finishTime;
		System.out.println("Program starting");
		
		
		for(int testNum = 2; testNum < 200; testNum+=2) {
			startTime = System.nanoTime();
			BPlusTree tree = new BPlusTree(testNum);
			
			for(int i = 0; i < 500000; i ++) {
				tree.insertItem(i);
			}
			finishTime = System.nanoTime();
			System.out.println("B+ Tree of order: " + (testNum/2) + " Has finished inserted in: " + ((finishTime - startTime)/1000000000.0) + " Seconds");
			
			startTime = System.nanoTime();
			for(int i = 0; i < 500000; i++) {
				int result = tree.getItem(i);
				if(result == -1) {
					System.out.println("Couldn't find item: " + i);
				}
			}	
			
			finishTime = System.nanoTime();
			System.out.println("B+ Tree of order: " + testNum/2 + " Has finished searching in: " + ((finishTime - startTime)/1000000000.0) + " Seconds");
		}
	

		
		System.out.println("\n\nDone processing\n\n");
	}
}
