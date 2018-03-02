/*
 * Author : Oscar Bergqvist
 * Date: 2018-02-25
 */

package se.oscarbergqvist.gameoflife;

/**
 * Matrix stores an array with boolean values: true - element contains cell, false - element contains no cell.
 */
class Matrix {

    private final int rows;
    private final int columns;
    private boolean[][] matrix;

    /**
     * Constructor
     *
     * Creates a Matrix with no initial cells
     *
     * @param columns number of columns
     * @param rows number of rows
     */
    Matrix(int columns, int rows) {
        this.rows = rows + 2;
        this.columns = columns + 2;
        this.matrix = new boolean[this.columns][this.rows];

        // Set all elements in matrix to false
        for (int i = 0; i < columns + 2; i++) {
            for (int j = 0; j < rows + 2; j++)
                matrix[i][j] = false;
        }
    }

    /**
     * Creates a new matrix as a clone of another one.
     *
     * @param m matrix to clone state from
     */
    Matrix(Matrix m) {
        this.rows = m.getRows() + 2;
        this.columns = m.getColumns() + 2;
        this.matrix = new boolean[this.columns][this.rows];

        // Copy all element values of m to matrix and set border elements to false
        for (int i = -1; i < this.rows - 1; i++) {
            for (int j = -1; j < this.columns - 1; j++)
                this.matrix[i + 1][j + 1] = indexWithinBounds(i, j) && m.getValueAt(i, j);
        }
    }

    /**
     * Determines if a given index is within range constrained by the number of rows and columns.
     * @param i column
     * @param j row
     * @return boolean
     */
    private boolean indexWithinBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < this.columns - 2 && j < this.rows - 2;
    }

    /**
     * Toggles state of element at (i, j)
     * @param i column
     * @param j row
     */
    void toggle(int i, int j) {
        if (i >= 0 && j >= 0 && i < this.columns - 2 && j < this.rows)
            this.matrix[i + 1][j + 1] = !matrix[i + 1][j + 1];
        else
            Controller.log(Controller.LogType.ERROR, "Error: Array index out of bounds in Matrix.toggle(i, j)");
        Controller.log(Controller.LogType.LOG, "cell (" + Integer.toString(i) + ", " + Integer.toString(j) + ") " +
                "toggled");
    }

    /**
     * Changes the state of element at (i, j) to either true or false.
     * @param i column
     * @param j row
     * @param state desired state of element at column i and row j
     */
    void change(int i, int j, boolean state) {
        if (i >= 0 && j >= 0 && i < this.columns - 2 && j < this.columns)
            this.matrix[i + 1][j + 1] = state;
        else
            Controller.log(Controller.LogType.ERROR, "Error: Array index out of bounds in Matrix.change(i, j)");
    }

    /**
     * Determines if a cell with be at element (i, j) in the next generation, according to life rules.
     * @param i column
     * @param j row
     * @return boolean
     */
    boolean cellSurviving(int i, int j) {
        if (indexWithinBounds(i, j)) {
            int livingNeighbors = 0;
            for (int k = i - 1; k <= i + 1; k++) {
                for (int l = j - 1; l <= j + 1; l++) {
                    if ((k != i || l != j) && matrix[k + 1][l + 1])
                        livingNeighbors++;
                }
            }

            return livingNeighbors == 3 || (livingNeighbors == 2 && matrix[i + 1][j + 1]);
        } else {
            Controller.log(Controller.LogType.ERROR, "Index out of bounds in Matrix.cellSurviving(int i, int j)");
            return false;
        }
    }

    /**
     * Retruns true if cell exists at (i, j) otherwise false.
     * @param i column
     * @param j row
     * @return boolean
     */
    boolean getValueAt(int i, int j) {
        try {
            if (indexWithinBounds(i, j))
                return matrix[i + 1][j + 1];
            else
                throw new Exception();
        } catch (Exception e) {
            String message = "Index out of bounds in Matrix.getValueAt(" + Integer.toString(i) + ", " + Integer
                    .toString(j) + ")";
            Controller.log(Controller.LogType.ERROR, message);
            return false;
        }
    }

    /**
     * Returns number of rows.
     * @return int rows
     */
    int getRows() {
        return rows - 2;
    }

    /**
     * Returns number of columns.
     * @return int columns
     */
    int getColumns() {
        return columns - 2;
    }

}
