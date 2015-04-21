/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class FileView {
    JFileChooser jfc;
    Component parent;
    
    public FileView(Component p) {
        this.jfc = new JFileChooser();
        FileNameExtensionFilter filter = 
                new FileNameExtensionFilter ("Sudoku XML files", "xml");
        this.jfc.setFileFilter(filter);
        
        this.parent = p;
    }
    
    public File getFile() {
        int ret = this.jfc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return this.jfc.getSelectedFile();
        } else return null;
    } 
}