package cz.cuni.mff.mansuroa.sudoku;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Zobrazeni dialogu pro vyber souboru.
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
public class FileView {
    JFileChooser jfc;
    Component parent;
    
    /**
     * Vytvori FileChooser, nastavi filtr na jmeno souboru
     * @param parent rodicovske okno
     */
    public FileView(Component parent) {
        this.jfc = new JFileChooser();
        FileNameExtensionFilter filter = 
                new FileNameExtensionFilter ("Sudoku XML files", "xml");
        this.jfc.setFileFilter(filter);
        
        this.parent = parent;
    }
    
    /**
     * Zobrazi dialog a ziska jmeno vybraneho souboru
     * @return jmeno vybraneho souboru
     */
    public File getFile() {
        int ret = this.jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return this.jfc.getSelectedFile();
        } else return null;
    } 
}
