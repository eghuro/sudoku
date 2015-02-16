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
public class Controller {
    private final String VIEW_UNASSIGNED = " ";
    private Sudoku model;
    private Viewer view;
    
    public Controller() {
        model = new Sudoku();
        view = null;
    }
    
    // uzivatelske prikazy
    public boolean verify() {
        return Verificator.verify(model);
    }
    
    public void solve() {
        Solver.solve(model);
        updateView();
    }
    
    public void generate() {
        model = Generator.generate();
        updateView();
    }
    
    public void clean() {
        assert (view != null);
        int size = view.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; i < size; j++) {
                view.setValue(j, j, VIEW_UNASSIGNED);
            }
        }
    }
    
    //TODO
    public void startTimer() {}
    
    //uzivatelske akce
    public void change(int row, int col, int value) {
        model.setValue(row, col, value);
        view.setValue(row, col, value+"");  /* TODO: lze vyhodit pokud change 
                volano jen z GUI */
    }
    
    // set up
    public void setViewer(Viewer v) {
        assert(model.getSize() == v.getSize());
        assert(v!=null);
        
        this.view = v;
        updateView();
    }
    
    private void updateView() {
        assert (view != null);
        int size = view.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; i < size; j++) {
                view.setValue(i, j, model.getValue(i, j)+"");
            }
        }
    }
}
