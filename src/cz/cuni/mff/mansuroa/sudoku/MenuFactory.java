package cz.cuni.mff.mansuroa.sudoku;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class MenuFactory {
    private static final MenuFactory INSTANCE = new MenuFactory();
    private MenuFactory() {}

    public static MenuFactory getInstance() {
        return MenuFactory.INSTANCE;
    }

    public JMenuBar createMenu(Viewer view, Controller ctrl) {
        return new MenuBuilder(view, ctrl).makeFileMenu().makeSudokuMenu().build();
    }
    
    private class MenuBuilder {
        final Viewer VIEW;
        final Controller CTRL;
        final JMenuBar MENU;

        public MenuBuilder(Viewer view, Controller ctrl) {
            this.VIEW=view;
            this.CTRL=ctrl;
            this.MENU=new JMenuBar();
        }

        public MenuBuilder makeFileMenu() {
            MENU.add(makeMenu("File", makeLoadItem(), makeStoreItem(), makeQuitItem()));
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
            Arrays.stream(items).forEach(item -> menu.add(item));
            return menu;
        }

        private JMenuItem makeSolveItem() {
            return new JMenuItem(new AbstractAction("Solve"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    VIEW.updateModel();
                    CTRL.solve(); 
                }
            });
        }

        private JMenuItem makeVerifyItem() {
            return new JMenuItem(new AbstractAction("Verify"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    VIEW.updateModel();
                    boolean result = CTRL.verify();
                    String msg = result ? "VALID" : "INVALID";
                    JOptionPane.showMessageDialog(null,msg,"SUDOKU",JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        private JMenuItem makeClearItem() {
            return new JMenuItem(new AbstractAction("Clear"){
                @Override
                public void actionPerformed(ActionEvent e) {
                    CTRL.clear();
                }
            });
        }

        private JMenuItem makeLoadItem() {
            return new JMenuItem(new AbstractAction("Load board") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CTRL.load();
                }
            });
        }

        private JMenuItem makeStoreItem() {
            return new JMenuItem(new AbstractAction("Store current board") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    VIEW.updateModel();
                    CTRL.store();
                }
            });
        }
        
        private JMenuItem makeQuitItem() {
            return new JMenuItem(new AbstractAction("Quit") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
    }
}
