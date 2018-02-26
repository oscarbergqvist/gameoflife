package se.oscarbergqvist.gameoflife;

import java.io.File;

/**
 * Created by Oscar Bergqvist on 2018-02-24.
 */
public class Controller {

    /* State matrices */
    private Matrix currentState;
    private Matrix previousState;

    /**/
    private int step;
    private boolean autoUpdate;

    /* UI */
    private View view;


    public static void main(String[] args){
        new Controller();
    }

    private Controller() {
        currentState = new Matrix(100,100);
        currentState.toggleCell(1,0);
        currentState.toggleCell(1,1);
        currentState.toggleCell(1,2);

        this.view = new View(this, currentState);
    }

    void change(int i, int j){
        currentState.toggleCell(i, j);
        view.updateWorld(currentState);
    }

    void update(int step) {
        previousState = new Matrix(currentState);
        for(int i = 0; i < currentState.getRows(); i++){
            for(int j = 0; j < currentState.getColumns(); j++){
                //System.out.println("In update: " + Integer.toString(i) + " " + Integer.toString(j));
                currentState.changeCell(i, j, previousState.cellSurviving(i, j));

            }
        }

        view.updateWorld(currentState);

    }

    void autoUpdate(){

    }

    /* Log type enum */
    public enum LogType {
        ERROR, WARNING ,INFO
    }

    static void log(LogType LOG_TYPE, String message){

    }

    void readStateFile(File file) {

    }

    void setStep(int step) {
        this.step = step;
    }

    void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    int getStep() {
        return step;
    }
}