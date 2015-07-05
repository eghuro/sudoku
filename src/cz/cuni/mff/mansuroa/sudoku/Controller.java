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
     * @return zda plati vsechna omezeni sudoku - kazde cislo prave jednou ve
     * vsech radcich sloupcich a blocich
     */
    public boolean verify() {
        assert (model != null);
        return Verificator.verify(model);
    }
    
    /**
     * Predej stavajici data Solveru k vyreseni a prekresli obrazovku.
     * Reseni probiha v samostatnem vlakne pomoci SwingWorkeru.
     * 
     * @throws java.lang.InterruptedException soucasne vlakno bylo preruseno pri cekani na vysledek
     */
    public void solve() throws InterruptedException {
        assert (model != null);
        if (Verificator.verify(model)) {
            SwingWorker worker = new SwingWorker() {
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
                            
                            System.out.println(message);
                            JOptionPane.showMessageDialog(view.getPanel(), "Reseni nenalezeno.", "Execution error", JOptionPane.ERROR_MESSAGE);
                            model = (Sudoku)get();
                        } else {
                            model = (Sudoku)get();
                        }
                    } catch (HeadlessException | InterruptedException | ExecutionException e) {
                        System.out.println(e.getMessage());
                        JOptionPane.showMessageDialog(view.getPanel(), "Reseni nenalezeno.", "Execution error", JOptionPane.ERROR_MESSAGE);
                        
                        model = new Sudoku(view.getSize());
                    } finally {
                        updateView();
                    }
                }
            };
            worker.execute();       
        } else {
            JOptionPane.showMessageDialog(view.getPanel(), "Zadani neni validni.", "Solve error", JOptionPane.ERROR_MESSAGE);
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
        Font old = view.getFont();
                    
        SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 30, 1);
        JSpinner spinner = new JSpinner(sModel);
        spinner.setValue(old.getSize());
        
        int option = JOptionPane.showOptionDialog(null, spinner, "Font size", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option == JOptionPane.OK_OPTION) {
            // zadano cislo
            int newSize = (int)sModel.getNumber();
            if(old.getSize()!=newSize) {
                // zmena velikosti pisma
                Font derived = old.deriveFont((float)newSize);
                view.setFont(derived);
            }
        }
    }
}
