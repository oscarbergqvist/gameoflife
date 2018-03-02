package se.oscarbergqvist.gameoflife;

/**
 * Created by Oscar Bergqvist on 2018-02-25.
 */
class Matrix {

    private int rows;
    private int columns;
    private boolean[][] matrix;


    Matrix(int columns, int rows) {
        this.rows = rows + 2;
        this.columns = columns + 2;
        this.matrix = new boolean[this.columns][this.rows];
        for (int i = 0; i < columns + 2; i++) {
            for (int j = 0; j < rows + 2; j++) {
                matrix[i][j] = false;
            }
        }
    }

    Matrix(Matrix m) {
        this.rows = m.getRows() + 2;
        this.columns = m.getColumns() + 2;
        this.matrix = new boolean[this.columns][this.rows];
        for (int i = -1; i < this.rows - 1; i++) {
            for (int j = -1; j < this.columns - 1; j++) {
                this.matrix[i + 1][j + 1] =  indexWithinBounds(i, j) && m.getValueAt(i, j);
            }
        }
    }


    void toggleCell(int i, int j) {
        if (i < 0 || j < 0 || i >= this.columns - 2 || j >= this.rows) {
            Controller.log(Controller.LogType.ERROR,
                    "Error: Array index out of bounds in Matrix.toggle(i, j)");
        } else {
            this.matrix[i + 1][j + 1] = !matrix[i + 1][j + 1];
        }

    }

    void changeCell(int i, int j, boolean cellState) {
        if (i < 0 || j < 0 || i >= this.columns - 2 || j >= this.columns) {
            Controller.log(Controller.LogType.ERROR,
                    "Error: Array index out of bounds in Matrix.change(i, j)");
        } else {
            this.matrix[i + 1][j + 1] = cellState;
        }
    }

    boolean cellSurviving(int i, int j) {
        //System.out.println("In cell surviving");
        //System.out.println(Boolean.toString(matrix[i+1][j+1]));
        if (indexWithinBounds(i, j)) {
            int livingNeighbors = 0;
            for (int k = i - 1; k <= i + 1; k++) {
                for (int l = j - 1; l <= j + 1; l++) {
                    if ((k != i || l != j) && matrix[k + 1][l + 1]) {
                        livingNeighbors++;
                        //System.out.println("LivingNeighbous increasing for: " + Integer.toString(k) + " " + Integer.toString(l));
                    }
                }
            }

            //System.out.println(Integer.toString(i) + " " + Integer.toString(j) + " " + Integer.toString(livingNeighbors));
            if (livingNeighbors == 3)
                return true;
            else if (livingNeighbors == 2 && matrix[i+1][j+1])
                return true;
            else {
                return false;
            }
        } else {
            Controller.log(Controller.LogType.ERROR, "Index out of bounds in Matrix.cellSurviving");
            return false;
        }
    }

    boolean getValueAt(int i, int j) {
        try
        {
            if (indexWithinBounds(i, j))
                return matrix[i + 1][j + 1];
            else {
                throw new Exception();
            }
        } catch (Exception e)
        {
            String message = "Index out of bounds in Matrix.getValueAt("
                    + Integer.toString(i) + ", " + Integer.toString(j) + ")";
            Controller.log(Controller.LogType.ERROR, message);
            return false;
        }
    }

    int getRows() {
        return rows - 2;
    }

    int getColumns() {
        return columns - 2;
    }

    private boolean indexWithinBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < this.columns - 2 && j < this.rows - 2;
    }

}
