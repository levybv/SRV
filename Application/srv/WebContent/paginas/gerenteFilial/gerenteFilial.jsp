<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.gerenteFilial.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	function detalheFilial(codFunc){
		modal(600,250,'paginas/gerenteFilial/detalheGerenteFilial.jsp','Marisa - Detalhe Gerente de Loja',true,'',true);
	}
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Gerente duas Lojas</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente"><label class="label">Matrícula:</label>
                                	<input id="idFuncF" name="idFuncF" value="<c:out value='${idFuncF}'/>" class="campo2" maxlength="11" style="width: 60px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label">Nome:</label>
                                	<input id="nomeFuncF" name="nomeFuncF" value="<c:out value='${nomeFuncF}'/>" class="campo2" maxlength="100" style="width: 250px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label">Atuação:</label>
									<select id="codAtuacaoF" name="codAtuacaoF" class="campo">
										<option value="" selected>Selecione...</option>
										<option value="1" <c:if test="${codAtuacaoF==1}">selected</c:if>>Definitiva</option>
										<option value="2" <c:if test="${codAtuacaoF==2}">selected</c:if>>Provisória</option>
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
					Gerente duas Lojas
					<input id="excluir" type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					<!-- input id="alterar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" / -->
					<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>

				<display:table pagesize='10' id="gerenteFilial" name="gerenteFilial" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="text-align: center"><input type="radio" name="checkbox" id="checkbox" value="${gerenteFilial.filial.codFilial};${gerenteFilial.idFuncionario}"/></display:column>
					<display:column property="idFuncionario" title="Matrícula" style="width: 6%; text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="nomeFuncionario" title="Funcionário" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column title="Filial Origem" style="text-align: center; cursor: pointer; vertical-align: middle;">
						<c:out value="${gerenteFilial.filialOrigem.codFilial}"/> - <c:out value="${gerenteFilial.filialOrigem.descricao}"/>
					</display:column>
					<display:column title="Atuação" style="text-align: center; cursor: pointer; vertical-align: middle;">
						<c:choose>
							<c:when test="${gerenteFilial.codAtuacao == 1}">Definitiva</c:when>
							<c:when test="${gerenteFilial.codAtuacao == 2}">Provisória</c:when>
							<c:otherwise>-</c:otherwise>
						</c:choose>
					</display:column> 
					<display:column title="Filial" style="text-align: center; cursor: pointer; vertical-align: middle;">
						<c:out value="${gerenteFilial.filial.codFilial}"/> - <c:out value="${gerenteFilial.filial.descricao}"/>
					</display:column>
					<display:column property="dataInclusaoFilial" title="Data Inclusão" format="{0,date,dd/MM/yyyy HH:mm:ss}" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>