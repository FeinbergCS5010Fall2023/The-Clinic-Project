package models;

/**
 * This interface contains all of the public methods that will be used and defined
 * in the Room class.
 */

public interface RoomFunctionality {
  
  /**
   * This function gets the element for the id.
   * @param index is the index of the id array.
   * @return what the number at that index is.
   */
  
  public int getElement(int index);
}
