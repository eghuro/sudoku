package cz.cuni.mff.mansuroa.sudoku.gui;

import cz.cuni.mff.mansuroa.sudoku.Controller;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

/**
 * Tovarna na ItemComponent
 */
public class ItemComponentFactory {
   private static final ItemComponentFactory INSTANCE = new ItemComponentFactory();
   private ItemComponentFactory() {}

   /**
    * Umozni pristup k jedine instanci tovarny.
    * @return instance tovarny
    */
   public static ItemComponentFactory getInstance() {
       return INSTANCE;
   }

   /**
    * Vytvori ItemComponent pro sudoku dane velikosti.
    * 
    * @param size velikost sudoku (nezaporne)
    * @param row
    * @param col
    * @param controller
    * @return nova komponenta
    */
   public ItemComponent create(int size, int row, int col, Controller controller) {
       if (size<0) throw new IllegalArgumentException();

       ItemComponent component = new ItemComponent(row, col);
       component.setInputVerifier(ItemComponentFactory.getInputVerifier(size, controller));
       //component.addComponentListener(ItemComponentFactory.getComponentListener());
       return component;
   }

  /**
   * Vytvori ComponentListener pro zmenu fontu pri zmene rozmeru komponenty.
   * 
   * @return ComponentAdapter implementujici componentResized() a upravujici 
   * velikost textu
   */
  /*private static ComponentListener getComponentListener() {
      return new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
              Dimension newDimension = e.getComponent().getSize();
              //System.out.println("New area: "+ItemComponent.getArea(newDimension));
              ItemComponent ic = (ItemComponent) e.getComponent();
              //ic.setFont(newDimension);
          }
      };
  }*/

   /**
    * Vytvori overovac vstupu pro danou komponentu.
    * 
    * @param size rozmer sudoku
    * @return InputVerifier pro platne vstupni hodnoty
    */
   private static InputVerifier getInputVerifier(int size, Controller controller) {
       return new InputVerifier() {
           @Override
           public boolean verify(JComponent input) {
               ItemComponent ic = (ItemComponent) input;
               String text = ic.getText();
               try {
                   int val = Integer.parseInt(text);
                   if ((val > 0) && (val <= size)) {
                       ic.setValue(val);
                       controller.change(ic.getRow(), ic.getCol(), val);
                   } else {
                       ic.setEmpty();
                   }
               } catch (NumberFormatException e) {
                   ic.setEmpty();
               }
               return true;
           }
       };
   }
}
