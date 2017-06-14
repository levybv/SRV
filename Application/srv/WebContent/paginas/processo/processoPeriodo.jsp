<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" >

	
</script> 

<script type="text/javascript" src="js/ms/srv.processoPeriodo.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Períodos de Processamento</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="filialDesc">Ano:</label>
 	                        	</td>
 	                        	<td class="componente" width="95%">
                                	<input id="anoF" name="anoF" value="<c:out value='${anoF}'/>" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
                                </td>
                            </tr> 
                            
                            <tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label" for="filialDesc">Mes:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
									<select id="mesF" name="mesF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<option value="01" <c:if test="${01 == mesF}">selected</c:if> >Janeiro</option>
                                		<option value="02" <c:if test="${02 == mesF}">selected</c:if> >Fevereiro</option>
										<option value="03" <c:if test="${03 == mesF}">selected</c:if> >Mar&ccedil;o</option>
                                		<option value="04" <c:if test="${04 == mesF}">selected</c:if> >Abril</option>
                                		<option value="05" <c:if test="${05 == mesF}">selected</c:if> >Maio</option>
                                		<option value="06" <c:if test="${06 == mesF}">selected</c:if> >Junho</option>                                		
                                		<option value="07" <c:if test="${07 == mesF}">selected</c:if> >Julho</option>
                                		<option value="08" <c:if test="${08 == mesF}">selected</c:if> >Agosto</option>
										<option value="09" <c:if test="${09 == mesF}">selected</c:if> >Setembro</option>
                                		<option value="10" <c:if test="${10 == mesF}">selected</c:if> >Outubro</option>
                                		<option value="11" <c:if test="${11 == mesF}">selected</c:if> >Novembro</option>
                                		<option value="12" <c:if test="${12 == mesF}">selected</c:if> >Dezembro</option>                                		
                                    </select>
                                </td>
                                
                            </tr>
                             
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<c:if test="${exibeReprocessar != null}">
								<input id="reprocessar" type="button" class="buttonAtualizar120" value="Reprocessar" />
							</c:if>
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
					Períodos de Processamento
					<input id="atualizar" 	type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<input id="incluir" 	type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="processosPeriodo" name="processosPeriodo" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="width:5%; text-align:center; "><input type="checkbox" name="checkbox" id="checkbox" value="${processosPeriodo.ano};${processosPeriodo.mes};${processosPeriodo.status};${processosPeriodo.periodoFormatado};${processosPeriodo.idProcesso};${processosPeriodo.descricaoProcesso}"></display:column>
					<display:column property="periodoFormatado" 	title="Período" 	style="width:25%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="statusFormatado" 		title="Situação" 	style="width:30%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoProcesso" 	title="Processo" 	style="width:40%; text-align:center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>