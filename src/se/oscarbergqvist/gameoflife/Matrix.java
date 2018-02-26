package se.oscarbergqvist.gameoflife;

/**
 * Created by Oscar Bergqvist on 2018-02-25.
 */
class Matrix {

    private int rows;
    private int columns;
    private boolean[][] matrix;


    Matrix(int rows, int columns) {
            this.rows = rows + 2;
            this.columns = columns + 2;
            this. matrix = new boolean[this.rows][this.columns];
            for(int i = 0; i < rows + 2; i++){
                for(int j = 0; j < columns + 2; j++){
                    matrix[i][j] = false;
                }
            }
    }

    Matrix(Matrix m){
        this.rows = m.getRows() + 2;
        this.columns = m.getColumns() + 2;
        this.matrix = new boolean[this.rows][this.columns];
        for(int i = -1; i < this.rows - 1; i++){
            for(int j = -1; j < this.columns - 1; j++){
                this.matrix[i+1][j+1] = m.getValueAt(i, j) && indexWithinBounds(i, j);
            }
        }
    }


    void toggleCell(int i, int j) {
        if(i < 0 || j < 0|| i >= this.rows - 2 || j >= this.columns) {
            Controller.log(Controller.LogType.ERROR,
                    "Error: Array index out of bounds in Matrix.toggle(i, j)");
        }
        else
            this.matrix[i+1][j+1] = !matrix[i+1][j+1];

    }

    void changeCell(int i, int j, boolean cellState){
        if(i < 0 || j < 0 || i >= this.rows - 2 || j >= this.columns) {
            Controller.log(Controller.LogType.ERROR,
                    "Error: Array index out of bounds in Matrix.change(i, j)");
        }
        else
            this.matrix[i+1][j+1] = cellState;
    }

    boolean cellSurviving(int i, int j){
        //System.out.println("In cell surviving");
        //System.out.println(Boolean.toString(matrix[i+1][j+1]));
        if(indexWithinBounds(i, j)){
            int livingNeighbors = 0;
            for(int k = i-1; k <= i+1; k++){
                for(int l = j-1; l <= j+1; l++){
                    if((k != i || l != j) && matrix[k + 1][l + 1]){
                        livingNeighbors++;
                        //System.out.println("LivingNeighbous increasing for: " + Integer.toString(k) + " " + Integer.toString(l));
                    }
                }
            }

            //System.out.println(Integer.toString(i) + " " + Integer.toString(j) + " " + Integer.toString(livingNeighbors));
            if(livingNeighbors < 2 || livingNeighbors > 3)
                return false;
            else if(livingNeighbors == 3)
                return true;
            else{
                return matrix[i + 1][j + 1];
            }
        }
        else {
            Controller.log(Controller.LogType.ERROR, "Index out of bounds in Matrix.cellSurviving");
            return false;
        }
    }

    boolean getValueAt(int i, int j){
        if(indexWithinBounds(i, j))
            return matrix[i+1][j+1];
        else {
            Controller.log(Controller.LogType.ERROR, "Index out of bounds in Matrix.getValueAt(i,j)");
            return false;
        }

    }

    int getRows() {
        return rows - 2;
    }

    int getColumns() {
        return columns - 2;
    }

    private boolean indexWithinBounds(int i, int j){
        return i >= 0 && j >= 0 && i < this.rows - 2 && j < this.columns - 2;
    }

}
