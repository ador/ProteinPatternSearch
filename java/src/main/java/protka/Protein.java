package protka;

import java.util.ArrayList;
import java.util.List;

public class Protein {

  private List<String> lines = new ArrayList<String>();
  private String sequence = "";
  public String acNum;
  private List<SequencePart> tmNums = null;
  private static int MINLENGTH;
  private static int MAXLENGTH;

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
  public List<String> getLines(String startsWith) {
    ArrayList<String> ret = new ArrayList<String>();
    for (String line : lines) {
      if (line.startsWith(startsWith)) {
        ret.add(line);
      }
    }
    return ret;
  }

  public String getSequence() {
    return sequence;
  }

  public List<SequencePart> getTmNumbers() {
    if (tmNums == null) {
      readTmNumbers();
    }
    return tmNums;
  }

  private void readTmNumbers() {
    List<String> ftLines = getLines("FT   TRANSMEM");
    tmNums = new ArrayList<SequencePart>();
    for (String ftLine : ftLines) {
      String fromStr = ftLine.substring(15, 20).trim();
      String toStr = ftLine.substring(21, 27).trim();
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
      tmNums.add(new SequencePart(from, to));
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
                .getTo() + MAXLENGTH);
          } else {
            return new SequencePart(tmNums.get(i).getFrom(), getSequence()
                .length());
          }
        }
      } else {
        int diff = tmNums.get(i + 1).getFrom() - 1 - tmNums.get(i).getTo();
        if (diff > MINLENGTH) {
          if (diff > MAXLENGTH) {
            return new SequencePart(tmNums.get(i).getFrom(), tmNums.get(i)
                .getTo() + MAXLENGTH);
          } else {
            return new SequencePart(tmNums.get(i).getFrom(), tmNums.get(i + 1)
                .getFrom() - 1);
          }
        }
      }
    } else {
      if (i == 0) {
        if (tmNums.get(0).getFrom() > MINLENGTH) {
          if (tmNums.get(0).getFrom() > MAXLENGTH) {
            return new SequencePart(tmNums.get(0).getFrom() - MAXLENGTH,
                tmNums.get(0).getTo());
          } else {
            return new SequencePart(1, tmNums.get(i).getTo());
          }
        }
      } else {
        int diff = tmNums.get(i).getFrom() - 1 - tmNums.get(i - 1).getTo();
        if (diff > MINLENGTH) {
          if (diff > MAXLENGTH) {
            return new SequencePart(tmNums.get(i).getFrom() - MAXLENGTH,
                tmNums.get(i).getTo());
          } else {
            return new SequencePart(tmNums.get(i - 1).getTo() + 1, tmNums
                .get(i).getTo());
          }
        }
      }
    }

    return null;
  }

}
