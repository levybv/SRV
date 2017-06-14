<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

	<display:table pagesize='20' id="listaPremOper" name="listaPremOper" class="table" requestURI="" decorator="">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<c:choose>
			<c:when test="${listaPremOper.codIndic == 524 || listaPremOper.codIndic == 525 || listaPremOper.codIndic == 526 || listaPremOper.codIndic == 527}">
				<display:column property="idFilial" title="Loja<br>&nbsp;" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle; font-weight:bold;" />
				<display:column title="Funcion&aacute;rio" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.codFunc}"/> - <c:out value="${listaPremOper.nomeFunc}"/>
				</display:column>
				<display:column title="Cargo" style="text-align: left;cursor: pointer; width: 15%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.codCargo}"/> - <c:out value="${listaPremOper.descrCargo}"/>
				</display:column>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.codIndic}"/> - <c:out value="${listaPremOper.descIndic}"/>
				</display:column>
				<display:column title="Meta" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.metaFormatado}"/>
				</display:column>
				<display:column title="Realizado <br/>Total (Loja)" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.realzFormatado}"/>
				</display:column>
				<display:column title="% Atingimento <br/>(Realizado x Meta)" style="text-align: center;cursor: pointer; width: 10%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremOper.realzXMetaFormatado}"/>
				</display:column>
				<display:column title="Realizado Colaborador" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle; font-weight:bold;">
				</display:column>
			</c:when>
			<c:otherwise>
				<display:column property="idFilial" title="Loja<br>&nbsp;" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;" />
				<display:column title="Funcion&aacute;rio" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle;">
					<c:out value="${listaPremOper.codFunc}"/> - <c:out value="${listaPremOper.nomeFunc}"/>
				</display:column>
				<display:column title="Cargo" style="text-align: left;cursor: pointer; width: 15%; vertical-align: middle;">
					<c:out value="${listaPremOper.codCargo}"/> - <c:out value="${listaPremOper.descrCargo}"/>
				</display:column>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle;">
					<c:out value="${listaPremOper.codIndic}"/> - <c:out value="${listaPremOper.descIndic}"/>
				</display:column>
				<display:column title="Meta" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle;">
					<c:out value="${listaPremOper.metaFormatado}"/>
				</display:column>
				<display:column title="Realizado <br/>Total (Loja)" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle;">
					<c:out value="${listaPremOper.realzFilFormatado}"/>
				</display:column>
				<display:column title="% Atingimento <br/>(Realizado x Meta)" style="text-align: center;cursor: pointer; width: 10%; vertical-align: middle;">
					<c:out value="${listaPremOper.realzXMetaFormatado}"/>
				</display:column>
				<display:column title="Realizado Colaborador" style="text-align: right;cursor: pointer; width: 10%; vertical-align: middle;">
					<c:out value="${listaPremOper.realzFormatado}"/>
				</display:column>
			</c:otherwise>
		</c:choose>
	</display:table>
