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
public class Main {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //new SudokuViewerController(new Sudoku(),new Viewer(9)).setUpViewer();
        Viewer v = new Viewer(9);
        Controller c = new Controller();
        v.setController(c);
        c.setViewer(v);
        v.setUp();
    }
    
}
