import java.util.Arrays;
import java.util.stream.Stream;

class Board {
    private final Cell[][] configuration;
    private final int dim;

    public Board(int dim) {
        this.dim = dim;
        configuration = new Cell[dim][dim];
        for (int i = 0; i < configuration.length; i++) {
            for (int j = 0; j < configuration[i].length; j++) {
                configuration[i][j] = new Cell();
            }
        }
    }

    public void populateHomeBases(int numSoldiers) {
        setCell(1, 1, new Cell(1, numSoldiers));
        setCell(dim, dim, new Cell(2, numSoldiers));
    }

    public void setCell(int col, int row, Cell cell) {
        if (cell == null)
            throw new RuntimeException("Cell cannot hold null value.");
        configuration[col - 1][row - 1] = cell;
    }

    public Cell getCell(int col, int row) {
        return configuration[col - 1][row - 1];
    }

    public int getDim() {
        return dim;
    }

    public int getPlayerCellCount(int playerId) {
        return (int) getBoardCellStream()
                .filter(cell -> cell.isControlledBy(playerId))
                .count();
    }

    public Cell getHome1Cell() {
        return getCell(1, 1);
    }

    public Cell getHome2Cell() {
        return getCell(dim, dim);
    }

    private Stream<Cell> getBoardCellStream() {
        return Arrays.stream(configuration)
                .flatMap(Arrays::stream);
    }
}
