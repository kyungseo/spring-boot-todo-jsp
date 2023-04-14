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
          <h5 class="card-title">${title}</h5>
          <div class="col-md-12">
            <c:forEach items="${todos}" var="todo">
              <div class="card">
                <div class="card-body">
                  <h5 class="card-title">
                    <fmt:parseDate value="${todo.targetDate}" pattern="yyyy-MM-dd" var="parseTargetDate" type="date" />
                    <fmt:formatDate value="${parseTargetDate}" pattern="yyyy-MM-dd" />
                  </h5>
                  <h6 class="card-subtitle mb-2 text-muted">
                    <c:choose>
                      <c:when test="${todo.done}">completed</c:when>
                      <c:otherwise>pending...</c:otherwise>
                    </c:choose>
                  </h6>
                  <p class="card-text">${todo.description}</p>
                </div>
              </div>
              <p>
            </c:forEach>
          </div>
        </div>
      </div>
      <p>

    </div>

  </tiles:putAttribute>
</tiles:insertDefinition>
