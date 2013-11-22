package protka;

import java.util.ArrayList;
import java.util.List;

public class FastaItem {

  public final String headerRow;
  public final String acNum; // access number, works as an ID
  public List<String> sequenceRows = new ArrayList<String>();

  public FastaItem(String header, String ac) {
    headerRow = header;
    acNum = ac;
  }

  public void addSeqRow(String s) {
    sequenceRows.add(s);
  }

  public String getSequenceString() {
    StringBuilder sb = new StringBuilder();
    for (String s : sequenceRows) {
      sb.append(s);
    }
    return sb.toString();
  }
  
  public List<String> getSequenceRows(){
    return sequenceRows;
  }
}
