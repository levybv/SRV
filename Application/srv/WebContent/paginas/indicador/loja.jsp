<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

<!--private static final int GRUPO_VENDAS = 2;
	private static final int GRUPO_PRODUTOS_SERVICOS_FINANCEIROS = 3;
	private static final int GRUPO_EMPRESTIMO_PESSOAL = 4;
	private static final int GRUPO_EMPRESTIMO_PESSOAL_SAX = 5;-->

	<display:table pagesize='15' id="lista" name="lista" class="table" requestURI="" decorator="">
		<display:setProperty name="paging.banner.placement" value="bottom" />
		<display:column property="idFilial"                                       title="Loja"             style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
		<display:column property="numMeta"           format="{0,number,#,##0}"    title="Meta"             style="text-align: right;cursor: pointer; width: 15%; vertical-align: middle;"/>
		<display:column property="numRealizado"      format="{0,number,#,##0}"    title="Realizado"        style="text-align: right;cursor: pointer; width: 20%; vertical-align: middle;"/>
		<display:column property="numRealizadoMeta"  format="{0,number,#,##0.00}" title="Realizado x Meta" style="text-align: right;cursor: pointer; width: 20%; vertical-align: middle;"/>
		<c:choose>
			<c:when test="${2 == grupoSelecionado}">
				<display:column property="vlrPremioFilial"           format="{0,number,#,##0.00}" title="Pr&ecirc;mio Meta"      style="text-align: right;cursor: pointer; width: 20%; vertical-align: middle;"/>
				<display:column property="vlrPremioFilialCalculado"  format="{0,number,#,##0.00}" title="Pr&ecirc;mio Calculado" style="text-align: right;cursor: pointer; width: 20%; vertical-align: middle;"/>
				<display:footer>
				  	<tr>
		  				<td style="font-weight:bold;text-align: left;">Total:</td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numMetaTotal")%></td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numRealizadoTotal")%></td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numRealizadoMetaTotal")%></td>
		  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("vlrPremioFilialTotal")%></td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("vlrPremioFilialCalculadoTotal")%></td>
		  			<tr>
			   </display:footer>
			</c:when>
			<c:otherwise>
				<display:column property="quantidadeRealizada"  title="Realizado Qtde" format="{0,number,#,##0}"  style="text-align: right;cursor: pointer; width: 20%; vertical-align: middle;"/>
				<display:footer>
				  	<tr>
				  		<td style="font-weight:bold;text-align: left;">Total:</td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numMetaTotal")%></td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numRealizadoTotal")%></td>
						<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numRealizadoMetaTotal")%></td>
		  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("quantidadeRealizadaTotal")%></td>
		  			</tr>
		  		</display:footer>
			</c:otherwise>
		</c:choose>
		<display:column title="" style="text-align: center;width: 1%"><a href="javascript:consultaDetalheCalculoLoja(${lista.idIndicador}, ${lista.idEmpresa}, ${lista.idFilial}, ${lista.ano}, ${lista.mes})"><img src="images/menu-consultas-ccm.png"/></a></display:column>
	</display:table>
	
