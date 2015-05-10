package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class ArffWriterTest {

  @Test
  public void testWriteHeader() {
    OutputStream os = new ByteArrayOutputStream();
    try {
      ArffWriter arffWriter = new ArffWriter(os);
      arffWriter.writeHeader();
      String expected = "@relation 'protka'\n@attribute A real\n@attribute C real\n"
          + "@attribute D real\n@attribute E real\n@attribute F real\n"
          + "@attribute G real\n@attribute H real\n@attribute I real\n"
          + "@attribute K real\n@attribute L real\n@attribute M real\n"
          + "@attribute N real\n@attribute P real\n@attribute Q real\n"
          + "@attribute R real\n@attribute S real\n@attribute T real\n"
          + "@attribute V real\n@attribute W real\n@attribute Y real\n"
          + "@attribute Apiphatic real\n"
          + "@attribute HydroxylOrSulfurContaining real\n@attribute Cyclic real\n"
          + "@attribute Aromatic real\n@attribute Basic real\n"
          + "@attribute AcidicAndTheirAmide real\n@data\n";
      assertTrue(os.toString().equals(expected));
      arffWriter.closeOS();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteData() {
    OutputStream os = new ByteArrayOutputStream();
    try {
      ArffWriter arffWriter = new ArffWriter(os);
      arffWriter.writeData("4,2,4,0,1,3,9");
      String expected = "4,2,4,0,1,3,9\n";
      assertEquals(expected, os.toString());
      arffWriter.closeOS();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
