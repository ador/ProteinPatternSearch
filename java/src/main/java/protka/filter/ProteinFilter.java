package protka.filter;

import java.util.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import protka.Protein;

public class ProteinFilter {

  private int minSeqLen = 25;
  
  private List<Pair<String, String> > requiredPatterns = 
      new ArrayList<Pair<String, String> >();

  public void addPattern(String rowStart, String pattern) {
    requiredPatterns.add(new ImmutablePair<String, String>(rowStart, pattern));
  }

  public boolean match(Protein protein) {
    // check sequence length:
    int seqLen = protein.getSequence().length();
    if (seqLen < minSeqLen) {
      return false;
    }
    // check patterns
    for (Pair<String, String> entry : requiredPatterns) {
      boolean thisPatternMatches = false;
      for (String line : protein.getLines(entry.getKey(), "")) {
        String value = entry.getValue().toString();
        if (line.contains(value)) {
          thisPatternMatches = true;          
        }
      }
      if (!thisPatternMatches) {
        return false;
      }
    }
    return true;
  }

  public void setMinSeqLen(int minSeqLen) {
    this.minSeqLen = minSeqLen;
  }
}
