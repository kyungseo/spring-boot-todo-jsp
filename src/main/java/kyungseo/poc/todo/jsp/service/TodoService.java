/* ============================================================================
 * KYUNGSEO.PoC > Development Templates for building Web Apps
 *
 * Copyright 2023 Kyungseo Park <Kyungseo.Park@gmail.com>
 * ----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================= */

package kyungseo.poc.todo.jsp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kyungseo.poc.todo.common.exception.ResourceNotFoundException;
import kyungseo.poc.todo.jsp.payload.TodoDto;
import kyungseo.poc.todo.jsp.persistence.entity.Todo;
import kyungseo.poc.todo.jsp.persistence.repository.TodoRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
    private final ModelMapper modelMapper;

	public List<TodoDto> getTodosByUser(String username) {
		return todoRepository.findByUserName(username)
		        .stream()
		        .map(todo -> modelMapper.map(todo, TodoDto.class))
		        .collect(Collectors.toList());
	}

	public TodoDto getTodoById(long id) {
		return todoRepository.findById(id)
		        .map(todo -> modelMapper.map(todo, TodoDto.class))
		        .orElseThrow(() -> new ResourceNotFoundException("할일을 찾지 못했습니다."));
	}

	public void addTodo(String username, String description, LocalDate targetDate, boolean isDone) {
		todoRepository.save(new Todo(username, description, targetDate, isDone));
	}

	public void deleteTodo(long id) {
		Optional<Todo> todo = todoRepository.findById(id);
		if (todo.isPresent()) {
			todoRepository.delete(todo.get());
		}
	}

    public Long deleteTodosByUser(String username) {
        return todoRepository.deleteByUserName(username);
    }

	public void saveTodo(TodoDto todo) {
		todoRepository.save(modelMapper.map(todo, Todo.class));
	}

}