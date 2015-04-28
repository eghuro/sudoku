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
public class Verificator {
    public static boolean verify(Sudoku sudoku) {
        //projdu po radcich/sloupcich/blocich a overim, ze kazde cislo je prave jednou
        return checkRows(sudoku) && checkCols(sudoku) && checkBlocks(sudoku);
    }
    
    private static boolean[] getValueArray(int size) {
        boolean hasValue[] = new boolean[size];
        for (int i = 0; i < size; i++) {
            hasValue[i] = false;
        }
        return hasValue;
    }
    
    private static boolean checkPosition (int i, int j, boolean[] haveValue, Sudoku sudoku) {
        if(sudoku.isset(i, j)){
            int valIndex = sudoku.getValue(i, j) -1 ;
            if (haveValue[valIndex]) {
                return false;
            } else {
                haveValue[valIndex] = true;
                return true;
            }
        } else {
            return true;
        }
    }

    private static boolean checkRows(Sudoku sudoku) {
        int size = sudoku.getSize();
        for (int i = 0; i < size; i++) {
            boolean haveValue[] = getValueArray(size);
            for (int j = 0; j < size; j++) {
                if(!checkPosition(i,j,haveValue,sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkCols(Sudoku sudoku) {
        int size = sudoku.getSize();
        for (int j = 0; j < size; j++) {
            boolean haveValue[] = getValueArray(size);
            for (int i = 0; i < size; i++) {
                if(!checkPosition(i,j, haveValue, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkBlocks(Sudoku sudoku) {
        int size = (int)Math.ceil(Math.sqrt(sudoku.getSize()));
        for(int blockRow = 0; blockRow < size; blockRow++) {
            for(int blockCol = 0; blockCol < size; blockCol++) {
                if(!checkBlock(blockRow, blockCol, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkBlock(int blockRow, int blockCol, Sudoku sudoku) {
        int size = sudoku.getSize();
        int koef = (int)Math.ceil(Math.sqrt(sudoku.getSize()));
        boolean haveValue[] = getValueArray(size);
        for(int row = blockRow*koef; row < blockRow*(koef+1); row++) {
            for (int col = blockCol*koef; col < blockCol*(koef+1); col++) {
                if(!checkPosition(row,col, haveValue, sudoku)) {
                    return false;
                }
            }
        }
        return true;
    }
}
