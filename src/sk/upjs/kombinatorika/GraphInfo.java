package sk.upjs.kombinatorika;

import java.util.Set;
import java.util.Stack;
import sk.upjs.paz.Edge;
import sk.upjs.paz.Graph;
import sk.upjs.paz.Vertex;

/**
 *
 * @author student
 */
public class GraphInfo {

    private Graph graph;
    private Set<Edge> edges;
    private Set<Vertex> vertices;

    public GraphInfo(Graph graph) {
        this.graph = graph;
        this.edges = graph.getEdges();
        this.vertices = graph.getVertices();
    }

    /**
     * Returns a string containing basic information about the graph: whether it
     * is directed, weighted, connected, how many vertices and edges it has,
     * list of edges, minimum and maximum vertex degree, whether the graph is
     * regular and bipartite.
     *
     * @return string with basic information about the graph
     */
    public String basicGraphInfo() {
        vertices = graph.getVertices();

        boolean directed = directed();
        int minDegree = minDegree(directed);
        int maxDegree = maxDegree(directed);
        String basicInfo = "Orientovaný: " + booleanValueToString(directed)
                + "\n" + "Ohodnotený: " + weighted() + "\n" + "Súvislý: "
                + connected() + "\n" + "Počet vrcholov: " + vertices.size()
                + "\n" + "Počet hrán: " + edges.size() + "\n" + "Zoznam hrán: "
                + "\n" + listOfEdges() + "\n" + "Minimálny stupeň vrchola: "
                + minDegree + "\n" + "Maximálny stupeň vrchola: " + maxDegree
                + "\n" + "Regulárny: "
                + regular(directed, minDegree, maxDegree) + "\n"
                + "Bipartitný: " + isBipartite();

        return basicInfo;
    }

    /**
     * Tests if the undirected graph is connected. If the graph is directed, it
     * tests if the graph is strongly connected.
     *
     * @return "áno" if the graph is connected; "nie" otherwise
     */
    private String connected() {
        // tests if there is a directed path from every vertex to all the other vertices 
        for (Vertex start : vertices) {
            // visited - array with values saying if a vertex has been visited
            // during depth-first search
            boolean[] visited = new boolean[vertices.size()];
            for (int i = 0; i < visited.length; i++) {
                visited[i] = false;
            }

            Stack<Vertex> stack = new Stack<Vertex>();

            int startLabel = Integer.parseInt(start.getLabel());
            visited[startLabel - 1] = true;

            stack.push(start);

            // depth-first search
            while (!stack.isEmpty()) {
                Vertex v = stack.pop();

                for (Vertex neighbour : v.getOutNeighbours()) {
                    int neighbourLabel = Integer.parseInt(neighbour.getLabel());
                    if (visited[neighbourLabel - 1] == false) {
                        visited[neighbourLabel - 1] = true;
                        stack.push(neighbour);
                    }
                }
            }

            // if there is still an unvisited vertex, the graph is not connected
            for (int i = 0; i < visited.length; i++) {
                if (visited[i] == false) {
                    return "nie";
                }
            }
        }
        return "áno";
    }

    /**
     * Tests if the graph is weighted
     *
     * @return "áno" if the graph is weighted; "nie" otherwise
     */
    private String weighted() {
        for (Edge edge : edges) {
            if (edge.getWeight() > 1) {
                return "áno";
            }
        }
        return "nie";
    }

    /**
     * Tests if the graph is regular.
     *
     * @param directed whether the graph is directed
     * @param minDegree minimum degree of a vertex in the graph
     * @param maxDegree maximum degree of a vertex in the graph
     * @return áno" if the graph is regular; "nie" otherwise
     */
    private String regular(boolean directed, int minDegree, int maxDegree) {
        if (minDegree != maxDegree) {
            return "nie";
        }

        if (directed) {
            for (Vertex vertex : vertices) {
                if (vertex.getInNeighbours().size() != vertex
                        .getOutNeighbours().size()) {
                    return "nie";
                }
            }
            return "áno";

        } else {
            return "áno";
        }
    }

