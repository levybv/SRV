<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

	<display:table pagesize='50' id="listaPremFilial" name="listaPremFilial" class="table" requestURI="" decorator="">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<c:choose>
			<c:when test="${listaPremFilial.codIndic == null}">
				<display:column title="Loja<br>&nbsp;" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; vertical-align: middle;">
					<font color="blue"><b><c:out value="${listaPremFilial.descIndic}"/></b></font>
				</display:column>
				<display:column title="Meta" style="text-align: right;cursor: pointer; width: 15%; vertical-align: middle;">
				</display:column>
				<display:column title="Realizado <br/>Total" style="text-align: right;cursor: pointer; width: 15%; vertical-align: middle;">
				</display:column>
				<display:column title="% Atingimento <br/>(Realizado x Meta)" style="text-align: center;cursor: pointer; width: 15%; vertical-align: middle;">
				</display:column>
			</c:when>
			<c:otherwise>
				<display:column title="Loja<br>&nbsp;" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;">
					<c:out value="${listaPremFilial.idFilial}"/>
				</display:column>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; vertical-align: middle;">
					<c:out value="${listaPremFilial.codIndic}"/> - <c:out value="${listaPremFilial.descIndic}"/>
				</display:column>
				<display:column title="Meta" style="text-align: right;cursor: pointer; width: 15%; vertical-align: middle;">
					<c:out value="${listaPremFilial.metaFormatado}"/>
				</display:column>
				<display:column title="Realizado <br/>Total" style="text-align: right;cursor: pointer; width: 15%; vertical-align: middle;">
					<c:out value="${listaPremFilial.realzFormatado}"/>
				</display:column>
				<display:column title="% Atingimento <br/>(Realizado x Meta)" style="text-align: center;cursor: pointer; width: 15%; vertical-align: middle;">
					<c:out value="${listaPremFilial.realzXMetaFormatado}"/>
				</display:column>
			</c:otherwise>
		</c:choose>
	</display:table>
