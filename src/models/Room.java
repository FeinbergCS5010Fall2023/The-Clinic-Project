package models;

import java.util.Arrays;
import java.util.Objects;

/**
 * The purpose of this class is to store the id, room type, and room name of each room.
 */

public class Room implements RoomFunctionality {
  private int[] id;
  private String roomType;
  private String roomName;
  private boolean status = false;

  /**
   * Execute a move in the position specified by the given row and column.
   *
   * @param id is an array that represents the unique id of the room
   * @param roomType is the type the room is.
   * @param roomName is the name of the room.
   */
  
  public Room(int[] id, String roomType, String roomName) {
    super();
    this.roomType = roomType;
    this.roomName = roomName;
    this.id = id;
  }
  
  /**
   * This is a getter function for RoomType.
   * @return the roomType.
   */
  
  public String getRoomType() {
    return roomType;
  }
  /**
   * This is a getter function for roomName.
   * @return the roomName.
   */
  
  public String getRoomName() {
    return roomName;
  }
  
  /**
   * This is a getter function for the room id.
   * @return the id of the room.
   */
  
  public int[] getId() {
    return id;
  }
  
  /**
   * This is a getter function for the status of the room's occupancy.
   * @return the status of the room's occupancy.
   */
  
  public boolean getStatus() {
    return status;
  }

  /**
   * This is a setter function for RoomType.
   * @param roomType is the new roomType.
   */
  
  public void setRoomType(String roomType) {
    this.roomType = roomType;
  }
  
  /**
   * This is a setter function for room name.
   * @param roomName is the new name of the room.
   */
  
  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
  
  /**
   * This is a setter function for the room id.
   * @param id is the new id for the room.
   */
  
  public void setId(int[] id) {
    this.id = id;
  }

  /**
   * This is a setter function for the room's status.
   * @param status is the new status.
   */
  
  public void setStatus(boolean status) {
    this.status = status;
  }
  /**
 * This method isolates the coordinates from the id that is used for graphing the room.
 * @param index is the index of the array
 * @return what is in that specific index.
 */
  
  public int getElement(int index) {
    if (index >= 0 && index < id.length) {
      return id[index];
    } else {
      throw new IndexOutOfBoundsException("Index is out of bounds");
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(id);
    result = prime * result + Objects.hash(roomName, roomType, status);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Room other = (Room) obj;
    return Arrays.equals(id, other.id) && Objects.equals(roomName, other.roomName)
        && Objects.equals(roomType, other.roomType) && status == other.status;
  }

  @Override
  public String toString() {
    return "Room: Room Type= " + roomType + ", Name of Room= " + roomName + ", Room ID= "
        + Arrays.toString(id) + ", Status of Room= " + status;
  }

}
