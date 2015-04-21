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
        //projdu po radcich/sloupcich a overim, ze kazde cislo je prave jednou
        int size = sudoku.getSize();
        System.out.println("Verificator: size = "+size);
        for (int i = 0; i < size; i++) {
            boolean haveValue[] = getValueArray(size);
            for (int j = 0; j < size; j++) {
                int valIndex = sudoku.getValue(i, j) - 1;
                if (haveValue[valIndex]) {
                    return false;
                }
                else {
                    haveValue[valIndex] = true;
                }
            }
        }
        
        for (int j = 0; j < size; j++) {
            boolean haveValue[] = getValueArray(size);
            for (int i = 0; i < size; i++) {
                int valIndex = sudoku.getValue(i, j) - 1;
                if (haveValue[valIndex]) {
                    return false;
                }
                else {
                    haveValue[valIndex] = true;
                }
            }
        }
        
        //TODO doplnit kontroly pro ctverce 3*3
        return true;
    }
    
    private static boolean[] getValueArray(int size) {
        boolean hasValue[] = new boolean[size];
        for (int i = 0; i < size; i++) {
            hasValue[i] = false;
        }
        return hasValue;
    }
}
