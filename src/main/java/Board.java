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
}