    /**
     * Returns minimum of all degrees of vertices
     *
     * @param directed whether the graph is directed
     * @return minimum degree of a vertex in the graph
     */
    private int minDegree(boolean directed) {
        if (vertices.isEmpty()) {
            return 0;
        }
        int minDegree = Integer.MAX_VALUE;
        for (Vertex vertex : vertices) {
            int vertexDegree = vertex.getEdges().size();
            if (minDegree > vertexDegree) {
                minDegree = vertexDegree;
            }
        }
        if (!directed) {
            minDegree = minDegree / 2;
        }
        return minDegree;
    }

    /**
     * Returns maximum of all degrees of vertices
     *
     * @param directed whether the graph is directed
     * @return maximum degree of a vertex in the graph
     */
    private int maxDegree(boolean directed) {
        if (vertices.isEmpty()) {
            return 0;
        }
        int maxDegree = 0;
        for (Vertex vertex : vertices) {
            int vertexDegree = vertex.getEdges().size();
            if (maxDegree < vertexDegree) {
                maxDegree = vertexDegree;
            }
        }

        if (!directed) {
            maxDegree = maxDegree / 2;
        }
        return maxDegree;
    }

    /**
     * Converts boolean value to string
     *
     * @param value a boolean value that will be converted
     * @return "áno" if the parameter value is true; "nie" otherwise
     */
    private String booleanValueToString(boolean value) {
        if (value) {
            return "áno";
        } else {
            return "nie";
        }
    }

    /**
     * Tests if the graph is directed. The graph is undirected if every edge
     * from Vertex v1 to Vertex v2 has its counterpart - the edge from v2 to v1
     * which has the same weight, otherwise the graph is directed.
     *
     * @return true if the graph is oriented; false otherwise
     */
    private boolean directed() {
        for (Vertex vertex : vertices) {
            Set<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                if (!(graph.hasEdge(vertex, neighbour)
                        && graph.hasEdge(neighbour, vertex) && graph.getEdge(
                                vertex, neighbour).getWeight() == graph.getEdge(
                                neighbour, vertex).getWeight())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns string of all edges. Every edge from Vertex v1 to Vertex v2 with
     * weight w is written out in this form: v1 -> v2: w. Edges are separated by
     * a comma.
     *
     * @return string with all edges and their weights
     */
    private String listOfEdges() {
        String list = "";
        for (Edge edge : edges) {
            list += edge.getSource().getLabel() + "->"
                    + edge.getTarget().getLabel() + ": " + edge.getWeight()
                    + ", ";
        }

        return list;
    }

    /**
     * Tests if the graph is bipartite.
     *
     * @return "áno" if the graph is bipartite; "nie" otherwise
     */
    public String isBipartite() {
        int[] colors = new int[vertices.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = -1;
        }
        Stack<Vertex> stack = new Stack<>();

        // group - marks one of the two groups of vertices, it can have value 0
        // or 1
        int group = 0;

        // every component is checked
        for (Vertex start : vertices) {
            int startLabel = Integer.parseInt(start.getLabel());

            if (colors[startLabel - 1] == -1) {
                colors[startLabel - 1] = group;

                stack.push(start);

                // depth-first search of the component
                while (!stack.isEmpty()) {
                    Vertex v = stack.pop();
                    int vLabel = Integer.parseInt(v.getLabel());
                    group = changeNumber(colors[vLabel - 1]);

                    for (Vertex neighbour : v.getNeighbours()) {
                        int vertexLabel = Integer
                                .parseInt(neighbour.getLabel());
                        if (colors[vertexLabel - 1] == -1) {
                            colors[vertexLabel - 1] = group;
                            stack.push(neighbour);
                            continue;
                        }
                        // the graph is not bipartite if there are two vertices from
                        // the
                        // same group that are connected with an edge
                        if (colors[vertexLabel - 1] != group) {
                            return "nie";
                        }
                    }
                }
            }
        }
        return "áno";
    }

    /**
     * If parameter n is 0, the method returns 1, otherwise it returns 0.
     *
     * @param n integer value
     * @return 1 if parameter n is 0; 0 otherwise
     */
    private int changeNumber(int n) {
        if (n == 0) {
            return 1;
        } else {
            return 0;
        }

    }
}
