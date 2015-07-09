package cz.cuni.mff.mansuroa.sudoku.io;

import cz.cuni.mff.mansuroa.sudoku.Sudoku;
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
 * Storer uklada data do XML souboru.
 * 
 * @author Alexandr Mansurov
 */
public class Storer {
    private Storer() {}
    
    /**
     * Ulozi data sudoku do souboru.
     * 
     * @param sudoku data
     * @param file soubor
     * @throws StoreException ulozeni se nezdarilo
     */
    public static void store(Sudoku sudoku, File file) throws StoreException {
        try {
            Document doc = createDocument(sudoku);
            storeDocument(doc, file);
        } catch (ParserConfigurationException | TransformerException ex) {
            throw new StoreException(ex);
        }
    }

    /**
     * Vytvor XML elementy.
     * Projdi data a vytvor XML element entry pro kazde policko.
     * 
     * @param sudoku data
     * @param doc vysledny dokument
     * @param rootElement korenovy element - kam novy element pripojit
     */
    private static void createElements(Sudoku sudoku, Document doc, Element rootElement) {
        int size = sudoku.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Element entry = createEntry(sudoku, i, j, doc);
                if (entry != null) {
                    rootElement.appendChild(entry);
                }
            }
        }
    }

    /**
     * Vytvori element entry s daty o jednom policku.
     * 
     * @param sudoku data
     * @param row souradnice (radek)
     * @param col souradnice (sloupec)
     * @param doc XML dokument pro vytvoreni elementu
     * @return vytvoreny element nebo null, pokud dane policko neni vyplneno
     */
    private static Element createEntry(Sudoku sudoku, int row, int col, Document doc) {
        int value = sudoku.getValue(row, col);
        if (value != 0) {
            Element entry = doc.createElement("entry");
            entry.setAttribute("row", row + "");
            entry.setAttribute("col", col + "");
            entry.setAttribute("value", value + "");
            return entry;
        } else {
            return null;
        }
    }

    /**
     * Vytvori XML dokument s daty daneho sudoku.
     * 
     * @param sudoku data k ulozeni
     * @return XML dokument
     * @throws ParserConfigurationException doslo k chybe
     */
    private static Document createDocument(Sudoku sudoku) throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("sudoku");
        doc.appendChild(rootElement);

        createElements(sudoku, doc, rootElement);
        
        return doc;
    }

    /**
     * Uloz vytvoreny XML dokument do souboru.
     * 
     * @param doc XML dokument
     * @param file Soubor
     * @throws TransformerConfigurationException nelze vytvorit transformer
     * @throws TransformerException chyba pri ukladani
     */
    private static void storeDocument(Document doc, File file) throws TransformerConfigurationException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        t.transform(source, result);
    }
}
