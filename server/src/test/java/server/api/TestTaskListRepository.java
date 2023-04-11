package server.api;

import commons.TaskList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.TaskListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestTaskListRepository implements TaskListRepository {

    public final List<TaskList> list = new ArrayList<>();


    @Override
    public List<TaskList> findAll() {
        return this.list;
    }

    @Override
    public List<TaskList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<TaskList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<TaskList> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public List<TaskList> findAllByParentBoard_Id(long id) {
        List<TaskList> list1 = new ArrayList<>();
        for (TaskList list : this.list) {
            if (list.getParentBoard().getId() == id)
                list1.add(list);
        }
        return list1;
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
    public void delete(TaskList entity) {
        this.list.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends TaskList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends TaskList> S save(S entity) {
        this.list.add(entity);
        entity.setId((long) this.list.size());
        return entity;
    }

    @Override
    public <S extends TaskList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TaskList> findById(Long aLong) {
        return this.list.stream().filter(b -> b.getId() == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        for (TaskList b : this.list){
            if (b.getId() == aLong){
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends TaskList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends TaskList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TaskList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TaskList getOne(Long aLong) {
        return null;
    }

    @Override
    public TaskList getById(Long aLong) {
        return this.list.get(aLong.intValue() - 1);
    }

    @Override
    public <S extends TaskList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TaskList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TaskList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends TaskList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TaskList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TaskList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TaskList, R> R findBy(Example<S> example,
                                            Function<FluentQuery.FetchableFluentQuery<S>,
                                                    R> queryFunction) {
        return null;
    }
}
