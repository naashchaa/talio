package client.services;

import client.scenes.BoardCtrl;
import client.scenes.TaskListCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.TaskList;
import javafx.scene.Node;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BoardService {

    private final ServerUtils server;

    @Inject
    public BoardService (ServerUtils server) {
        this.server = server;
    }


    public void loadContents(BoardCtrl ctrl) {
        this.loadTaskLists(ctrl);
    }

    public void loadTaskLists(BoardCtrl ctrl) {
        this.removeTaskLists(ctrl);
        List<TaskList> taskLists = this.server.getTaskListsByParentBoard(ctrl.getBoard());
        for (TaskList list : taskLists) {
            ctrl.addTaskListToBoard(list);
        }
    }

    /** This method removes all task lists in a board.
     * @param ctrl The board controller to remove for
     */
    public void removeTaskLists(BoardCtrl ctrl) {
        for (Node node : ctrl.getListContainer().getChildren()) {
            if (!"buttonAddTaskList".equals(node.getId()))
                ((TaskListCtrl) node.getUserData()).disconnect();
        }
        while (ctrl.getListContainer().getChildren().size() > 1) {
            ctrl.getListContainer().getChildren().remove(0);
        }
    }

    /** This method updates the clientside remembered contexts when there are changes.
     * @param server The connection string to update for
     * @param boards the list of boards that contains the changed context
     */
    public void updateRememberedBoards(String server, List<Board> boards) {
        // get all known boards
        List<Board> knownBoards = this.server.getRememberedBoards();

        // check the given list against known boards and add or remove accordingly

        Set<Board> hashedKnownBoards = new HashSet<>(knownBoards);
        Set<Board> hashedPassedBoards = new HashSet<>(boards);

        // if sets are equal, do nothing
        if (hashedKnownBoards.equals(hashedPassedBoards))
            return;

        List<Board> difference = new LinkedList<>();

        // check for new boards
        if (!hashedKnownBoards.containsAll(hashedPassedBoards)) {
            // more boards were passed than what was known
            // boards hence have to be remembered
            difference.addAll(hashedPassedBoards);
            difference.removeAll(hashedKnownBoards);
            knownBoards.addAll(difference);
            difference.clear();
        }

        // check for forgotten boards
        if (!hashedPassedBoards.containsAll(hashedKnownBoards)) {
            // more boards were known than what was passed
            // boards hence have to be forgotten
            difference.addAll(hashedKnownBoards);
            difference.removeAll(hashedPassedBoards);
            knownBoards.removeAll(difference);
            difference.clear();
        }

        // after changes have been fetched, write the new context to file
        this.writeUpdatedKnownBoards(server, knownBoards);
    }

    /** This is a helper method that determines what changes have to be made
     * and then writes the updated context.
     * @param server The server connection string
     * @param knownBoards the list of known boards
     */
    public void writeUpdatedKnownBoards(String server, List<Board> knownBoards) {
        // parse existing contexts
        Map<String, List<Long>> contexts = this.parseAllContexts();

        // get a list of ids
        List<Long> ids = knownBoards.stream().map(Board::getId).collect(Collectors.toList());

        // modify the current one with the given list
        contexts.get(server).clear();
        contexts.get(server).addAll(ids);

        // write the contexts again
        this.writeContexts(contexts);
    }

    /** This method parses all known contexts into a simple to process structure
     * which consists of a map with the server connection string as the key
     * and the list of known boards as the value.
     * @return the map representation of all known contexts
     */
    public Map<String, List<Long>> parseAllContexts() {
        Map<String, List<Long>> contexts = new HashMap<>();
        try {
            Scanner contextsScanner = new Scanner(new File("config/contexts.txt"));
            contextsScanner.useDelimiter(System.lineSeparator() + System.lineSeparator());

            List<String> contextStrings = contextsScanner.tokens().collect(Collectors.toList());

            for (String context : contextStrings) {
                Scanner contextScanner = new Scanner(context);
                List<String> contents = contextScanner.tokens().collect(Collectors.toList());

                String server = contents.remove(0);
                List<Long> ids = new LinkedList<>();

                for (String id : contents) {
                    ids.add(Long.parseLong(id));
                }

                contexts.put(server, ids);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the contexts config file");
        }
        return contexts;
    }

    /** This method writes to the file the provided contexts data structure.
     * @param contexts the contexts to write
     */
    public void writeContexts(Map<String, List<Long>> contexts) {
        try {
            PrintWriter writer = new PrintWriter(
                new FileWriter("config/contexts.txt"));

            for (String server : contexts.keySet()) {
                writer.println(server);

                for (Long id : contexts.get(server)) {
                    writer.println(id);
                }

                writer.println();
            }

            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
