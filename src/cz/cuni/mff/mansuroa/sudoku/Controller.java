package cz.cuni.mff.mansuroa.sudoku;

import cz.cuni.mff.mansuroa.sudoku.io.LoadException;
import cz.cuni.mff.mansuroa.sudoku.io.Storer;
import cz.cuni.mff.mansuroa.sudoku.io.StoreException;
import cz.cuni.mff.mansuroa.sudoku.io.Loader;
import cz.cuni.mff.mansuroa.sudoku.gui.Viewer;
import cz.cuni.mff.mansuroa.sudoku.gui.FileView;
import cz.cuni.mff.mansuroa.sudoku.gui.ItemComponent;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import say.swing.JFontChooser;

/**
 * Controller zajistuje komunikaci mezi datovou vrstvou - tridou Sudoku a 
 * grafickym rozhranim ve tride Viewer.
 */
public class Controller {
    private Sudoku model;
    private Viewer view;
    private JFrame frame;
    
    /**
     * Konstruktor inicializuje model a view na null.
     * ViewerFactory pri vytvareni Vieweru jej nasledne zaregistruje metodou
     * setViewer, ktera k Vieweru vytvori instanci Sudoku.
     */
    public Controller() {
        view = null;
        model = null;
        frame = null;
    }
    
    /**
     * Over model Verifikatorem, zda jde o platne Sudoku.
     */
    public void verify() {
        assert (view != null);
        assert (model != null);
        
        boolean result = Verificator.verify(model);
        String msg = result ? "VALID" : "INVALID";
        JOptionPane.showMessageDialog(view.getPanel(), msg, "SUDOKU", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Predej stavajici data Solveru k vyreseni a prekresli obrazovku.
     * Reseni probiha v samostatnem vlakne pomoci SwingWorkeru.
     */
    public void solve() {
        assert (view != null);
        assert (model != null);
        
        if (Verificator.verify(model)) {
             getWorker().execute();       
        } else {
            JOptionPane.showMessageDialog(view.getPanel(), "Zadani neni validni.", "Solve error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Vycisti obrazovku a data.
     */
    public void clear() {
        assert (view != null);
        assert (model != null);
        
        int size = view.getSize();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                view.setValue(i, j, ItemComponent.getUnassignedValue());
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
     * @return zda byla zmena provedena, nebo zda slo o neplatnou hodnotu
     */
    public boolean change(int row, int col, int value) {
        assert (model != null);
        assert (view != null);
        
        if ((value > 0) && (value <= view.getSize())) {
            model.setValue(row, col, value);
            return true;
        } else if (value == 0) {
            model.unsetValue(row, col);
            return true;
        } else {
            return false;
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
            FileView lw = new FileView(view.getPanel(), "Open");
            File file = lw.getFile();
            if (file != null) {
                this.model = Loader.load(file);
            }
        } catch (LoadException e) {
            this.model = new Sudoku(view.getSize()); // model byl modifikovan, navrat do konsistentniho stavu
            JOptionPane.showMessageDialog(view.getPanel(), "Nacteni ze souboru selhalo.", "Load error", JOptionPane.ERROR_MESSAGE);
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
            FileView sw = new FileView(view.getPanel(), "Save");
            File file = sw.getFile();
            if (file != null){
                Storer.store(this.model, file);
            }
        } catch (StoreException e) {
            JOptionPane.showMessageDialog(view.getPanel(), "Ulozeni do souboru selhalo.", "Store error", JOptionPane.ERROR_MESSAGE);

        }
    }
    
    /**
     * Prekresli obrazovku hodnotami aktualniho modelu.
     */
    private void updateView() {
        assert (view != null);
        assert (model != null);
        
        int size = view.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int val = model.getValue(i, j);
                if ((val > 0) && (val <= size)) {
                    view.setValue(i, j, val);
                } else {
                    view.setValue(i,j,ItemComponent.getUnassignedValue());
                }              
            }
        }
    }
    
    /**
     * Zaregistruje frame
     * @param frame frame k registraci
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
    /**
     * Ukonceni aplikace
     */
    public void exit() {
        assert (frame != null);
        
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Zmena fontu
     */
    public void font() {
        assert (view != null);
        assert (frame != null);
        
        JFontChooser fontChooser = new JFontChooser();
        fontChooser.setSelectedFont(view.getFont());
        int result = fontChooser.showDialog(frame);
        if (result == JFontChooser.OK_OPTION) {
            view.setFont(fontChooser.getSelectedFont());
        }
    }

    private SwingWorker getWorker() {
        assert (model != null);
        assert (view != null);
        
        return new SwingWorker() {
            private boolean solveException = false;
            private String message = null;

            @Override
            protected Sudoku doInBackground() {
                this.solveException = false;
                Sudoku copy = model.copy();
                try {
                   Solver.solve(copy);
                } catch (SolverException e) {
                    this.solveException = true;
                    this.message = e.getMessage();
                    copy = model;
                }
                return copy;
            }

            @Override
            protected void done() {
                try {
                    if(solveException) {
                        assert (message != null);
                        notFoundError(message);
                    }
                    model = (Sudoku)get();
                } catch (HeadlessException | InterruptedException | ExecutionException e) {
                    notFoundError(e.getMessage());

                    model = new Sudoku(view.getSize());
                } finally {
                    updateView();
                }
            }
            
            private void notFoundError(String message) {
                System.out.println(message);
                JOptionPane.showMessageDialog(view.getPanel(), "Reseni nenalezeno.", "Execution error", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
