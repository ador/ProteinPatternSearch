package protka;

public class SequencePart {
  private final int from;
  private final int to;

  public SequencePart(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

}
