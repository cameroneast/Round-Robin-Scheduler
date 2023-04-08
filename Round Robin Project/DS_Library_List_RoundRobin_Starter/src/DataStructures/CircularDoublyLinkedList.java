package DataStructures;

import ADTs.ListADT;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Exceptions.InvalidArgumentException;
/**
 * 
 * @author ITSC 2214 Q.
 * @param <T> 
 * @version 1.0
 */
public class CircularDoublyLinkedList<T extends Comparable<T>> implements ListADT<T> {
    private DoublyLinkedNode<T> first;
    private int numNodes;

    /**
     * Constructor.
     */
    public CircularDoublyLinkedList() {
        first = null;
        numNodes = 0;
    }
    
    
    /**
     * Retrieve the first element in the circular list,
     * which is stored in the current first node
     * @return first element
     */
    @Override
    public T first() throws EmptyCollectionException{
        if (first == null)
            throw new EmptyCollectionException("CircularDoublyLinkedList");

        return first.getElement();
    }
    
    /**
     * retrieve the element of the current first node in the list
     * @return 
     */
    public T current(){
        if (first == null)
            return null;

        return first.getElement();
    }
    
    /**
     * Retrieve the last element in the list,
     * which next reference points to the current first node
     * @return
     * @throws EmptyCollectionException 
     */
    @Override
    public T last() throws EmptyCollectionException{
        if (first == null)
            throw new EmptyCollectionException("CircularDoublyLinkedList");

        return first.getPrev().getElement();
    }

    @Override
    /**
     * Examine whether the circular list is empty.
    */
    public boolean isEmpty() {
        return (first == null);
    }

    /**
     * Print all elements in the list.
     */
    public void printList() {
        // Is list empty?
        if(first == null)
            return;

        // start at the head and print everyone
        boolean startFlag = true;
        for(DoublyLinkedNode tmp=first; (tmp != first) || startFlag;
                      tmp=tmp.getNext()) {
            System.out.println(tmp.getElement().toString());
            startFlag = false;
        }
    }
    
    /**
     * Add a new element in the end of the circular list,
     * which next reference points to the current first node.
     * @param element 
     */
    @Override
    public void addLast(T element) {
        //TODO create the new node
        DoublyLinkedNode<T> temp = new DoublyLinkedNode<T>(element);
        //TODO adjust references for new node
        if(isEmpty()){
            temp.setNext(temp);
            temp.setPrev(temp);
            first = temp;
        }
        else{
            DoublyLinkedNode<T> last = first.getPrev();
            temp.setNext(first);
            temp.setPrev(last);
            first.setPrev(temp);
            last.setNext(temp);
            
        }
        //TODO increase the numNodes variable
        numNodes++;

    }
    /**
     * Add a new element in the beginning of the circular list,
     * which is represented as the current first node.
     */
    @Override
    public void addFirst(T element) {
        // create the new node
        DoublyLinkedNode<T> newNode = new DoublyLinkedNode<T>(element);
        //TODO adjust the current first node reference 
        if(isEmpty()){
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
            first = newNode;
        }
        else{
            newNode.setNext(first);
            newNode.setPrev(first.getPrev());
            first.getPrev().setNext(newNode);
            first.setPrev(newNode);
            first = newNode;
        }
        numNodes++;

    }
    
    /**
     * Insert a new node which holds the reference to the given element,
     * after the node which holds the given existing.
     * Namely insert the given element after the existing element in the list
        If the given existing is not found, throw an 
        ElementNotFoundException.

        Note: the CircularDoublyLinkedList class uses the data member variable first
        and numNodes to maintain the status of a CircularDoublyLinkedList instance.
        When the method addAfter is invoked, we need to make sure that the
        data member variable first, and numNodes are changed properly.
        Do we need to change the data member variable first?
        And when?
     * @param existing
     * @param element
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException 
     */
    @Override
    public void addAfter(T existing, T element) throws ElementNotFoundException, EmptyCollectionException {
        DoublyLinkedNode<T> foundNode = find(existing);

        if (foundNode == null) {
            throw new ElementNotFoundException("Node with element " + existing + " not found.");
        }

        DoublyLinkedNode<T> newNode = new DoublyLinkedNode<>(element);
        newNode.setNext(foundNode.getNext());
        newNode.setPrev(foundNode);
        foundNode.getNext().setPrev(newNode);
        foundNode.setNext(newNode);

        if (foundNode == first.getPrev()) {
            first.setPrev(newNode);
        }

        numNodes++;
}


