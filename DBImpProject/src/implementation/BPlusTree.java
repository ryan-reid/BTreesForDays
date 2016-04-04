
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
	
	public String getItem(String item) {
		return root.get(item);
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
		public abstract String get(String value);
	
	}
	
	private class LeafNode extends Node {

		void insert(DataNode item) {
			int insertLocation = findSpot(item.getData());
			boolean alreadyInThere = false;
			
			if(insertLocation < dataList.size()) {
				if(dataList.elementAt(insertLocation).getData().compareTo(item.getData()) == 0) {
					alreadyInThere = true;
				}
			}
			
			if(!alreadyInThere) {
				if(dataList.size() == maxLeafSize) {	
					dataList.insertElementAt(item, insertLocation);
					splitTree();				
				} else {			
					
					dataList.insertElementAt(item, insertLocation);
					System.out.println("Inserted");
				}
			} else {
				System.out.println("Duplicate entry");
			}
			
		}
		
		public String get(String item) {
			String returnItem = null;
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(item) == 0) {
					returnItem = item;
					break;
				}
			}
			
			return returnItem;
		}
		
		private void splitTree() {
			LeafNode right = new LeafNode();	
			int count = 0;
			
			for(int i = (maxLeafSize/2); i < maxLeafSize + 1; i ++) {
				right.dataList.insertElementAt(dataList.elementAt(i), count);
				count++;
			}
			
			for(int i = (maxLeafSize/2); i < maxLeafSize + 1; i ++) {
				this.dataList.remove(maxLeafSize/2);
			}
			
			if (parent == null) {
				parent = new TreeNode(right.dataList.elementAt(0));
				root = parent;	

				parent.parent = null;
				parent.dataList.elementAt(0).setLeft(this);				
				parent.dataList.elementAt(0).setRight(right);
				right.parent = parent;		
				
			} else {
				boolean split = parent.insertIndex(right.dataList.elementAt(0));
				int index = parent.getLocation(right.dataList.elementAt(0));
				right.parent = parent;
				
				if(split) {					
					 if (index == 0) {
						parent.dataList.elementAt(index).setLeft(right);
						
					} else if(index == parent.dataList.size() - 1) {
						parent.dataList.elementAt(index).setRight(right);
						parent.dataList.elementAt(index).setLeft(this);
						parent.dataList.elementAt(index - 1).setRight(this);
					} else	{
						parent.dataList.elementAt(index).setLeft(right);
						parent.dataList.elementAt(index - 1).setRight(this);
					}
				} else {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					
					if(index == 0) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					} else if(index == maxLeafSize - 1) {
						parent.dataList.elementAt(index - 1).setRight(this);
					} else {
						parent.dataList.elementAt(index + 1).setLeft(right);
						parent.dataList.elementAt(index - 1).setRight(this);
					}
				}
			}
			
			System.out.println("Inserted and split");
		}
		
		private int findSpot(String value) {
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(value) >= 0) {
					location = i;
					break;
				}
			}			
			
			return location;			
		}
		
	}
	
	private class TreeNode extends Node {
		
		public TreeNode(DataNode data) {
			dataList.insertElementAt(new DataNode(data.getData()), 0);
			parent = null;
		}

		public TreeNode() {
			
		}
		
		boolean insertIndex(DataNode item) {
			boolean split = false;
			int location = getLocation(item);
			dataList.insertElementAt(new DataNode(item.getData()), location);
			
			if(dataList.size() > maxLeafSize) {
				
				splitTree();
				split = true;
			} 
			
			return split;
		}
		
		
		void splitTree() {
			TreeNode right = new TreeNode();	
			int count = 0;
			
			for(int i = (maxLeafSize/2); i < maxLeafSize + 1; i ++) {
				right.dataList.insertElementAt(dataList.elementAt(i), count);
				count++;
			}
			
			for(int i = (maxLeafSize/2); i < maxLeafSize + 1; i ++) {
				this.dataList.remove(maxLeafSize/2);
			}
			
			if(parent == null) {
				parent = new TreeNode(right.dataList.elementAt(0));
				root = parent;
				parent.parent = null;
				
				right.parent = parent;					
				right.dataList.remove(0);
				
				parent.dataList.elementAt(0).left = this;
				parent.dataList.elementAt(0).right = right;
				for(int i = 0; i < right.dataList.size(); i++) {
					if(right.dataList.elementAt(i).left != null) {
						right.dataList.elementAt(i).left.parent = right;
						right.dataList.elementAt(i).right.parent = right;
					}

				}
				
			} else {
				boolean split = parent.insertIndex(right.dataList.elementAt(0));
				int index = getLocation(right.dataList.elementAt(0));
				right.parent = parent;			
				
				
				if(split) {
					if (index == dataList.size() - 1) {
						parent.dataList.elementAt(index).setLeft(this);
						parent.dataList.elementAt(index).setRight(right);
						parent.dataList.elementAt(index - 1).setRight(this);
					} else if (index == 0) {
						parent.dataList.elementAt(index).setLeft(this);
						parent.dataList.elementAt(index).setRight(right);
					} else {
						parent.dataList.elementAt(index).setLeft(right);
						parent.dataList.elementAt(index - 1).setRight(this);
					}
					
					right.dataList.remove(0);
				} else {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					
					if(index == 0) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					}  else if(index == maxLeafSize - 1) {
						parent.dataList.elementAt(index - 1).setRight(this);
					} else {
						parent.dataList.elementAt(index + 1).setLeft(right);
						parent.dataList.elementAt(index - 1).setRight(this);
					}
				}
			}
		}

		
		public String get(String item) {
			String returnString = null;
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(item) > 0) {
					location = i;
					break;
				}
			}
			
			//Right most location
			if(location == dataList.size()) {
				returnString = dataList.elementAt(location - 1).right.get(item);
			} else if (location == 0) {
				returnString = dataList.elementAt(location).left.get(item);
			} else {
				returnString = dataList.elementAt(location).left.get(item);
			}
			
			
			return returnString;
		}
		
		public int getLocation(DataNode item) {			
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData().compareTo(item.getData()) >= 0) {
					location = i;
					break;
				}
			}	
			
			return location;
		}
		
		void insert(DataNode item) {
			int location = getLocation(item);
			
			if(location == dataList.size()) {
				dataList.elementAt(location  - 1).insertRight(item);
			} else {
				if(dataList.elementAt(location).getData().compareTo(item.getData()) > 0) {
					dataList.elementAt(location).insertLeft(item);
				} else {
					dataList.elementAt(location).insertRight(item);
				}
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
