/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.util.Arrays;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Sudoku{
    private final int SIZE=9;
    private final int[][] MATRIX;
    
    public Sudoku(){
        this.MATRIX=new int[SIZE][SIZE];
    }
    
    public int getSize()
    {
        return this.SIZE;
    }
    
    public void setValue(int row,int col,int value)
    {
        if(!validPosition(row,col)|(value<1)|(value>SIZE))
        {
            throw new IllegalArgumentException();
        }
        else
        {
            MATRIX[col][row]=value;
        }
    }
    
    public int getValue(int row,int col)
    {
        if(!validPosition(row,col))
        {
            throw new IllegalArgumentException();
        }
        else
        {
            return MATRIX[col][row];
        }
    }
    
    public void unsetValue(int row,int col)
    {
        if(!validPosition(row,col))
        {
            throw new IllegalArgumentException();
        }
        else
        {
            MATRIX[col][row]=0;
        }
    }
    
    public boolean isset(int row,int col)
    {
        if(!validPosition(row,col))
        {
            throw new IllegalArgumentException();
        }
        else
        {
            return MATRIX[col][row]!=0;
        }
    }
    
    private boolean validPosition(int row,int col)
    {
        return (row>=0)|(row<SIZE)|(col>=0)|(col<SIZE);
    }
}
