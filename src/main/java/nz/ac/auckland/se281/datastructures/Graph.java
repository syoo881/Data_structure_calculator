package nz.ac.auckland.se281.datastructures;

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
  private Set<T> verticies = new HashSet<>();
  private Set<Edge<T>> edges = new HashSet<>();
  private Set<T> roots;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
    this.roots = new TreeSet<>();
  }

  public Set<T> getRoots() {
    for (T vertex : this.verticies) {
      boolean isRoot = true;
      boolean hasEdge = false;
      for (Edge<T> edge : this.edges) {
        T source = edge.getSource();
        T destination = edge.getDestination();

        if (vertex.equals(source)) {
          hasEdge = true;
        }
        if ((vertex.equals(destination)) && (!(vertex.equals(source)))) {
          // System.out.println("destination = " + destination);
          // System.out.println("vertex = " + vertex);
          isRoot = false;
        }
      }
      if (hasEdge == false) {
        isRoot = false;
      }
      if (isRoot) {
        roots.add(vertex);
      }
    }

    return roots;

    // Add to this, when you find an equivalence class
    // Maybe also have to add a way to filter out verticies without any outgoing edge (no source)
  }

  // Not even using these am I?
  public boolean isInSource(T vertex) {
    for (Edge<T> edge : this.edges) {
      T source = edge.getSource();
      if (vertex.equals(source)) {
        return true;
      }
    }
    return false;
  }

  public boolean isInDestination(T vertex) {
    for (Edge<T> edge : this.edges) {
      T destination = edge.getDestination();
      if (vertex.equals(destination)) {
        return true;
      }
    }
    return false;
  }

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
    if (isReflexive() && isSymmetric() && isTransitive()) {
      return true;
    }

    return false;
  }

  public Set<T> getEquivalenceClass(T vertex) {
    // TODO: Task 1.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeBreadthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeDepthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveBreadthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveDepthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }
}
