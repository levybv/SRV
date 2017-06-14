<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.bonusAnualFuncionario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idFuncionario=-1;
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Bonus Anual - Funcion&aacute;rios</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
			
				<input type="hidden" id="idFuncionarioSelecionado" name="idFuncionarioSelecionado" value=""/>
				<input type="hidden" id="nomeFuncionarioSelecionado" name="nomeFuncionarioSelecionado" value=""/>
				<input type="hidden" id="cargoFuncionarioSelecionado" name="cargoFuncionarioSelecionado" value=""/>
				<input type="hidden" id="centroCustoFuncionarioSelecionado" name="centroCustoFuncionarioSelecionado" value=""/>
				<input type="hidden" id="situacaoColaborador" name="situacaoColaborador" value="<c:out value='${situacaoColaborador}'/>"/>
				
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="filialDesc">Matrícula:</label>
 	                        	</td>
 	                        	<td class="componente">
                                	<input id="idFuncionario" name="idFuncionario" class="campo2" maxlength="11" style="width: 100px;" value="<c:out value='${idFuncionario}'/>" type="text"/>
                                </td>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="filialDesc">Nome:</label>
 	                        	</td>
 	                        	<td class="componente">
                                	<input id="nomeFuncionario" name="nomeFuncionario" class="campo2" maxlength="60" style="width: 300px;" value="<c:out value='${nomeFuncionario}'/>" type="text"/>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Crachá:</label>
 	                        	</td>
 	                        	<td class="componente">
                                	<input id="cracha" name="cracha" class="campo2" maxlength="20" style="width: 100px;" value="<c:out value='${cracha}'/>" type="text"/>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">CPF:</label>
 	                        	</td>
 	                        	<td class="componente">
                                	<input id="cpfFuncionario" name="cpfFuncionario" class="campo2" maxlength="14" style="width: 150px;" value="<c:out value='${cpfFuncionario}'/>" type="text"/>
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
					Bonus Anual - Funcion&aacute;rios Eleg&iacute;veis
					<br/>
				</div>
				<display:table pagesize='15' id="funcionarios" name="funcionarios" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column property="idFilial"        title="Loja"  			style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="nomeFuncionario" title="Nome"  			style="text-align: left; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoCargo"  title="Cargo" 			style="text-align: left; cursor: pointer; vertical-align: middle;"/>
					<display:column property="dataAdmissao"    title="Data Admissão" 	format="{0,date,dd/MM/yyyy}" style="text-align: center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="dataDemissao"    title="Data Demissão" 	format="{0,date,dd/MM/yyyy}" style="text-align: center; cursor: pointer; vertical-align: middle;"/>
					<display:column title="" style="text-align: center;width: 1%"><a href="javascript:consultaFuncionario(${funcionarios.idFuncionario}, '${funcionarios.nomeFuncionario}', '${funcionarios.descricaoCargo}', '${funcionarios.descricaoCentroCusto}')"><img src="images/menu-consultas-ccm.png"/></a></display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>