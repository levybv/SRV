<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.escala.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >

	//chave
	var idEscala	=-1;
	
	//Filtro
	var idEscalaF		="";
	var descricaoF		="";
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Escalas</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	
                            
                        	<tr>
                               
 	                        	<td class="componente" width="20%">
 	                        		<label class="label" for="filialDesc">Código:</label>
 	                        		<input id="idEscalaF" name="idEscalaF" value="<c:out value='${idEscalaF}'/>" class="campo2" maxlength="6" style="width: 100px;" value="" type="text"/>
 	                        	</td>
 	                        	
 	                        	<td class="componente" width="20%">
 	                        		<label class="label" for="filialDesc">Nº Escala:</label>
 	                        		<input id="numEscalaF" name="numEscalaF" value="<c:out value='${numEscalaF}'/>" class="campo2" maxlength="6" style="width: 50px;" value="" type="text"/>
 	                        	</td>
 	                        	
 	                        	<td class="componente" width="60%">
 	                        		<label class="label" for="filialDesc">Descrição:</label>
 	                        		<input id="descricaoF" name="descricaoF" value="<c:out value='${descricaoF}'/>" class="campo2" maxlength="100" style="width: 300px; margin-right:50px;" value="" type="text"/>
 	                        	</td>
 	                        	
                            </tr> 
                          
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="button" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Escalas
                    <c:if test="${'true' == stBtRemover}">
						<input id="excluir" type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					</c:if>
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
					<input id="faixas" type="button" class="buttonAtualizar" style="float:right" value="Faixas" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="escalas" name="escalas" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:6%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${escalas.idEscala}"></display:column>
					<display:column property="idEscala" title="Código" style="cursor: pointer; text-align:center; vertical-align: middle; width:6%; "/> 
					<display:column property="numEscala"                 title="Nº Escala"       style="width:8%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoEscala" title="Descrição" style="cursor: pointer; vertical-align: middle; width:22%; "/> 
					<display:column property="descricaoIndicador" title="Indicador" style="cursor: pointer; vertical-align: middle; width:22%;" /> 
					<display:column property="descricaoGrupoIndicador" title="Grupo Indicador" style="cursor: pointer; vertical-align: middle; width:22%; "/> 
					<display:column property="descricaoGrupoRemVar" title="Grupo Remuneração Variável" style="width:22%; text-align:left; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>