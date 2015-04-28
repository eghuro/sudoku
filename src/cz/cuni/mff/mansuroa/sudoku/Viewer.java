/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public Viewer(Controller ctrl, int size)
    {
        this.ctrl = ctrl;
        if (size<0) throw new IllegalArgumentException();
        
        this.SIZE=size;
        this.FRAME=new JFrame(TITLE);
        this.PANEL=new JPanel(new GridLayout(9,9));
        
        this.FRAME.addComponentListener(this.getFrameListener());
        
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.add(PANEL);
        
        COMPONENTS = new ItemComponent[SIZE][SIZE];
        
        for(int i=0;i<SIZE;++i){
            for(int j=0;j<SIZE;++j){
                COMPONENTS[i][j]=new ItemComponent(SIZE);
            }
        }
        
        MenuFactory mf = MenuFactory.getInstance();
        FRAME.setJMenuBar(mf.createMenu(this, ctrl));
    }
    
    public void setUp()
    {
        fillGrid();
        FRAME.pack();
        FRAME.setVisible(true);
    }
    
    public void setValue(int row,int col,String s)
    {
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

    private void fillGrid()
    {
        for(int x = 0; x < SIZE; ++x) {
            for(int y=0;y<SIZE;++y){
                PANEL.add(COMPONENTS[x][y],getConstraints(x,y));
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
    
    public void updateModel() {
        System.out.println("Update model");
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                ctrl.change(row, col, COMPONENTS[col][row].getVal());
            }
        }
    }
}