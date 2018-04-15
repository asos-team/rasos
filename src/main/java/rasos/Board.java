package rasos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Board {
    @JsonProperty
    private Cell[][] configuration;
    @JsonProperty
    private int dim;

    @SuppressWarnings("unused") // necessary for Jackson
    private Board() {
    }

    Board(int dim) {
        this.dim = dim;
        configuration = new Cell[dim][dim];
        for (int i = 0; i < configuration.length; i++) {
            for (int j = 0; j < configuration[i].length; j++) {
                configuration[i][j] = Cell.neutral();
            }
        }
    }

    public Iterable<CellCoordinates> getControlledCoordinates(int playerId) {
        List<CellCoordinates> res = new LinkedList<>();
        for (int colIdx = 0; colIdx < dim; colIdx++) {
            for (int rowIdx = 0; rowIdx < dim; rowIdx++) {
                if (configuration[colIdx][rowIdx].isControlledBy(playerId)) {
                    res.add(new CellCoordinates(colIdx + 1, rowIdx + 1));
                }
            }
        }
        return res;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return getBoardCellStream().allMatch(Cell::isNeutral);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rowIdx = 0; rowIdx < dim; rowIdx++) {
            for (int colIdx = 0; colIdx < dim; colIdx++) {
                sb.append(configuration[colIdx][rowIdx].toString()).append(" ");
            }
            sb.append(System.lineSeparator());

        }
        return sb.toString().trim();
    }

    public Cell cellAt(int col, int row) {
        return configuration[col - 1][row - 1];
    }

    void populateHomeBases(int numSoldiers, int idA, int idB) {
        if (numSoldiers == 0) {
            getHome1Cell().makeNeutral();
            getHome2Cell().makeNeutral();
        } else {
            getHome1Cell().setValues(idA, numSoldiers);
            getHome2Cell().setValues(idB, numSoldiers);
        }
    }

    int getDim() {
        return dim;
    }

    int getPlayerCellCount(int playerId) {
        return (int) getBoardCellStream()
                .filter(cell -> cell.isControlledBy(playerId))
                .count();
    }

    Cell getHome1Cell() {
        return cellAt(1, 1);
    }

    Cell getHome2Cell() {
        return cellAt(dim, dim);
    }

    private Stream<Cell> getBoardCellStream() {
        return Stream.of(configuration)
                .flatMap(Stream::of);
    }
}
