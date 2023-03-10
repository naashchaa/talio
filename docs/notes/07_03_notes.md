Tuesday 7th of March

13:47
Meeting started with everyone present

### **Backend check:**  
need to change unit classes to use java persistence + parentID  
Max did task implementation, Lucas did board implementation, Marit will do tasklist


13:53
### **Frontend check:**  
wireframe was made, and everyone agreed on the design  
idea arose to start on the heuristics availability report since we already have a prototype design but postponed till later

also some endpoints were made for testing purposes

14:01
### **Backlog discussion:**  
we missed client/server, we have to figure out how we will implement this (web sockets vs. endpoints)  
will have to use https and json, so most likely not websockets  
we didn't have the first 3 points of basic requirements because we didn't have server/client in mind

we'll have to think about how to implement task list, what to sort it on because hibernate/javax persistence was weird about mapping a list.
(This is solved using @ElementCollection or @OneToMany/@ManyToOne mapping)

have to change our idea about joining boards, will have to keep a local memory which boards have been joined by a user  
figure out if we want to just do a box with name/id or have a screenshot of the board like some other applications (google docs, trello, etc.)

we need to implement to have a description for a task from the start
  

14:20
### **Buddycheck:**  
deadline on friday  
give COMPREHENSIVE feedback (strength and weaknesses) to teammates  
it is anonymous


14:23
### **Miscellaneous points of interest:**  
start planning forward, use sprints  
use issues and milestones in gitlab

don't approve your own MR  
leave comments on others' MR + give feedback on their code (important)  
the bigger the MR, the more comments  
the people that approve a MR must also comment + code must be changed according to the instruction based on the discussion  
be careful with pipeline, check if it runs + check if test passes + check checkstyle

Checkstyle currently checks the template files which should either be changed to conform with them or suppressed

HCI report after the midterms, will be talked about later

backend meaningful MR is around 200 lines or something important for the project  
frontend meaningful MR is something like 1 scene

put the backlog requirements in the issues tab + milestones on git

no meeting next week (14/03/23) because of midterms, next meeting will be the 21st


14:31
Meeting adjourned