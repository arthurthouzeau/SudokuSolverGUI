package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.ImportException;
import model.RecursiveSolver;
import model.SudokuBoard;

/**
 * This class represents the main window of the application.
 * It manages most of the user interaction.
 * 
 * @author Arthur Thouzeau
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {
    final JFileChooser fc = new JFileChooser();
    final FileNameExtensionFilter csvFilter = 
            new FileNameExtensionFilter("CSV only", "csv");
    private JPanel container = new JPanel(new BorderLayout());
    private JPanel controls = new JPanel();
    private JButton importButton = new JButton("Import CSV");
    private JButton generateButton = new JButton("Generate");
    private JButton solveButton = new JButton("Solve !");
    private JButton writeButton = new JButton("Export to CSV");
    private SudokuPanel grid = new SudokuPanel();

    /**
     * Initializes the main frame. Set up the layout, add the different
     * components and the listeners.
     */
    public SudokuFrame() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fc.setFileFilter(csvFilter);
        
        importButton.addActionListener(new ImportButtonListener());        
        ActionListener generateButtonListener = new GenerateButtonListener();
        ActionListener busyGenerate = CursorController.createListener(this, generateButtonListener);
        generateButton.addActionListener(busyGenerate);
        ActionListener solveButtonListener = new SolveButtonListener();
        ActionListener busySolve = CursorController.createListener(this, solveButtonListener);
        solveButton.addActionListener(busySolve);
        writeButton.addActionListener(new WriteButtonListener());
        controls.add(importButton);
        controls.add(generateButton);
        controls.add(solveButton);
        controls.add(writeButton);
        
        container.add(controls, BorderLayout.NORTH);
        container.add(grid, BorderLayout.CENTER);
        setContentPane(container);
        setSize(550, 530);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Internal class implementing the ActionListener interface for the
     * "Import CSV" button.
     */
    class ImportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            int result = fc.showDialog(controls, "Import");
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    // Import CSV file
                    String inputFile = fc.getSelectedFile().getAbsolutePath();
                    SudokuBoard sb = SudokuBoard.importFromCSV(inputFile);
                    // Check the dimension of the Sudoku
                    if (sb.size != grid.SIZE) {
                        String errorMsg = "Please check that the input CSV is a "
                                + grid.SIZE + "*" + grid.SIZE + " grid.\n"
                                + "This version of the program only accepts "
                                + grid.SIZE + "*" + grid.SIZE + " Sudokus.";
                        JOptionPane.showMessageDialog(fc, errorMsg, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Check that SudokuBoard respects Sudoku rules
                    if (!sb.checkInitialBoard()) {
                        String errorMsg = "The initial grid seems incorrect.\n"
                                + "Please check that :\n"
                                + "- integers are between 0 and "+ sb.size + "\n"
                                + "- each integer appears only once on each row, column and region.";
                        JOptionPane.showMessageDialog(fc, errorMsg, "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Update GUI
                    grid.updateLabelsNew(sb);
                } catch (ImportException e) {
                    String errorMsg = "An error occured during the import of the CSV file.\n";
                    errorMsg = errorMsg + e.getMessage();
                    JOptionPane.showMessageDialog(container, errorMsg, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Internal class implementing the ActionListener interface for the
     * "Generate" button. The duration of the current method to generate Sudokus
     * being slightly unpredicatble, the generation is stopped after 5 seconds
     * to let the user choose to restart under hopefully better conditions.
     */
    class GenerateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<SudokuBoard> future = executor.submit(new Task());
            try {
                SudokuBoard sb = future.get(5, TimeUnit.SECONDS);
                grid.updateLabelsNew(sb);
            } catch (TimeoutException e) {
                String infoMsg = "Oops! Bad random initialization, please try again.";
                JOptionPane.showMessageDialog(container, infoMsg, "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException e) {
                String errorMsg = "An error occured during the random generation.\n";
                errorMsg = errorMsg + e.toString();
                JOptionPane.showMessageDialog(container, errorMsg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ExecutionException e) {
                String errorMsg = "An error occured during the random generation.\n";
                errorMsg = errorMsg + e.toString();
                JOptionPane.showMessageDialog(container, errorMsg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            executor.shutdownNow();
        }
    }

    /**
     * Internal class implementing the ActionListener interface for the
     * "Solve it!" button.
     */
    class SolveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            RecursiveSolver rs = new RecursiveSolver(grid.getSudokuBoard());
            if (!rs.solve()) {
                JOptionPane.showMessageDialog(container, "No solution found!",
                        "Result", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            grid.updateLabelsResults(rs.getBoard());
        }
    }

    /**
     * Internal class implementing the ActionListener interface for the
     * "Export to CSV" button.
     */
    class WriteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            int result = fc.showSaveDialog(controls);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String outputFile = fc.getSelectedFile().getAbsolutePath();
                    grid.getSudokuBoard().writeToCSV(outputFile);
                    String success = "The file has been written to:\n"
                            + outputFile;
                    JOptionPane.showMessageDialog(container, success,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    String errorMsg = "An error occured while writing the file.\n"
                            + "Please check the path is correct, or that the "
                            + "destination file is not protected.\n";
                    errorMsg = errorMsg + e.toString();
                    JOptionPane.showMessageDialog(container, errorMsg, 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Runs the application.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        SudokuFrame f = new SudokuFrame();
        f.setVisible(true);
    }
}