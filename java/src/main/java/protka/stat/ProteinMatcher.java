package protka.stat;

import protka.Protein;

public class ProteinMatcher {

  private String pattern;
  private String rowType;
  
  public String getPattern() {
    return pattern;
  }
  
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }
  
  public String getRowType() {
    return rowType;
  }
  
  public void setRowType(String rowType) {
    this.rowType = rowType;
  }
  
  public void set(String type, String pattern) {
    this.rowType = type;
    this.pattern = pattern;
  }
  
  public boolean match(Protein p) {
    for (String s: p.getLines(rowType, "")) {
      if (s.contains(pattern)) {
        return true;
      }
    }
    return false;
  }

  public static int matchSequence(String pattern, Protein prot) {
    int cnt = 0;
    int lastIdx = prot.getSequence().indexOf(pattern);
    while (lastIdx >= 0) {
      cnt ++;
      lastIdx = prot.getSequence().indexOf(pattern, lastIdx + 1);
    }
    return cnt;
  }
  
}
