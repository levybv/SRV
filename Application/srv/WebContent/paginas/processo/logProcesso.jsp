<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.logProcesso.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Relat&oacute;rio Log Processos</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				
				<input type='hidden' id="dataS"     name="dataS"     value="<c:out value='${data}'/>"/>
    			<input type='hidden' id="processoS" name="processoS" value="<c:out value='${processo}'/>"/>
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="dataF">Data:</label>
 	                        	</td>
 	                        	<td class="componente" width="95%">
                                	<input id="dataF" name="dataF" value="" class="campo" maxlength="10" style="width: 86px;" value="" type="text"/>
                                </td>                 
                            </tr>
                            
                            <tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label" for="filialDesc">Processo:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
									<select id="processoF" name="processoF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<c:forEach var="log" items="${listaLogProcessos}">
                                		   <option value="${log.codProc}"><c:out value='${log.descrProc}'/></option>
                                		</c:forEach>                                		
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
							<input id="botaoConsultar" type="submit" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Processos
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaProcessosEncontrados" name="listaProcessosEncontrados" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column property="anoProc" 	   title="Ano"             style="width:3%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="mesProc"     title="Mes" 	           style="width:3%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="dtInicio"    title="Data Inicio"     format="{0,date,dd/MM/yyyy HH:mm:ss}" style="width:11%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="dtFim"       title="Data Fim" 	   format="{0,date,dd/MM/yyyy HH:mm:ss}" style="width:11%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="statProcStr" title="Status"          style="width:10%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="obsExec"     title="Obs" 	           style="width:10%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="filLog" 	   title="Filial Log"      style="width:5%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="funcLog"     title="Funcionario Log" style="width:7%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="indicLog"    title="Indicador Log"   style="width:3%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="erroLog"     title="Erro Log" 	   style="width:10%; text-align:center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				
				<BR>
			</form>
		</div>
	</div>
</div>