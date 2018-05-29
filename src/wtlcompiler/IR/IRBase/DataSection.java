package wtlcompiler.IR.IRBase;

import java.util.LinkedList;
import java.util.List;

public class DataSection {
    public class DataPiece {
        private String strValue;
        private String name;
        private int length;

        public DataPiece(String name, String strValue) {
            this.strValue = strValue;
            this.name = name;
            this.length = strValue.length();
        }

        public String getStrValue() {
            return strValue;
        }

        public String getName() {
            return name;
        }

        public int getLength() {
            return length;
        }

        public String toString() {
            return name + ": " + strValue;
        }
    }

    private List<DataPiece> dataPieces;
    private static int pieceNum = 0;

    public DataSection() {
        dataPieces = new LinkedList<>();
    }

    public String addData(String name, String value) {
        dataPieces.add(new DataPiece(name, value));
        return name;
    }

    public String addData(String value) {
        String name = "String_" + String.valueOf(pieceNum++);
        dataPieces.add(new DataPiece(name, value));
        return name;
    }

    public List<DataPiece> getDataPieces() {
        return dataPieces;
    }
}
