package implementation;

import java.util.Vector;

public class BPlusTree {
	private static int maxLeafSize;
	private Node root;
	
	public BPlusTree(int maxItems) {
		BPlusTree.maxLeafSize = maxItems;
		root = new LeafNode();
	}
	
	public void insertItem(String item) {
		root.insert(new DataNode(item));
	}

	
	abstract class Node {
		protected Vector<DataNode> dataList = new Vector<DataNode>();
		protected TreeNode parent;
		protected int maxSize;
				
		protected boolean isFull() {
			boolean full = false;
			
			if(dataList.size() == maxLeafSize) {
				full = true;
			}
			
			return full;
		}
		
		protected DataNode getDataAt(int index) {
			DataNode data = null;
			
			if(dataList.size() > index) {
				data = (DataNode) dataList.elementAt(index);
			}
	
			return data;
		}
		
		abstract void insert(DataNode value);
	
	}
	
	private class LeafNode extends Node {

		void insert(DataNode item) {
			int insertLocation = findSpot(item.getData());
			
			if(dataList.size() == maxLeafSize) {	
				dataList.insertElementAt(item, insertLocation);
				splitTree();				
			} else {			
				
				dataList.insertElementAt(item, insertLocation);
				System.out.println("Inserted");
			}
			
		}
		
		private void splitTree() {
			LeafNode left = new LeafNode();
			LeafNode right = new LeafNode();	
			int count = 0;
			
			
			for(int i = 0; i < (maxLeafSize/2); i ++) {
				left.dataList.insertElementAt(dataList.elementAt(i), i);					
			}
			
			
			for(int i = (maxLeafSize/2); i < maxLeafSize + 1; i ++) {
				right.dataList.insertElementAt(dataList.elementAt(i), count);
				count++;
			}
			
			if (parent == null) {
				parent = new TreeNode(dataList.elementAt(maxLeafSize/2));
				root = parent;	
				parent.parent = null;
				parent.dataList.elementAt(0).setLeft(left);
				left.parent = parent;				
				parent.dataList.elementAt(0).setRight(right);
				right.parent = parent;		
				
			} else {
				int index = parent.insertIndex(dataList.elementAt(maxLeafSize/2));
				boolean split = parent.wasSplit();
				parent.dataList.elementAt(index).setLeft(left);
				parent.dataList.elementAt(index).setRight(right);
				left.parent = parent;
				right.parent = parent;				
				
				if(index == 0) {
					parent.dataList.elementAt(index + 1).left = right; 
				} else if(index == maxLeafSize) {
					parent.dataList.elementAt(index - 1).right = left;
				} else {
					if(split) {
						parent.dataList.elementAt(index).left = right; 
					} else {
						parent.dataList.elementAt(index + 1).left = right; 
					}
					
					parent.dataList.elementAt(index - 1).right = left;
				}
			}
			
			dataList.remove(maxLeafSize/2);
			
			System.out.println("Inserted and split");
		}
		
		private int findSpot(String value) {
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(value) >= 0) {
					location = i;
				}
			}
			
			
			return location;			
		}
		
	}
	
	private class TreeNode extends Node {
		private boolean lastInsertSplit = false;
		
		public TreeNode(DataNode data) {
			dataList.insertElementAt(new DataNode(data.getData()), 0);
			parent = null;
		}

		int insertIndex(DataNode item) {			
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(item.getData()) >= 0) {
					location = i;
				}
			}
			
			dataList.insertElementAt(new DataNode(item.getData()), location);
			
			if(dataList.size() > maxLeafSize) {
				
				splitTree();
				lastInsertSplit = true;
			} else {
				lastInsertSplit = false;
			}
			
			return location;
		}
		
		boolean wasSplit() {
			return lastInsertSplit;
		}
		
		void splitTree() {
			LeafNode left = new LeafNode();
			LeafNode right = new LeafNode();	
			int count = 0;
			
			
			for(int i = 0; i < (maxLeafSize/2); i ++) {
				left.dataList.insertElementAt(dataList.elementAt(i), i);					
			}
			
			
			for(int i = (maxLeafSize/2) + 1; i < maxLeafSize + 1; i ++) {
				right.dataList.insertElementAt(dataList.elementAt(i), count);
				count++;
			}
			
			if(parent == null) {
				parent = new TreeNode(dataList.elementAt(maxLeafSize/2));
				root = parent;
				parent.parent = null;
				parent.dataList.elementAt(0).left = left;
				parent.dataList.elementAt(0).right = right;
				left.parent = parent;
				right.parent = parent;
			} else {
				int index = parent.insertIndex(dataList.elementAt(maxLeafSize/2));
				boolean split = parent.wasSplit();
				parent.dataList.elementAt(index).setLeft(left);
				parent.dataList.elementAt(index).setRight(right);
				left.parent = parent;
				right.parent = parent;				
				
				if(index == 0) {
					parent.dataList.elementAt(index + 1).left = right; 
				} else if(index == maxLeafSize) {
					parent.dataList.elementAt(index - 1).right = left;
				} else {
					if(split) {
						parent.dataList.elementAt(index).left = right; 
					} else {
						parent.dataList.elementAt(index + 1).left = right; 
					}
					
					parent.dataList.elementAt(index - 1).right = left;
				}
			}
			
			dataList.remove(maxLeafSize/2);
		}

		
		void insert(DataNode item) {
			
			if(dataList.elementAt(0).getData().compareTo(item.getData()) >= 0) {
				dataList.elementAt(0).insertLeft(item);
			} else {
				dataList.elementAt(0).insertRight(item);
			}
		}	
	}
		
	private class DataNode {
		String value;
		Node right;
		Node left;

		public DataNode(String value) {
			this.value = value;
			right = null;
			left = null;
		}
				
		public String getData() {
			return value;
		}
		
		public void setLeft(Node node) {
			left = node;
		}
		
		public void setRight(Node node) {
			right = node;
		}

		void insertLeft(DataNode value) {
			left.insert(value);	
		}
		
		void insertRight(DataNode value) {
			right.insert(value);
		}
	}
}
