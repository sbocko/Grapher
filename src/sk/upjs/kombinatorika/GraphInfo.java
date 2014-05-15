package sk.upjs.kombinatorika;

import java.util.HashMap;
import java.util.Map;
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
        int minDegree = minDegree();
        int maxDegree = maxDegree();
        String basicInfo = "Orientovaný: " + booleanValueToString(graph.isDirected())
                + "\n" + "Ohodnotený: " + weighted() + "\n" + "Súvislý: "
                + connected() + "\n" + "Počet vrcholov: " + vertices.size()
                + "\n" + "Počet hrán: " + edges.size() + "\n" + "Zoznam hrán: "
                + "\n" + listOfEdges() + "\n" + "Minimálny stupeň vrchola: "
                + minDegree + "\n" + "Maximálny stupeň vrchola: " + maxDegree
                + "\n" + "Regulárny: "
                + regular(minDegree, maxDegree) + "\n"
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
            // visited - map with values saying if a vertex has been visited
            // during depth-first search
            Map<String, Boolean> visited = new HashMap<>();
            for (Vertex v : vertices) {
                visited.put(v.getLabel(), false);
            }

            Stack<Vertex> stack = new Stack<>();

            String startLabel = start.getLabel();
            visited.put(startLabel, true);

            stack.push(start);

            // depth-first search
            while (!stack.isEmpty()) {
                Vertex v = stack.pop();

                for (Vertex neighbour : v.getOutNeighbours()) {
                    String neighbourLabel = neighbour.getLabel();
                    if (visited.get(neighbourLabel) == false) {
                        visited.put(neighbourLabel, true);
                        stack.push(neighbour);
                    }
                }
            }

            // if there is still an unvisited vertex, the graph is not connected
            for (Boolean visitedVertex : visited.values()) {
                if (!visitedVertex) {
                    return "nie";
                }
            }
        }
        return "áno";
    }

    /**
     * Tests if the graph is weighted.
     *
     * @return "áno" if the graph is weighted; "nie" otherwise
     */
    private String weighted() {
        for (Edge edge : edges) {
            if (edge.getWeight() != 1) {
                return "áno";
            }
        }
        return "nie";
    }

    /**
     * Tests if the graph is regular.
     *
     * @param minDegree minimum degree of a vertex in the graph
     * @param maxDegree maximum degree of a vertex in the graph
     * @return áno" if the graph is regular; "nie" otherwise
     */
    private String regular(int minDegree, int maxDegree) {
        if (minDegree != maxDegree) {
            return "nie";
        }

        if (graph.isDirected()) {
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
     * @return minimum degree of a vertex in the graph
     */
    private int minDegree() {
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

        return minDegree;
    }

    /**
     * Returns maximum of all degrees of vertices.
     *
     * @return maximum degree of a vertex in the graph
     */
    private int maxDegree() {
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
     * Returns string of all edges. Every edge from Vertex v1 to Vertex v2 with
     * weight w is written out in this form: v1->v2: w, if the graph is
     * directed; v1-v2: w, otherwise. Edges are separated by a comma.
     *
     * @return string with all edges and their weights
     */
    private String listOfEdges() {
        String list = "";
        String separator;
        if (graph.isDirected()) {
            separator = "->";
        } else {
            separator = "-";
        }
        for (Edge edge : edges) {
            list += edge.getSource().getLabel() + separator
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
        Map<String, Integer> colors = new HashMap<>();
        for (Vertex vertex : vertices) {
            colors.put(vertex.getLabel(), -1);
        }
        Stack<Vertex> stack = new Stack<>();

        // group - marks one of the two groups of vertices, it can have value 0
        // or 1
        int group = 0;

        // every component is checked
        for (Vertex start : vertices) {
            String startLabel = start.getLabel();

            if (colors.get(startLabel) == -1) {
                colors.put(startLabel, group);

                stack.push(start);

                // depth-first search of the component
                while (!stack.isEmpty()) {
                    Vertex v = stack.pop();
                    String vLabel = v.getLabel();
                    group = changeNumber(colors.get(vLabel));

                    for (Vertex neighbour : v.getNeighbours()) {
                        String neighbourLabel = neighbour.getLabel();
                        if (colors.get(neighbourLabel) == -1) {
                            colors.put(neighbourLabel, group);
                            stack.push(neighbour);
                            continue;
                        }
                        // the graph is not bipartite if there are two vertices from
                        // the
                        // same group that are connected with an edge
                        if (colors.get(neighbourLabel) != group) {
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
