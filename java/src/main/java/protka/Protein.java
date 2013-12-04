package protka;

import java.util.ArrayList;
import java.util.List;

public class Protein {

  private List<String> lines = new ArrayList<String>();
  private String sequence = "";
  public String acNum;
  private List<SequencePart> tmNums;
  private static int MINLENGTH;
  private static int MAXLENGTH;
  private List<SequencePart> allDomains;

  public static void setMinLength(int length) {
    MINLENGTH = length;
  }

  public static void setMaxLength(int length) {
    MAXLENGTH = length;
  }

  public Boolean getBeginsInside() {
    return beginsInside;
  }

  public void setBeginsInside(boolean beginsInside) {
    this.beginsInside = new Boolean(beginsInside);
  }

  private Boolean beginsInside = null;

  public List<String> getLines() {
    return lines;
  }

  public void setLines(List<String> data) {
    lines.clear();
    boolean seqReading = false;
    for (String l : data) {
      if (seqReading && !l.startsWith("//")) {
        String seqPart = l.trim();
        String s2 = seqPart.replaceAll(" ", "");
        sequence = sequence + s2;
      }
      lines.add(l);
      if (l.startsWith("SQ")) {
        seqReading = true;
      }
      if (l.startsWith("AC")) {
        String id = l.split("   ")[1];
        acNum = id.split(";")[0];
      }
    }
    readDomains();
  }

  // returns the first matching line, or null if no match
  public String getLine(String startsWith) {
    for (String line : lines) {
      if (line.startsWith(startsWith)) {
        return line;
      }
    }
    return null;
  }

  // returns all matching lines, or empty list if no match
  public List<String> getLines(String startsWith, String contains) {
    ArrayList<String> ret = new ArrayList<String>();
    for (String line : lines) {
      if (line.startsWith(startsWith) && line.contains(contains)) {
        ret.add(line);
      }
    }
    return ret;
  }

  public String getSequence() {
    return sequence;
  }

  public List<SequencePart> getTmNumbers() {
    return tmNums;
  }

  private ArrayList<SequencePart> parseDomainParts(List<String> lines, 
      String type) {
    ArrayList<SequencePart> ret = new ArrayList<SequencePart>();
    for (String line : lines) {
      String fromStr = line.substring(15, 20).trim();
      String toStr = line.substring(21, 27).trim();
      if (fromStr.contains("?") || toStr.contains("?")) {
        continue;
      }
      if (fromStr.startsWith("<") || fromStr.startsWith(">")) {
        fromStr = fromStr.substring(1);
      }
      if (toStr.startsWith("<") || toStr.startsWith(">")) {
        toStr = toStr.substring(1);
      }
      int from = Integer.parseInt(fromStr);
      int to = Integer.parseInt(toStr);
      ret.add(new SequencePart(from, to, type));
    }
    return ret;
  }
  
  private void readDomains() {
    if (tmNums == null || allDomains == null) {
      tmNums = new ArrayList<SequencePart>();
      allDomains = new ArrayList<SequencePart>();
    } else {
      tmNums.clear();
      allDomains.clear();
    }
    readTmNumbers();
    readInOutDomains();
  }
  
  private void readTmNumbers() {
    List<String> ftLines = getLines("FT   TRANSMEM" , "");
    tmNums = parseDomainParts(ftLines, "TM");
    allDomains.addAll(tmNums);
  }
  
  private void readInOutDomains() {
    List<String> innerDomainLines = getLines("FT   TOPO_DOM", "Cytoplasmic");
    List<SequencePart> inDomains = parseDomainParts(innerDomainLines, "IN");
    List<String> outerDomainLines = getLines("FT   TOPO_DOM", "Extracellular");
    List<SequencePart> outDomains = parseDomainParts(outerDomainLines, "OUT");
    allDomains.addAll(inDomains);
    allDomains.addAll(outDomains);
    int firstIn = Integer.MAX_VALUE;
    int firstOut = Integer.MAX_VALUE;
    if (!inDomains.isEmpty()) {
      firstIn = inDomains.get(0).getFrom();
    }
    if (!outDomains.isEmpty()) {
      firstOut = outDomains.get(0).getFrom();
    }
    if (firstIn < firstOut) {
      setBeginsInside(true);
    } else if (firstOut < Integer.MAX_VALUE) {
      setBeginsInside(false);
    }
  }

  public String getSequencePart(int from, int to) {
    String sequence = getSequence();
    if (to <= sequence.length() && from > 0)
      return getSequence().substring(from - 1, to);
    else
      return null;
  }

  public SequencePart getSeqForTmPart(int i) {
    getTmNumbers();
    if ((!beginsInside && i % 2 == 1 || beginsInside && i % 2 == 0)) {
      // check if beginning is far away enough from TM part
      if (i == tmNums.size() - 1) {
        int diff = getSequence().length() - tmNums.get(i).getTo();
        if (diff > MINLENGTH) {
          if (diff > MAXLENGTH) {
            return new SequencePart(tmNums.get(i).getFrom(), tmNums.get(i)
                .getTo() + MAXLENGTH, "TM");
          } else {
            return new SequencePart(tmNums.get(i).getFrom(), getSequence()
                .length(), "TM");
          }
        }
      } else {
        int diff = tmNums.get(i + 1).getFrom() - 1 - tmNums.get(i).getTo();
        if (diff > MINLENGTH) {
          if (diff > MAXLENGTH) {
            return new SequencePart(tmNums.get(i).getFrom(), tmNums.get(i)
                .getTo() + MAXLENGTH, "TM");
          } else {
            return new SequencePart(tmNums.get(i).getFrom(), tmNums.get(i + 1)
                .getFrom() - 1, "TM");
          }
        }
      }
    } else {
      if (i == 0) {
        if (tmNums.get(0).getFrom() > MINLENGTH) {
          if (tmNums.get(0).getFrom() > MAXLENGTH) {
            return new SequencePart(tmNums.get(0).getFrom() - MAXLENGTH,
                tmNums.get(0).getTo(), "TM");
          } else {
            return new SequencePart(1, tmNums.get(i).getTo(), "TM");
          }
        }
      } else {
        int diff = tmNums.get(i).getFrom() - 1 - tmNums.get(i - 1).getTo();
        if (diff > MINLENGTH) {
          if (diff > MAXLENGTH) {
            return new SequencePart(tmNums.get(i).getFrom() - MAXLENGTH,
                tmNums.get(i).getTo(), "TM");
          } else {
            return new SequencePart(tmNums.get(i - 1).getTo() + 1, tmNums
                .get(i).getTo(), "TM");
          }
        }
      }
    }

    return null;
  }

  public boolean hasTmOrientationInfo() {
    if (tmNums.size() > 0 && allDomains.size() > tmNums.size()) {
      return true;
    }
    return false;
  }
  
}
