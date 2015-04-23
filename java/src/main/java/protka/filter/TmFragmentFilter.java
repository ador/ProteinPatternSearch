package protka.filter;

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

}
