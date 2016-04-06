package implementation;

import java.util.Vector;

public class BPlusTree {
	private static int maxLeafSize;
	private Node root;
	
	public BPlusTree(int maxItems) {
		BPlusTree.maxLeafSize = maxItems;
		root = new LeafNode();
	}
	
	public void insertItem(int item) {
		root.insert(new DataNode(item));
	}
	
	public int getItem(int item) {
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
		public abstract int get(int value);
	
	}
	
	private class LeafNode extends Node {

		void insert(DataNode item) {
			int insertLocation = findSpot(item.getData());
			boolean alreadyInThere = false;
			
			if(insertLocation < dataList.size()) {
				if(dataList.elementAt(insertLocation).getData() == item.getData()) {
					alreadyInThere = true;
				}
			}
			
			if(!alreadyInThere) {
				if(dataList.size() == maxLeafSize) {	
					dataList.insertElementAt(item, insertLocation);
					splitTree();				
				} else {			
					
					dataList.insertElementAt(item, insertLocation);
				}
			} else {
				System.out.println("Duplicate entry");
			}
			
		}
		
		public int get(int item) {
			int returnItem = -1;
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData() == item) {
		
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
				
				
				//Case 1. It's the first element.
				if(index == 0) {

					if(split) {
						if(parent.parent.getLocation(parent) > 0 && parent.parent.getLocation(parent) < maxLeafSize) {
							parent.dataList.elementAt(index).setLeft(right);
						} else {
							parent.dataList.elementAt(index).setLeft(this);
							parent.dataList.elementAt(index).setRight(right);
							if(parent.dataList.size() > 1) {
								parent.dataList.elementAt(index + 1).setLeft(right);
							}
						}
					} else {
						parent.dataList.elementAt(index).setLeft(this);
						parent.dataList.elementAt(index).setRight(right);
						if(parent.dataList.size() > 1) {
							parent.dataList.elementAt(index + 1).setLeft(right);
						}
					}
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					if(parent.dataList.size() > 1) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					}
							
					//Case 2 Some where in the middle
				} else if(index < parent.dataList.size() - 1) {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					parent.dataList.elementAt(index + 1).setLeft(right);
					parent.dataList.elementAt(index - 1).setRight(this);

					//Case 3 At the end
				} else if(index == parent.dataList.size()) {
					parent.dataList.elementAt(index - 1).setLeft(this);
					parent.dataList.elementAt(index - 1).setRight(right);
					
					if(index >= 2) {
						parent.dataList.elementAt(index - 2).setRight(this);
					}
				} else {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					
					if(index >= 1) {
						parent.dataList.elementAt(index - 1).setRight(this);
					}
					
					if (index < parent.dataList.size() - 1) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					}
				}
				
			}	
		}
		
		private int findSpot(int value) {
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData() >= value) {
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
				
				parent.dataList.elementAt(0).left = this;
				parent.dataList.elementAt(0).right = right;
				for(int i = 0; i < right.dataList.size(); i++) {
					if(right.dataList.elementAt(i).left != null) {
						//right.dataList.elementAt(i).left.parent = right;
						right.dataList.elementAt(i).right.parent = right;
					}
				}
				
				if(right.dataList.elementAt(0).left != null) {
					right.dataList.elementAt(0).left.parent = this;
				}
				
				right.dataList.remove(0);
				
			} else {
				boolean split = parent.insertIndex(right.dataList.elementAt(0));
				right.parent = parent;						
				int index = parent.getLocation(right.dataList.elementAt(0));
				
				for(int i = 0; i < right.dataList.size(); i++) {
					if(right.dataList.elementAt(i).left != null) {
						right.dataList.elementAt(i).left.parent = right;
						right.dataList.elementAt(i).right.parent = right;
					}
				}
				
				if(!split) {
					if(right.dataList.elementAt(0).left != null) {
						right.dataList.elementAt(0).left.parent = this;
					}
				}

				right.dataList.remove(0);		
				
				//Case 1. It's the first element.
				if(index == 0) {

					if(split) {
						if(parent.parent.getLocation(parent) > 0 && parent.parent.getLocation(parent) < maxLeafSize) {
							parent.dataList.elementAt(index).setLeft(right);
						} else {
							parent.dataList.elementAt(index).setLeft(this);
							parent.dataList.elementAt(index).setRight(right);
							if(parent.dataList.size() > 1) {
								parent.dataList.elementAt(index + 1).setLeft(right);
							}
						}
					} else {
						parent.dataList.elementAt(index).setLeft(this);
						parent.dataList.elementAt(index).setRight(right);
						if(parent.dataList.size() > 1) {
							parent.dataList.elementAt(index + 1).setLeft(right);
						}
					}
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					if(parent.dataList.size() > 1) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					}
							
					//Case 2 Some where in the middle
				} else if(index < parent.dataList.size() - 1) {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					parent.dataList.elementAt(index + 1).setLeft(right);
					parent.dataList.elementAt(index - 1).setRight(this);

					//Case 3 At the end
				} else if(index == parent.dataList.size()) {
					parent.dataList.elementAt(index - 1).setLeft(this);
					parent.dataList.elementAt(index - 1).setRight(right);
					
					if(index >= 2) {
						parent.dataList.elementAt(index - 2).setRight(this);
					}
				} else {
					parent.dataList.elementAt(index).setLeft(this);
					parent.dataList.elementAt(index).setRight(right);
					
					if(index >= 1) {
						parent.dataList.elementAt(index - 1).setRight(this);
					}
					
					if (index < parent.dataList.size() - 1) {
						parent.dataList.elementAt(index + 1).setLeft(right);
					}
				}
				
			}		
		
		}

		
		public int get(int item) {
			int returnString = -1;
			int location = -1;
			
			for(int i = 0; i < dataList.size(); i++) {
				if(item > dataList.elementAt(i).getData()) {		
					location = i;
				} else if(item == dataList.elementAt(i).getData()) {
					location = i;
					break;
				} else if(location == -1 && item < dataList.elementAt(i).getData()) {
					location = i;
					break;
				}
			}
			
			
			if(location >= dataList.size()) {
				if(dataList.elementAt(dataList.size() - 1).getData() > item) {
					returnString = dataList.elementAt(dataList.size() - 1).left.get(item);
				} else {
					returnString = dataList.elementAt(dataList.size() - 1).right.get(item);
				}
			} else {
				if(dataList.elementAt(location).getData() > item) {
					returnString = dataList.elementAt(location).left.get(item);
				} else {
					returnString = dataList.elementAt(location).right.get(item);
				}
			}

			
			
			return returnString;
		}
		
		public int getLocation(DataNode item) {			
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).getData() >= item.getData()) {
					location = i ;
					break;
				} 
			}	
			
			return location;
		}
		
		public int getLocation(TreeNode node) {
			int location = dataList.size();
			
			for(int i = 0; i < dataList.size(); i++) {
				if(dataList.elementAt(i).equals(node)) {
					location = i ;
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
				if(dataList.elementAt(location).getData() > item.getData()) {
					dataList.elementAt(location).insertLeft(item);
				} else {
					dataList.elementAt(location).insertRight(item);
				}
			}
			
		}	
	}
		
	private class DataNode {
		int value;
		Node right;
		Node left;

		public DataNode(int value) {
			this.value = value;
			right = null;
			left = null;
		}
				
		public int getData() {
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