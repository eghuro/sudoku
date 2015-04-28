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
    private final String VIEW_UNASSIGNED = "";
    private Sudoku model;
    private Viewer view;
    
    public Controller() {
        view = null;
        model = null;
    }
    
    // uzivatelske prikazy
    public boolean verify() {
        return Verificator.verify(model);
    }
    
    public void solve() {
        Solver.solve(model);
        updateView();
    }
    
    public void clear() {
        assert (view != null);
        int size = view.getSize();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                view.setValue(i, j, VIEW_UNASSIGNED);
                model.unsetValue(i, j);
            }
        }
    }
    
    //uzivatelske akce
    public void change(int row, int col, int value) {
        if (value != 0) {
            //System.out.println("Change ["+col+"]["+row+"] to "+value);
            model.setValue(row, col, value);
        } else {
            model.unsetValue(row, col);
        }
    }
    
    // set up
    public void setViewer(Viewer v) {
        assert(model.getSize() == v.getSize());
        assert(v!=null);
        
        this.model = new Sudoku(v.getSize());
        this.view = v;
        updateView();
    }
    
    public void load() {
        try {
            clear();
            FileView lw = new FileView(view.getComponent());
            this.model = Loader.load(lw.getFile());
            updateView();
        } catch (LoadException e) {
            
        }
    }
    
    public void store() {
        try {
            FileView sw = new FileView(view.getComponent());
            Storer.store(this.model, sw.getFile());
        } catch (StoreException e) {
            
        }
    }
    
    private void updateView() {
        System.out.println("Updating view");
        assert (view != null);
        int size = view.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = model.getValue(i, j);
                String set = VIEW_UNASSIGNED;
                if ((val > 0) && (val <= size)) {
                    set = val+"";
                }
                view.setValue(i, j, set);
                
            }
        }
    }
}
