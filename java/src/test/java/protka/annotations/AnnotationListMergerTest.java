package protka.annotations;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import protka.annotations.Annotation;
import protka.annotations.Annotation.Type;

public class AnnotationListMergerTest {

  List<Annotation> parts1; 
  List<Annotation> parts2;
  List<Annotation> parts3;
  
  @Before
  public void setUp() {
    parts1 = new ArrayList<Annotation>();
    parts2 = new ArrayList<Annotation>();
    parts3 = new ArrayList<Annotation>();
  }
  
  @Test
  public void test1() {
    AnnotationListMerger merger = new AnnotationListMerger();
    // fill lists with data
    parts1.add(new Annotation(Type.UNKNOWN, 11, 20));
    parts1.add(new Annotation(Type.UNKNOWN, 31, 40));
    
    parts2.add(new Annotation(Type.UNKNOWN, 1, 10));
    parts2.add(new Annotation(Type.UNKNOWN, 21, 30));
    parts2.add(new Annotation(Type.UNKNOWN, 41, 50));

    
    List<List<Annotation> > listOflists =
        new ArrayList<List<Annotation> >();
    listOflists.add(parts1);
    listOflists.add(parts2);
    
    List<Annotation> result = merger.mergeParts(listOflists);
    
    assertEquals(1, result.get(0).from);
    assertEquals(11, result.get(1).from);
    assertEquals(21, result.get(2).from);
    assertEquals(31, result.get(3).from);
    assertEquals(41, result.get(4).from);
  }

  @Test
  public void test2() {
    AnnotationListMerger merger = new AnnotationListMerger();
    // fill lists with data
    parts1.add(new Annotation(Type.UNKNOWN, 11, 20));
    parts1.add(new Annotation(Type.UNKNOWN, 51, 60));
    parts1.add(new Annotation(Type.UNKNOWN, 81, 90));
    
    parts2.add(new Annotation(Type.UNKNOWN, 1, 10));
    parts2.add(new Annotation(Type.UNKNOWN, 71, 80));
    parts2.add(new Annotation(Type.UNKNOWN, 91, 100));

    parts3.add(new Annotation(Type.UNKNOWN, 21, 30));
    parts3.add(new Annotation(Type.UNKNOWN, 31, 40));
    parts3.add(new Annotation(Type.UNKNOWN, 41, 50));
    parts3.add(new Annotation(Type.UNKNOWN, 61, 70));
    
    List<List<Annotation> > listOflists =
        new ArrayList<List<Annotation> >();
    listOflists.add(parts1);
    listOflists.add(parts2);
    listOflists.add(parts3);
    
    List<Annotation> result = merger.mergeParts(listOflists);
    
    assertEquals(1, result.get(0).from);
    assertEquals(11, result.get(1).from);
    assertEquals(21, result.get(2).from);
    assertEquals(31, result.get(3).from);
    assertEquals(41, result.get(4).from);
    assertEquals(51, result.get(5).from);
    assertEquals(61, result.get(6).from);
    assertEquals(71, result.get(7).from);
    assertEquals(81, result.get(8).from);
    assertEquals(91, result.get(9).from);
  }

}
