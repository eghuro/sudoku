/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Controller {
    private final String VIEW_UNASSIGNED = "";
    private Sudoku model;
    private Viewer view;
    
    public Controller() {
        //model = new Sudoku();
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
            }
        }
    }
    
    //TODO
    public void startTimer() {}
    
    //uzivatelske akce
    public void change(int row, int col, int value) {
        System.out.println("Change ["+col+"]["+row+"] to "+value);
        model.setValue(row, col, value);
        view.setValue(row, col, value+"");  /* TODO: lze vyhodit pokud change 
                volano jen z GUI */
    }
    
    // set up
    public void setViewer(Viewer v) {
        assert(model.getSize() == v.getSize());
        assert(v!=null);
        
        this.model = new Sudoku(v.getSize());
        this.view = v;
        updateView();
    }
    
    private void updateView() {
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
    
    public void load() {
        try {
            LoadView lw = new LoadView(view.getComponent());
            this.model = Loader.load(lw.load());
            updateView();
        } catch (LoadException e) {
            
        }
    }
    
    public void store() {
        try {
            StoreView sw = new StoreView(view.getComponent());
            sw.store(this.model);
        } catch (StoreException e) {
            
        }
    }
}
