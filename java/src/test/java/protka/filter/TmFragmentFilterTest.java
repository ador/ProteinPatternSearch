package protka.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import protka.Protein;

public class TmFragmentFilterTest {

  @Test
  public void testFilter1() {
    TmFragmentFilter tmFilter = new TmFragmentFilter();
    assertNotNull(tmFilter);
    tmFilter.setMinExtracellularLength(10);
    tmFilter.setMaxExtracellularLength(20);
    assertEquals(10, tmFilter.getMinExtracellularLength());
    assertEquals(20, tmFilter.getMaxExtracellularLength());    
  }
}
