package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import commons.Board;
import server.database.BoardRepository;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;

    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * @return a list of all boards in the repository
     */
    @GetMapping(path = {"", "/" })
    public List<Board> getAll() {
        return this.repo.findAll();
    }

    /**
     * @param id of the board to be gotten
     * @return the ResponseEntity with the board
     */
    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.repo.findById(id).get());
    }

    /**
     * Adds a board to the repository.
     * @param board to be added
     * @return ResponseEntity
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        //TODO: long polling / websockets syncing clients

        if (board.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        Board saved = this.repo.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * @param id of the board to be deleted
     * @return ResponseEntity
     */
    //delete mapping
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        //TODO: long polling / websockets syncing clients
        if (id < 0 || !this.repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        this.repo.deleteById(id);
        return ResponseEntity.ok("delete successful");
    }

    /**
     * @param board to be updated
     * @return ResponseEntity
     */
    @PostMapping(path = {"/update"})
    //TODO: long polling / websockets syncing clients
    public ResponseEntity<Board> update(@RequestBody Board board) {
        if (board.getId() < 0 || !this.repo.existsById(board.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Board saved = this.repo.findById(board.getId()).get();
        saved.setName(board.getName());

        this.repo.save(saved);
        return ResponseEntity.ok(saved);
    }
}