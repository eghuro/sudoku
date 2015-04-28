/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
class MenuBuilder {
    final Viewer VIEW;
    final Controller CTRL;
    final JMenuBar MENU;
    
    public MenuBuilder(Viewer view, Controller ctrl) {
        this.VIEW=view;
        this.CTRL=ctrl;
        this.MENU=new JMenuBar();
    }

    public MenuBuilder makeFileMenu() {
        MENU.add(makeMenu("File", makeLoadItem(CTRL), makeStoreItem(VIEW, CTRL)));
        return this;
    }

    public MenuBuilder makeSudokuMenu() {
        MENU.add(makeMenu("Sudoku", makeSolveItem(), makeVerifyItem(), makeClearItem()));
        return this;
    }
    
    public JMenuBar build() {
        return this.MENU;
    }
    
    private JMenu makeMenu (final String name, JMenuItem ... items) {
        JMenu menu = new JMenu(name);
        for(JMenuItem item : items) {
            menu.add(item);
        }
        return menu;
    }
    
    private JMenuItem makeSolveItem() {
        JMenuItem sol = new JMenuItem(new AbstractAction("Solve") {
            @Override
            public void actionPerformed(ActionEvent e) {
                VIEW.updateModel();
                CTRL.solve();
            }
        });
        return sol;
    }

    private JMenuItem makeVerifyItem() {
        JMenuItem ver = new JMenuItem(new AbstractAction("Verify"){
            @Override
            public void actionPerformed(ActionEvent e) {
                VIEW.updateModel();
                boolean result = CTRL.verify();
                String msg = result ? "VALID" : "INVALID";
                JOptionPane.showMessageDialog(null,msg,"SUDOKU",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return ver;
    }

    private JMenuItem makeClearItem() {
        JMenuItem clr = new JMenuItem(new AbstractAction("Clear"){
            @Override
            public void actionPerformed(ActionEvent e) {
                CTRL.clear();
            }
        });
        return clr;
    }

    private JMenuItem makeLoadItem(Controller ctrl) {
        JMenuItem load = new JMenuItem(new AbstractAction("Load board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ctrl.load();
            }
        }); 
        return load;
    }

    private JMenuItem makeStoreItem(Viewer view, Controller ctrl) {
        JMenuItem store = new JMenuItem(new AbstractAction("Store current board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.updateModel();
                ctrl.store();
            }
        });
        return store;
    }
    
}
