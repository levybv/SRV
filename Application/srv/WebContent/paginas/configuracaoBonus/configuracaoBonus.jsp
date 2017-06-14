<%@ page contentType="text/html" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.configuracaoBonus.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Configura&ccedil;&atilde;o B&ocirc;nus</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">Ano:</label>
                                	<input type="text" class="campo2" id="anoF" name="anoF" value="<c:out value='${configuracaoBonusVO.ano}'/>" size="6" maxlength="4" />
                                </td>
 	                        	<td class="componente" style="width:34%">
 	                        		<label class="label">Encerrado:</label>
									<select id="encerradoF" name="encerradoF" class="campo">
										<option value="" <c:if test="${configuracaoBonusVO.flgEncerrado == null}">selected</c:if>>Selecione...</option>
										<option value="S" <c:if test="${configuracaoBonusVO.flgEncerrado == 'S'}">selected</c:if>>Sim</option>
										<option value="N" <c:if test="${configuracaoBonusVO.flgEncerrado == 'N'}">selected</c:if>>N&atilde;o</option>
									</select>
                                </td>
 	                        	<td class="componente" style="width:33%">
 	                        		<label class="label">&nbsp;</label>
                                	&nbsp;
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
					Configura&ccedil;&atilde;o B&ocirc;nus
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" />
					</c:if>
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaConfiguracaoBonusVO" name="listaConfiguracaoBonusVO" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="text-align: center; width:5%;"><input type="radio" name="checkbox" id="checkbox" value="${listaConfiguracaoBonusVO.ano}"/></display:column>
					<display:column property="ano" title="Ano" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;" /> 
					<display:column title="Encerrado" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;">
	                    <c:if test="${listaConfiguracaoBonusVO.isEncerrado}">Sim</c:if>
	                    <c:if test="${!listaConfiguracaoBonusVO.isEncerrado}">N&atilde;o</c:if>
					</display:column>
					<display:column title="Range Indicadores" style="text-align: center; cursor: pointer; vertical-align: middle;">
						De <c:out value="${listaConfiguracaoBonusVO.codIndicIni}"/> à <c:out value="${listaConfiguracaoBonusVO.codIndicFim}"/>
					</display:column>
					<display:column title="Funding" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;">
	                    <c:if test="${listaConfiguracaoBonusVO.isFunding}">Sim</c:if>
	                    <c:if test="${!listaConfiguracaoBonusVO.isFunding}">N&atilde;o</c:if>
					</display:column>
					<display:column property="funding" title="Valor Funding" format="{0,number,0.00}" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;" /> 					
					<display:column title="Contrato Meta" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%;">
	                    <c:if test="${listaConfiguracaoBonusVO.isContratoMeta}">Sim</c:if>
	                    <c:if test="${!listaConfiguracaoBonusVO.isContratoMeta}">N&atilde;o</c:if>
					</display:column>
					<display:column property="dataLimiteAceite" title="Data Limite Aceite" format="{0,date,dd/MM/yyyy}" style="text-align: center; cursor: pointer; vertical-align: middle;" />
					<display:column property="periodoDisponivel" title="Per&iacute;odo Dispon&iacute;vel" style="text-align: center; cursor: pointer; vertical-align: middle;" />
					<display:column title="Escalas" style="text-align: center; cursor: pointer; vertical-align: middle;">
	                                   	<c:forEach var="itemEscala" items="${listaConfiguracaoBonusVO.listaEscala}">
	                                   		<c:out value="${itemEscala.idEscala}"/> - <c:out value="${itemEscala.descricaoEscala}"/> (NUM: <c:out value="${itemEscala.numEscala}"/>);
										</c:forEach>
					</display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>