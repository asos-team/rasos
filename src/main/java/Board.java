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
        setCell(0, 0, new Cell(1, numSoldiers));
        setCell(dim - 1, dim - 1, new Cell(2, numSoldiers));
    }

    public void setCell(int col, int row, Cell cell) {
        if (cell == null)
            throw new RuntimeException("Cell cannot hold null value.");
        configuration[col][row] = cell;
    }

    public Cell getCell(int col, int row) {
        return configuration[col][row];
    }

    public int getDim() {
        return dim;
    }

    public int getPlayerCellCount(int playerId) {
        int playerCellCount = 0;
        for (Cell[] column : configuration) {
            for (Cell cell : column) {
                if (cell.getControllingPlayerId() == playerId) {
                    playerCellCount++;
                }
            }
        }
        return playerCellCount;
    }

    public Cell getHome1Cell() {
        return getCell(0, 0);
    }

    public Cell getHome2Cell() {
        return getCell(dim - 1, dim - 1);
    }
}
