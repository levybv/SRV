<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

	<display:table pagesize='20' id="listaPremLider" name="listaPremLider" class="table" requestURI="" decorator="">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<c:choose>
			<c:when test="${listaPremLider.codIndic == 524 || listaPremLider.codIndic == 525 || listaPremLider.codIndic == 526 || listaPremLider.codIndic == 527}">
				<display:column title="Loja<br>&nbsp;" property="idFilial" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle; font-weight:bold;"/>
				<display:column title="Funcion&aacute;rio" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremLider.codFunc}"/> - <c:out value="${listaPremLider.nomeFunc}"/>
				</display:column>
				<display:column title="Cargo" style="text-align: left;cursor: pointer; width: 20%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremLider.codCargo}"/> - <c:out value="${listaPremLider.descrCargo}"/>
				</display:column>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle; font-weight:bold;">
					<c:out value="${listaPremLider.codIndic}"/> - <c:out value="${listaPremLider.descIndic}"/>
				</display:column>
				<display:column title="Meta" property="metaFormatado" style="text-align: right;cursor: pointer; vertical-align: middle; font-weight:bold;"/>
				<display:column title="Realizado" property="realzFormatado" style="text-align: right;cursor: pointer; vertical-align: middle; font-weight:bold;"/>
				<display:column title="Realizado <br>x Meta" property="realzXMetaFormatado" style="text-align: center;cursor: pointer; vertical-align: middle; font-weight:bold;"/>
			</c:when>
			<c:otherwise>
				<display:column title="Loja<br>&nbsp;" property="idFilial" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
				<display:column title="Funcion&aacute;rio" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle;">
					<c:out value="${listaPremLider.codFunc}"/> - <c:out value="${listaPremLider.nomeFunc}"/>
				</display:column>
				<display:column title="Cargo" style="text-align: left;cursor: pointer; width: 20%; vertical-align: middle;">
					<c:out value="${listaPremLider.codCargo}"/> - <c:out value="${listaPremLider.descrCargo}"/>
				</display:column>
				<display:column title="Indicador" style="text-align: left;cursor: pointer; width: 25%; vertical-align: middle;">
					<c:out value="${listaPremLider.codIndic}"/> - <c:out value="${listaPremLider.descIndic}"/>
				</display:column>
				<display:column title="Meta" property="metaFormatado" style="text-align: right;cursor: pointer; vertical-align: middle;"/>
				<display:column title="Realizado" property="realzFormatado" style="text-align: right;cursor: pointer; vertical-align: middle;"/>
				<display:column title="Realizado <br>x Meta" property="realzXMetaFormatado" style="text-align: center;cursor: pointer; vertical-align: middle;"/>
			</c:otherwise>
		</c:choose>
	</display:table>
