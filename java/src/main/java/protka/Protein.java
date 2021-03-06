package protka;

import java.util.ArrayList;
import java.util.List;

public class Protein {

  private List<String> lines = new ArrayList<String>();
  private String sequence = "";
  private String acNum;

  private List<SequencePart> tmDomains;
  private List<SequencePart> inOutDomains;
  private List<SequencePart> funcDomains;
  private static int MIN_EXT_FRAGMENT_LEN = 40;
  private static int MAX_EXT_FRAGMENT_LEN = 70;
  private List<SequencePart> allDomains;
  private List<String> taxonomy;

  public String getAcNum() {
    return acNum;
  }

  public static void setMinFragmentLength(int length) {
    MIN_EXT_FRAGMENT_LEN = length;
  }

  public static void setMaxFragmentLength(int length) {
    MAX_EXT_FRAGMENT_LEN = length;
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
    sequence = "";
    boolean seqReading = false;
    for (String dataPart: data) {
      String[] linesRead = dataPart.split("\\r?\\n");
      for (String l : linesRead){
        if (seqReading && !l.startsWith("//")) {
          String seqPart = l.trim();
          String s2 = seqPart.replaceAll(" ", "");
          sequence = sequence + s2;
        }
        lines.add(dataPart);
        if (l.startsWith("SQ")) {
          seqReading = true;
        }
        if (l.startsWith("AC")) {
          String id = dataPart.split("   ")[1];
          acNum = id.split(";")[0];
        }
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

  public List<SequencePart> getTmDomains() {
    return tmDomains;
  }

  public List<SequencePart> getFuncDomains() {
    return funcDomains;
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
      // -1  because the data file indexes letters in strings starting with 1
      int from = Integer.parseInt(fromStr) - 1; 
      int to = Integer.parseInt(toStr) - 1;
      ret.add(new SequencePart(from, to, type));
    }
    return ret;
  }
  
  private void readDomains() {
    if (tmDomains == null || allDomains == null) {
      tmDomains = new ArrayList<SequencePart>();
      inOutDomains = new ArrayList<SequencePart>();
      funcDomains = new ArrayList<SequencePart>();
      allDomains = new ArrayList<SequencePart>();
    } else {
      tmDomains.clear();
      inOutDomains.clear();
      funcDomains.clear();
      allDomains.clear();
    }
    readTmDomains();
    readInOutDomains();
    readFuncDomains();
  }
  
  private void readTmDomains() {
    List<String> ftLines = getLines("FT   TRANSMEM" , "");
    tmDomains = parseDomainParts(ftLines, "TM");
    allDomains.addAll(tmDomains);
  }

  private void readFuncDomains() {
    List<String> ftLines = getLines("FT   DOMAIN" , "");
    if (ftLines.isEmpty()) { 
      return;
    }
    funcDomains = parseDomainParts(ftLines, "DOMAIN");
    allDomains.addAll(funcDomains);
  }

  private void readInOutDomains() {
    List<String> innerDomainLines = getLines("FT   TOPO_DOM", "Cytoplasmic");
    List<SequencePart> inDomains = parseDomainParts(innerDomainLines, "IN");
    List<String> outerDomainLines = getLines("FT   TOPO_DOM", "Extracellular");
    List<SequencePart> outDomains = parseDomainParts(outerDomainLines, "OUT");
    inOutDomains.addAll(inDomains);   
    inOutDomains.addAll(outDomains);
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

  // indexing goes from 0 ! returns inclusive
  public String getSequencePart(int from, int to) {
    String sequence = getSequence();
    if (to < sequence.length() && from >= 0 && from <= to) {
      String ret = sequence.substring(from, to + 1);
      return ret;
    }
    return null;
  }

  private SequencePart getForwardSection(int i) {
    int tmBeginning = tmDomains.get(i).getFrom();
    int tmEnding = tmDomains.get(i).getTo();
    // looking forwards from tm part
    int externalBeginning = tmEnding + 1;
    int externalEnding;
    if (i == tmDomains.size() - 1) { // the last part.. checking against the end of the whole sequence      
      externalEnding = getSequence().length() - 1;
    } else {
      externalEnding = tmDomains.get(i + 1).getFrom() - 1;
    }
    int diff = externalEnding - tmEnding;
    if (diff >= MIN_EXT_FRAGMENT_LEN) {
      if (diff > MAX_EXT_FRAGMENT_LEN) {
        return new SequencePart(tmBeginning, tmEnding + MAX_EXT_FRAGMENT_LEN, "MIXED");
      } else {
        return new SequencePart(tmBeginning, externalEnding, "MIXED");
      }
    }
    return null;
  }
  
  private SequencePart getBackwardSection(int i) {
    int tmBeginning = tmDomains.get(i).getFrom();
    int tmEnding = tmDomains.get(i).getTo();
    // looking backwards from tm part
    int externalBeginning;
    int externalEnding = tmBeginning - 1;
    if (i == 0) {
      // potentially beginning at the very beginning of the whole seq.
      externalBeginning = 0; 
    } else {
      externalBeginning = tmDomains.get(i - 1).getTo() + 1;
    }
    int diff = externalEnding - externalBeginning;
    if (diff >= MIN_EXT_FRAGMENT_LEN) {
      if (diff > MAX_EXT_FRAGMENT_LEN) {
        return new SequencePart(tmBeginning - MAX_EXT_FRAGMENT_LEN, tmEnding, "MIXED");
      } else {
        return new SequencePart(externalBeginning, tmEnding, "MIXED");
      }
    }
    return null;
  }

  public SequencePart getSeqForTmPart(int i) {
    getTmDomains();
    if (i >= 0 && i < tmDomains.size()) {
      return tmDomains.get(i);
    }
    return null;
  }

  public SequencePart getSeqForTmPlusExtraPartFwd(int i) {
    getTmDomains();
    if ((!beginsInside && i % 2 == 1 || beginsInside && i % 2 == 0)) {
      return getForwardSection(i);
    } else {
      return null;
    }
  }

  public SequencePart getSeqForTmPlusExtraPartBwd(int i) {
    getTmDomains();
    if ((!beginsInside && i % 2 == 0 || beginsInside && i % 2 == 1)) {
      return getBackwardSection(i);
    } else {
      return null;
    }
  }
  
  public List<SequencePart> getExtracellularFragmentsAfterTmPart() {
    getTmDomains();
    List<SequencePart> result = new ArrayList<SequencePart>();
    if (hasTmOrientationInfo()) {
      for (int i = 0; i < tmDomains.size(); i++) {
        SequencePart sp = getSeqForTmPlusExtraPartFwd(i);
        if (null != sp) {
          result.add(sp);
        }
      }
    }
    return result;
  }

  public List<SequencePart> getExtracellularFragmentsBeforeTmPart() {
    getTmDomains();
    List<SequencePart> result = new ArrayList<SequencePart>();
    if (hasTmOrientationInfo()) {
      for (int i = 0; i < tmDomains.size(); i++) {
        SequencePart sp = getSeqForTmPlusExtraPartBwd(i);
        if (null != sp) {
          result.add(sp);
        }
      }
    }
    return result;
  }
  
  public boolean hasTmOrientationInfo() {
    if (!inOutDomains.isEmpty()) {
      return true;
    }
    return false;
  }

  public SequencePart getNearestDomain(int posInSeq, int maxDist) {
    if (funcDomains.isEmpty()) {
      return null;
    }
    int minDist = maxDist;
    SequencePart ret = null;
    for (SequencePart sqp: funcDomains) {
      if (Math.abs(sqp.getFrom() - posInSeq) < minDist) {
        minDist = Math.abs(sqp.getFrom() - posInSeq);
        ret = sqp;
      }
      if (Math.abs(sqp.getTo() - posInSeq) < minDist) {
        minDist = Math.abs(sqp.getTo() - posInSeq);
        ret = sqp;
      }
    }
    if (minDist <= maxDist) {
      return ret;
    }
    return null;
  }

  public void readTaxonomy() {
    List<String> ocLines = getLines("OC", "");
    taxonomy = new ArrayList<String>();
    for (String line: ocLines) {
      String lineContent = line.substring(3);
      String[] sArray = lineContent.split(";");
      for (String s: sArray) {
        if (s.trim().length() > 0)
          taxonomy.add(s.trim());
      }
    }
  }
  
  public List<String> getTaxonomyList() {
    if (taxonomy == null) {
      readTaxonomy();
    }
    return taxonomy;
  }

  public String getSpecies() {
    List<String> osLines = getLines("OS", "");
    String species = "";
    for (String line: osLines) {
      String lineContent = line.substring(3).trim();
      species += lineContent;
    }
    return species;
  }
  
}
