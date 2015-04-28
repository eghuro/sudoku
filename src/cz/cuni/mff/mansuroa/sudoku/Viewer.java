package cz.cuni.mff.mansuroa.sudoku;

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
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Viewer {
    private static final String TITLE="Sudoku";
    
    private final int SIZE;
    private final JFrame FRAME;
    private final JPanel PANEL;
    private final ItemComponent[][] COMPONENTS;
    private final Controller ctrl;
    
    private Viewer(ItemComponent[][] components, MenuFactory mf, ComponentListener frameListener, Controller ctrl, int size) {
        this.ctrl = ctrl;
        this.SIZE=size;
        this.PANEL=new JPanel(new GridLayout(size,size));
        this.COMPONENTS = components;
        this.FRAME=new JFrame(TITLE);
        this.FRAME.addComponentListener(frameListener);
        this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.FRAME.add(PANEL);
        this.FRAME.setJMenuBar(mf.createMenu(this, ctrl));
    }
    
    public void setValue(int row,int col,String s) {
        if((row >= 0) && (row < SIZE) && (col >= 0) && (col < SIZE)) {
            COMPONENTS[col][row].setValue(s);
        } else throw new IllegalArgumentException();
    }
    
    public int getSize() {
        return this.SIZE;
    }
    
    public Component getComponent() {
        return FRAME;
    }
    
    public void updateModel() {
        System.out.println("Update model");
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                ctrl.change(row, col, COMPONENTS[col][row].getVal());
            }
        }
    }
 
    public static class ViewerFactory {
        private static final ViewerFactory INSTANCE = new ViewerFactory();
        public static ViewerFactory getViewerFactory() { return INSTANCE; }
        private ViewerFactory() { }

        private Viewer createViewer(Controller controller, int size) throws IllegalArgumentException {
            if (size<0) throw new IllegalArgumentException();
            ItemComponent[][] components = this.getComponents(size);
            Viewer v= new Viewer(components, MenuFactory.getInstance(), this.getFrameListener(), controller, size);
            this.fillGrid(v, components, size);
            v.FRAME.pack();
            v.FRAME.setVisible(true);
            controller.setViewer(v);
            return v;
        }
        
        public Viewer createViewer(int size){
            return this.createViewer(new Controller(), size);
        }

        private ComponentListener getFrameListener() {
            return new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    JFrame frame = (JFrame)e.getComponent();
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

        private ItemComponent[][] getComponents(int size) {
            ItemComponent[][] components = new ItemComponent[size][size];
            for(int i=0;i<size;++i){
                for(int j=0;j<size;++j){
                    components[i][j]=new ItemComponent(size);
                }
            }
            return components;
        }
        
        private void fillGrid(Viewer v, ItemComponent[][] components, int size) {
            for(int x = 0; x < size; ++x) {
                for(int y=0;y<size;++y){
                    v.PANEL.add(components[x][y],getConstraints(x,y));
                }
            }
        }
        
        private GridBagConstraints getConstraints(int x, int y) {
            GridBagConstraints c=new GridBagConstraints();
            c.gridx=x;
            c.gridy=y;
            c.fill=GridBagConstraints.BOTH;
            c.gridwidth=1;
            c.gridheight=1;    
            return c;
        }
    }
}