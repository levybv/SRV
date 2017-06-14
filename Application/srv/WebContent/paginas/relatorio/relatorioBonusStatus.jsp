<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" >
	
</script> 

<script type="text/javascript" src="js/ms/srv.relatorioBonusStatus.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Relat&oacute;rio Bonus por Status</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
			
			<input type='hidden' id="periodosS" name="periodosS"  value="<c:out value='${periodos}'/>"/>
			<input type='hidden' id="statusS" name="statusS" value="<c:out value='${status}'/>"/>
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    	   
                    	   <tr>
 	                        	<td class="componente" width="10%" colspan="2">
 	                        		<label class="label" for="filialDesc">Status:</label>
 	                        	</td>
 	                        	<td>	 	                        	
									<select id="status" name="status" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<option value="NAO INICIADO">Nao Iniciado</option>
                                		<option value="EM ANDAMENTO">Em Andamento</option>
                                		<option value="FINALIZADO">Finalizado</option>                             		
                                    </select>
                                </td>
                            </tr>
                    	
                        	<tr>
 	                        	<td class="componente" width="10%" colspan="2">
 	                        		<label class="label" for="filialDesc">Periodo:</label>
 	                        	</td>
 	                        	<td>	
									<select id="periodos" name="periodos" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="per" items="${periodosMesAno}">
                                           <option value="${per.mesAno}"><c:out value='${per.periodoMesAno}'/></option>
                                        </c:forEach>
                                    </select>
                                </td> 
                            </tr> 
                        	
                        </tbody>
                    </table>
                    <c:out value='${mensagem}'/>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="pesquisar" type="submit" class="button"   value="Pesquisar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					<input id="exportar" type="button" class="buttonAtualizar" style="float:right" value="Exportar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaRelatorioBonus" name="listaRelatorioBonus" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column property="codCargo" 	title="Codigo Cargo"       style="width:3%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descrCargo"   title="Cargo" 	           style="width:3%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="codFil"       title="Filial"             style="width:4%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="codFunc"      title="Codigo Funcionario" style="width:7%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="nomeFunc"     title="Funcionario"        style="width:10%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="situacaoRh"   title="Situacao RH"        style="width:10%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="dataDemissao" title="Demissao"           style="width:5%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="grupoRemVar"  title="Agrupamento"        style="width:7%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="ano"          title="Ano"                style="width:3%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="mes"          title="Mes" 	           style="width:3%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="status"       title="status" 	           style="width:4%; text-align:center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				
				
				<BR>
			</form>
		</div>
	</div>
</div>