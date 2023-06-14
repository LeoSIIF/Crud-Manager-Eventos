<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="base-head.jsp"%>
</head>
<body>
    <%@include file="nav-menu.jsp"%>
    <div id="container" class="container-fluid">
        <h3 class="page-header">${not empty event ? 'Atualizar' : 'Cadastrar'} Evento</h3>

        <form action="${pageContext.request.contextPath}/event/${action}" method="POST">

            <input type="hidden" value="${event.getId()}" name="eventId">
            <div class="row">
                <div class="form-group col-md-6">
                    <label for="event_name">Nome do Evento</label>
                    <input type="text" class="form-control" id="event_name" name="event_name" 
                           autofocus="autofocus" placeholder="Nome do evento" 
                           required oninvalid="this.setCustomValidity('Por favor, informe o nome do evento.')"
                           oninput="setCustomValidity('')" value="${event.getEventName()}"/>
                </div>
            </div>

            <div class="row">
                <div class="form-group col-md-4">
                    <label for="date_start">Data do início</label>
                    <input type="date" class="form-control" id="date_start" name="date_start" 
                           autofocus="autofocus" placeholder="Data de início" 
                           required oninvalid="this.setCustomValidity('Por favor, informe a data de início do evento.')"
                           oninput="setCustomValidity('')" value="${event.getDateStart()}"/>
                </div>

                <div class="form-group col-md-4">
                    <label for="date_end">Data final</label>
                    <input type="date" class="form-control" id="date_end" name="date_end" 
                           autofocus="autofocus" placeholder="Data final"                    
                           oninvalid="this.setCustomValidity('Por favor, informe a data final do evento')"
                           oninput="setCustomValidity('')" value="${event.getDateEnd()}"/>
                </div>

                <div class="form-group col-md-6">
                    <label for="location">Localização</label>
                    <input type="text" class="form-control" id="location" name="location" 
                           autofocus="autofocus" placeholder="Localização" 
                           required oninvalid="this.setCustomValidity('Por favor, informe a localização do Evento.')"
                           oninput="setCustomValidity('')" value="${event.getLocation()}"/>
                </div>              
            </div>
            <div class="row">
                <div class="form-group col-md-6">
                    <label for="description">Descrição do Evento</label>
                    <input type="text" class="form-control" id="description" name="description" 
                           autofocus="autofocus" placeholder="Descrição" 
                           required oninvalid="this.setCustomValidity('Por favor, informe a Descrição do Evento.')"
                           oninput="setCustomValidity('')" value="${event.getDescription()}"/>
                </div>              

                <div class="form-group col-md-4">
                    <label for="user_id">Usuário</label>
                    <select id="user_id" class="form-control selectpicker" name="user_id" 
                            required oninvalid="this.setCustomValidity('Por favor, informe o usuário.')"
                            oninput="setCustomValidity('')">
                      <option value="" ${not empty event ? "" : 'selected' } >Selecione um usuario</option>
                      <c:forEach var="user" items="${users}">
                       <option value="${user.getId()}" 
                              ${event.getUser().getId() == user.getId() ? 'selected' : ""}>
                                  ${user.getName()}
                              </option>   
                      </c:forEach>
                    </select>
                </div>
            </div>

            <hr />
            <div id="actions" class="row pull-right">
                <div class="col-md-12">
                    
                    <a href="${pageContext.request.contextPath}/events" class="btn btn-default">Cancelar</a>
                    <button type="submit" class="btn btn-primary">${not empty event ? 'Atualizar' : 'Cadastrar'} Evento</button>
                </div>
            </div>
        </form>
    </div>
</body>
</html>