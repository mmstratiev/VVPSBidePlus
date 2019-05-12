import org.apache.commons.csv.CSVRecord;

public class LogFileRow {
    //Time,Event context,Component,Event name,Description,Origin,IP address

    private CSVRecord RowRecord;

    public LogFileRow(CSVRecord row){
        RowRecord = row;
    }

    public String GetTime() {
        return RowRecord.get("Time");
    }

    public String GetEventContext() {
        return RowRecord.get("Event context");
    }

    public String GetComponent() {
        return RowRecord.get("Component");
    }

    public String GetEventName() {
        return RowRecord.get("Event name");
    }

    public String GetDescription() {
        return RowRecord.get("Description");
    }

    public String GetOrigin() {
        return RowRecord.get("Origin");
    }

    public String GetIP() {
        return RowRecord.get("IP address");
    }
}
