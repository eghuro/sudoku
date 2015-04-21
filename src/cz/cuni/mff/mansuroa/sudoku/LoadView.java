/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.awt.Component;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class LoadView {
    JFileChooser jfc;
    Component parent;
    
    public LoadView(Component p) {
        this.jfc = new JFileChooser();
        FileNameExtensionFilter filter = 
                new FileNameExtensionFilter ("Sudoku XML files", "xml");
        this.jfc.setFileFilter(filter);
        
        this.parent = p;
    }
    
    public Sudoku load() throws LoadException {
        int ret = this.jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                return Loader.load(this.jfc.getSelectedFile());
            } catch(SAXException | IOException | ParserConfigurationException e) {
                throw new LoadException(e);
            }
        } else throw new LoadException();
    } 
}
