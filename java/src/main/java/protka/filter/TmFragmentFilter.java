package protka.filter;

import java.util.ArrayList;
import java.util.List;

import protka.Protein;
import protka.SequencePart;
import protka.FastaItem;

public class TmFragmentFilter {
  private int minFragmentLen;
  private int maxFragmentLen;
  
  public void setMinFragmentLength(int len) {
    minFragmentLen = len;
  }

  public void setMaxFragmentLength(int len) {
    maxFragmentLen = len;
  }

  public int getMinFragmentLength() {
    return minFragmentLen;
  }

  public int getMaxFragmentLength() {
    return maxFragmentLen;
  }

  public List<FastaItem> getFragments(Protein protein) {
    Protein.setMinFragmentLength(minFragmentLen);
    Protein.setMaxFragmentLength(maxFragmentLen);
    List<SequencePart> parts = protein.getExtracellularFragmentsAfterTmPart();
    List<FastaItem> result = new ArrayList<FastaItem>();
    for (SequencePart sp : parts) {
      if (null != sp) {
        String header = "> " + protein.getAcNum() + "|" + (sp.getFrom() + 1) + "-" + (sp.getTo() + 1);
        FastaItem newItem = new FastaItem(header, protein.getAcNum());
        newItem.addSeqRow(protein.getSequencePart(sp.getFrom(), sp.getTo()));
        result.add(newItem);
      }
    }
    return result;
  }
}
