package com.example.todoapp.controller;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.request.AddTodoRequest;
import com.example.todoapp.request.AddUserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private TodoRepository todoRepository;

    public UserController(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId){
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }
    @PostMapping
    public User addUser(@RequestBody AddUserRequest userRequest){
        User user=new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());

        return userRepository.save(user);
    }

    @PostMapping("/{userId}/todo")
    public void addTodo(@PathVariable Long userId, @RequestBody AddTodoRequest todoRequest){
        User user =userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        Todo todo=new Todo();
        todo.setContent(todoRequest.getContent());
        user.getTodoList().add(todo);

        todoRepository.save(todo);
        userRepository.save(user);

    }
    @PostMapping("/todo/{todoId}")
    public void toggleTodoCompleted(@PathVariable Long todoId){
        Todo todo= todoRepository.findById(todoId).orElseThrow(NoSuchElementException::new);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }
    @DeleteMapping("/{userId}/todo/{todoId}")
    public void deleteTodo(@PathVariable Long userId,@PathVariable Long todoId){
        User user=userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Todo todo= todoRepository.findById(todoId).orElseThrow(NoSuchElementException::new);
        user.getTodoList().remove(todo);
        todoRepository.delete(todo);

    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        User user=userRepository.findById(userId).orElseThrow((NoSuchElementException::new));
        userRepository.delete(user);
    }
}
