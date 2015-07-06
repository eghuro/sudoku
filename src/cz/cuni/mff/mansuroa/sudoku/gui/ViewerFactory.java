package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Tovarna na Viewer se stara o vytvoreni okna.
 * Jde o singleton, verejne API je facade createViewer, ktere bere pouze 
 * rozmer vysledneho sudoku
 */

public class ViewerFactory {
    private static final String TITLE="Sudoku";
    
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
   /*
    * ponechano private static - createViewer(int) je verejne API, udela potrebne
    * kontroly a spusti soukromou metodu, ktera nekontroluje vstup, ale kona praci
    */
   private static Viewer createViewer(Controller controller, int size) {
       ItemComponent[][] components = ViewerFactory.getComponents(size, controller);
       
       Viewer viewer = new Viewer(components, controller, size);
       JFrame frame = ViewerFactory.buildFrame(viewer.getPanel());

       frame.setJMenuBar(MenuFactory.createMenu(controller));
       ViewerFactory.fillGrid(viewer, components);
       frame.pack();
       frame.setVisible(true);
       
       controller.setViewer(viewer);
       controller.setFrame(frame);
       return viewer;
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
       return createViewer(new Controller(), size);
   }

   /**
    * Vytvori ComponentListener, ktery bude reagovat na zmenu velikosti
    * JFrame a zajisti, ze okno bude mit vzdy shodnou vysku a sirku.
    * 
    * @return vytvoreny ComponentListener
    */
   private static ComponentListener getFrameListener(JPanel panel) {
       return new ComponentAdapter() {
           private int oldSize = ViewerFactory.getSize(panel.getSize());
           
           @Override
           public void componentResized(ComponentEvent e) {
               JFrame frame = (JFrame) e.getComponent();
               int size = ViewerFactory.getSize(frame.getSize());
               
               if (size != oldSize) {
                   oldSize = size;
                   frame.setSize(size, size);
               }
           }
       };
   }
   
   /**
    * Vypocet delky strany ctvercoveho okna
    * @param dimension rozmer okna
    * @return nova velikost, stanovena jako prumer vysky a sirky, pokud se lisi
    */
   private static int getSize(Dimension dimension) {
       if (dimension.height != dimension.width) {
         return (int) Math.floor((dimension.width+dimension.height)/2);
       } else {
         return dimension.height;
       }
   }

   /**
    * Vytvori policka mrizky grafickeho rozhrani.
    * 
    * @param size rozmer mrizky
    * @return pole vytvorenych komponent
    */
   private static ItemComponent[][] getComponents(int size, Controller control) {
       ItemComponentFactory factory = ItemComponentFactory.getInstance();
       ItemComponent[][] components = new ItemComponent[size][size];
       for (int row = 0; row < size; ++row) {
           for (int col = 0; col < size; ++col) {
               components[col][row] = factory.create(size, row, col, control);
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
   private static void fillGrid(Viewer viewer, ItemComponent[][] components) {
       for (int x = 0; x < components.length; ++x) {
           for (int y = 0; y < components.length; ++y){
               viewer.addComponent(components[x][y], getConstraints(x,y));
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
   private static GridBagConstraints getConstraints(int x, int y) {
       GridBagConstraints c = new GridBagConstraints();
       c.gridx = x;
       c.gridy = y;
       c.fill = GridBagConstraints.BOTH;
       c.gridwidth = 1;
       c.gridheight = 1;    
       return c;
   }
   
    private static JFrame buildFrame(JPanel panel) {
        JFrame frame = new JFrame(TITLE);
        frame.addComponentListener(ViewerFactory.getFrameListener(panel));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(panel);
        return frame;
    }
}