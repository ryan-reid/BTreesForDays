package implementation;

public class Main {

	public static void main(String[] args) {
		BPlusTree tree = new BPlusTree(700);
		
		
		for(int i = 0; i < 50000; i++) {
			tree.insertItem(i);
		}
		
		
		for(int i = 0; i < 50000; i++) {
			int result = tree.getItem(i);
			if(result == -1) {
				System.out.println("Couldn't find item: " + i);
			}
		}	
		
		System.out.println("\n\nDone processing\n\n");
	}
}
