import java.util.HashMap;
import java.util.Iterator;

public class DpllResult {

  private HashMap<String, Boolean> map;
  private long timeTaken;

  public DpllResult(HashMap<String, Boolean> map) {
    this.map = map;
  }

  public void setTimeTaken(long time) {
    this.timeTaken = time;
  }

  /**
   * Returns how many milliseconds the algorithm has taken
   * @return
   */
  public long getTimeTaken() {
    return timeTaken;
  }

  /**
   * Returns the DPLL result as a String
   * @return
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator iterator = map.keySet().iterator();
    while(iterator.hasNext()) {
      String literal = (String) iterator.next();
      boolean value = map.get(literal);
      sb.append(literal + " -> ");
      sb.append(value ? "1" : "0");
      if(iterator.hasNext()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  public HashMap<String, Boolean> getMap() {
    return map;
  }

}
