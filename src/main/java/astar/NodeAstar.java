package astar;

public class NodeAstar implements Comparable<NodeAstar> {

    // distance from starting node
    private int g;

    // distance to end node(heuristic)
    private int h;

    // f = g + h
    private int f;

    private final int xPosition;
    private final int yPosition;

    private NodeAstar parent;

    private final boolean obstacle;

    public NodeAstar(int xPosition, int yPosition, boolean obstacle) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.obstacle = obstacle;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public int getF() {
        return f;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getxPosition() {
        return xPosition;
    }

    public NodeAstar getParent() {
        return parent;
    }

    public void setParent(NodeAstar parent) {
        this.parent = parent;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + f;
        result = prime * result + g;
        result = prime * result + h;
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
        if (!(obj instanceof NodeAstar)) {
            return false;
        }
        final NodeAstar other = (NodeAstar) obj;
        if (f != other.f) {
            return false;
        }
        if (g != other.g) {
            return false;
        }
        if (h != other.h) {
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
    public int compareTo(NodeAstar o) {
        final int value = Integer.compare(this.getF(), o.getF());
        if (value == 0) {
            return Integer.compare(this.getH(), o.getH());
        }
        return value;
    }

}
