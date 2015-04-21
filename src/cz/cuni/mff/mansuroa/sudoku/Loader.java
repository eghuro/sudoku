/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.io.File;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
class Loader {
    public static Sudoku load(File file) {
        Sudoku sudoku = new Sudoku(9);
        return sudoku;
    }
}
