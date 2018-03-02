package se.oscarbergqvist.gameoflife;

import sun.text.normalizer.UTF16;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Oscar Bergqvist on 2018-02-24.
 */
public class Controller
{

    /* State matrices */
    private Matrix currentState;
    private Matrix previousState;

    /**/
    private int step;
    private boolean autoUpdate;

    /* UI */
    private View view;


    public static void main(String[] args)
    {
        new Controller();
    }

    private Controller()
    {
        log(LogType.LOG, "in Controller constructor");
        this.step = 1;
        currentState = new Matrix(100, 100);
        currentState.toggleCell(1, 0);
        currentState.toggleCell(1, 1);
        currentState.toggleCell(1, 2);

        this.view = new View(this, currentState);
    }

    void change(int i, int j)
    {
        log(LogType.LOG, "changing status of cell at " + Integer.toString(i) + " " + Integer.toString(j));
        currentState.toggleCell(i, j);
        view.updateWorld(currentState);
    }

    void update(int step)
    {
        log(LogType.LOG, "in Controller.update()");
        for (int k = 0; k < step; k++)
        {
            previousState = new Matrix(currentState);
            for (int i = 0; i < currentState.getRows(); i++)
            {
                for (int j = 0; j < currentState.getColumns(); j++)
                {
                    //System.out.println("In update: " + Integer.toString(i) + " " + Integer.toString(j));
                    currentState.changeCell(i, j, previousState.cellSurviving(i, j));
                }
            }
        }
        view.updateWorld(currentState);
    }

    void newInstance(int rows, int columns)
    {
        currentState = new Matrix(rows, columns);
        view.updateWorld(currentState);
    }

    /* Log type enum */
    public enum LogType
    {
        ERROR,
        INFO,
        LOG;
    }

    static void log(LogType LOG_TYPE, String message)
    {
        System.out.println(LOG_TYPE + ": " + message);
    }

    void readFile(File file)
    {
        log(LogType.LOG, "reading file");
        ArrayList<Integer> indexList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getPath())))
        {
            String line = reader.readLine();
            Pattern p = Pattern.compile("[0-9] [0-9]$");
            while (!p.matcher(line).matches())
            {
                line = reader.readLine();
            }
            while (line != null)
            {
                String[] splitLine = line.split(" ");
                int x = Integer.parseInt(splitLine[0]);
                int y = Integer.parseInt(splitLine[1]);
                indexList.add(x);
                indexList.add(y);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e)
        {
            log(LogType.ERROR, e.getMessage());
        } catch (IOException e)
        {
            log(LogType.ERROR, e.getMessage());
        } finally
        {
            log(LogType.LOG, "file read successfully");
        }
        int maxIndex = Collections.max(indexList);
        currentState = new Matrix(maxIndex + 9, maxIndex + 9);
        for (int i = 0; i < indexList.size(); i += 2)
        {
            currentState.changeCell(indexList.get(i) + 4, indexList.get(i + 1) + 4, true);
        }
        view.updateWorld(currentState);
    }

    void setStep(int step)
    {
        this.step = step;
    }

    int getStep()
    {
        return step;
    }
}
