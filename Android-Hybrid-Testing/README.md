**Description**: Modern day mobile developers face incredibly competitive marketplaces.  Small bugs in applications can translate to immense losses in user share or, in the worst case, the abandonment of an app altogether.  External pressures such as frequent API platform updates, pressure for continuous updates from the marketplace, and platform and device fragmentation further complicate the mobile development process. As a response to some of these pressures, developers have increasingly turned to hybrid mobile applications, which rely on relative web interfaces rather than native Android GUI components.  However, currently developers lack automated testing support for hybrid mobile applications, despite the fact that a large body of research exists for their native counterparts. Current automated input generation approaches (e.g., Monkey, Dynodroid, MonkeyLab) for native mobile applications cannot be easily ported to hybrid apps due to difficulties in accurately extracting gui-widget information for the web components of these apps, as well as nuances related to interplay between javascript web code and native android java code.  In this project, we aim to overcome these challenges and implement the first systematic automated input generation approach for hybrid Android applications. This approach should be able to accurately extract information from the GUI of the web-components of hybrid android applications, and exercise these in a systematic manner through actions on the GUI.

Team Members: Liang Wu | Thomas Blackwell | Robert Xing | William Stalcup

**Technologies Used**: Java 8 Programming Language, UIAutomator (as part of the Android SDK), and Selendroid. 

Who did what:

- **Liang**:


- **Thomas**: Initial project design and setup. Developed method for extracting elements, developed method for automated execution of single inputs. Provided documentation for project setup and usage. Conducted research on Selendroid. 


- **Robert**: Developed most of the front end GUI for both screens, excluding file and directory chooser. Connected front end to back end. Researched and tested Appium as a viable alternative to Selendroid. Found methods to inject commands to AVDs through ADB. Developed all logging methods. Developed log replay tester. Decompiled several APKs to check compatibility with this application.


- **Will**:  Focused on input generation. Developed and wrote method for automated random input as well as the depth first method(developed the tester interface). Additionally worked on the first stage gui, adding in the multithreading for the server startup as well as file selection for the apk and input files.


+ We provided valuable support to each other through bugs large or small in the program!
