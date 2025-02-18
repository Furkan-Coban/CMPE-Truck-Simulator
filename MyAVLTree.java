
public class MyAVLTree {
    private class Node {
        ParkingLot element;
        Node left, right;
        int height;
        int subtreeTruckNumber;
        Node(ParkingLot element) {
            this.element = element;
            this.height = 1;
            this.subtreeTruckNumber = element.getCurrentTruckNumber();
        }
    }
    private Node root;


    private void updateSubtreeTruckNumber(Node node) {
        if (node != null) {
            node.subtreeTruckNumber = node.element.getCurrentTruckNumber() // Truck number of node itself is added.
                    + (node.left != null ? node.left.subtreeTruckNumber : 0) // If node.left is not null, its subTreeCount is added.
                    + (node.right != null ? node.right.subtreeTruckNumber : 0); // If node.right is not null its subTreeCount is added.
        }
    }


    public void updateSubtreeCount(ParkingLot element, int i){
        updateSubtreeCountForAncestors(root,element,i);
    }

    //Helper method to update truck count for all ancestors in the AVL Tree.
    private Node updateSubtreeCountForAncestors(Node node, ParkingLot element, int adjustment) {
        if (node == null) return null;

        int compare = element.compareTo(node.element);

        if (compare < 0) {
            node.left = updateSubtreeCountForAncestors(node.left, element, adjustment);
        } else if (compare > 0) {
            node.right = updateSubtreeCountForAncestors(node.right, element, adjustment);
        }
        node.subtreeTruckNumber += adjustment;

        return balance(node);
    }

    public void insert(ParkingLot element) {
        root = insert(root, element);

    }

    //Recursive insert helper method.
    private Node insert(Node node, ParkingLot element) {
        if (node == null) {
            return new Node(element);
        }
        int compare = element.compareTo(node.element);
        if (compare < 0) {
            // Insert in the left subtree if element is less than current node's element
            node.left = insert(node.left, element);
        } else if (compare > 0) {
            // Insert in the right subtree if element is greater than current node's element
            node.right = insert(node.right, element);
        } else {
            // Duplicate element; do nothing and return the current node
            return node;
        }
        // Update height and truck count for balancing purposes
        updateHeight(node);
        updateSubtreeTruckNumber(node);
        return balance(node);
    }

    public void delete(ParkingLot element) {
        root = delete(root, element);
    }


    private Node delete(Node node, ParkingLot element) {
        if (node == null) return null;

        int compare = element.compareTo(node.element);

        if (compare < 0) {
            // Traverse to the left subtree if the element is smaller
            node.left = delete(node.left, element);
        } else if (compare > 0) {
            // Traverse to the right subtree if the element is larger
            node.right = delete(node.right, element);
        } else {
            // Node to be deleted found
            if (node.left == null) {
                return node.right; // Case 1: No child or right-only child
            } else if (node.right == null) {
                return node.left;  // Case 2: Left-only child
            } else {
                // Case 3: Node with two children
                Node temp = getMinValueNode(node.right); // Successor from the right subtree
                node.element = temp.element;             // Replace element with successor's element
                node.right = delete(node.right, temp.element); // Delete the successor
            }
        }

        // Update the height of the current node
        updateHeight(node);
        updateSubtreeTruckNumber(node);
        return balance(node);
    }

    private Node getMinValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        } else {
            return node.height;
        }
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }


    private Node rotateRight(Node node) {
        Node leftStart = node.left;  //Node that will become new root of this subtree
        node.left = leftStart.right; // Move leftStart's right subtree to node's left
        leftStart.right = node; // Set node as the right child of leftStart

        updateHeight(node);
        updateHeight(leftStart);
        updateSubtreeTruckNumber(node);
        updateSubtreeTruckNumber(leftStart);
        return leftStart;
    }

    private Node rotateLeft(Node node) {
        Node rightStart = node.right; // Node that will become new root of this subtree
        node.right = rightStart.left; // Move rightStart's left subtree to node's right
        rightStart.left = node; // Set node as the left child of rightStart

        updateHeight(node);
        updateHeight(rightStart);
        updateSubtreeTruckNumber(node);
        updateSubtreeTruckNumber(rightStart);
        return rightStart;
    }


    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        } else {
            return getHeight(node.left) - getHeight(node.right); //return the balanceFactor of the node
        }
    }

    private Node balance(Node node) {
        int balanceFactor = getBalanceFactor(node);
        if (balanceFactor > 1) { //Left is heavy
            if (getBalanceFactor(node.left) < 0) { // Left-Right case, requires double rotation
                node.left = rotateLeft(node.left); // Perform left rotation on left child
            }
            return rotateRight(node); // Perform right rotation on node
        }
        if (balanceFactor < -1) { //Right is heavy
            if (getBalanceFactor(node.right) > 0) {   // Right-Left case, requires double rotation
                node.right = rotateRight(node.right); // Perform right rotation on right child
            }
            return rotateLeft(node); // Perform left rotation on node
        }
        return node;

    }

    // Finds the node with the smallest value greater than the specified element.
    public ParkingLot getSmallestAbove(ParkingLot element){
        Node current = root;
        Node temporaryAns = null;

        while(current!=null){
            if(element.compareTo(current.element)<0){  //Current is bigger, so it goes to left child
                temporaryAns = current;
                current = current.left;
            }
            else{ // elements are equal or current is smaller, in both cases it goes to right child.
                current = current.right;
            }
        }
        if(temporaryAns==null){
            return null;
        }
        else{
            return temporaryAns.element;
        }
    }

    public ParkingLot getLargestBelow(ParkingLot element) {
        Node current = root;
        Node temporaryAns = null;

        while (current != null) {
            if (element.compareTo(current.element) > 0) {  // Current is smaller, it goes the right child.
                temporaryAns = current;
                current = current.right;
            } else {  // Current is equal or larger, it goes to left child.
                current = current.left;
            }
        }

        if (temporaryAns == null) {
            return null;
        } else {
            return temporaryAns.element;
        }
    }

    //Calculates the total truck count for all parking lots with a capacity greater than the specified element.
    public int getAllLarger(ParkingLot element) {
        return getAllLarger(root, element);
    }

    //Recursive Helper method for getAllLarger
    private int getAllLarger(Node node, ParkingLot element) {
        if (node == null) {
            return 0;
        }

        int compare = element.compareTo(node.element);
        int count = 0;

        if (compare < 0) {

            // Count this node's trucks and add the trucks in its right subtree because right subtree is also bigger than element
            count += node.element.getCurrentTruckNumber();;
            if (node.right != null) {
                count += node.right.subtreeTruckNumber;
            }
            // Recursively check the left subtree for more nodes greater than the element
            count += getAllLarger(node.left, element);
        } else if (compare==0) {
            if(node.right!=null){count+=node.right.subtreeTruckNumber;}
        }
        else{
            // Node element is less than or equal to the input element, move to the right subtree
            count += getAllLarger(node.right, element);
        }
        return count;
    }

}