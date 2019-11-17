
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Stack<Edge>[] adj;

    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (Stack<Edge>[]) new Stack[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Stack<Edge>();
        }
    }

    public EdgeWeightedGraph(String filename) throws IOException {

        FileReader fr = new FileReader("src/" + filename);

        BufferedReader br = new BufferedReader(fr);

        Scanner sc = new Scanner(br);

        String[] inputSet1 = sc.nextLine().split(" ");

        int cityNumber = Integer.parseInt(inputSet1[0]), edgeNumber = Integer.parseInt(inputSet1[1]);

        String[] Cities = new String[cityNumber];
        int[][] Coord = new int[cityNumber][2];

        for (int i = 0; i != cityNumber; i++) {

            String[] inputSet2 = sc.nextLine().split(" ");

            Cities[i] = inputSet2[3];

            Coord[i][0] = Integer.parseInt(inputSet2[1]);
            Coord[i][1] = Integer.parseInt(inputSet2[2]);

        }


        V = cityNumber;
        adj = (Stack<Edge>[]) new Stack[V];

        for (int v = 0; v < V; v++) {
            adj[v] = new Stack<Edge>();
        }

        int E = edgeNumber;
        for (int i = 0; i < E; i++) {
            int v = sc.nextInt();
            int w = sc.nextInt();
            validateVertex(v);
            validateVertex(w);

            int weight = getDistance(v,w, Coord);

            Edge e = new Edge(v, w, weight);

            addEdge(e);


        }



        br.close();
        fr.close();

    }

    private int getDistance(int v, int w, int[][] coord) {

        int firstX = coord[v][0], firstY = coord[v][1], secondX = coord[w][0], secondY = coord[w][1];

        return (int) Math.sqrt(Math.pow(secondX-firstX,2)+Math.pow(secondY-firstY,2));

    }


//    public EdgeWeightedGraph(In in) {
//        if (in == null) throw new IllegalArgumentException("argument is null");
//
//        try {
//            V = in.readInt();
//            adj = (Stack<Edge>[]) new Stack[V];
//            for (int v = 0; v < V; v++) {
//                adj[v] = new Stack<Edge>();
//            }*
//
//            int E = in.readInt();
//            if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
//            for (int i = 0; i < E; i++) {
//                int v = in.readInt();
//                int w = in.readInt();*
//                validateVertex(v);*
//                validateVertex(w);*
//                double weight = in.readDouble();
//                Edge e = new Edge(v, w, weight);
//                addEdge(e);
//            }
//        }
//        catch (NoSuchElementException e) {
//            throw new IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e);
//        }

//    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of {@code G}.
     *
     * @param G the edge-weighted graph to copy
     */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adj[v].add(e);
            }
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<Edge> edges() {
        Stack<Edge> list = new Stack<Edge>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedGraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
//        In in = new In(args[0]);
//        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
//        StdOut.println(G);
    }

}