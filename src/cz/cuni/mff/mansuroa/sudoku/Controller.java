package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.io.LoadException;
import cz.cuni.mff.mansuroa.sudoku.io.Storer;
import cz.cuni.mff.mansuroa.sudoku.io.StoreException;
import cz.cuni.mff.mansuroa.sudoku.io.Loader;
import cz.cuni.mff.mansuroa.sudoku.gui.Viewer;
import cz.cuni.mff.mansuroa.sudoku.gui.FileView;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * Controller zajistuje komunikaci mezi datovou vrstvou - tridou Sudoku a 
 * grafickym rozhranim ve tride Viewer.
 * 
 * @author Alexandr Mansurov
 */
public class Controller {
    private final String VIEW_UNASSIGNED = "";
    private Sudoku model;
    private Viewer view;
    
    /**
     * Konstruktor inicializuje model a view na null.
     * ViewerFactory pri vytvareni Vieweru jej nasledne zaregistruje metodou
     * setViewer, ktera k Vieweru vytvori instanci Sudoku.
     */
    public Controller() {
        view = null;
        model = null;
    }
    
    /**
     * Over model Verifikatorem, zda jde o platne Sudoku.
     * @return zda plati vsechna omezeni sudoku - kazde cislo prave jednou ve
     * vsech radcich sloupcich a blocich
     */
    public boolean verify() {
        assert (model != null);
        return Verificator.verify(model);
    }
    
    /**
     * Predej stavajici data Solveru k vyreseni a prekresli obrazovku.
     * Reseni probiha v samostatnemvlakne.
     */
    public void solve() {
        assert (model != null);
        if (Verificator.verify(model)) {
            worker.start();
        } else {
            JOptionPane.showMessageDialog(view.getComponent(), "Zadani neni validni.", "Solve error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Vycisti obrazovku a data.
     */
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
    
    /**
     * Zmen hodnotu Sudoku na datove vrstve.
     * Kontrolu hodnot provede trida Sudoku.
     * 
     * @param row Radek
     * @param col Sloupec
     * @param value Hodnota (pro hodnotu 0 bude provedeno vycisteni dat na dane pozici)
     */
    public void change(int row, int col, int value) {
        assert (model != null);
        
        if (value != 0) {
            model.setValue(row, col, value);
        } else {
            model.unsetValue(row, col);
        }
    }
    
    /**
     * Zaregistruj Viewer a vytvor k nemu model prislusne velikosti.
     * 
     * @param viewer Viewer k zaregistrovani
     */
    public void setViewer(Viewer viewer) {
        assert (viewer != null);
        
        this.model = new Sudoku(viewer.getSize());
        this.view = viewer;
        updateView();
    }
    
    /**
     * Nacti sudoku ze souboru.
     * Vycisti obrazovku, zobrazi dialog pro vyber souboru, preda Loaderu
     * zvoleny soubor a ziska novy model, prekresli obrazovku.
     * Pokud nacitani selze, nahradi model prazdnym.
     */
    public void load() {
        assert (view != null);
        
        try {
            clear();
            FileView lw = new FileView(view.getComponent());
            File file = lw.getFile();
            if (file != null) {
                this.model = Loader.load(file);
            }
        } catch (LoadException e) {
            this.model = new Sudoku(view.getSize()); // model byl modifikovan, navrat do konsistentniho stavu
            JOptionPane.showMessageDialog(view.getComponent(), "Nacteni ze souboru selhalo.", "Load error", JOptionPane.ERROR_MESSAGE);
        } finally {
            updateView();
        }
    }
    
    /**
     * Uloz sudoku do souboru.
     * Zobrazi dialog pro vyber souboru a preda Storeru model a zvoleny soubor
     * pro ulozeni.
     */
    public void store() {
        assert (view != null);
        
        try {
            FileView sw = new FileView(view.getComponent());
            File file = sw.getFile();
            if (file != null){
                Storer.store(this.model, file);
            }
        } catch (StoreException e) {
            JOptionPane.showMessageDialog(view.getComponent(), "Ulozeni do souboru selhalo.", "Store error", JOptionPane.ERROR_MESSAGE);

        }
    }
    
    /**
     * Prekresli obrazovku hodnotami aktualniho modelu.
     */
    private void updateView() {
        assert (view != null);
        
        int size = view.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = model.getValue(i, j);
                String set = VIEW_UNASSIGNED;
                if ((val > 0) && (val <= size)) {
                    set = val + "";
                }
                view.setValue(i, j, set);                
            }
        }
    }
    
    private final Thread worker = new Thread() {
        @Override
        /**
         * Vyres sudoku. Zobraz dialog, pokud reseni selze. Prekresli obrazovku.
         */
        public void run() {
            try {
                Solver.solve(model);
            } catch (SolverException e) {
                JOptionPane.showMessageDialog(view.getComponent(), "Reseni nenalezeno.", "Solve error", JOptionPane.ERROR_MESSAGE);
                model = new Sudoku(view.getSize()); // sudoku mohlo byt modifikovano, navrat do konsistentniho stavu
            } finally {
                updateView();
            }
        }
    };
}
