package protka.annotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import protka.annotations.Annotation;

public class AnnotationListMerger {

  public List<Annotation> mergeParts(List<List<Annotation> > listOfLists) {
    List<Annotation> ret = new ArrayList<Annotation>();
    List<Iterator<Annotation> > iterList = new ArrayList<Iterator<Annotation> >();
    for (List<Annotation> list : listOfLists) {
      iterList.add(list.iterator());
    }
    
    Annotation[] lastItems = new Annotation[listOfLists.size()];
    // init lastItems
    int i = 0;
    for (Iterator<Annotation> iter: iterList) {
      if (iter.hasNext()) {
        lastItems[i] = iter.next();
      } else {
        lastItems[i] = null;
      }
      i++;
    }
    boolean hasMore = true;
    while (hasMore) {
      int minIdx = getMinIdx(lastItems);
      if (minIdx == -1) { // all null, done
        break;
      }
      // add this to result, then step iter, update lastItem
      ret.add(lastItems[minIdx]);
      if (iterList.get(minIdx).hasNext()) {
        Annotation nextItem = iterList.get(minIdx).next();
        lastItems[minIdx] = nextItem;
      } else {
        lastItems[minIdx] = null;
      }
    }
    
    return ret;
  }
  
  private int getMinIdx(Annotation[] items) {
    int minIdx = -1;
    int minVal = Integer.MAX_VALUE;
    for (int i = 0; i < items.length; i++) {
      Annotation sp = items[i];
      if (sp != null && sp.from < minVal) {
        minIdx = i;
        minVal = sp.from;
      }
    }
    return minIdx;
  }
  
}
