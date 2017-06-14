<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.agendamentoCarga.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >

	//chave
	var idEscala	=-1;
	
	//Filtro
	var idEscalaF		="";
	var descricaoF		="";
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Agendamento de Carga</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="codigoCargaF">Código:</label>
 	                        		<input id="codigoCargaF" name="codigoCargaF" value="<c:out value='${filtroAgendamento.codigoCarga}'/>" class="campo2" maxlength="2" style="width: 40px;" type="text"/>
 	                        	</td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label" for="descricaoCargaF">Descrição:</label>
 	                        		<input id="descricaoCargaF" name="descricaoCargaF" value="<c:out value='${filtroAgendamento.descricaoCarga}'/>" class="campo2" maxlength="50" style="width: 300px; margin-right:50px;" type="text"/>
 	                        	</td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="mesF">Mês:</label>
									<select id="mesF" name="mesF" class="campo">
										<option value="" selected>[SELECIONE]</option>
                                		<option value="1" <c:if test="${1 == filtroAgendamento.mes}">selected</c:if> >Janeiro</option>
                                		<option value="2" <c:if test="${2 == filtroAgendamento.mes}">selected</c:if> >Fevereiro</option>
										<option value="3" <c:if test="${3 == filtroAgendamento.mes}">selected</c:if> >Março</option>
                                		<option value="4" <c:if test="${4 == filtroAgendamento.mes}">selected</c:if> >Abril</option>
                                		<option value="5" <c:if test="${5 == filtroAgendamento.mes}">selected</c:if> >Maio</option>
                                		<option value="6" <c:if test="${6 == filtroAgendamento.mes}">selected</c:if> >Junho</option>                                		
                                		<option value="7" <c:if test="${7 == filtroAgendamento.mes}">selected</c:if> >Julho</option>
                                		<option value="8" <c:if test="${8 == filtroAgendamento.mes}">selected</c:if> >Agosto</option>
										<option value="9" <c:if test="${9 == filtroAgendamento.mes}">selected</c:if> >Setembro</option>
                                		<option value="10" <c:if test="${10 == filtroAgendamento.mes}">selected</c:if> >Outubro</option>
                                		<option value="11" <c:if test="${11 == filtroAgendamento.mes}">selected</c:if> >Novembro</option>
                                		<option value="12" <c:if test="${12 == filtroAgendamento.mes}">selected</c:if> >Dezembro</option>                                		
									</select>
 	                        	</td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="dataUltimoProcessamentoF">Último Processamento:</label>
 	                        		<input id="dataUltimoProcessamentoF" name="dataUltimoProcessamentoF" value="<c:out value='${filtroAgendamento.dataUltimoProcessamentoStr}'/>" class="campo2" maxlength="10" style="width: 70px;" type="text"/> (dd/mm/aaaa)
 	                        	</td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label" for="dataAgendamentoF">Data Agendamento:</label>
 	                        		<input id="dataAgendamentoF" name="dataAgendamentoF" value="<c:out value='${filtroAgendamento.dataAgendamentoStr}'/>" class="campo2" maxlength="10" style="width: 70px;" type="text"/> (dd/mm/aaaa)
 	                        	</td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="anoF">Ano:</label>
									<input id="anoF" name="anoF" value="<c:out value='${filtroAgendamento.ano}'/>" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
 	                        	</td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="flagAtivaF">Ativa?</label>
									<select id="flagAtivaF" name="flagAtivaF" class="campo">
										<option value="" selected>[SELECIONE]</option>
										<option value="1" <c:if test="${1 == filtroAgendamento.flagAtiva}">selected</c:if> >Sim</option>
										<option value="0" <c:if test="${0 == filtroAgendamento.flagAtiva}">selected</c:if> >Não</option>
									</select>
 	                        	</td>
 	                        	<td class="componente" width="34%">
 	                        	</td>
 	                        	<td class="componente" width="33%">
 	                        	</td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="reprocessar" type="button" class="buttonAtualizar120" value="Reprocessar" />
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
					Agendamento de Carga
					<input id="atualizar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaAgendamento" name="listaAgendamento" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:6%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${listaAgendamento.codigoCarga}"></display:column>
					<display:column property="codigoCarga" title="Código" style="cursor: pointer; text-align:center; vertical-align: middle; width:6%; "/> 
					<display:column property="descricaoCarga" title="Descrição" style="cursor: pointer; vertical-align: middle;"/> 
					<display:column property="dataAgendamento" title="Data <br>Agendamento" format="{0,date,dd/MM/yyyy}" style="text-align:center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="dataUltimoProcessamento" title="Último <br>Processamento" format="{0,date,dd/MM/yyyy}" style="text-align:center; cursor: pointer; vertical-align: middle;"/> 
					<!-- display:column property="dataLimiteProcessamento" title="Limite <br>Processamento" format="{0,date,dd/MM/yyyy}" style="text-align:center; cursor: pointer; vertical-align: middle;"/ -->
					<display:column title="Período" style="text-align:center; cursor: pointer; vertical-align: middle;">
						<c:out value="${listaAgendamento.mesStr}/${listaAgendamento.ano}"/>
					</display:column>
					<display:column title="Ativa?" style="text-align:center; cursor: pointer; vertical-align: middle;">
						<c:choose>
							<c:when test="${listaAgendamento.flagAtiva==1}">Sim</c:when>
							<c:when test="${listaAgendamento.flagAtiva==0}">Não</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>