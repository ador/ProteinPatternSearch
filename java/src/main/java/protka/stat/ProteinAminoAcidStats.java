package protka.stat;

import protka.FastaItem;
import protka.Protein;

public class ProteinAminoAcidStats {

  private final String acids = "ACDEFGHIKLMNPQRSTVWY";
  private final String[] names = {
      "A",
      "C",
      "D",
      "E",
      "F",
      "G",
      "H",
      "I",
      "K",
      "L",
      "M",
      "N",
      "P",
      "Q",
      "R",
      "S",
      "T",
      "V",
      "W",
      "Y",
      "Apiphatic",
      "HydroxylOrSulfurContaining",
      "Cyclic",
      "Aromatic",
      "Basic",
      "AcidicAndTheirAmide" 
  };
  

  public String computeCsvStatsString(Protein protein) {
    String sequence = protein.getSequence();
    int[] stat = computeIntStats(sequence);
    return statToString(stat, ",");
  }

  public String[] computeStringStats(Protein protein) {
    String sequence = protein.getSequence();
    int[] stat = computeIntStats(sequence);
    int len = 26;
    String[] ret = new String[len];
    for (int i = 0; i< len; i++) {
      ret[i] = "" + stat[i];
    }
    return ret;
  }

  public String computeCsvStatsString(FastaItem fastaItem) {
    String sequence = fastaItem.getSequenceString();
    int[] stat = computeIntStats(sequence);
    return statToString(stat, ",");
  }

  public int[] computeIntStats(String sequence) {
    int[] stat = new int[26];
    for (int i = 0; i < sequence.length(); ++i) {
      char aminoAcid = sequence.charAt(i);
      int index = acids.indexOf(aminoAcid);
      if (index >= 0) {
        ++stat[index];
      }
      if ("AGILV".indexOf(aminoAcid) > -1) {
        ++stat[20];
      } else if ("CMST".indexOf(aminoAcid) > -1) {
        ++stat[21];
      } else if (aminoAcid == 'P') {
        ++stat[22];
      } else if ("FWY".indexOf(aminoAcid) > -1) {
        ++stat[23];
      } else if ("HKR".indexOf(aminoAcid) > -1) {
        ++stat[24];
      } else {
        ++stat[25];
      }
    }
    return stat;
  }
  
  private String statToString(int[] stat, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 25; ++i) {
      sb.append(stat[i]).append(separator);
    }
    sb.append(stat[25]);
    return sb.toString();
  }
  
  public String[] getNames() {
    return names;
  }

  public String[] getTypes() {
    String[] ret = new String[26];
    for (int i = 0; i < 26; i++) {
      ret[i] = "numeric";
    }
    return ret;
  }

}
