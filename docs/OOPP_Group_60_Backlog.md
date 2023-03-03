**STAKEHOLDERS:** (Identify Stakeholders)

- Task Board Admin: User that can create, delete or edit a board and change privileges of members.
- Task Board User: User that can interact with the board and has different privileges. (e.g. “Editing, viewing, changing privileges.”)

**TERMINOLOGY:** (Identify key terminology)

- Window: Same as the window in an html.
- Board: The collection of Task Lists grouped by some common environment (e. g. “Frontend” || “At Home Work”)
- Task List: A list of tasks grouped by some common trait (e. g. “TODO”)
- Task: An entity that represents an action that the users want done.
- Tag: An identifying trait that allows grouping of different tasks (e.g. “Urgent”)
- User: Someone that uses the app.
- Admin: is a user with more privileges

**BASIC REQUIREMENTS (Epic):**

As a User, I want to…

- Use an application to organize my tasks, so that I can optimize my workflow.
  - I can use a Board.
  - I can create a Task List on a board.
  - I can create tasks in a Task List.
- Have network support in the application that allows other people to join the board and help me so that I can finish my work faster and more efficiently.
  - Multiple people can connect to the Board.
- Have other users be able to edit the boards without having to register in the application so that they are less hesitant to join and help me.
  - Every user in the board can edit it.
- Have my board synchronized with all the other users, so that the needed work is consistent for all users of a board.
  - The board gets updated in real time for every user connected to it.
  - The information on the board is consistent for all users.
- Be able to create/delete/edit task lists, so that I can keep tasks logically organized relative to each other.
  - Have task lists
  - Have the ability to create, delete, and edit task lists
  - Have the ability to add tasks to task lists
- Be able to create/delete/edit tasks, so that I can represent real-life tasks that I need done.
  - Have tasks
  - Have the ability to create, delete, and edit tasks
- Be able to drag and drop tasks so that I can move them easily between task lists at any moment.
  - Have support for mouse input and dragging for task organization.

**MULTIBOARD  (Epic):**

As a User, I want to…

- Use multiple boards with different names or keys/id so that I can separate tasks for different environments I need work done in.
  - Have multiple boards
  - every board has a unique ID
  - Have the ability to connect to a particular board via its ID
  - Have named boards

As an Admin, I want to…

- Be able to create multiple boards which I can join later.
  - Have the ability to create boards
  - Boards are given unique ID
  - Have ID uniqueness validation
  - Boards can be given a name

**CARD DETAILS (Epic):**

As a User, I want to…

- Add descriptions to tasks, so that I can have a clearer picture for what exactly needs to be done.
  - Have descriptions in tasks
  - Have the ability to create, edit, or delete a description for a task
- Nest tasks, so that I can represent bigger, more complicated tasks as a set of subtasks.
  - Tasks can have sub-tasks inside of them (up to a maximum depth and count)

**TAG SUPPORT (Epic):**

As a User, I want to…

- Be able to assign a tag to a task (list) so that it can be sorted and searched for.
  - Have tags
  - Tasks can be assigned a tag.
  - Tasks can be searched for using their tag.

**CUSTOMIZATION (Epic):**

As a User, I want to…

- Customize the appearance of tasks, task lists, tags, etc. through back- and foreground colors, so that I can use subconscious color perception to further optimize the task prioritization.
  - Have color customization
  - Have the ability to change task and task list background and text colors
  - Have board themes

**KEYBOARD SHORTCUTS (Epic):**

As a User, I want to…

- Have different keyboard shortcuts to quickly do the most common actions, so that I can optimize my workflow.
  - Have the ability to use shortcuts
    - Create tasks
    - Delete tasks
    - Copy, cut, paste tasks
    - Navigate between tasks
    - Create a task list
    - Move tasks

As an Admin, I want to…

- Have different keyboard shortcuts that help me manage my boards
  - Have admin-specific shortcuts
    - Have a shortcut for creating a board
    - have a shortcut for deleting a board (with confirmation?)

**PASSWORD PROTECTION (Epic):**

As a User, I want to…

- Have a password protection feature so that I make sure the boards I use are secure from malicious actions.
  - Have password protected boards
  - Have a distinction between connecting to a board via its ID and getting editing permissions via the board’s password

As an Admin, I want to…

- Be able to add, edit and delete passwords from boards so that only the people I want can access and/or edit it.
  - Have the ability to create, edit, and delete a password for a board
