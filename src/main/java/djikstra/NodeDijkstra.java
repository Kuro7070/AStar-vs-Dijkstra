package djikstra;

public class NodeDijkstra implements Comparable<NodeDijkstra> {

    // distance from starting node
    private int g;

    private final int xPosition;
    private final int yPosition;

    private NodeDijkstra parent;

    private final boolean obstacle;

    public NodeDijkstra(int xPosition, int yPosition, boolean obstacle) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.obstacle = obstacle;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getxPosition() {
        return xPosition;
    }

    public NodeDijkstra getParent() {
        return parent;
    }

    public void setParent(NodeDijkstra parent) {
        this.parent = parent;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + g;
        result = prime * result + (obstacle ? 1231 : 1237);
        result = prime * result + xPosition;
        result = prime * result + yPosition;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NodeDijkstra)) {
            return false;
        }
        final NodeDijkstra other = (NodeDijkstra) obj;
        if (g != other.g) {
            return false;
        }
        if (obstacle != other.obstacle) {
            return false;
        }
        if (xPosition != other.xPosition) {
            return false;
        }
        if (yPosition != other.yPosition) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Node [" + xPosition + ", " + yPosition + "]";
    }

    @Override
    public int compareTo(NodeDijkstra o) {
        return Integer.compare(this.getG(), o.getG());
    }

}
