/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.mansuroa.sudoku;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Storer {
    public static void store(Sudoku sudoku, File file) throws StoreException {
        try {
            Document doc = createDocument(sudoku);
            storeDocument(doc, file);
            
        } catch (ParserConfigurationException | TransformerException ex) {
            throw new StoreException(ex);
        }
    }

    private static void createElements(Sudoku sudoku, Document doc, Element rootElement) {
        int size = sudoku.getSize();
        for (int i = 0; i < size; i++ ) {
            for (int j = 0; j < size; j++ ) {
                Element entry = createEntry(sudoku, i, j, doc);
                if (entry!=null) {
                    System.out.println("Appending entry");
                    rootElement.appendChild(entry);
                }
            }
        }
    }

    private static Element createEntry(Sudoku sudoku, int row, int col, Document doc) {
        int value = sudoku.getValue(row, col);
        if (value != 0) {
            Element entry = doc.createElement("entry");
            System.out.println("New entry ["+row+","+col+"] : "+ value);
            entry.setAttribute("row", row+"");
            entry.setAttribute("col", col+"");
            entry.setAttribute("value", sudoku.getValue(row, col)+"");
            return entry;
        } else return null;
    }

    private static Document createDocument(Sudoku sudoku) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("sudoku");
        doc.appendChild(rootElement);

        createElements(sudoku, doc, rootElement);
        
        return doc;
    }

    private static void storeDocument(Document doc, File file) throws TransformerConfigurationException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        t.transform(source, result);
    }
}
