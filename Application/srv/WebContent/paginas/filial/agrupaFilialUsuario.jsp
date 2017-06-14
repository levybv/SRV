<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<script type="text/javascript" src="js/ms/srv.agrupamentoFilial.js" charset="ISO-8859-1"></script>	

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Cargos</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Agrupamento<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
		                   	<tr>
			                   	<td class="componente" style="width:25%">
			                   	<label class="label" for="filialDesc">Matricula:</label>
			                   		<input id="idCodFunF" name="idCodFunF" value="<c:out value='${matricula}'/>" class="campo2" maxlength="15" style="width: 50px; "  type="text"/>
			                   	</td>
 	                        	<td class="componente" style="width:75%">
 	                        		<label class="label" for="filialDesc">Nome Colaborador:<span class="requerido">*</span></label>
 	                        		<select  id="idFuncionario" name="idFuncionario" class="campo2" maxlength="30" style="width: 250px;" type="text" >
 	                        			<option value="">Selecione...</option>
	 	                        		<c:forEach items="${listaFunc}" var="item">
	    									<option value="<c:out value='${item.idFuncionario}'/>" <c:if test="${item.idFuncionario == matricula }">selected</c:if>><c:out value='${item.nomeFuncionario}' /></option>
										</c:forEach>
									</select><span id="idFuncionarioE" class="requerido"></span>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar"    type="button" class="button" value="Limpar" />
							<input id="botaoFiltrar"  type="button" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">Filiais 
				<input id="botaoAtualizar" name="botaoAtualizar" type="button" style="float:right" class="buttonAtualizar" value="Concluir" />
				<br/></div>
				<table class="tabelaComponente">
                    	<tbody>
                        	<tr style="vertical-align:middle">
 	                        	<td class="componente" style="width:30%;text-align: right" valign="right">
 	                        		<label class="label" for="filialDesc">Disponíveis:</label>
 	                        	</td>
								<td class="componente" style="width:10%;text-align: center" valign="middle" >
 	                        		<select size="7" multiple class="campo2" id='filiaisDisponiveis'>
	 	                        		<c:forEach items="${filiaisDisponiveis}" var="item">
	    									<option value="<c:out value='${item.codFilial}'/>"><c:out value='${item.codFilial}'/> - <c:out value='${item.descricao}'/></option>
										</c:forEach>
									</select>
                                </td>
                                <td class="componente" style="width:20%;text-align: center" align="center">
                                	<input id="botaoVincular" type="button" class="button" value="=>"  /><br><br>
                                	<input id="botaoDesvinvular" type="button" class="button" value="<=" />
                                </td>
                                          
								<td class="componente" style="width:10%">
                                    <label class="label" for="filialDesc">Vinculadas ao Colaborador:</label>
                                </td>
								<td class="componente" style="width:30%" valign="middle">
									<select size="7" multiple class="campo2" name='filiaisUtilizadas' id='filiaisUtilizadas'>
	 	                        		<c:forEach items="${listaFilialVinvuladasColaborador}" var="item">
	    									<option value="<c:out value='${item.codFilial}'/>"><c:out value='${item.codFilial}'/> - <c:out value='${item.descricao}'/></option>
										</c:forEach>
									</select>
                                </td> 
                            </tr> 
                        </tbody>
                    </table>
				<BR>
			</form>
		</div>
	</div>
</div>