package site.yapp.study.todolist.api.todo.repository;

import org.springframework.stereotype.Repository;
import site.yapp.study.todolist.api.todo.domain.Todo;
import site.yapp.study.todolist.common.exception.NotFoundException;
import site.yapp.study.todolist.common.response.ErrorCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TodoRepository {

    private final Map<Long, Todo> todoList = new HashMap<>();

    public Todo findByIdOrThrow(Long id) {

        Todo todo = todoList.get(id);

        if (todo == null) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_TODO);
        }

        return todo;
    }

    public void save(Todo todo) {
        todoList.put(todo.getId(), todo);
    }

    public List<Todo> findAll() {
        List<Todo> results = new ArrayList<>();

        todoList.forEach((id, todo) -> {
                results.add(todo);
            }
        );

        return results;
    }

    public void delete(Todo todo) {
        todoList.remove(todo.getId(), todo);
    }
}
