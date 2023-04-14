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
      <div class="row">

        <div class="col-md-6 col-md-offset-3 ">
          <div class="card">
            <div class="card-body">
              <h5 class="card-title"><spring:message code="todo.page.form.title" /> ${mode}</h5>
              <p/>
              <form:form method="POST" modelAttribute="todo" action="${pageContext.request.contextPath}/todos/save">
                <form:hidden path="id" />

                <fieldset class="form-group">
                  <form:label path="userName"><spring:message code="todo.username.label" /></form:label>
                  <form:input path="userName" type="text" class="form-control" required="required" value="kyungseo" />
                  <form:errors path="userName" cssClass="text-warning" />
                </fieldset>

                <fieldset class="form-group">
                  <form:label path="description"><spring:message code="todo.description.label" /></form:label>
                  <form:input path="description" type="text" class="form-control" required="required" />
                  <form:errors path="description" cssClass="text-warning" />
                </fieldset>

                <fieldset class="form-group">
                  <form:label path="targetDate"><spring:message code="todo.targetdate.label" /></form:label>
                  <form:input path="targetDate" type="date" class="form-control" required="required" placeholder="YYYY-MM-DD" />
                  <form:errors path="targetDate" cssClass="text-warning" />
                </fieldset>

                <p><br/></p>
                <button type="submit" class="btn btn-success"><spring:message code="todo.action.button.save" /></button>
              </form:form>
            </div>
          </div>
        </div>

      </div>
    </div>

  <%--
  <script>
    $('#targetDate').datepicker({
        format : 'yyyy-MM-dd'
    });
  </script>
  --%>

  </tiles:putAttribute>
</tiles:insertDefinition>
