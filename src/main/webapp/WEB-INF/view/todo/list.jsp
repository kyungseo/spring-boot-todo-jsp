<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%-- --------------------------------------------------------------------------
     [KYUNGSEO.PoC] Todo App - Spring Boot and JSP Sample Project

       @version v0.1.0
       @link https://github.com/kyungseo/spring-boot-todo-jsp
       Copyright (c) 2023 박경서 (Kyungseo.Park@gmail.com)
       License Apache 2.0
-------------------------------------------------------------------------- --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<tiles:insertDefinition name="defaultTemplate">
  <tiles:putAttribute name="body">

    <div class="container">

      <div class="card">
        <div class="card-body">
          <h5 class="card-title"><spring:message code="todo.page.list.title" /></h5>
          <div class="col-md-12">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th width="45%" class="text-center"><spring:message code="todo.description.label" /></th>
                  <th width="15%" class="text-center"><spring:message code="todo.targetdate.label" /></th>
                  <th width="15%" class="text-center"><spring:message code="todo.isdone.label" /></th>
                  <th width="25%" class="text-center"><spring:message code="todo.action.label" /></th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${todos}" var="todo">
                  <tr>
                    <td>
                      <a href="/todos/${todo.id}">${todo.description}</a>
                    </td>
                    <td class="text-center">
                      <fmt:parseDate value="${todo.targetDate}" pattern="yyyy-MM-dd" var="parseTargetDate" type="date" />
                      <fmt:formatDate value="${parseTargetDate}" pattern="yyyy-MM-dd" />
                    </td>
                    <td class="text-center">
                      <c:choose>
                        <c:when test="${todo.done}">completed</c:when>
                        <c:otherwise>pending...</c:otherwise>
                      </c:choose>
                    </td>
                    <td class="text-center">
                      <a type="button" id="btnDone" class="btn btn-success" href="/todos/${todo.id}/done">
                        <spring:message code="todo.action.button.change" />
                      </a>
                      <a type="button" id="btnDelete" class="btn btn-danger" href="/todos/${todo.id}/delete">
                        <spring:message code="todo.action.button.delete" />
                      </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            <a type="button" class="btn btn-primary btn-md" href="/todos/new">
              <spring:message code="todo.action.button.add" />
            </a>
          </div>
        </div>
      </div>

    </div>

  </tiles:putAttribute>
</tiles:insertDefinition>