    /**
     * Remove a node that holds the given element.
     * @param element
     * @return
     * @throws EmptyCollectionException
     * @throws ElementNotFoundException 
     */
    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) {
          throw new EmptyCollectionException("The list is empty!");
        }
        try{
            
        DoublyLinkedNode<T> foundNode = find(element);
     
        // Remove the node
        if (size() == 1) { // List has only one element
           first = null;
           first.setPrev(null);
        } else {
           foundNode.getPrev().setNext(foundNode.getNext());
           foundNode.getNext().setPrev(foundNode.getPrev());
           if (foundNode == first) {
              first = foundNode.getNext();
          }
          if (foundNode == first.getPrev()) {
             first.setPrev(foundNode.getPrev());
         }
        }
        numNodes--;

        return foundNode.getElement();
        }
        catch(Exception ex){
            
            throw new ElementNotFoundException("Element was not found in the list!");
        }

}


    /**
     * Remove the first node in the list.
     * (prior to the current first node)
     * @return
     * @throws EmptyCollectionException 
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {
        // case 1: empty list
        if(first == null)
            throw new EmptyCollectionException("The list is empty!");
        
        DoublyLinkedNode<T> temp = first;
        if (this.numNodes == 1){ 
            //case 2
            first.setNext(null);
            first.setPrev(null);
            first = null;
        } else {
            //case 3
            first.getPrev().setNext(first.getNext());
            first.getNext().setPrev(first.getPrev());
            first = temp.getNext();
        }
        numNodes--;
        // return a pointer to the removed node
        return temp.getElement();
    }

    /**
     * Remove the last node in the list.
     * (prior to the current first node)
     * @return
     * @throws EmptyCollectionException 
     */
    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The list is empty!");
        }
        
        // Store a reference to the node to be removed
        DoublyLinkedNode<T> temp = first.getPrev();
     
        // Update the surrounding nodes to bypass the node to be removed
        first.setPrev(temp.getPrev());
        first.getPrev().setNext(first);
        first.setPrev(first.getPrev());
        
        // If the list had only one node, now it is empty
        if (size() == 1) {
            first = null;
        }
        
        numNodes--;
        
        // Return the element of the removed node
        return temp.getElement();
}

    /**
     * Examine whether the list includes the given element.
     * @param element
     * @return true if the element is found, false otherwise
     * @throws EmptyCollectionException 
     */
    @Override
    public boolean contains(T element) throws EmptyCollectionException {
      if (isEmpty()) {
          throw new EmptyCollectionException("CircularDoublyLinkedList");
      }
      
      DoublyLinkedNode<T> current = first;
      do {
         if (current.getElement().equals(element)) {
             return true;
            }
            current = current.getNext();
        } while (current != first);
      
     return false;
    }

    
    /**
     * Find the first node which holds the given element.
     * @param element
     * @return
     * @throws EmptyCollectionException 
     */
    public DoublyLinkedNode<T> find(T element) throws ElementNotFoundException, EmptyCollectionException {
        if (numNodes == 0)
            throw new EmptyCollectionException("CircularDoublyLinkedList");

        // walk through the list and find the given element in the list
        boolean startFlag = true;
        DoublyLinkedNode<T> tmp;
        for(tmp=first; (tmp != first) || startFlag; tmp=tmp.getNext()) {
            startFlag = false;
            if (element.compareTo(tmp.getElement()) == 0)
                return tmp;
        }
        throw new ElementNotFoundException("CircularDoublyLinkedList");
    }
    
    /**
     * Retrieve the node at the given index of the list.
     * @param index
     * @return
     * @throws EmptyCollectionException
     * @throws InvalidArgumentException 
     */
    public DoublyLinkedNode<T> getNode(int index) throws EmptyCollectionException, InvalidArgumentException {
        if (numNodes == 0)
            throw new EmptyCollectionException("CircularDoublyLinkedList");

        if (index < 0 || index >= numNodes) 
            throw new InvalidArgumentException("CircularDoublyLinkedList");
        
        // TODO walk through the list till the given index
        DoublyLinkedNode<T> tmp=first;
        for (int count = 0; count < index; count++)
            tmp=tmp.getNext();

        return tmp;
    }
    
    /**
     * Change the current first node reference to its next node.
     */
    public void next() {
        if(this.first != null)
            first = first.getNext();
    }
    
    /**
     * Retrieve the element at the given index of the list.
     * @param index
     * @return
     * @throws EmptyCollectionException
     * @throws InvalidArgumentException 
     */
    @Override
    public T get(int index) throws EmptyCollectionException, InvalidArgumentException {
        DoublyLinkedNode<T> tmp = getNode(index);
        return tmp.getElement();
    }

    /**
     * Reset the element in the given index of the list.
     * @param index
     * @param element
     * @throws EmptyCollectionException
     * @throws InvalidArgumentException 
     */
    @Override
    public void set(int index, T element) throws EmptyCollectionException, InvalidArgumentException {
        DoublyLinkedNode<T> tmp = getNode(index);
        tmp.setElement(element);
    }

    /**
     * Return the total number of elements in the list.
     * @return 
     */
    @Override
    public int size() {
        return this.numNodes;
    }
}
