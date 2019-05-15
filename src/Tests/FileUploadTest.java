import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileUploadTest {

    @Test
    void getParsedCSV() {
        File file = new File(".//temp.csv");
        try {
            file.createNewFile();

            CSVPrinter printer = new CSVPrinter(new FileWriter(file.getPath()), CSVFormat.DEFAULT);
            printer.printRecord("Time", "Event context", "Component", "Event name", "Description", "Origin", "IP address");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important", "Description test", "Origin Test", "IP1");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important2", "Description test", "Origin Test", "IP1");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important3", "Description test", "Origin Test", "IP1");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important2", "Description test", "Origin Test", "IP2");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important3", "Description test", "Origin Test", "IP2");
            printer.printRecord("Time Test", "Event context Test", "Component Test", "Important", "Description test", "Origin Test", "IP1");
            printer.close();

            FileUpload fileUpload = new FileUpload();
            String parsedCSV = fileUpload.GetParsedCSV(file.getPath());

            String[] expectedResult = {
            "@CONVERTED_FROM_TEXT",
            "@ITEM=-1=|",
            "@ITEM=2=Important2",
            "@ITEM=3=Important3",
            "@ITEM=1=Important",
            "2 3 -2",
            "1 2 3 -1 1 -2" };

            int count = 0;
            for(String line : Files.readAllLines(Paths.get(parsedCSV)))
            {
               assertEquals(line, expectedResult[count]);
               count++;
            }

            file.delete();
        } catch (IOException ex) {
            ex.printStackTrace();

            // forced assert
            assertEquals(false, true);
        }
    }
}