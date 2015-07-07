package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Tovarna na vyrobu menu Vieweru.
 * Menu se vyrobi volanim staticke metody createMenu.
 * Jednotliva menu se tvori z polozek metodou makeMenu
 * Jednotlive polozky menu se tvori v samostatnych metodach pro vetsi citelnost.
 */

public class MenuFactory {
    /**
     * Vytvori menu pro dany Controller.
     * 
     * @param controller Controller
     * @return menu
     */
    public static JMenuBar createMenu(Controller controller) {
        JMenuBar menu = new JMenuBar();
        menu.add(makeMenu("File", makeLoadItem(controller), makeStoreItem(controller), makeQuitItem(controller)));
        menu.add(makeMenu("Sudoku", makeSolveItem(controller), makeVerifyItem(controller), makeClearItem(controller)));
        menu.add(makeMenu("View", makeFontItem(controller)));
        return menu;
    }

    /**
     * Vytvori menu s danym nazvem a polozkami.
     * 
     * @param name nazev menu
     * @param items polozky menu
     * @return vytvorene menu
     */
    private static JMenu makeMenu (final String name, JMenuItem ... items) {
        JMenu menu = new JMenu(name);
        for(JMenuItem item : items) {
            menu.add(item);
        }
        return menu;
    }

    /**
     * Vytvori polozku menu "Solve", ktera vola solve v Controlleru.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeSolveItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Solve"){
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.solve();
            }
        });
    }

    /**
     * Vytvori polozku menu "Verify", ktera vola verify v Controlleru a 
     * nasledne zobrazi dialog s vysledkem.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeVerifyItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Verify"){
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.verify();
            }
        });
    }

    /**
     * Vytvori polozku menu "Clear", ktera vola clear v Controlleru.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeClearItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Clear"){
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
    }

    /**
     * Vytvori polozku menu "Load", ktera vola load v Controlleru.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeLoadItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Load board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.load();
            }
        });
    }

    /**
     * Vytvori polozku menu "Store", ktera vola store v Controlleru.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeStoreItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Store current board") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.store();
            }
        });
    }

    /**
     * Vytvori polozku menu "Quit", ktera ukonci aplikaci.
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeQuitItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.exit();
            }
        });
    }

    /**
     * Vytvori polozku menu "Font", ktera umozni menit font v polich sudoku
     * 
     * @param controller controller k provedeni akce
     * @return polozka menu
     */
    private static JMenuItem makeFontItem(Controller controller) {
        return new JMenuItem(new AbstractAction("Font"){
            @Override
            public void actionPerformed(ActionEvent ae) {
                controller.font();
            }
        });
    }
}