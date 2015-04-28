package cz.cuni.mff.mansuroa.sudoku;

import javax.swing.JMenuBar;

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
}
