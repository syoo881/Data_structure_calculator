package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {

  private class NumericalComparator implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
      Integer i1 = Integer.parseInt(o1.toString());
      Integer i2 = Integer.parseInt(o2.toString());
      return i1.compareTo(i2);
    }
  }

  private class ReverseNumericalComparator implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
      Integer i1 = Integer.parseInt(o1.toString());
      Integer i2 = Integer.parseInt(o2.toString());
      return i2.compareTo(i1);
    }
  }

  private class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    private void add(T data) {
      // Create a new node, and add it to the end of the linked list
      Node<T> newNode = new Node<>(data);
      if (head == null) {
        head = newNode;
        tail = newNode;
      } else {
        tail.setNext(newNode);
        tail = newNode;
      }
    }

    private T removeLast() {
      T lastData = tail.getData();
      if (head == tail) {
        head = null;
        tail = null;
        // remove the last node if head is not equal to tail
      } else {
        Node<T> currentNode = head;
        // Keep getting the next node until the next node is the tail
        while (currentNode.getNext() != tail) {
          currentNode = currentNode.getNext();
        }
        currentNode.setNext(null);
        tail = currentNode;
      }
      return lastData;
    }

    private boolean isEmpty() {
      return head == null;
    }

    private List<T> toList() {
      List<T> list = new ArrayList<>();
      Node<T> currentNode = head;
      // Using nodes to get the data of each node, and add it to the list
      while (currentNode != null) {
        list.add(currentNode.getData());
        currentNode = currentNode.getNext();
      }
      return list;
    }
  }

  private class CustomQueue<T> {
    private CustomLinkedList<T> queue;

    private CustomQueue() {
      queue = new CustomLinkedList<>();
    }

    private void enqueue(T data) {
      queue.add(data);
    }

    private T dequeue() {
      T data = queue.head.getData();
      queue.head = queue.head.getNext();
      return data;
    }

    private boolean isEmpty() {
      return queue.head == null;
    }
  }

  private class CustomStack<T> {
    private CustomLinkedList<T> list;

    private CustomStack() {
      list = new CustomLinkedList<>();
    }

    private void push(T data) {
      list.add(data);
    }

    private T pop() {
      return list.removeLast();
    }

    private boolean isEmpty() {
      return list.isEmpty();
    }
  }

  private class Node<T> {
    private T data;
    private Node<T> next;

    private Node(T data) {
      this.data = data;
      next = null;
    }

    private T getData() {
      return data;
    }

    private Node<T> getNext() {
      return next;
    }

    private void setNext(Node<T> next) {
      this.next = next;
    }
  }

  private Set<T> verticies;
  private Set<Edge<T>> edges;
  private Set<T> roots;

  /**
   * Creates a new graph.
   *
   * @param verticies The set of verticies in the graph.
   * @param edges The set of edges in the graph.
   */
  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
    this.roots = new TreeSet<>(new NumericalComparator());
  }

  /**
   * takes the set of verticies and edge, and returns the root verticies of the graph.
   *
   * @return The set of root verticies.
   */
  public Set<T> getRoots() {
    // Looping through verticies and edges.
    for (T vertex : this.verticies) {
      boolean isRoot = true;
      for (Edge<T> edge : this.edges) {
        T source = edge.getSource();
        T destination = edge.getDestination();
        // Set isRoot to false if the vertex is a destination but not a source.
        if ((vertex.equals(destination)) && (!(vertex.equals(source)))) {
          isRoot = false;
        }
      }
      // If the vertex is a root, add it to the set of roots.
      if (isRoot) {
        roots.add(vertex);
      }
    }
    // If the graph is an equivalence, add the equivalence class vertex to the set of roots.
    if (this.isEquivalence()) {
      for (T vertex : this.getEquivalenceClassVertex()) {
        roots.add(vertex);
      }
    }
    return roots;
    // Maybe also have to add a way to filter out verticies without any outgoing edge (no source)
  }

  /**
   * adds lowest indexed number of the equivalence classes to a list and returns it.
   *
   * @return A list of veriticies that are the root of an equivalence class.
   */
  public List<T> getEquivalenceClassVertex() {
    List<T> vertexList = new ArrayList<>();
    for (T vertex : this.verticies) {
      // Turn the equivalence class set into a list, to allow indexing
      List<T> equivalenceClass = new ArrayList<>(this.getEquivalenceClass(vertex));
      if ((getEquivalenceClass(vertex).size() > 0) && (!roots.contains(vertex))) {
        // Only adding the first vertex, the lowest number, to the list of roots
        if (vertex == equivalenceClass.get(0)) {
          vertexList.add(vertex);
        }
      }
    }
    return vertexList;
  }

  /**
   * checks if the vertex is both a source and destination, and returns a boolean value.
   *
   * @param vertex the vertex to check.
   * @return boolean value of whether the vertex is both a source and destination.
   */
  public boolean isInBothSourceAndDestination(T vertex) {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      // Check if the vertex is both a source and destination
      if (vertex.equals(source) && vertex.equals(destination)) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks if the graph is reflexive, and returns a boolean value.
   *
   * @return boolean value of whether the graph is reflexive.
   */
  public boolean isReflexive() {
    for (T vertex : this.verticies) {
      if (!(isInBothSourceAndDestination(vertex))) {
        return false;
      }
    }
    return true;
  }

  /**
   * checks if source and destination are the same, and returns a boolean value.
   *
   * @param source the source vertex.
   * @param destination the destination vertex.
   * @return boolean value of whether the graph is symmetric.
   */
  public boolean isSourceAndDestinationSymmetric(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      // If source if equal to destination and destination is equal to source, return true
      if (source.equals(destination1) && destination.equals(source1)) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks if the graph is symmetric, and returns a boolean value.
   *
   * @return boolean value of whether the graph is symmetric.
   */
  public boolean isSymmetric() {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      // use the isSourceAndDestinationSymmetric method to check if the graph is symmetric
      if (!(isSourceAndDestinationSymmetric(source, destination))) {
        return false;
      }
    }
    return true;
  }

  /**
   * checks whether the given source and destination are connected by the same edge and returns a
   * boolean value.
   *
   * @param source the source vertex.
   * @param destination the source destination.
   * @return boolean value whether the source and destination are connected by the same edge.
   */
  public boolean isInSourceAndDestination(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      // If source and destination are connected by the same edge, return true
      if (source.equals(source1) && destination.equals(destination1)) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks whether the source and destination are transitive.
   *
   * @param source the source vertex.
   * @param destination the destination vertex.
   * @return boolean value whether the source and destination are transitive.
   */
  public boolean isSourceAndDestinationTransitive(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      // check whether the source vertex has an edge to the new destination vertex
      if (destination.equals(source1)) {
        if (!(isInSourceAndDestination(source, destination1))) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * checks whether the graph is transitive.
   *
   * @return boolean value whether the graph is transitive.
   */
  public boolean isTransitive() {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      if (!(isSourceAndDestinationTransitive(source, destination))) {
        return false;
      }
    }
    return true;
  }

  /**
   * checks whether there exists an edge from the destination to the source and returns a boolean
   * value.
   *
   * @param source the source vertex.
   * @param destination the source destination.
   * @return boolean value whether there exists an edge from the destination to the source.
   */
  public boolean reverseIsInSourceAndDestination(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      // If there exists an edge from the destination to the source, return true
      if (source.equals(destination1) && destination.equals(source1)) {
        return true;
      }
    }
    return false;
  }

  /**
   * checks whether the graph is antisymmetric.
   *
   * @return boolean value whether the graph is antisymmetric.
   */
  public boolean isAntiSymmetric() {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      // check if the source and destination are connected by an edge
      if (isInSourceAndDestination(source, destination)) {
        // check if there is an edge from the destination to the source
        if ((reverseIsInSourceAndDestination(source, destination))) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * checks whether the graph is an Equivalence relation.
   *
   * @return boolean value whether the graph is an equivalence relation.
   */
  public boolean isEquivalence() {
    if (this.isReflexive() && this.isSymmetric() && this.isTransitive()) {
      return true;
    }
    return false;
  }

  /**
   * goes through all verticies, and returns a numerically ordered set of verticies related to the
   * given vertex.
   *
   * @param vertex the vertex to check.
   * @return a numerically ordered set of verticies related to the given vertex.
   */
  public Set<T> equivalenceList(T vertex) {
    // Create a new set to store the equivalence list in numerical order
    Set<T> equivalenceList = new TreeSet<>(new NumericalComparator());
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      // If the vertex is a source, add the destination to the equivalence list
      if (vertex.equals(source1)) {
        equivalenceList.add(destination1);
      }
    }
    return equivalenceList;
  }

  /**
   * returns the equivalence class of a given vertex.
   *
   * @param vertex the vertex to check.
   * @return the set of equivalence class of the vertex.
   */
  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClass = new HashSet<T>();
    // return empty set if not equivalence relation
    if (!(this.isEquivalence())) {
      return equivalenceClass;
    }
    return this.equivalenceList(vertex);
  }

  /**
   * performs an iterative BreadthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the BreadthFirstSearch algorithm.
   */
  public List<T> iterativeBreadthFirstSearch() {
    if (verticies.isEmpty()) {
      return new ArrayList<>();
    }

    Set<T> temp = this.getRoots();
    List<T> tempRoots = new ArrayList<>(temp);
    Set<T> visited = new HashSet<>();
    // Create a custom LinkedList, and queue using this custom Linkedlist
    CustomLinkedList<T> linkedList = new CustomLinkedList<>();
    CustomQueue<T> queue = new CustomQueue<>();

    // Perform the queue operation for every root vertex
    for (int i = 0; i < tempRoots.size(); i++) {
      T startVertex = tempRoots.get(i);
      visited.add(startVertex);
      queue.enqueue(startVertex);

      // While the queue is not empty, dequeue the first element, and add it to the linkedList
      while (!queue.isEmpty()) {
        T currentVertex = queue.dequeue();
        linkedList.add(currentVertex);
        Set<T> adjacentVertices = getAdjacentVertices(currentVertex);

        // Use the adjacent verticies to add to the queue
        for (T adjacentVertex : adjacentVertices) {
          if (!visited.contains(adjacentVertex)) {
            visited.add(adjacentVertex);
            queue.enqueue(adjacentVertex);
          }
        }
      }
    }
    return linkedList.toList();
  }

  /**
   * returns the set of adjacent verticies of the given vertex.
   *
   * @param vertex the vertex to find the adjacent verticies of.
   * @return the set of adjacent verticies of the given vertex.
   */
  private Set<T> getAdjacentVertices(T vertex) {
    // make the set of adjacent veticies, be in numerical order
    Set<T> adjacentVertices = new TreeSet<>(new NumericalComparator());
    for (Edge<T> edge : edges) {
      // If the vertex is a source, add the destination to the set of adjacent verticies
      if (edge.getSource().equals(vertex)) {
        adjacentVertices.add(edge.getDestination());
      }
    }
    return adjacentVertices;
  }

  /**
   * returns the set of verticies that are adjacent to the given vertex, in reverse numerical order.
   *
   * @param vertex the vertex to find the adjacent verticies of.
   * @return the set of verticies that are adjacent to the given vertex, in reverse numerical order.
   */
  private Set<T> getNeighborsReverse(T vertex) {
    // Get neighbors of a given vertex, and put them in revere numerical order
    Set<T> neighbors = new TreeSet<>(new ReverseNumericalComparator());
    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        neighbors.add(edge.getDestination());
      }
    }
    return neighbors;
  }

  /**
   * performs an iterative DepthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through by the DepthFirstSearch algorithm.
   */
  public List<T> iterativeDepthFirstSearch() {
    // Create a stack implementation using the customLinkedList
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomStack<T> stack = new CustomStack<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    Set<T> temp = this.getRoots();
    // convert the roots Set into an ArrayList
    List<T> tempRoots = new ArrayList<>(temp);

    // Perform the stack operation for every root vertex
    for (int i = 0; i < tempRoots.size(); i++) {
      T startVertex = tempRoots.get(i);
      stack.push(startVertex);

      // While the stack is not empty, pop the current vertex, and add it to the visited list
      while (!stack.isEmpty()) {
        T currentVertex = stack.pop();
        if (!visitedSet.contains(currentVertex)) {
          visited.add(currentVertex);
          visitedSet.add(currentVertex);
          Set<T> neighbors = getNeighborsReverse(currentVertex);

          // Push the unvisited neighbors onto the stack to continue iterating
          for (T neighbor : neighbors) {
            if (!visitedSet.contains(neighbor)) {
              stack.push(neighbor);
            }
          }
        }
      }
    }

    return visited;
  }

  /**
   * performs a recursive BreadthFirstSearch to update the linkedList.
   *
   * @param roots roots of the vertex.
   * @param index index of the roots array.
   * @param queue queue of verticies to visit.
   * @param visited visited set of verticies of the graph.
   * @param linkedList linkedList of the vertex.
   */
  private void recursivebfs(
      List<T> roots,
      int index,
      CustomQueue<T> queue,
      Set<T> visited,
      CustomLinkedList<T> linkedList) {
    if (index >= roots.size()) {
      return;
    }

    // process the root one at a time
    T root = roots.get(index);
    queue.enqueue(root);
    // Use the helper processQueue method, to actually process the queue and add values
    processQueue(queue, visited, linkedList);

    // Calling recursively
    recursivebfs(roots, index + 1, queue, visited, linkedList);
  }

  /**
   * helper method to process the queue and add values to the linkedList.
   *
   * @param queue queue of verticies to visit.
   * @param visited visited set of verticies of the graph.
   * @param linkedList linkedList of the vertex.
   */
  private void processQueue(CustomQueue<T> queue, Set<T> visited, CustomLinkedList<T> linkedList) {
    if (queue.isEmpty()) {
      return;
    }

    // dequeue the first element, and add it to the linkedList
    T currentVertex = queue.dequeue();
    if (!visited.contains(currentVertex)) {
      visited.add(currentVertex);
      linkedList.add(currentVertex);

      // Use the adjacent vertices to add to the queue
      Set<T> adjacentVertices = getAdjacentVertices(currentVertex);
      for (T adjacentVertex : adjacentVertices) {
        if (!visited.contains(adjacentVertex)) {
          queue.enqueue(adjacentVertex);
        }
      }
    }

    // recursive call
    processQueue(queue, visited, linkedList);
  }

  /**
   * performs a recursive BreadthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through recursively by the BreadthFirstSearch
   *     algorithm.
   */
  public List<T> recursiveBreadthFirstSearch() {
    if (verticies.isEmpty()) {
      return new ArrayList<>();
    }

    Set<T> temp = getRoots();
    List<T> tempRoots = new ArrayList<>(temp);

    // Create a custom LinkedList, and queue using this custom Linkedlist
    Set<T> visited = new HashSet<>();
    CustomLinkedList<T> linkedList = new CustomLinkedList<>();
    CustomQueue<T> queue = new CustomQueue<>();

    // Call the recursiveBFS method to perform the recursive BFS
    recursivebfs(tempRoots, 0, queue, visited, linkedList);

    return linkedList.toList();
  }

  /**
   * returns the set of verticies that are adjacent to the given vertex, in numerical order.
   *
   * @param vertex the vertex to find the adjacent verticies of.
   * @return the set of verticies that are adjacent to the given vertex, in numerical order.
   */
  private Set<T> getNeighbors(T vertex) {
    // create a set of neighbors, to add them in numerical order
    Set<T> neighbors = new TreeSet<>(new NumericalComparator());

    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        neighbors.add(edge.getDestination());
      }
    }

    return neighbors;
  }

  /**
   * performs a recursive DepthFirstSearch to update the visited list.
   *
   * @param vertex vertex to start the DFS from.
   * @param visited visited list of verticies.
   * @param visitedSet visited set of verticies.
   */
  private void recursivedfs(T vertex, List<T> visited, Set<T> visitedSet) {
    visited.add(vertex);
    visitedSet.add(vertex);

    // Create a set of neighbors, to add them in reverse numerical order
    Set<T> neighbors;
    neighbors = getNeighbors(vertex);

    // Using the neighbors, recursively call the recursiveDFS method
    for (T neighbor : neighbors) {
      if (!visitedSet.contains(neighbor)) {
        recursivedfs(neighbor, visited, visitedSet);
      }
    }
  }

  /**
   * performs a recursive DepthFirstSearch, and returns the searched list of verticies.
   *
   * @return the list of verticies, as searched through recursively by the DepthFirstSearch
   *     algorithm.
   */
  public List<T> recursiveDepthFirstSearch() {
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    Set<T> temp = getRoots();
    List<T> tempRoots = new ArrayList<>(temp);

    // For every root vertex, call the recursiveDFS method
    for (T startVertex : tempRoots) {
      // Call the recursiveDFS method to perform the recursive DFS
      recursivedfs(startVertex, visited, visitedSet);
    }

    return visited;
  }
}
