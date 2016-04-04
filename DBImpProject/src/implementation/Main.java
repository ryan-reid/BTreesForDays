package implementation;

public class Main {

	public static void main(String[] args) {
		BPlusTree tree = new BPlusTree(3);
		
		
		tree.insertItem(4);
		tree.insertItem(5);
		tree.insertItem(6);
		tree.insertItem(1);
		tree.insertItem(2);
		tree.insertItem(3);
		tree.insertItem(7);
		tree.insertItem(8);
		tree.insertItem(9);
		tree.insertItem(10);
		tree.insertItem(11);
		tree.insertItem(12);
		tree.insertItem(13);
		tree.insertItem(14);
		tree.insertItem(15);

		
		
		System.out.println("Got this value: " + tree.getItem(4));
		System.out.println("Got this value: " + tree.getItem(5));
		System.out.println("Got this value: " + tree.getItem(6));
		System.out.println("Got this value: " + tree.getItem(7));
		System.out.println("Got this value: " + tree.getItem(1));
		System.out.println("Got this value: " + tree.getItem(2));
		System.out.println("Got this value: " + tree.getItem(3));
		System.out.println("Got this value: " + tree.getItem(8));
		System.out.println("Got this value: " + tree.getItem(9));
		System.out.println("Got this value: " + tree.getItem(10));
		System.out.println("Got this value: " + tree.getItem(11));
		System.out.println("Got this value: " + tree.getItem(12));
		System.out.println("Got this value: " + tree.getItem(13));
		System.out.println("Got this value: " + tree.getItem(14));
		System.out.println("Got this value: " + tree.getItem(15));
		
		
	}
}
