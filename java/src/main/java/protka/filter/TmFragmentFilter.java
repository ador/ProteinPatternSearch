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

  private String createHeader(Protein protein, SequencePart sp, char where) {
    String header = "> " + protein.getAcNum() + "|" + (sp.getFrom() + 1) + "-" + (sp.getTo() + 1) + 
          "|" + where; // where='a' means that the extracellular part is after the TM part; 'b' means before
    return header;
  }
  
  public List<FastaItem> getFragments(Protein protein) {
    Protein.setMinFragmentLength(minFragmentLen);
    Protein.setMaxFragmentLength(maxFragmentLen);
    List<SequencePart> partsAfter = protein.getExtracellularFragmentsAfterTmPart();
    List<SequencePart> partsBefore = protein.getExtracellularFragmentsBeforeTmPart();
    List<FastaItem> result = new ArrayList<FastaItem>();
    for (SequencePart sp : partsAfter) {
      if (null != sp) {
        String header = createHeader(protein, sp, 'a');
        FastaItem newItem = new FastaItem(header, protein.getAcNum());
        newItem.addSeqRow(protein.getSequencePart(sp.getFrom(), sp.getTo()));
        result.add(newItem);
      }
    }
    for (SequencePart sp : partsBefore) {
      if (null != sp) {
        String header = createHeader(protein, sp, 'b');
        FastaItem newItem = new FastaItem(header, protein.getAcNum());
        newItem.addSeqRow(protein.getSequencePart(sp.getFrom(), sp.getTo()));
        result.add(newItem);
      }
    }
    return result;
  }
}
