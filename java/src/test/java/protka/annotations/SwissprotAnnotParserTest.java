package protka.annotations;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import protka.CreateTestProteins;
import protka.Protein;

public class SwissprotAnnotParserTest {

  @Test
  public void test1() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    assertNotNull(parser); 
  }

  @Test
  public void test2() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p1 = testProteins.get(0);
    String annotStr = parser.parseAnnotStringFromProtein(p1);
    assertEquals("t:13-31;", annotStr); 
  }
  
  @Test
  public void test3() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p2 = testProteins.get(1);
    String annotStr = parser.parseAnnotStringFromProtein(p2);
    assertEquals("t:12-21;t:23-25;t:101-111;t:123-123;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p2);
    assertEquals(4, annot.getAnnotations().size());
  }

  @Test
  public void test4() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p3 = testProteins.get(2);
    String annotStr = parser.parseAnnotStringFromProtein(p3);
    assertEquals("i:88-239;t:240-250;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p3);
    assertEquals(2, annot.getAnnotations().size());
    assertTrue(annot.isValid());
    assertFalse(annot.isComplete(p3.getSequenceLength()));
  }

  @Test
  public void test5() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p4 = testProteins.get(3);
    String annotStr = parser.parseAnnotStringFromProtein(p4);
    assertEquals("o:40-66;t:67-87;i:88-130;t:131-151;o:152-182;t:183-203;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p4);
    assertEquals(6, annot.getAnnotations().size());
    assertTrue(annot.isValid());
    assertFalse(annot.isComplete(p4.getSequenceLength()));
  }

  @Test
  public void test6() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p8 = testProteins.get(7);
    String annotStr = parser.parseAnnotStringFromProtein(p8);
    assertEquals("i:1-~29;t:30-50;o:51-256;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p8);
    assertEquals(3, annot.getAnnotations().size());
    assertTrue(annot.isValid());
  }
  
  @Test
  public void test7() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p7 = testProteins.get(6);
    String annotStr = parser.parseAnnotStringFromProtein(p7);
    assertEquals("t:~12-21;t:23-~25;t:101-111;t:123-123;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p7);
    assertEquals(4, annot.getAnnotations().size());
  }


  @Test
  public void fuzzyTest1() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p7 = testProteins.get(6);
    TransmemAnnotation annot = parser.getAnnotations(p7);
    assertTrue(annot.getAnnotations().get(0).isFuzzy());
    assertTrue(annot.getAnnotations().get(0).hasFuzzyStart());
    assertFalse(annot.getAnnotations().get(0).hasFuzzyEnd());
    assertTrue(annot.getAnnotations().get(1).hasFuzzyEnd());
    assertTrue(annot.getAnnotations().get(1).isFuzzy());
  }

  @Test
  public void fuzzyTest2() {
    SwissprotAnnotParser parser = new SwissprotAnnotParser();
    List<Protein> testProteins = CreateTestProteins.getTestProteins();
    Protein p8 = testProteins.get(7);
    String annotStr = parser.parseAnnotStringFromProtein(p8);
    assertEquals("i:1-~29;t:30-50;o:51-256;", annotStr);
    TransmemAnnotation annot = parser.getAnnotations(p8);
    assertTrue(annot.getAnnotations().get(0).isFuzzy());
    assertTrue(annot.getAnnotations().get(0).hasFuzzyEnd());
    assertFalse(annot.getAnnotations().get(0).hasFuzzyStart());
  }
  
}
