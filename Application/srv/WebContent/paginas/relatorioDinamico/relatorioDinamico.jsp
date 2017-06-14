<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.relatorioDinamico.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Relat&oacute;rio Din&acirc;nico</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">C&oacute;digo:</label>
 	                        		<input id="idRelatorioDinamicoF" name="idRelatorioDinamicoF" value="<c:out value='${pesquisaRelatorioDinamicoVO.codigo}'/>" class="campo2" maxlength="5" style="width: 100px;" type="text"/>
 	                        	</td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label">Nome:</label>
 	                        		<input id="nomeRelatorioDinamicoF" name="nomeRelatorioDinamicoF" value="<c:out value='${pesquisaRelatorioDinamicoVO.nome}'/>" class="campo2" maxlength="50" style="width: 250px;" type="text"/>
 	                        	</td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Descri&ccedil;&atilde;o:</label>
 	                        		<input id="descricaoRelatorioDinamicoF" name="descricaoRelatorioDinamicoF" value="<c:out value='${pesquisaRelatorioDinamicoVO.descricao}'/>" class="campo2" maxlength="300" style="width: 300px;" type="text"/>
 	                        	</td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Ativo?</label>
                                	<select id="ativoRelatorioDinamicoF" name="ativoRelatorioDinamicoF" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaRelatorioDinamicoVO.isAtivo}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaRelatorioDinamicoVO.isAtivo==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaRelatorioDinamicoVO.isAtivo==false}">selected</c:if>>Não</option>
                                    </select>
 	                        	</td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label">Tipo Relat&oacute;rio:</label>
                                	<select id="tipoRelatorioDinamicoF" name="tipoRelatorioDinamicoF" class="campo">
                                    	<option value="" selected>[TODOS]</option>
										<c:forEach var="itemTipoRelatorio" items="${listaTipoRelatorio}">
											<option value="<c:out value='${itemTipoRelatorio.codigo}'/>" <c:if test="${itemTipoRelatorio.codigo == pesquisaRelatorioDinamicoVO.relatorioTipoVO.codigo}">selected</c:if> ><c:out value='${itemTipoRelatorio.codigo}' /> - <c:out value='${itemTipoRelatorio.nome}' /></option>
										</c:forEach>
                                    </select>
 	                        	</td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Per&iacute;odo?</label>
                                	<select id="periodoRelatorioDinamicoF" name="periodoRelatorioDinamicoF" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaRelatorioDinamicoVO.isPeriodo}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaRelatorioDinamicoVO.isPeriodo==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaRelatorioDinamicoVO.isPeriodo==false}">selected</c:if>>Não</option>
                                    </select>
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
					Relat&oacute;rios Din&acirc;nico
					<input id="excluir" type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					<input id="atualizar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaRelatorioDinamico" name="listaRelatorioDinamico" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:4%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${listaRelatorioDinamico.codigo}"></display:column>
					<display:column property="codigo" title="Código" style="width:6%; cursor: pointer; text-align:center; vertical-align: middle;"/> 
					<display:column property="nome" title="Nome" style="text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricao" title="Descrição" style="cursor: pointer; vertical-align: middle;"/>
					<display:column title="Tipo Relatório" style="cursor: pointer; vertical-align: middle; text-align:center;">
						<c:out value='${listaRelatorioDinamico.relatorioTipoVO.codigo}'/> - <c:out value='${listaRelatorioDinamico.relatorioTipoVO.nome}'/>
					</display:column>
					<display:column title="Ativo?" style="width:6%; cursor: pointer; vertical-align: middle; text-align:center;">
						<c:choose>
							<c:when test="${listaRelatorioDinamico.isAtivo==true}">Sim</c:when>
							<c:when test="${listaRelatorioDinamico.isAtivo==false}">N&atilde;o</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="Período?" style="width:6%; cursor: pointer; vertical-align: middle; text-align:center;">
						<c:choose>
							<c:when test="${listaRelatorioDinamico.isPeriodo==true}">Sim</c:when>
							<c:when test="${listaRelatorioDinamico.isPeriodo==false}">N&atilde;o</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>