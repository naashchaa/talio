package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

import commons.Board;
import server.database.BoardRepository;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final Random random;
    private final BoardRepository repo;

    public BoardController(Random random, BoardRepository repo) {
        this.random = random;
        this.repo = repo;
    }

    @GetMapping(path = {"", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (board.getName() == null){
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("rnd")
    public ResponseEntity<Board> getRandom() {
        var boards = repo.findAll();
        var idx = random.nextInt((int) repo.count());
        return ResponseEntity.ok(boards.get(idx));
    }
}
