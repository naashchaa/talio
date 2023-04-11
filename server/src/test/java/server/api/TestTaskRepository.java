package server.api;

import commons.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTaskRepository implements TaskRepository {

    public final List<Task> list = new ArrayList<>();

    @Override
    public List<Task> findAllByParentTaskList_Id(long id) {
        List<Task> list1 = new ArrayList<>();
        for (Task task: this.list) {
            if (task.getParentTaskList().getId() == id)
                list1.add(task);
        }
        return list1;
    }

    @Override
    public List<Task> findAll() {
        return this.list;
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Task> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return this.list.size();
    }

    @Override
    public void deleteById(Long aLong) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getId() == aLong) this.list.remove(this.list.get(i));
        }
    }

    @Override
    public void delete(Task entity) {
        this.list.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Task> S save(S entity) {
        this.list.add(entity);
        entity.setId((long) this.list.size());
        return entity;
    }

    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long aLong) {
        return this.list.stream().filter(x -> x.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        for (Task task : this.list) {
            if (task.getId() == aLong)  return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Task getOne(Long aLong) {
        return null;
    }

    @Override
    public Task getById(Long aLong) {
        return this.list.get(aLong.intValue() - 1);
    }

    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Task, R> R findBy(Example<S> example,
                    Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
