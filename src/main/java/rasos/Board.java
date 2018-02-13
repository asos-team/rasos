package rasos;

import java.util.stream.Stream;

class Board {
    private final Cell[][] configuration;
    private final int dim;

    Board(int dim) {
        this.dim = dim;
        configuration = new Cell[dim][dim];
        for (int i = 0; i < configuration.length; i++) {
            for (int j = 0; j < configuration[i].length; j++) {
                configuration[i][j] = Cell.neutral();
            }
        }
    }

    void populateHomeBases(int numSoldiers) {
        if (numSoldiers == 0) {
            cellAt(1, 1).makeNeutral();
            cellAt(dim, dim).makeNeutral();
        } else {
            cellAt(1, 1).setValues(1, numSoldiers);
            cellAt(dim, dim).setValues(2, numSoldiers);
        }
    }

    /**
     * @deprecated Use {@link #cellAt(int, int)} with {@link Cell#setValues(int, int)}
     */
    @Deprecated
    void setCell(int col, int row, Cell cell) {
        if (cell == null)
            throw new RuntimeException("Cell cannot hold null value.");
        configuration[col - 1][row - 1] = cell;
    }

    Cell cellAt(int col, int row) {
        return configuration[col - 1][row - 1];
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : configuration) {
            for (Cell cell : row) {
                sb.append(cell.toString());
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }

    private Stream<Cell> getBoardCellStream() {
        return Stream.of(configuration)
                .flatMap(Stream::of);
    }
}
