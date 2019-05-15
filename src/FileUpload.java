import ca.pfv.spmf.algorithms.frequentpatterns.dci_closed_optimized.AlgoDCI_Closed_Optimized;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;
import ca.pfv.spmf.gui.CommandProcessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FileUpload {
    // UI Components
    private JButton ChooseButton;
    private JButton RunButton;
    private JLabel FileNameLabel;
    private JPanel MainPanel;
    private JTextField MinsupField;

    private String FilePath;

    public FileUpload() {
        ChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame parentFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, MainPanel);

                if (parentFrame != null)
                {
                    FileDialog fileDialog = new java.awt.FileDialog(parentFrame, "Choose a file:", FileDialog.LOAD);
                    fileDialog.setFile("*.csv");
                    fileDialog.setVisible(true);

                    String selectedFile = fileDialog.getFile();
                    if(selectedFile != null)
                    {
                        FilePath = fileDialog.getDirectory() + selectedFile;
                        FileNameLabel.setText(selectedFile);
                        RunButton.setEnabled(true);
                    }
                }
            }
        });
        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Reader reader = Files.newBufferedReader(Paths.get(FilePath));
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .withIgnoreHeaderCase()
                            .withTrim());

                    // read log file
                    HashMap<String, Integer> eventToIndex = new HashMap<>();
                    HashMap<String, AlgorithmLine> algorithmLines = new HashMap<>();

                    String lastIP = "";
                    Integer lastEventIndex = 1;

                    for (CSVRecord csvRecord : csvParser) {
                        LogFileRow logFileRow = new LogFileRow(csvRecord);
                        Integer eventIndex = eventToIndex.putIfAbsent(logFileRow.GetEventName(), lastEventIndex);

                        if(eventIndex == null){
                            eventIndex = lastEventIndex++;
                        }

                        String thisIP = logFileRow.GetIP();

                        AlgorithmLine algoLine = algorithmLines.get(thisIP);
                        if(algoLine == null){
                            algoLine = new AlgorithmLine();
                            algorithmLines.put(thisIP, algoLine);
                        }

                        if(!lastIP.isEmpty() && !lastIP.equals(thisIP)){
                            algoLine.AddNewItemSet(eventIndex);
                        }
                        else{
                            algoLine.AddCurrentItemSet(eventIndex);
                        }

                        lastIP = thisIP;
                    }
                    reader.close();

                    // write convertedLog
                    String convertedLogFilename = ".//convertedLog.txt";
                    PrintWriter writer = new PrintWriter(convertedLogFilename, "UTF-8");

                    String itemString = "@ITEM=";
                    writer.println("@CONVERTED_FROM_TEXT");
                    writer.println(itemString + "-1=|"); // @ITEM=-1=|

                    for(HashMap.Entry<String, Integer> entry : eventToIndex.entrySet()){
                        StringBuilder stringBuilder = new StringBuilder();

                        stringBuilder.append(itemString).append(entry.getValue()).append('=').append(entry.getKey());

                        writer.println(stringBuilder.toString());
                    }

                    for(HashMap.Entry<String, AlgorithmLine> entry : algorithmLines.entrySet()){
                        writer.println(entry.getValue().toString());
                    }
                    writer.close();

                    // execute the algorithm
                    try {
                        try {
                            Double.parseDouble(MinsupField.getText());
                        } catch (NumberFormatException numEx) {
                            MinsupField.setText(Double.toString(0.4));
                        }

                        String outputFilename = ".//output.txt";
                        String parameters[] = new String[7];
                        Arrays.fill(parameters, "");
                        parameters[0] = MinsupField.getText();
                        CommandProcessor.runAlgorithm("BIDE+", convertedLogFilename, outputFilename, parameters);

                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.OPEN)) {
                            try {
                                desktop.open(new File(outputFilename));
                            } catch (Exception fileEx) {
                                JOptionPane.showMessageDialog(null,
                                        "An error occured while opening the output file.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (Exception ex) {
                        RunButton.setEnabled(false);
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "An error occured while creating the output file.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (IOException|IllegalArgumentException ex){
                    RunButton.setEnabled(false);

                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "An error occured opening the input file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Log file upload");
        frame.setContentPane(new FileUpload().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void showFileUpload(JFrame frame) {
        frame.setContentPane(new FileUpload().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}