/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class SudokuViewerController {
    private static final String UNSET="X";
    
    private Sudoku SUDOKU;
    private final Viewer VIEWER;
    
    public SudokuViewerController(Sudoku s,Viewer v)
    {
        this.SUDOKU=s;
        this.VIEWER=v;
        v.setController(this);
    }
    
    public void repaintViewer()
    {
        for(int x=0;x<SUDOKU.getSize();++x)
        {
            for(int y=0;y<SUDOKU.getSize();++y)
            {
                if(SUDOKU.isset(y, x))
                {
                    VIEWER.setValue(x, y, ""+SUDOKU.getValue(y, x));
                }
                else
                {
                    VIEWER.setValue(x,y,UNSET);
                }
            }
        }
    }
    
    public void repaintViewer(int x,int y)
    {
        if(SUDOKU.isset(y, x))
        {
            VIEWER.setValue(x, y, ""+SUDOKU.getValue(y, x));
        }
        else
        {
            VIEWER.setValue(x, y, UNSET);
        }
    }
    
    public void updateValue(int x,int y)
    {
        SUDOKU.unsetValue(y, x);
    }
    
    public void updateValue(int x,int y,int value)
    {
        SUDOKU.setValue(y, x, value);
    }
    
    public void setUpViewer()
    {
        repaintViewer();
        VIEWER.setUp();
    }
    
    public void generateClicked()
    {
        
    }
    
    public void solveClicked()
    {
        
    }
    
    public void verifyClicked()
    {
        
    }
    
    public void clearClicked()
    {
    }
}
