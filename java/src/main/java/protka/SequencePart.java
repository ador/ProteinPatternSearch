package protka;

public class SequencePart {
  private final int from;
  private final int to;
  private String type;  // TM, IN, OUT, DOMAIN, MIXED

  public SequencePart(int from, int to, String type) {
    this.from = from;
    this.to = to;
    this.type = type;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }
  
  public String getType() {
    return type;
  }

  public int getLength() {
    return to - from + 1;
  }
  
}
