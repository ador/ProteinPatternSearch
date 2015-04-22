package protka.annotations;

public class Annotation {
  public enum Type {
    TRANSMEM, INNER, OUTER, UNKNOWN, MIXED
  }

  // indexing from 1 to N
  public final int from;
  public final int to;
  public final Type type;
  private final boolean fuzzyStart;
  private final boolean fuzzyEnd;

  public Annotation(Type t, int from, int to) {
    this.from = from;
    this.to = to;
    this.type = t;
    this.fuzzyStart = false;
    this.fuzzyEnd = false;
  }

  public Annotation(Type t, int from, int to, 
      boolean fuzzyStart, boolean fuzzyEnd) {
    this.from = from;
    this.to = to;
    this.type = t;
    this.fuzzyStart = fuzzyStart;
    this.fuzzyEnd = fuzzyEnd;
  }

  public static Type charToType(char c) {
    switch (c) {
    case 'o': 
      return Type.OUTER;
    case 'i': 
      return Type.INNER;
    case 'h': 
      return Type.TRANSMEM;
    case 't': 
      return Type.TRANSMEM;
    default:
      return Type.UNKNOWN;
    }
  }
  
  public static char typeToChar(Type t) {
    if (t == Type.TRANSMEM) {
      return 't';
    } else if (t == Type.INNER) {
      return 'i';
    } else if (t == Type.OUTER) {
      return 'o';
    }
    return '?';
  }
  
  public static Type revIo(Type t) {
    if (t == Type.INNER) {
      return Type.OUTER;
    }
    if (t == Type.OUTER) {
      return Type.INNER;
    }
    return null;
  }

  public boolean isFuzzy() {
    return fuzzyStart || fuzzyEnd;
  }

  public boolean hasFuzzyStart() {
    return fuzzyStart;
  }

  public boolean hasFuzzyEnd() {
    return fuzzyEnd;
  }
}
