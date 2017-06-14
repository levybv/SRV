<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.cargo.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idCargo=-1;
	var idCargoF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Cargos</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">Código:</label>
                                	<input id="idCargoF" name="idCargoF" value="<c:out value='${pesquisaCargoVO.idCargo}'/>" class="campo2" maxlength="4" style="width: 50px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente" style="width:34%">
 	                        		<label class="label">Descrição:</label>
                                	<input id="descricaoF" name="descricaoF" value="<c:out value='${pesquisaCargoVO.descricaoCargo}'/>" class="campo2" maxlength="100" style="width: 250px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">Classe Hay:</label>
									<select id="idClasseHayF" name="idClasseHayF" class="campo">
										<option value="" selected>[TODOS]</option>
										<option value="-1" <c:if test="${pesquisaCargoVO.idClasseHay == -1}">selected</c:if>>[VAZIO]</option>
	                                   	<c:forEach var="itemClasseHay" items="${listaClasseHayVO}">
	                                   		<option value="<c:out value='${itemClasseHay.idClasseHay}'/>" <c:if test="${itemClasseHay.idClasseHay == pesquisaCargoVO.idClasseHay}">selected</c:if> ><c:out value='${itemClasseHay.idClasseHay} - ${itemClasseHay.descricao}'/></option>
										</c:forEach>
									</select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">Agrupa Filial:</label>
									<select id="agrupaFilialF" name="agrupaFilialF" class="campo">
										<option value="" selected>[TODOS]</option>
										<option value="S" <c:if test="${pesquisaCargoVO.agrupaFiliais == true}">selected</c:if>>Sim</option>
										<option value="N" <c:if test="${pesquisaCargoVO.agrupaFiliais == false}">selected</c:if>>N&atilde;o</option>
									</select>
                                </td>
 	                        	<td class="componente" style="width:34%">
 	                        		<label class="label">Grupo Remunera&ccedil;&atilde;o:</label>
									<select id="idGrupoRemuneracaoF" name="idGrupoRemuneracaoF" class="campo">
										<option value="" selected>[TODOS]</option>
										<option value="-1" <c:if test="${pesquisaCargoVO.idGrpRemVar == -1}">selected</c:if>>[VAZIO]</option>
	                                   	<c:forEach var="itemGrupoRemuneracaoVariavel" items="${listaGrupoRemuneracaoVariavelVO}">
	                                   		<option value="<c:out value='${itemGrupoRemuneracaoVariavel.idGrupoRemuneracao}'/>" <c:if test="${itemGrupoRemuneracaoVariavel.idGrupoRemuneracao == pesquisaCargoVO.idGrpRemVar}">selected</c:if> ><c:out value='${itemGrupoRemuneracaoVariavel.idGrupoRemuneracao} - ${itemGrupoRemuneracaoVariavel.descricaoOnline}'/></option>
										</c:forEach>
									</select>
                                </td>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label"></label>
                                	
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
					Cargos
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" />
					</c:if>
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaCargoVO" name="listaCargoVO" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="text-align: center; width:5%;"><input type="radio" name="checkbox" id="checkbox" value="${listaCargoVO.idCargo}"/></display:column>
					<display:column property="idCargo" title="C&oacute;digo" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;" /> 
					<display:column property="descricaoCargo" title="Descri&ccedil;&atilde;o" style="cursor: pointer; vertical-align: middle; width:30%;" />
					<display:column property="descricaoClasseHay" title="Classe Hay" style="text-align: center; cursor: pointer; vertical-align: middle; width:13%;" /> 					
					<display:column property="agrupaFiliaisFormatado" title="Agrupa Filiais" style="text-align: center; cursor: pointer; vertical-align: middle; width:7%;" /> 
					<display:column property="descricaoTodosGrpRemVar" title="Grupo Remunera&ccedil;&atilde;o" style="cursor: pointer; vertical-align: middle;" />
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>