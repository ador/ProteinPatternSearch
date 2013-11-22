package protka.stat;

import protka.FastaItem;

public class FastaStats {

  private static final String acids = "ACDEFGHIKLMNPQRSTVWY";

  public String computeStats(FastaItem fastaItem) {
    String sequence = fastaItem.getSequenceString();
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

    String ret = "";
    for (int i = 0; i < 25; ++i) {
      ret += stat[i] + ",";
    }
    ret += Integer.toString(stat[25]);
    return ret;
  }

}
