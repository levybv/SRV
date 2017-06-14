<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript" src="js/ms/srv.tlmkt.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idCargo=-1;
	var idCargoF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Telemarketing</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">Per&iacute;odo:</label>
									<select id="periodoF" name="periodoF" class="campo">
										<option value="" selected>SELECIONE...</option>
	                                   	<c:forEach var="itemPeriodo" items="${periodoLista}">
	                                   		<option value="<c:out value='${itemPeriodo.periodo}'/>" <c:if test="${itemPeriodo.periodo == periodoFiltro}">selected</c:if> ><c:out value='${itemPeriodo.periodoDesc}'/></option>
										</c:forEach>
									</select>
                                </td>
 	                        	<td class="componente" style="width:34%">
                                </td>
 	                        	<td class="componente" style="width:33%">
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="submit" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Telemarketing
                    <c:if test="${'true' == stBtAlterar}">
						<input id="btIncluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
					<br/>
				</div>

				<display:table pagesize='10' id="listaTlmktVO" name="listaTlmktVO" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column property="ano" title="Ano" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;" />
					<display:column property="mes" title="M&ecirc;s" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;" />
					<display:column title="Funcion&aacute;rios Eleg&iacute;veis" style="text-align: center; cursor: pointer; vertical-align: middle;">
						<c:forEach var="itemFunc" items="${listaTlmktVO.listaFuncionarioVO}">
							<c:out value='${itemFunc.idFuncionario}'/> - <c:out value='${itemFunc.nomeFuncionario}'/></br>
						</c:forEach>
					</display:column>
					<display:column title="Indicador (Meta/Realizado)" style="text-align: center; cursor: pointer; vertical-align: middle;">
						<c:forEach var="itemIndic" items="${listaTlmktVO.listaIndicadorTlmktVO}">
							<c:out value='${itemIndic.codIndicador}'/> - <c:out value='${itemIndic.nomeIndicador}'/> (Meta: <fmt:formatNumber type="number" maxFractionDigits="3" value="${itemIndic.numMeta}"/> / Realizado: <fmt:formatNumber type="number" maxFractionDigits="3" value="${itemIndic.numRealizado}"/>)</br>
						</c:forEach>
					</display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>