class Board {
    private final Cell[][] configuration;

    Board(Cell[][] configuration) {
        this.configuration = configuration;
    }

    static Cell[][] getDefaultBoard(int width, int height) {
        Cell[][] board = new Cell[width][height];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell(0, 0);
            }
        }
        board[0][0] = new Cell(1, 20);
        board[width - 1][height - 1] = new Cell(2, 20);
        return board;
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

    void validateBoardInitialized() {
        for (Cell[] column : configuration) {
            for (Cell cell : column) {
                if (cell == null) {
                    throw new RuntimeException("Board isn't fully initialized!");
                }
            }
        }
    }
}
