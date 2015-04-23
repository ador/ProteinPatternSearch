package protka.filter;

import java.util.ArrayList;
import java.util.List;

import protka.Protein;
import protka.SequencePart;


public class TmFragmentFilter {
  private int minExtracellularLen;
  private int maxExtracellularLen;

  public void setMinExtracellularLength(int len) {
    minExtracellularLen = len;
  }

  public void setMaxExtracellularLength(int len) {
    maxExtracellularLen = len;
  }

  public int getMinExtracellularLength() {
    return minExtracellularLen;
  }

  public int getMaxExtracellularLength() {
    return maxExtracellularLen;
  }

  public List<SequencePart> getFragments(Protein p){
    return new ArrayList<SequencePart>();
  }
}
