<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.bonusGrupoRemVar.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idGrupoRemVar=-1;
	var descricaoGrupoRemVar="";
	var idGrupoRemVarF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Desempenho do Colaborador por Grupos de Remuneração Variável</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente"><label class="label" for="filialDesc">Código:</label>
                                	<input id="idGrupoRemVarF" name="idGrupoRemVarF" value="<c:out value='${idGrupoRemVarF}'/>" class="campo2" maxlength="4" style="width: 50px;" value="" type="text"/>
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
							<img id="aguardeGrupoRemVar" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Desempenho do Colaborador por Grupos de Remuneração Variável
					<input id="processar" type="button" class="buttonAtualizar120" style="float:right" value="Processar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="gruposRemuneracao" name="gruposRemuneracao" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="text-align: center"><input type="radio" name="checkbox" id="checkbox" value="${gruposRemuneracao.idGrupoRemuneracao};${gruposRemuneracao.descricaoOnline}"/></display:column>
					<display:column property="idGrupoRemuneracao" title="Código" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="descricaoOnline" title="Descrição" style="cursor: pointer; vertical-align: middle;" /> 
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>