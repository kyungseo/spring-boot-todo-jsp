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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kyungseo.poc.todo.jsp.payload.TodoDto;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
public class TodoServiceData {

    List<TodoDto> myTodoList;

    public TodoServiceData(List<TodoDto> todos) {
        this.myTodoList = todos;
    }

    public List<TodoDto> getActiveTodayTodos() {
        LocalDate today = LocalDate.now();
        Predicate<TodoDto> pending = todo -> today.isEqual(todo.getTargetDate()) && !todo.isDone();

        return myTodoList
                .stream()
                .filter(pending)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getCompletedTodayTodos() {
        //LocalDate today = LocalDate.now();
        //Predicate<TodoDto> pending = todo -> (todo.getTargetDate()).isBefore(today.plusDays(1)) && todo.isDone();
        Predicate<TodoDto> pending = todo -> todo.isDone();

        return myTodoList
                .stream()
                .filter(pending)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getPendingTodos() {
        Predicate<TodoDto> pending = todo -> !todo.isDone();

        return myTodoList
                .stream()
                .filter(pending)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getAllTodayTodos() {
        LocalDate today = LocalDate.now();
        Predicate<TodoDto> pending = todo -> today.isEqual(todo.getTargetDate());

        return myTodoList
                .stream()
                .filter(pending)
                .collect(Collectors.toList());
    }

    public List<TodoDto> getAllTodos() {
        return myTodoList;
    }

}