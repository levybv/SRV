<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" >

	//chave
	var idEscala				= <c:out value="${idEscalaF}"/>;
	var sequencial				=-1;
	var idUnidadeFaixa			= null;
	var faixaInicialFormatada	= null;
	var faixaFinalFormatada		= null;
	var idUnidadeRealizado		= null;
	var realizadoFormatado		= null;
	
	//Filtro
	var idEscalaF		="";
	var descricaoF		="";
	
	var alteracoesPendentes = <c:out value="${alteracoesPendentes}"/>;
	
</script> 
<script type='text/javascript' src='srvdwr/interface/EscalaBusinessAjax.js'></script>
<script type="text/javascript" src="js/ms/srv.faixaEscala.js" charset="ISO-8859-1"></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Faixas de Escalas</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="100%">
 	                        		<label class="label" for="filialDesc">Escala:</label>
									<select id="idEscalaF" name="idEscalaF" class="campo">
										<option value="" selected>Selecione...</option>
									</select> 	                        		
 	                        	</td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoEscalas" type="button" class="button" value="Escalas" />
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="button" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				
				<c:if test="${alteracoesPendentes}">
					<br>
					<div class="barAlert">
						Existem Alterações Pendentes
						<input id="confirmar" type="button" class="buttonAtualizar" style="float:right" value="Confirmar" />
						<input id="descartar" type="button" class="buttonAtualizar" style="float:right" value="Descartar" />
						<br/>
					</div>
				</c:if>
				
				<br>
				<div class="barTitle">
					Faixas de Escalas
                    <c:if test="${'true' == stBtRemover}">
						<input id="excluir" type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					</c:if>
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
					<br/>
				</div>
				<display:table pagesize='50' id="faixasEscalas" name="faixasEscalas" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:4%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${faixasEscalas.idEscala};${faixasEscalas.sequencial};${faixasEscalas.idUnidadeFaixa};${faixasEscalas.faixaInicialFormatada};${faixasEscalas.faixaFinalFormatada};${faixasEscalas.idUnidadeRealizado};${faixasEscalas.realizadoFormatado}"></display:column>
					<display:column property="sequencial" title="Sequência" style="cursor: pointer; text-align:center; vertical-align: middle; width:6%; "/> 
					<display:column property="faixaInicialFormatada" title="Faixa Inicial" style="cursor: pointer; vertical-align: middle; width:30%; text-align:right; "/> 
					<display:column property="faixaFinalFormatada" title="Faixa Final" style="cursor: pointer; vertical-align: middle; width:30%; text-align:right; " /> 
					<display:column property="realizadoFormatado" title="Realizado" style="cursor: pointer; vertical-align: middle; width:30%; text-align:right; "/> 
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>