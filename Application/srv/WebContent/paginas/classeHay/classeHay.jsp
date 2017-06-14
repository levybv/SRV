<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.classeHay.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idClasseHay=-1;
	var idClasseHayF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Classes Hay</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente"><label class="label" for="filialDesc">Código:</label>
                                	<input id="idClasseHayF" name="idClasseHayF" value="<c:out value='${idClasseHayF}'/>" class="campo2" maxlength="4" style="width: 50px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label" for="filialDesc">Descrição:</label>
                                	<input id="descricaoF" name="descricaoF" value="<c:out value='${descricaoF}'/>" class="campo2" maxlength="100" style="width: 250px;" value="" type="text"/>
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
					Classes Hay 
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" />
					</c:if>
					<br/>
				</div>
				
				<display:table pagesize='10' id="classesHay" name="classesHay" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:5%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${classesHay.idClasseHay}"></display:column>
					<display:column property="idClasseHay" title="Código" style="cursor: pointer; vertical-align: middle; width:10%; text-align:center; "/> 
					<display:column property="descricao" title="Descrição" style="cursor: pointer; vertical-align: middle; width:45%;" /> 
					<display:column property="salarioMinimoFormatado" title="Qtde Mínima de Salários" style="width:20%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="salarioMaximoFormatado"  title="Qtde Máxima de Salários" style="width:20%; text-align:center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>