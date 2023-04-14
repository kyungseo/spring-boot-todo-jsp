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

package kyungseo.poc.todo.jsp.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kyungseo.poc.todo.jsp.payload.TodoDto;
import kyungseo.poc.todo.jsp.service.TodoService;
import kyungseo.poc.todo.jsp.service.TodoServiceData;
import kyungseo.poc.todo.jsp.web.validation.TodoValidator;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
@Controller
@RequestMapping("/todos")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@Autowired
    TodoValidator todoValidator;

	@GetMapping({"", "/"}) // 목록 조회
	public String getList(final HttpServletRequest request, final ModelMap model) {
		model.addAttribute("todos", todoService.getTodosByUser("kyungseo"));
		return "todo/list";
	}

	@GetMapping({"/new"}) // 단건 생성
	public String showAddTodoForm(Model model) {
		model.addAttribute("todo", new TodoDto());
		model.addAttribute("mode", "등록");
		return "todo/form";
	}

    @GetMapping("/{id}") // 단건 조회
	public String showUpdateTodoForm(@PathVariable("id") Long id,  final ModelMap model) {
		TodoDto todo = todoService.getTodoById(id);
		model.addAttribute("todo", todo);
        model.addAttribute("mode", "갱신");
		return "todo/form";
	}

    @PostMapping("/save")
	public String updateTodo(@Valid @ModelAttribute("todo") final TodoDto todo, final BindingResult result, final ModelMap model) {
        todoValidator.validate(todo, result);
        if (result.hasErrors()) {
            return "todo/form";
        }
		todoService.saveTodo(todo);
		return "redirect:/todos";
	}

    @GetMapping("/{id}/done") // 상태 변경
    public String updateIsDone(@PathVariable("id") final Long id, final ModelMap modell, RedirectAttributes redirectAttributes) {
        TodoDto todo = todoService.getTodoById(id);
        todo.setDone(!todo.isDone()); // isDone 토글
        todoService.saveTodo(todo);
        redirectAttributes.addFlashAttribute("message", "할일(" + id + ")의 상태가 갱신 되었습니다.");
        return "redirect:/todos";
    }

    //@DeleteMapping("/{id}")
    @GetMapping("/{id}/delete") // 단건 삭제
    public String deleteTodo(@PathVariable("id") final Long id, final ModelMap model, RedirectAttributes redirectAttributes) {
        todoService.deleteTodo(id);
        redirectAttributes.addFlashAttribute("message", "할일(" + id + ")가 삭제되었습니다.");
        return "redirect:/todos";
    }

    @GetMapping({"/dashboard"})
    public String dashboardAll(
            @RequestParam(value="category", defaultValue="all") final String category,
            final ModelMap model) {
        String title;
        TodoServiceData todoList = new TodoServiceData(todoService.getTodosByUser("kyungseo"));

        if (category.equals("today")) {
            title = "오늘의 할일 목록";
            model.addAttribute("todos", todoList.getAllTodayTodos());
        }
        else if (category.equals("done")) {
            title = "완료된 할일 목록";
            model.addAttribute("todos", todoList.getCompletedTodayTodos());
        }
        else if (category.equals("pending")) {
            title = "진행중인 할일 목록";
            model.addAttribute("todos", todoList.getPendingTodos());
        }
        else { // category == "all"
            title = "모든 할일 목록";
            model.addAttribute("todos", todoList.getAllTodos());
        }

        model.addAttribute("title", title);
        return "todo/dashboard";
    }

}
