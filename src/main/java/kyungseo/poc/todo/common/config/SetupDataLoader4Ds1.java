/* ============================================================================
 * KYUNGSEO.PoC > Development Templates for building Web Apps
 *
 * Copyright 2023 Kyungseo Park <Kyungseo.Park@gmail.com>
 *
 * Original Code: https://github.com/Baeldung/spring-security-registration
 * modified by Kyungseo Park
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

package kyungseo.poc.todo.common.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kyungseo.poc.todo.jsp.persistence.entity.Todo;
import kyungseo.poc.todo.jsp.persistence.repository.TodoRepository;

/**
 * @author 박경서 (Kyungseo.Park@gmail.com)
 * @version 1.0
 */
@Component
public class SetupDataLoader4Ds1 implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private TodoRepository todoRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) return;

        String username = "kyungseo";

        todoRepository.deleteByUserName(username); // 초기화

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate yesterday = today.minusDays(1);

        createTodo(username, yesterday, "자전거 수리 - 타이어 바람 넣기", true);
        createTodo(username, yesterday, "밀린 독서 하기 + 향긋한 커피 한 잔", false);
        createTodo(username, today, "KYUNGSEO.PoC 코드 작성하기", true);
        createTodo(username, today, "변경사항 GitHub에 Push하기", false);
        createTodo(username, tomorrow, "홍제천 따라 한강 주변 라이딩", false);

        alreadySetup = true;
    }

    @Transactional
    Todo createTodo(String username, LocalDate targetDate, String desc, boolean isDone) {
        Todo todo = new Todo();
        todo.setUserName(username);
        todo.setTargetDate(targetDate);
        todo.setDescription(desc);
        todo.setDone(isDone);

        todo = todoRepository.save(todo);
        return todo;
    }

}