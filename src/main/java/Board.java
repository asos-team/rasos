class Board {
    private final Cell[][] configuration;

    Board(Cell[][] configuration) {
        this.configuration = configuration;
    }

    public Cell[][] getConfiguration() {
        return configuration;
    }

    public void setCell(int col, int row, Cell cell) {
        configuration[col][row] = cell;
    }

    public Cell getCell(int col, int row) {
        return configuration[col][row];
    }

    int getPlayerCellCount(int playerId) {
        int playerCellCount = 0;
        for (Cell[] column : configuration) {
            for (Cell cell : column) {
                if (cell.getControllingPlayer() == playerId) {
                    playerCellCount++;
                }
            }
        }
        return playerCellCount;
    }
}
