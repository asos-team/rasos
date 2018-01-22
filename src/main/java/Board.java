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

    public void validateBoardInitialized() {
        for (Cell[] column : configuration) {
            for (Cell cell : column) {
                if (cell == null) {
                    throw new RuntimeException("Board isn't fully initialized!");
                }
            }
        }
    }
}
