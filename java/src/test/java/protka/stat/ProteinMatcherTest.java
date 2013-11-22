package protka.stat;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import protka.Protein;

public class ProteinMatcherTest {

  @Test
  public void testSimpleMatch() {
    ProteinMatcher pm = new ProteinMatcher();
    pm.set("KW", "Transmembrane;");
    
    Protein p1 = new Protein();
    List<String> data = new ArrayList<String>();
    data.add("ID   234;");
    data.add("KW   alma1; korte1;");
    data.add("KW   barack1;");
    p1.setLines(data);

    Protein p2 = new Protein();
    List<String> data2 = new ArrayList<String>();
    data2.add("ID   236;");
    data2.add("KW   alma2; korte2; Transmembrane;");
    data2.add("KW   barack2; swd");
    p2.setLines(data2);

    boolean result1 = pm.match(p1);
    boolean result2 = pm.match(p2);
    assertFalse(result1);
    assertTrue(result2);
  }

  @Test
  public void testSequenceMatch() {
    Protein p1 = new Protein();
    List<String> data = new ArrayList<String>();
    data.add("ID   234;");
    data.add("KW   alma1; korte1;");
    data.add("KW   barack1;");
    data.add("SQ   info");
    data.add("     ALMAFFFAA");
    data.add("     CCDDCCDDCC");
    p1.setLines(data);

    Protein p2 = new Protein();
    List<String> data2 = new ArrayList<String>();
    data2.add("ID   236;");
    data2.add("KW   alma2; korte2; Transmembrane;");
    data2.add("KW   barack2; swd");
    data2.add("SQ   info2");
    data2.add("     AADDDGGGDDGAH");
    data2.add("     DCICA");
    p2.setLines(data2);

    assertEquals(0, ProteinMatcher.matchSequence("FFFFF", p1));
    assertEquals(1, ProteinMatcher.matchSequence("FFF", p1));
    assertEquals(4, ProteinMatcher.matchSequence("A", p1));
    assertEquals(1, ProteinMatcher.matchSequence("AADD", p2));
    assertEquals(1, ProteinMatcher.matchSequence("HD", p2));
    assertEquals(2, ProteinMatcher.matchSequence("DDG", p2));
  }

}
