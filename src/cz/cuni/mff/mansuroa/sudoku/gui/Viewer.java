package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import cz.cuni.mff.mansuroa.sudoku.gui.ItemComponent.ItemComponentFactory;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Trida Viewer zobrazuje uzivatelske rozhrani a predava akce Controlleru.
 * Instance se vytvareji pomoci tovarny ViewerFactory
 * API umoznuje predevsim zmenit hodnotu nektereho policka Sudoku, dale
 * pomocne metody pro dalsi komponenty.
 * 
 * @author Alexandr Mansurov
 */
public class Viewer {
    private final String TITLE="Sudoku";
    private final int SIZE;
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final ItemComponent[][] COMPONENTS;
    private final Controller CNTRL;
    
    /**
     * Vytvori Viewer s danymi parametry.
     * 
     * @param components policka sudoku
     * @param mf tovarna na menu
     * @param frameListener obsluha zmeny velikosti
     * @param ctrl Controller
     * @param size pocet policek na radku resp. v sloupci, resp. v bloku
     */
    private Viewer(ItemComponent[][] components, MenuFactory mf,
            ComponentListener frameListener, Controller ctrl, int size) {
        this.CNTRL = ctrl;
        this.SIZE = size;
        this.PANEL = new JPanel(new GridLayout(size, size));
        this.COMPONENTS = components;
        this.FRAME = new JFrame(TITLE);
        this.FRAME.addComponentListener(frameListener);
        this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.FRAME.add(PANEL);
        this.FRAME.setJMenuBar(mf.createMenu(this, ctrl));
    }
    
    /**
     * Nastavi hodnotu do prislusneho pole mrizky.
     * 
     * @param row radek
     * @param col sloupec
     * @param value hodnota
     * @throws IllegalArgumentException neplatna pozice
     */
    public void setValue(int row, int col, String value) 
            throws IllegalArgumentException {
        if (validPosition(row, col)) {
            COMPONENTS[col][row].setValue(value);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Vrati rozmer zobrazovaneho Sudoku.
     * 
     * @return pocet policek na radku resp. v sloupci, resp. v bloku
     */
    public int getSize() {
        return this.SIZE;
    }
    
    /**
     * Umozni pristup k panelu pro zobrazeni okna.
     * 
     * @return okno
     */
    public Component getComponent() {
        return FRAME;
    }
    
    /**
     * Zapise aktualni hodnoty z mrizky do modelu.
     */
    public void updateModel() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                CNTRL.change(row, col, COMPONENTS[col][row].getVal());
            }
        }
    }
 
    /**
     * Vyhodnoceni platnosti pozice.
     * 
     * @param row radek
     * @param col sloupec
     * @return radek i sloupec nezaporne, mensi nez rozmer mrizky
     */
    private boolean validPosition(int row, int col) {
        return (row >= 0) && (row < SIZE) && (col >= 0) && (col < SIZE);
    }
        
    /**
     * Tovarna na Viewer se stara o vytvoreni okna.
     * Jde o singleton, verejne API je facade createViewer, ktere bere pouze 
     * rozmer vysledneho sudoku
     */
    public static class ViewerFactory {
        /**
         * Jedina instance tovarny.
         */
        private static final ViewerFactory INSTANCE = new ViewerFactory();
        
        /**
         * Pristup k instanci.
         * 
         * @return instance ViewerFactory
         */
        public static ViewerFactory getViewerFactory() { return INSTANCE; }
        
        private ViewerFactory() { }

        /**
         * Vytvori Viewer s danym Controllerem dane velikosti.
         * 
         * @param controller Controller, zajistujici kontakt mezi View a Modelem
         * @param size rozmer vysledne mrizky
         * @return nove postaveny, zobrazeny, s controllerem propojeny Viewer
         */
        private Viewer createViewer(Controller controller, int size) {
            ItemComponent[][] components = this.getComponents(size);
            Viewer v = new Viewer(components, MenuFactory.getInstance(), 
                    this.getFrameListener(), controller, size);
            this.fillGrid(v, components);
            v.FRAME.pack();
            v.FRAME.setVisible(true);
            controller.setViewer(v);
            return v;
        }
        
        /**
         * Vytvori Viewer dane velikosti, kteremu bude dan novy Controller.
         * Fasada nad dalsim rozhranim - netreba se starat o vytvareni
         * controlleru, ani tvorbu Vieweru.
         *
         * @param size rozmer vysledne mrizky
         * @return nove postavene a zobrazene okno aplikace
         * @throws IllegalArgumentException size zaporne
         */
        public Viewer createViewer(int size) throws IllegalArgumentException {
            if (size < 0) {
                throw new IllegalArgumentException();
            }
            return this.createViewer(new Controller(), size);
        }

        /**
         * Vytvori ComponentListener, ktery bude reagovat na zmenu velikosti
         * JFrame a zajisti, ze okno bude mit vzdy shodnou vysku a sirku.
         * 
         * @return vytvoreny ComponentListener
         */
        private ComponentListener getFrameListener() {
            return new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    JFrame frame = (JFrame) e.getComponent();
                    Dimension dimension = frame.getSize();
                    int size;
                    if (dimension.height != dimension.width) {
                        size = Math.max(dimension.height, dimension.width);
                    } else {
                        size = dimension.height;
                    }
                    frame.setSize(size, size);
                }
            };
        }

        /**
         * Vytvori policka mrizky grafickeho rozhrani.
         * 
         * @param size rozmer mrizky
         * @return pole vytvorenych komponent
         */
        private ItemComponent[][] getComponents(int size) {
            ItemComponentFactory factory = ItemComponentFactory.getInstance();
            ItemComponent[][] components = new ItemComponent[size][size];
            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    components[i][j] = factory.create(size);
                }
            }
            return components;
        }
        
        /**
         * Vlozi ItemComponenty z pole do panelu Vieweru.
         * 
         * @param viewer Viewer
         * @param components pole ItemComponent
         */
        private void fillGrid(Viewer viewer, ItemComponent[][] components) {
            for (int x = 0; x < components.length; ++x) {
                for (int y = 0; y < components.length; ++y){
                    viewer.PANEL.add(components[x][y],getConstraints(x,y));
                }
            }
        }
        
        /**
         * Vytvori GridBagConstraints pro GridBagLayout okna pro komponentu
         * v Gridu na pozici x, y.
         * 
         * @param x vodorovna souradnice v gridu
         * @param y svisla souradnice v gridu
         * @return GridBagConstraints
         */
        private GridBagConstraints getConstraints(int x, int y) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = x;
            c.gridy = y;
            c.fill = GridBagConstraints.BOTH;
            c.gridwidth = 1;
            c.gridheight = 1;    
            return c;
        }
    }
}