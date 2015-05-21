package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * Tovarna na vyrobu menu Vieweru.
 */
//ALF: Overdesigned -- overly complex to create a very simple menu. I do not see a reason, why to make a factory (or factory with builder) to create this simple menu.
public class MenuFactory {
    private static final MenuFactory INSTANCE = new MenuFactory();
    private MenuFactory() {}

    /**
     * Pristup k instanci tovarny.
     * 
     * @return instance
     */
    public static MenuFactory getInstance() {
        return MenuFactory.INSTANCE;
    }

    /**
     * Vytvori menu pro dany Controller.
     * 
     * @param controller Controller
     * @return menu
     */
    public JMenuBar createMenu(Controller controller) {
        // fasada nad skrytym builderem
        return new MenuBuilder(controller).makeFileMenu().makeSudokuMenu().build();
    }
    
    /**
     * Builder pro vyrobu menu.
     */
    private class MenuBuilder {
        private final Controller controller;
        private final JMenuBar menu;

        /**
         * Vytvori builder pro dany Viewer a Controller.
         * 
         * @param controller instance Controlleru
         */
        public MenuBuilder(Controller controller) {
            assert (controller != null);
            
            this.controller=controller;
            this.menu=new JMenuBar();
        }

        /**
         * Vytvori menu "File".
         * 
         * @return "this"
         */
        public MenuBuilder makeFileMenu() {
            menu.add(makeMenu("File", makeLoadItem(), makeStoreItem(), makeQuitItem()));
            return this;
        }

        /**
         * Vytvori menu "Sudoku".
         * 
         * @return this
         */
        public MenuBuilder makeSudokuMenu() {
            menu.add(makeMenu("Sudoku", makeSolveItem(), makeVerifyItem(), makeClearItem()));
            return this;
        }

        /**
         * Vrat vysledne menu
         * @return menu
         */
        public JMenuBar build() {
            return this.menu;
        }

        /**
         * Vytvori menu s danym nazvem a polozkami.
         * 
         * @param name nazev menu
         * @param items polozky menu
         * @return vytvorene menu
         */
        private JMenu makeMenu (final String name, JMenuItem ... items) {
            JMenu menu = new JMenu(name);
            //ALF: An unnecessarily complex way of making for-each loop
            Arrays.stream(items).forEach(item -> menu.add(item));
            return menu;
        }

        /**
         * Vytvori polozku menu "Solve", ktera vola solve v Controlleru.
         * 
         * @return polozka menu
         */
        private JMenuItem makeSolveItem() {
            return new JMenuItem(new AbstractAction("Solve"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        controller.solve();
                    } catch (InterruptedException ex) {
                        System.out.println(ex.toString());
                    }
                }
            });
        }

        /**
         * Vytvori polozku menu "Verify", ktera vola verify v Controlleru a 
         * nasledne zobrazi dialog s vysledkem.
         * 
         * @return polozka menu
         */
        private JMenuItem makeVerifyItem() {
            return new JMenuItem(new AbstractAction("Verify"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result = controller.verify();
                    String msg = result ? "VALID" : "INVALID";
                    JOptionPane.showMessageDialog(null, msg, "SUDOKU", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        /**
         * Vytvori polozku menu "Clear", ktera vola clear v Controlleru.
         * 
         * @return polozka menu
         */
        private JMenuItem makeClearItem() {
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
         * @return polozka menu
         */
        private JMenuItem makeLoadItem() {
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
         * @return polozka menu
         */
        private JMenuItem makeStoreItem() {
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
         * @return polozka menu
         */
        private JMenuItem makeQuitItem() {
            return new JMenuItem(new AbstractAction("Quit") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // ALF: Not a proper way to terminate a program
                }
            });
        }
    }
}