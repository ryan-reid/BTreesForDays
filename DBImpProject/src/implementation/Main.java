package implementation;

public class Main {

	public static void main(String[] args) {
		BPlusTree tree = new BPlusTree(2);
		
		
		tree.insertItem("4");
		tree.insertItem("5");
		tree.insertItem("6");
		tree.insertItem("1");
		tree.insertItem("2");
		tree.insertItem("3");
		
		System.out.println("Got this value: " + tree.getItem("4"));
	}
}