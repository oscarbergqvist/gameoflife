/*
 * Author : Oscar Bergqvist
 * Date: 2018-02-24
 */

package se.oscarbergqvist.gameoflife;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * Controller creates View and Matrix objects and updates their states.
 *
 * @author Oscar Bergqvist
 * @version 1.0
 */
public class Controller {

    /* Private fields */
    private Matrix currentState;
    private Matrix previousState;
    private int step;
    private View view;

    /**
     * Start point of the program
     *
     * @param args call arguments
     */
    public static void main(String[] args) {
        new Controller();
    }

    /**
     * Constructor
     */
    private Controller() {
        step = 1;
        currentState = new Matrix(30, 30);
        view = new View(this, currentState);
        log(LogType.LOG, "Controller created");
    }

    /**
     * Toggles cell at i, j
     *
     * @param i column
     * @param j row
     */
    void toggle(int i, int j) {
        currentState.toggle(i, j);
        view.updateWorld(currentState);
    }

    /**
     * Updates the state according to life rules
     *
     * @param step number of generations to skip
     */
    void update(int step) {
        log(LogType.LOG, "in Controller.update(int step)");
        for (int k = 0; k < step; k++) {
            previousState = new Matrix(currentState);
            for (int i = 0; i < currentState.getRows(); i++) {
                for (int j = 0; j < currentState.getColumns(); j++) {
                    currentState.change(i, j, previousState.cellSurviving(i, j));
                }
            }
        }
        view.updateWorld(currentState);
    }

    /**
     * Sets current state to a new Matrix object with i columns and j rows
     *
     * @param i number of columns
     * @param j number of rows
     */
    void newInstance(int i, int j) {
        currentState = new Matrix(i, j);
        view.updateWorld(currentState);
    }

    /**
     * Sets current state from a .txt file,
     *
     * @param file file to get new state from
     */
    void readFile(File file) {
        log(LogType.LOG, "reading file");
        ArrayList<Integer> indexList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file.getPath()))) {
            String line = reader.readLine();
            Pattern p = Pattern.compile("[0-9]+ +[0-9]+$");

            while (!p.matcher(line).matches()) {
                line = reader.readLine();
            }

            do {
                String[] splitLine = line.split(" +");
                indexList.add(Integer.parseInt(splitLine[0]));
                indexList.add(Integer.parseInt(splitLine[1]));
            } while ((line = reader.readLine()) != null);

        } catch (IOException e) {
            log(LogType.ERROR, e.getMessage());
        } finally {
            log(LogType.LOG, "file read successfully");
        }

        int maxIndex = Collections.max(indexList);
        currentState = new Matrix(maxIndex + 9, maxIndex + 9);
        for (int i = 0; i < indexList.size(); i += 2) {
            currentState.change(indexList.get(i) + 4, indexList.get(i + 1) + 4, true);
        }

        view.updateWorld(currentState);
    }

    /**
     * Sets the number of generations to skip in each update
     *
     * @param step number of generations to skip in each update
     */
    void setStep(int step) {
        this.step = step;
    }

    /**
     * Gets the number of generations to skip in each update
     *
     * @return int step
     */
    int getStep() {
        return step;
    }

    /**
     * Log type enum
     */
    public enum LogType {
        ERROR, INFO, LOG
    }

    /**
     * Log function
     *
     * @param LOG_TYPE Can be ERROR, INFO or LOG
     * @param message  Log message
     */
    static void log(LogType LOG_TYPE, String message) {
        System.out.println(LOG_TYPE + ": " + message);
    }
}
