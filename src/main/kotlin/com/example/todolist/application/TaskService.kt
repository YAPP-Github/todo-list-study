package com.example.todolist.application

import com.example.todolist.application.model.CreateTaskCommand
import com.example.todolist.application.model.Page
import com.example.todolist.application.model.Pageable
import com.example.todolist.application.model.UpdateTaskCommand
import com.example.todolist.application.port.TaskRepository
import com.example.todolist.application.port.TaskUseCase
import com.example.todolist.domain.Task
import com.example.todolist.domain.TaskNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TaskService(
    private val taskRepository: TaskRepository
): TaskUseCase {
    override fun getTasks(pageable: Pageable): Page<Task> {
        return taskRepository.findAllOrderByCreatedAtAsc(pageable)
    }

    override fun getTask(uuid: UUID): Task {
        return taskRepository.findByUuidOrNull(uuid) ?: throw TaskNotFoundException()
    }

    override fun createTask(command: CreateTaskCommand): Task {
        val task = Task(command.title, command.description)
        return taskRepository.save(task)
    }

    override fun createTasksInBulk(count: Int): Int {
        val tasks = (1..count).map {
            Task("title $it", "description $it")
        }
        return taskRepository.saveInBatch(tasks)
    }

    override fun deleteTask(uuid: UUID) {
        val task = taskRepository.findByUuidOrNull(uuid) ?: throw TaskNotFoundException()
        taskRepository.delete(task)
    }

    override fun updateTask(uuid: UUID, command: UpdateTaskCommand): Task {
        val task = taskRepository.findByUuidOrNull(uuid) ?: throw TaskNotFoundException()
        task.update(command.title, command.description, command.isComplete)
        taskRepository.save(task)
        return task
    }
}