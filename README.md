# CS 5010 Semester Project

This repo represents the coursework for CS 5010!

**Name:** Philopater Askander and Michael Eshak

**Email:** askander.p@northeastern.edu eshak.m@northeastern.edu

**Preferred Name:** Phil and Mikey



### About/Overview
The purpose of Milestone 4 was to create a graphical user interface with
the functionality that was developed from the previous three milestone
assignments. This was accomplished using java's JFrame library as well as
including features and functionality from java swing.


### List of Features
The features that are included with Milestone 4 include:
1. A welcome screen that contains instructions for starting the game and
   the developers of the game.
2. Load the text file directly from the GUI.
3. A visual of all the patients and staff members in their respected rooms.
4. The option to Register a patient.
5. The option to assign a clinical staff member to a patient.
6. The option to display a specific clinical patient's information/visit record.
7. The option to unassign a clinical staff member from a patient.
8. The option to discharge the patient.
9. The option to quit the game.
10. The option to assign a patient to a new room.
11. An about menu that contains the following:
    * Name of the clinic
    * Number of patients in the clinic
    * Number of active clinical staff members
    * Number of rooms

### How to Run
In order to run Milestone4.jar, which is included in the res folder, you must run the
following command in the command prompt:

cd /directory/with/JARfile java -jar Milestone4.jar

This will then prompt you to the welcome screen to begin the game. Instructions for
using the game are included below.

### How to Use the Program
When you run the JAR file, you will be greeted with the welcome page. From there,
you're going to click "file" from the menu on the top right and load in your clinic file.
Then you're be rediected to a visual representation of the clinic that conatins the patients
and clinical staff members in their respected rooms. At the menu, you have the option to preview
information about the room, refresh the room map, and the option to save lives. If you want to 
play the game, all the functionaliy is in the "save lives" tab of the menu. There are five 
actions you can do as the user:

1. Register a patient into the clinic.
2. Assign a clinical staff member to a patient.
3. Display the information/visit record of an existing patient.
4. Unassign a clinical staff member to a patient.
5. Discharge a patient from the clinic.

Furthermore, if you would like to move a patient to a new room, you can left click the 
room and click "add patient to room". If that room is available, that patient, along with
the clinical staff members assigned to them will be reassigned to that room.


### Example Runs
Within the res/ folder is the example run that contains a .png of running through all of the methods that were implemented in this milestone. It demonstrates the following:
1. Register a patient into the clinic.
2. Assign a clinical staff member to a patient.
3. Display the information/visit record of an existing patient.
4. Unassign a clinical staff member to a patient.
5. Discharge a patient from the clinic.

In the example run, we have a patient, Phil, who has been registered into the clinic due to his 40 degree celsius fever. He is assigned to Dr. Amy Angiush and is taken into the 
Triage where Dr. Amy Anguish gives him the necessary medical attention required to reduce his fever. Phil is then discharged from the clinic; however, finds himself back at the
clinic when he finds out that he has tested positive for COVID. Dr. Amy Anguish is once again assigned to Phil and she checks his medical history to determine if he has had COVID
previously. Upon investigation, she concludes that he hasn't had COVID previously, gave him the necessary medical attention, and discharged him. This concludes the example run.


### Design/Model Changes

The most significant design/model change involved displaying the clinic's room placement. We decided to replace Phil's design with Michael's design. The reason for this was because my design was not appearing in the format that we wanted it to, but Michael's design was so we added his design to the implementation.


### Assumptions

There were a few assumptions that were made when developing 
this program:
1. Occupations of the staff members could only be either a
   "receptionist", "physician", or "nurse".
   * The way my method is structured, it doesn't allow any
     other occupations to be registered as a clinical staff
2. Add Room is used for testing purposes only.
   * This was a method that was implemented to public access
     under the assumption that nobody needs to add a room.
     It's only use was for testing and nothing more. Users
     should not have the power to add rooms to the clinic.
3. The file being read is of the right format.
   * There are no verifications to identify if the format
     of the .txt file is of the correct format.


### Limitations

The functionality for this program works and functions 
exactly as intended, according to the list of requirements
from the assignment description.


### Citations

Be sure to cite your sources. A good guideline is if you take more than three lines of code from some source, you must include the information on where it came from. Citations should use proper [IEEE citation guidelines](https://ieee-dataport.org/sites/default/files/analysis/27/IEEE Citation Guidelines.pdf) and should include references (websites, papers, books, or other) for ***any site that you used to research a solution***. For websites, this includes name of website, title of the article, the url, and the date of retrieval**.** Citations should also include a qualitative description of what you used, and what you changed/contributed.



