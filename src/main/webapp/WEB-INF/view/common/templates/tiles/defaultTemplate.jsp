<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%-- --------------------------------------------------------------------------
     [KYUNGSEO.PoC] Todo App - Spring Boot and JSP Sample Project

       @version v0.1.0
       @link https://github.com/kyungseo/spring-boot-todo-jsp
       Copyright (c) 2023 박경서 (Kyungseo.Park@gmail.com)
       License Apache 2.0
-------------------------------------------------------------------------- --%>

<!DOCTYPE html>
<html lang="ko">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>[KYUNGSEO.PoC] Todo App - Spring Boot and JSP Sample Project</title>
  <!-- <link rel="icon" type="image/x-icon" href="/favicon.ico"> -->
  <script type="text/javascript" src="/vendor/jquery-3.5.1/jquery.min.js"></script>
  <!--/* Simple Sidebar - Core theme CSS (includes Bootstrap 5.2.3) */-->
  <link href="/css/styles.css" rel="stylesheet" />
  <!--/* Bootstrap core JS (5.2.3) */-->
  <script type="text/javascript" src="/vendor/bootstrap-5.2.3/js/bootstrap.bundle.min.js"></script>
  <!--/* datepicker */-->
  <script type="text/javascript" src="/vendor/bootstrap-datepicker-1.9.0/js/bootstrap-datepicker.min.js"></script>
  <link rel="stylesheet" href="/vendor/bootstrap-datepicker-1.9.0/css/bootstrap-datepicker.css">
  <!--/* Simple Sidebar - Core theme JS */-->
  <script src="/js/scripts.js"></script>
</head>

<body id="page-top">

  <div class="d-flex" id="wrapper">
    <!-- Sidebar-->
    <tiles:insertAttribute name="sidebar"/>
    <!-- Page content wrapper-->
    <div id="page-content-wrapper">
      <!-- Top navigation-->
      <tiles:insertAttribute name="navbar"/>
      <!-- Page content-->
      <div class="container-fluid">
        <p><br/></p>
        <tiles:insertAttribute name="body"/>
      </div>
    </div>
  </div>

</body>
</html>
