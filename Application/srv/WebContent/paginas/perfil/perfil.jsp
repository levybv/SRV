<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.perfil.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var codPerfil=-1;
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Perfil</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
								<td class="componente"><label class="label">Código:</label>
									<input id="codigo" name="codigo" class="campo2" maxlength="5" style="width: 50px;" value="<c:out value='${pesquisaPerfilVO.idPerfil}'/>" type="text"/>
								</td>
 	                        	<td class="componente"><label class="label">Descri&ccedil;&atilde;o:</label>
                                	<input id="descricao" name="descricao" class="campo2" maxlength="50" style="width: 250px;" value="<c:out value='${pesquisaPerfilVO.descricao}'/>" type="text"/>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Perfil Ativo?</label>
                                	<select id="ativo" name="ativo" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaPerfilVO.isAtivo}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaPerfilVO.isAtivo==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaPerfilVO.isAtivo==false}">selected</c:if>>Não</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
 	                        	<td class="componente">
 	                        		<label class="label">Acesso Resultado Bônus?</label>
                                	<select id="acessoResultadoBonus" name="acessoResultadoBonus" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaPerfilVO.isExibeBonus}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaPerfilVO.isExibeBonus==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaPerfilVO.isExibeBonus==false}">selected</c:if>>Não</option>
                                    </select>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Reabre Resultado Bônus?</label>
                                	<select id="reabreResultadoBonus" name="reabreResultadoBonus" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaPerfilVO.isReabreResultado}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaPerfilVO.isReabreResultado==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaPerfilVO.isReabreResultado==false}">selected</c:if>>Não</option>
                                    </select>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label">Valida Vlr Faixas das Escalas?</label>
                                	<select id="validaVlrFxEscala" name="validaVlrFxEscala" class="campo">
                                    	<option value="" <c:if test="${empty pesquisaPerfilVO.isValidaFaixaEscala}">selected</c:if>>[TODOS]</option>
                                    	<option value="S" <c:if test="${pesquisaPerfilVO.isValidaFaixaEscala==true}">selected</c:if>>Sim</option>
                                    	<option value="N" <c:if test="${pesquisaPerfilVO.isValidaFaixaEscala==false}">selected</c:if>>Não</option>
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
				<div class="barTitle">Perfis <input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" /><br/></div>

				<display:table pagesize='10' id="listaPerfis" name="listaPerfis" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"><input type="radio" name="checkbox" id="checkbox" name="checkbox" value="${listaPerfis.idPerfil}"> </display:column> 					
					<display:column property="idPerfil" title="C&oacute;digo" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;" />  
					<display:column property="descricao" title="Descri&ccedil;&atilde;o" style="text-align: center;cursor: pointer; width: 68%; vertical-align: middle;"/>             
					<display:column property="isAtivoStr" title="Perfil<br>Ativo?" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
					<display:column property="isExibeBonusStr" title="Acesso<br>Resultado<br>Bônus?" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
					<display:column property="isReabreResultadoStr" title="Reabre<br>Resultado<br>Bônus?" style="text-align: center;cursor: pointer; width: 5%; vertical-align: middle;"/>
					<display:column property="isValidaFaixaEscalaStr" title="Valida<br>Vlr Faixas<br>das Escalas?" style="text-align: center;cursor: pointer; width: 7%; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>

