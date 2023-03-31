# Rubric: Testing

The students are not supposed to submit a specific assignment, instead, you are supposed to look into their code base. You can share this rubric with the teams and ask them for pre-filled feedback until Friday.

### Coverage

Testing is an integral part of the coding activities. Unit tests cover all parts of the application (client, server, commons). Excellent teams will also pay attention to the (unit test) code coverage of crucial system components (according to the build result over all components).

- Good: All conceptual parts of the application have several automated tests (client, server, commons).

The server and commons folder have around 80% coverage of code with their tests. The client has about 50% but it does use Mockito to test classes using mocks. The following are some of the commits that increase coverage of code but the rest in the following points also do that.
Commit numbers:
aced16a7
c71834e9
b40fdd93
8dbe4741
a098ea7d
37578555
8b9811fe
63374e36
2d19a815
81b51066
d08783d9
a5170f4a
e589a5e1

### Unit Testing

Classes are tested in isolation. Configurable *dependent-on-components* are passed to the *system-under-test* to avoid integration tests (for example, to avoid running a database or opening REST requests in each test run).

- Excellent: Configurable subclasses are created to replace dependent-on-components in most of the tests.

We have configurable subclasses made to replace the database in all of the backend tests. A lot of these classes were introduced in commit c71834e9, and a lot of the unit testing was added in their own commits  27ff38f4, a5170f4a and d08783d9.

### Indirection

The project applies the test patterns that have been covered in the lecture on *Dependency Injection*. More specifically, the test suite includes tests for indirect input/output and behavior.

- Sufficient: The project contains at least one exemplary test that goes beyond asserting direct input/output of a system-under-test. For example, by asserting indirect input, indirect output, or behavior.

As we can see in the test classes for BoardCtrl, AddTaskCtrl, AddTaskListCtrl, and TaskListCtrl we can see that some of the methods are tested. In most cases it is using Mockito. We create stub tests using mocks that help us test the SUT’s. Take for instance the test in BoardCtrlTest “addTaskListToBoardReturnsTaskListCtrl” which creates a mock for a task list and also a spy to check the behavior of the methods that are being called.

The commits that include these tests are:
a098ea7d
37578555
8b9811fe
63374e36
2d19a815
81b51066

### Endpoint Testing

The REST API is tested through automated JUnit tests.

- Excellent: The project contains automated tests that cover regular and exceptional use of most endpoints.

The endpoints of BoardController and TaskController are tested thoroughly and TaskListController has most endpoints tested, with the tests added in commits b40fdd93, 8dbe4741 and e589a5e1.
