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

  // DON'T FORGET ABOUT THE CODESTYLE, WRITE YOUR OWN MANUAL THING
  private Set<T> verticies = new HashSet<>();
  private Set<Edge<T>> edges = new HashSet<>();
  private Set<T> roots;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
    this.roots = new TreeSet<>(new NumericalComparator());
  }

  public Set<T> getRoots() {
    for (T vertex : this.verticies) {
      boolean isRoot = true;
      for (Edge<T> edge : this.edges) {
        T source = edge.getSource();
        T destination = edge.getDestination();

        if ((vertex.equals(destination)) && (!(vertex.equals(source)))) {
          // System.out.println("destination = " + destination);
          // System.out.println("vertex = " + vertex);
          isRoot = false;
        }
      }
      if (isRoot) {
        roots.add(vertex);
      }
    }
    if (this.isEquivalence()) {
      roots.add(this.getEquivalenceClassVertex());
    }

    // let rearranged roots be roots, but rearranged from smallest number to largest

    return roots;

    // Add to this, when you find an equivalence class
    // Maybe also have to add a way to filter out verticies without any outgoing edge (no source)
  }

  private class NumericalComparator implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
      Integer i1 = Integer.parseInt(o1.toString());
      Integer i2 = Integer.parseInt(o2.toString());
      return i1.compareTo(i2);
    }
  }

  // I don't actually use this method below.
  public Set<T> orderRoots(Set<T> roots) {
    List<Integer> tempRootList = new ArrayList<>();
    Set<T> orderedRoots = new HashSet<>();
    for (T unorderedRoots : roots) {
      tempRootList.add(Integer.parseInt(unorderedRoots.toString()));
    }
    // Rearrange tempRootList to be lowest to highest
    for (int i = 0; i < tempRootList.size(); i++) {
      for (int j = i + 1; j < tempRootList.size(); j++) {
        if (tempRootList.get(i) > tempRootList.get(j)) {
          int temp = tempRootList.get(i);
          tempRootList.set(i, tempRootList.get(j));
          tempRootList.set(j, temp);
        }
      }
    }

    System.out.println(tempRootList);

    for (Integer arrayRoots : tempRootList) {
      orderedRoots.add((T) arrayRoots.toString());
    }
    return orderedRoots;
  }

  public T getEquivalenceClassVertex() {
    List<T> vertexList = new ArrayList<>();
    for (T vertex : this.verticies) {
      if ((getEquivalenceClass(vertex).size() > 0) && (!roots.contains(vertex))) {
        vertexList.add(vertex);
      }
    }
    return vertexList.get(0);
  }

  // Not even using these am I?
  // public boolean isInSource(T vertex) {
  //   for (Edge<T> edge : this.edges) {
  //     T source = edge.getSource();
  //     if (vertex.equals(source)) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  // public boolean isInDestination(T vertex) {
  //   for (Edge<T> edge : this.edges) {
  //     T destination = edge.getDestination();
  //     if (vertex.equals(destination)) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

  public boolean isInBothSourceAndDestination(T vertex) {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      if (vertex.equals(source) && vertex.equals(destination)) {
        return true;
      }
    }
    return false;
  }

  public boolean isReflexive() {
    for (T vertex : this.verticies) {
      if (!(isInBothSourceAndDestination(vertex))) {
        return false;
      }
    }
    return true;
  }

  public boolean isSourceAndDestinationSymmetric(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      if (source.equals(destination1) && destination.equals(source1)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSymmetric() {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      if (!(isSourceAndDestinationSymmetric(source, destination))) {
        return false;
      }
    }
    return true;
  }

  public boolean isInSourceAndDestination(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      if (source.equals(source1) && destination.equals(destination1)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSourceAndDestinationTransitive(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      if (destination.equals(source1)) {
        if (!(isInSourceAndDestination(source, destination1))) {
          return false;
        }
      }
    }
    return true;
  }

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

  public boolean reverseIsInSourceAndDestination(T source, T destination) {
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      if (source.equals(destination1) && destination.equals(source1)) {
        return true;
      }
    }
    return false;
  }

  public boolean isAntiSymmetric() {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();
      if (isInSourceAndDestination(source, destination)) {
        if ((reverseIsInSourceAndDestination(source, destination))) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isEquivalence() {
    if (this.isReflexive() && this.isSymmetric() && this.isTransitive()) {
      return true;
    }
    return false;
  }

  // all treesets might need implementation of numerical list
  public Set<T> equivalenceList(T vertex) {
    Set<T> equivalenceList = new TreeSet<>(new NumericalComparator());
    for (Edge<T> edge : this.edges) {
      T source1 = edge.getSource();
      T destination1 = edge.getDestination();
      if (vertex.equals(source1)) {
        equivalenceList.add(destination1);
      }
    }
    return equivalenceList;
  }

  // Equivalence class might have to consider explicit links, via transitivity
  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClass = new HashSet<T>();
    if (!(this.isEquivalence())) {
      return equivalenceClass;
    }
    return this.equivalenceList(vertex);
  }

  public List<T> iterativeBreadthFirstSearch() {
    if (verticies.isEmpty()) {
      return new ArrayList<>();
    }

    // Choose a starting vertex (in this case, the first vertex in the set)
    // let variable temp be the roots of the graph

    Set<T> temp = this.getRoots();
    // convert temp into a list
    List<T> tempRoots = new ArrayList<>(temp);

    // Create a visited set to keep track of visited vertices
    Set<T> visited = new HashSet<>();

    // Create a custom LinkedList implementation using nodes
    CustomLinkedList<T> linkedList = new CustomLinkedList<>();

    // Create a custom Queue implementation using the custom LinkedList
    CustomQueue<T> queue = new CustomQueue<>();

    for (int i = 0; i < tempRoots.size(); i++) {

      T startVertex = tempRoots.get(i);

      // Mark the start vertex as visited and enqueue it
      visited.add(startVertex);
      queue.enqueue(startVertex);

      while (!queue.isEmpty()) {
        T currentVertex = queue.dequeue();
        linkedList.add(currentVertex);

        // Get the adjacent vertices of the current vertex
        // This set of adjacent vertices, once again returns as random sequence - it should be
        // ordered
        // numerically.
        Set<T> adjacentVertices = getAdjacentVertices(currentVertex);

        for (T adjacentVertex : adjacentVertices) {
          if (!visited.contains(adjacentVertex)) {
            visited.add(adjacentVertex);
            queue.enqueue(adjacentVertex);
          }
        }
      }
    }

    // Return the BFS traversal as a list
    return linkedList.toList();
  }

  private Set<T> getAdjacentVertices(T vertex) {
    Set<T> adjacentVertices = new TreeSet<>(new NumericalComparator());
    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        adjacentVertices.add(edge.getDestination());
      }
    }
    return adjacentVertices;
  }

  // Custom LinkedList implementation using nodes
  private class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    private void add(T data) {
      Node<T> newNode = new Node<>(data);
      if (head == null) {
        head = newNode;
        tail = newNode;
      } else {
        tail.setNext(newNode);
        tail = newNode;
      }
    }

    private T getLast() {
      if (tail == null) {
        // throw new NoSuchElementException();
      }
      return tail.getData();
    }

    private T removeLast() {
      if (isEmpty()) {
        // throw new NoSuchElementException();
      }
      T lastData = tail.getData();
      if (head == tail) {
        head = null;
        tail = null;
      } else {
        Node<T> currentNode = head;
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
      while (currentNode != null) {
        list.add(currentNode.getData());
        currentNode = currentNode.getNext();
      }
      return list;
    }
  }

  // Custom Queue implementation using the custom LinkedList
  private class CustomQueue<T> {
    private CustomLinkedList<T> queue;

    private CustomQueue() {
      queue = new CustomLinkedList<>();
    }

    private void enqueue(T data) {
      queue.add(data);
    }

    private T dequeue() {
      if (queue.head == null) {}
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
      if (isEmpty()) {
        // throw new EmptyStackException();
      }
      return list.removeLast();
    }

    private boolean isEmpty() {
      return list.isEmpty();
    }
  }

  // Node class for the linked list
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

  private Set<T> getNeighborsReverse(T vertex) {
    Set<T> neighbors = new TreeSet<>(new ReverseNumericalComparator());

    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        neighbors.add(edge.getDestination());
      }
    }

    return neighbors;
  }

  private class ReverseNumericalComparator implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
      Integer i1 = Integer.parseInt(o1.toString());
      Integer i2 = Integer.parseInt(o2.toString());
      return i2.compareTo(i1);
    }
  }

  public List<T> iterativeDepthFirstSearch() {
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    CustomStack<T> stack = new CustomStack<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    Set<T> temp = this.getRoots();
    // convert temp into a list
    List<T> tempRoots = new ArrayList<>(temp);

    for (int i = 0; i < tempRoots.size(); i++) {

      T startVertex = tempRoots.get(i);

      stack.push(startVertex);

      while (!stack.isEmpty()) {
        T currentVertex = stack.pop();

        if (!visitedSet.contains(currentVertex)) {
          visited.add(currentVertex);
          visitedSet.add(currentVertex);

          // Get all the neighbors of the current vertex
          Set<T> neighbors = getNeighborsReverse(currentVertex);

          // Push the unvisited neighbors onto the stack
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

  private void recursiveBFS(T vertex, Set<T> visited, CustomLinkedList<T> linkedList) {
    CustomQueue<T> queue = new CustomQueue<>();
    queue.enqueue(vertex);

    while (!queue.isEmpty()) {
        T currentVertex = queue.dequeue();

        if (!visited.contains(currentVertex)) {
            visited.add(currentVertex);
            linkedList.add(currentVertex);

            Set<T> adjacentVertices = getAdjacentVertices(currentVertex);
            for (T adjacentVertex : adjacentVertices) {
                if (!visited.contains(adjacentVertex)) {
                    queue.enqueue(adjacentVertex);
                }
            }
        }
      }
    }

  public List<T> recursiveBreadthFirstSearch() {
    if (verticies.isEmpty()) {
      return new ArrayList<>();
  }

  Set<T> temp = getRoots();
  List<T> tempRoots = new ArrayList<>(temp);

  Set<T> visited = new HashSet<>();
  CustomLinkedList<T> linkedList = new CustomLinkedList<>();

  for (T root : tempRoots) {
      recursiveBFS(root, visited, linkedList);
  }

  return linkedList.toList();
  }

  private Set<T> getNeighbors(T vertex) {
    Set<T> neighbors = new TreeSet<>(new NumericalComparator());

    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(vertex)) {
        neighbors.add(edge.getDestination());
      }
    }

    return neighbors;
  }

  private void recursiveDFS(T vertex, List<T> visited, Set<T> visitedSet) {
    visited.add(vertex);
    visitedSet.add(vertex);

    Set<T> neighbors = new TreeSet<>(new ReverseNumericalComparator());
    neighbors = getNeighbors(vertex);

    for (T neighbor : neighbors) {
      if (!visitedSet.contains(neighbor)) {
        recursiveDFS(neighbor, visited, visitedSet);
      }
    }
  }

  public List<T> recursiveDepthFirstSearch() {
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();

    if (verticies.isEmpty()) {
      return visited;
    }

    Set<T> temp = getRoots();
    List<T> tempRoots = new ArrayList<>(temp);

    for (T startVertex : tempRoots) {
      recursiveDFS(startVertex, visited, visitedSet);
    }

    return visited;
  }
}
