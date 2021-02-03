package djikstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

public class GameDjikstra {

    private final NodeDijkstra startNode;
    private final NodeDijkstra endNode;
    private final NodeDijkstra[][] matrix;
    private final int horizontalDimension;
    private final int verticalDimension;

    private int stepCounter;

    public GameDjikstra(Pair<Integer, Integer> matrixSize, Pair<Integer, Integer> start, Pair<Integer, Integer> end,
            List<Pair<Integer, Integer>> obsticles) {

        horizontalDimension = matrixSize.getLeft();
        verticalDimension = matrixSize.getRight();
        matrix = new NodeDijkstra[verticalDimension][horizontalDimension];

        for (int x = 0; x < horizontalDimension; x++) {
            for (int y = 0; y < verticalDimension; y++) {
                matrix[x][y] = new NodeDijkstra(x, y, false);
            }
        }

        startNode = new NodeDijkstra(start.getLeft(), start.getRight(), false);
        endNode = new NodeDijkstra(end.getLeft(), end.getRight(), false);
        matrix[10][8] = startNode;
        matrix[8][2] = endNode;

        for (final Pair<Integer, Integer> obsticle : obsticles) {
            matrix[obsticle.getLeft()][obsticle.getRight()] = new NodeDijkstra(obsticle.getLeft(), obsticle.getRight(), true);
        }
    }

    public GameDjikstra(NodeDijkstra start, NodeDijkstra end, NodeDijkstra[][] matrix) {
        this.startNode = start;
        this.endNode = end;
        this.matrix = matrix;
        this.horizontalDimension = matrix[0].length;
        this.verticalDimension = matrix.length;
    }

    private static final int DISTANCE_DIAGONAL = 14;
    private static final int DISTANCE_DIRECT = 10;

    public void computePath() {

        final PriorityQueue<NodeDijkstra> openPriorityQueue = new PriorityQueue<>();
        openPriorityQueue.add(startNode);

        final List<NodeDijkstra> closed = new ArrayList<>();

        while (true) {

            final NodeDijkstra currentNode = openPriorityQueue.poll();

            if (closed.contains(currentNode)) {
                continue;
            }

            stepCounter++;

            if (currentNode.equals(endNode)) {
                System.out.println("Visited nodes total: " + stepCounter);
                calculatePath(startNode, endNode);
                return;
            }

            closed.add(currentNode);

            final List<NodeDijkstra> listOfNeighbours = findNeighbours(currentNode);
            for (final NodeDijkstra neighbour : listOfNeighbours) {
                if (!neighbour.isObstacle()) {
                    final int newGOfNeighbour = currentNode.getG() + getDistanceBetweenNodes(currentNode, neighbour);
                    if (newGOfNeighbour < neighbour.getG()) {
                        neighbour.setG(newGOfNeighbour);
                        neighbour.setParent(currentNode);
                        openPriorityQueue.add(neighbour);
                    }
                }
            }
        }
    }
    private List<NodeDijkstra> findNeighbours(NodeDijkstra node) {
        final List<NodeDijkstra> neighbours = new ArrayList<NodeDijkstra>();
        final int horizontal = node.getxPosition();
        final int vertical = node.getyPosition();
        for (int colNum = vertical - 1; colNum <= (vertical + 1); colNum += 1) {

            for (int rowNum = horizontal - 1; rowNum <= (horizontal + 1); rowNum += 1) {

                // if not the center cell
                if (!((colNum == vertical) && (rowNum == horizontal))) {

                    // make sure it is within grid
                    if (neighbourInBound(rowNum, colNum)) {
                        neighbours.add(matrix[rowNum][colNum]);
                    }
                }
            }
        }
        return neighbours;

    }
    private boolean neighbourInBound(int x, int y) {
        return y > 0 && y < horizontalDimension && x > 0 && x < verticalDimension;

    }

    private void calculatePath(NodeDijkstra start, NodeDijkstra end) {
        final List<NodeDijkstra> path = new ArrayList<>();
        NodeDijkstra current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = current.getParent();
        }
        final NodeDijkstra[] arrayx = path.toArray(new NodeDijkstra[0]);
        CollectionUtils.reverseArray(arrayx);
        System.out.println(Arrays.toString(arrayx));
    }

    private int getDistanceBetweenNodes(NodeDijkstra a, NodeDijkstra b) {
        final int distanceHorizontal = Math.abs(a.getxPosition() - b.getxPosition());
        final int distanceVertical = Math.abs(a.getyPosition() - b.getyPosition());
        if (distanceHorizontal > distanceVertical) {
            return DISTANCE_DIAGONAL * distanceVertical + DISTANCE_DIRECT * (distanceHorizontal - distanceVertical);
        }
        return DISTANCE_DIAGONAL * distanceHorizontal + DISTANCE_DIRECT * (distanceVertical - distanceHorizontal);
    }

    public static void main(String[] args) {

        final int horizontalDim = 20;
        final int verticalDim = 20;
        final NodeDijkstra[][] matrix = new NodeDijkstra[verticalDim][horizontalDim];

        for (int x = 0; x < horizontalDim; x++) {
            for (int y = 0; y < verticalDim; y++) {
                matrix[x][y] = new NodeDijkstra(x, y, false);
                matrix[x][y].setG(Integer.MAX_VALUE);
            }
        }

        final NodeDijkstra start = new NodeDijkstra(10, 8, false);
        final NodeDijkstra target = new NodeDijkstra(8, 2, false);
        target.setG(Integer.MAX_VALUE);
        matrix[10][8] = start;
        matrix[8][2] = target;

        matrix[7][5] = new NodeDijkstra(7, 5, true);
        matrix[8][5] = new NodeDijkstra(8, 5, true);
        matrix[9][5] = new NodeDijkstra(9, 5, true);
        matrix[10][5] = new NodeDijkstra(10, 5, true);
        matrix[11][5] = new NodeDijkstra(11, 5, true);
        matrix[12][5] = new NodeDijkstra(12, 5, true);
        final GameDjikstra game = new GameDjikstra(start, target, matrix);
        game.computePath();

        // new constructor
        final Pair<Integer, Integer> matrixDim = Pair.of(20, 20);
        final Pair<Integer, Integer> startPos = Pair.of(10, 8);
        final Pair<Integer, Integer> endPos = Pair.of(8, 2);

        final List<Pair<Integer, Integer>> obsticlePos = List.of(Pair.of(7, 5), Pair.of(8, 5), Pair.of(9, 5), Pair.of(10, 5),
                Pair.of(11, 5), Pair.of(12, 5));

        final GameDjikstra game2 = new GameDjikstra(matrixDim, startPos, endPos, obsticlePos);
        game2.computePath();

    }
}
