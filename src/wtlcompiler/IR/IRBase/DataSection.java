package wtlcompiler.IR.IRBase;

import java.util.LinkedList;
import java.util.List;

public class DataSection {
    public class DataPiece {
        private String strValue;
        private String otherValue;
        private String op;
        private String name;
        private int length;

        public DataPiece(String name, String value) {
            if (value != null) {
                this.strValue = value;
                this.length = strValue.length();
            }
            this.name = name;
        }

        public DataPiece(String name, String value, String op) {
            this.otherValue = value;
            this.op = op;
            this.name = name;
        }

        public String getOtherValue() {
            return otherValue;
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

    public String addData(String name, String value, String op) {
        dataPieces.add(new DataPiece(name, value, op));
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
