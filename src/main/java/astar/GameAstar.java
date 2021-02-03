package astar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

public class GameAstar {
    // 10 = 10*1
    private static int DISTANCE_DIRECT = 10;
    // 14 = 10*sqrt(2)
    private static int DISTANCE_DIAGONAL = 14;

    private final NodeAstar startNode;
    private final NodeAstar endNode;
    private final NodeAstar[][] matrix;
    private final int verticalDimension;
    private final int horizontalDimension;

    private int stepCounter;

    public GameAstar(Pair<Integer, Integer> matrixSize, Pair<Integer, Integer> start, Pair<Integer, Integer> end,
            List<Pair<Integer, Integer>> obsticles) {

        horizontalDimension = matrixSize.getLeft();
        verticalDimension = matrixSize.getRight();
        matrix = new NodeAstar[verticalDimension][horizontalDimension];

        for (int x = 0; x < horizontalDimension; x++) {
            for (int y = 0; y < verticalDimension; y++) {
                matrix[x][y] = new NodeAstar(x, y, false);
            }
        }

        startNode = new NodeAstar(start.getLeft(), start.getRight(), false);
        endNode = new NodeAstar(end.getLeft(), end.getRight(), false);
        matrix[10][8] = startNode;
        matrix[8][2] = endNode;

        for (final Pair<Integer, Integer> obsticle : obsticles) {
            matrix[obsticle.getLeft()][obsticle.getRight()] = new NodeAstar(obsticle.getLeft(), obsticle.getRight(), true);
        }
    }

    public void compute() {

        final List<NodeAstar> open = new ArrayList<>();
        final List<NodeAstar> closed = new ArrayList<>();

        open.add(startNode);

        while (true) {

            final NodeAstar currentNode;
            if (open.size() == 1) {
                currentNode = open.get(0);
            } else {
                currentNode = open.stream().min(NodeAstar::compareTo).get();
            }
            open.remove(currentNode);
            closed.add(currentNode);

            stepCounter++;

            if (currentNode.equals(endNode)) {
                System.out.println("Visited nodes total: " + stepCounter);
                calculatePath(startNode, endNode);
                return;
            }

            final List<NodeAstar> listOfNeighbours = findNeighbours(currentNode);

            for (final NodeAstar neighbour : listOfNeighbours) {
                if (!neighbour.isObstacle() && !closed.contains(neighbour)) {
                    final int newGOfNeighbour = currentNode.getG() + getDistanceBetweenNodes(currentNode, neighbour);
                    if (newGOfNeighbour < neighbour.getG() || !open.contains(neighbour)) {
                        final int hOfNeighbour = getDistanceBetweenNodes(neighbour, endNode);
                        neighbour.setG(newGOfNeighbour);
                        neighbour.setH(hOfNeighbour);
                        neighbour.setF(newGOfNeighbour + hOfNeighbour);
                        neighbour.setParent(currentNode);
                        if (!open.contains(neighbour)) {
                            open.add(neighbour);
                        }
                    }
                }
            }
        }
    }

    private List<NodeAstar> findNeighbours(NodeAstar node) {
        final List<NodeAstar> neighbours = new ArrayList<NodeAstar>();
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

    private int getDistanceBetweenNodes(NodeAstar a, NodeAstar b) {
        final int distanceHorizontal = Math.abs(a.getxPosition() - b.getxPosition());
        final int distanceVertical = Math.abs(a.getyPosition() - b.getyPosition());
        if (distanceHorizontal > distanceVertical) {
            return DISTANCE_DIAGONAL * distanceVertical + DISTANCE_DIRECT * (distanceHorizontal - distanceVertical);
        }
        return DISTANCE_DIAGONAL * distanceHorizontal + DISTANCE_DIRECT * (distanceVertical - distanceHorizontal);
    }

    private void calculatePath(NodeAstar start, NodeAstar end) {
        final List<NodeAstar> path = new ArrayList<>();
        NodeAstar current = end;

        while (!current.equals(start)) {
            path.add(current);
            current = current.getParent();
        }
        final NodeAstar[] arrayx = path.toArray(new NodeAstar[0]);
        CollectionUtils.reverseArray(arrayx);
        System.out.println(Arrays.toString(arrayx));
    }

    public static void main(String[] args) {

        final Pair<Integer, Integer> matrixDim = Pair.of(20, 20);
        final Pair<Integer, Integer> startPos = Pair.of(10, 8);
        final Pair<Integer, Integer> endPos = Pair.of(8, 2);

        final List<Pair<Integer, Integer>> obsticlePos = List.of(Pair.of(7, 5), Pair.of(8, 5), Pair.of(9, 5), Pair.of(10, 5),
                Pair.of(11, 5), Pair.of(12, 5));

        final GameAstar game = new GameAstar(matrixDim, startPos, endPos, obsticlePos);
        game.compute();
    }
}
