package cz.cuni.mff.mansuroa.sudoku.gui;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Zobrazeni dialogu pro vyber souboru.
 * 
 * @author Alexandr Mansurov
 */
public class FileView {
    private final JFileChooser jfc;
    private final Component parent;
    
    /**
     * Vytvori FileChooser, nastavi filtr na jmeno souboru.
     * 
     * @param parent rodicovske okno
     * @param text text na tlacitku
     */
    public FileView(Component parent, String text) {
        this.jfc = new JFileChooser();
        FileNameExtensionFilter filter = 
                new FileNameExtensionFilter ("Sudoku XML files", "xml");
        this.jfc.setFileFilter(filter);
        this.jfc.setApproveButtonText(text);
        this.parent = parent;
    }
    
    /**
     * Zobrazi dialog a ziska jmeno vybraneho souboru.
     * 
     * @return jmeno vybraneho souboru
     */
    public File getFile() {
        int ret = this.jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return this.jfc.getSelectedFile();
        } else {
            return null;
        }
    } 
}
