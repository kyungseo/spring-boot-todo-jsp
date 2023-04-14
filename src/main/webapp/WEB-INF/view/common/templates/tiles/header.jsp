<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<%-- --------------------------------------------------------------------------
     [KYUNGSEO.PoC] Todo App - Spring Boot and JSP Sample Project

       @version v0.1.0
       @link https://github.com/kyungseo/spring-boot-todo-jsp
       Copyright (c) 2023 박경서 (Kyungseo.Park@gmail.com)
       License Apache 2.0
-------------------------------------------------------------------------- --%>

<div class="navbar navbar-inverse navbar-static-top">
    <div class="container">
        <a href="/" class="navbar-brand">Todo App</a>

        <button class="navbar-toggle" data-toggle="collapse" data-target=".navHeaderCollapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>

        <div class="collapse navbar-collapse navHeaderCollapse">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="/">Home</a></li>
                <li><a href="/todos">Todo</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Social Media <b class="caret"></b> </a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Twitter</a></li>
                        <li><a href="#">Facebook</a></li>
                        <li><a href="#">Google+</a></li>
                        <li><a href="#">Instagram</a></li>
                    </ul>
                </li>
                <li><a href="#">About</a></li>
                <li><a href="#contact" data-toggle="modal">Contact</a></li>
            </ul>
        </div>
    </div>
</div>
