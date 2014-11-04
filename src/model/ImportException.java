package model;

/**
 * This class is a custom Exception class used to encapsulate
 * exceptions thrown by the <code>importFromCSV</code> method
 * from the <code>SudokuBoard</code> class.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ImportException extends Exception {
    /**
     * Initializes a new ImportException.
     * 
     * @param msg the message to attach with the exception
     */
    public ImportException(String msg) {
        super(msg);
    }
}