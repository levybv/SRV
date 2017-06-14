<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="true" import="java.util.*"%>


<fmt:setLocale value="pt_BR" scope="request"/>
	<display:table pagesize='15' id="lista" name="lista" class="table" requestURI="" decorator="">     
		<display:setProperty name="paging.banner.placement" value="bottom" />   
		<display:column property="codFilial"  title="Loja" style="cursor: pointer; width: 5%; vertical-align: middle;text-align: center;" />  
		<display:column property="nomeUsuario" title="Nome" style="cursor: pointer; width: 5%; vertical-align: middle;"/>             
		<display:column property="cargoUsuario"  title="Cargo" style="cursor: pointer; width: 5%; vertical-align: middle;"/>             					
		<display:column property="dataAdimissao"  format="{0,date,dd/MM/yyyy}"  title="Data Admissão" style="text-align: center; cursor: pointer; width: 5%; vertical-align: middle;"/>
		<display:column property="dataDemissao"   format="{0,date,dd/MM/yyyy}"  title="Data Demissão" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
		<display:column property="meta"    title="Meta" style="text-align: right; cursor: pointer; width: 5%; vertical-align: middle;"/>		
		<display:column property="valorRealizado"   format="{0,number,#,##0.00}" title="Realizado" style="text-align: right; cursor: pointer; width: 5%; vertical-align: middle;"/>		
		<display:column property="numRealizadoMeta"   format="{0,number,#,##0.00}" title="Realizado X Meta" style="text-align: right;cursor: pointer; width: 5%; vertical-align: middle;"/>						
		<display:column property="premio"   format="{0,number,#,##0.00}" title="Pr&ecirc;mio" style="text-align: right;cursor: pointer; width: 5%; vertical-align: middle;"/>	
		<display:column title="" style="text-align: center;width: 1%"><a href="javascript:consultaDetalheCalculo(${lista.idFuncionario}, ${lista.idIndicador}, ${lista.idEmpresa}, ${lista.codFilial}, ${lista.ano}, ${lista.mes})"><img src="images/menu-consultas-ccm.png"/></a></display:column>
		<display:footer>
		  	<tr>
  				<td style="font-weight:bold;text-align: left;">Total:</td>
  				<td></td>
  				<td></td>
  				<td></td>
  				<td></td>
				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numMetaTotal")%></td>
  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("vlrRealizadoTotal")%></td>
  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("numRealizadoMetaTotal")%></td>
  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("premioTotal")%></td>
  			</tr>
	   </display:footer>
   </display:table>