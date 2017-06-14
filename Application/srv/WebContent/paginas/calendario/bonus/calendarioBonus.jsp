<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript" src="js/ms/srv.calendario.bonus.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Calendário Bônus</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Ano:</label>
                                	<select id="anoF" name="anoF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaAnosCalendarioBonus}">
                                    		<option value="<c:out value='${item}' />" <c:if test="${item == calendarioBonusFiltro.ano}">selected</c:if> ><c:out value='${item}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Mês:</label>
                                	<select id="mesF" name="mesF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaMesCalendarioBonus}">
                                    		<option value="<c:out value='${item.mes}' />" <c:if test="${item.mes == calendarioBonusFiltro.mes}">selected</c:if> ><c:out value='${item.mesStr}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label">Descrição do Mês:</label>
                                   	<input type="text" class="campo2" id="mesDescrF" name="mesDescrF" value="<c:out value="${calendarioBonusFiltro.descricaoMes}"/>" maxlength="20" size="30" />
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Data Inicial:</label>
                                    <input type="text" class="campo2" id="dataInicialF" name="dataInicialF" value="<c:out value="${calendarioBonusFiltro.dataInicialStr}"/>" maxlength="10" size="12" />
                                </td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Data Final:</label>
                                	<input type="text" class="campo2" id="dataFinalF" name="dataFinalF" value="<c:out value="${calendarioBonusFiltro.dataFinalStr}"/>" maxlength="10" size="12" />
                                </td>
 	                        	<td class="componente" width="34%">
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
					Calendário Bônus
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
				<display:table pagesize='20' id="listaCalendarioBonus" name="listaCalendarioBonus" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="width:6%; text-align:center;"><input type="radio" name="checkbox" id="checkbox" value="${listaCalendarioBonus.mes};${listaCalendarioBonus.ano}"></display:column>
					<display:column property="ano" title="Ano" style="text-align: center; cursor: pointer; width: 20%; vertical-align: middle;" />   
					<display:column property ="mesStr" title="Mês" style="text-align: center; cursor: pointer; width: 20%; vertical-align: middle;" />  
					<display:column property ="descricaoMes" title="Descrição Mês" style="text-align: center; cursor: pointer; width: 20%; vertical-align: middle;" />  
					<display:column property="dataInicial" format="{0,date,dd/MM/yyyy}" title="Data Inicial" style="text-align: center; cursor: pointer; width: 20%; vertical-align: middle;"/>             
					<display:column property="dataFinal" format="{0,date,dd/MM/yyyy}" title="Data Final" style="text-align: center; cursor: pointer; width: 20%; vertical-align: middle;"/>
				</display:table>
			</form>
		</div>
	</div>
</div>

