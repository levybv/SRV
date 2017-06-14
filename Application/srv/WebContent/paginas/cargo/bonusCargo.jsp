<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.bonusCargo.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idCargo=-1;
	var descricaoCargo="";
	var idCargoF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Desempenho do Colaborador por Cargos</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente"><label class="label" for="filialDesc">C&oacute;digo:</label>
                                	<input id="idCargoF" name="idCargoF" value="<c:out value='${idCargoF}'/>" class="campo2" maxlength="4" style="width: 50px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente"><label class="label" for="filialDesc">Descri&ccedil;&atilde;o:</label>
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
					Desempenho do Colaborador por Cargos
					<input id="processar" type="button" class="buttonAtualizar120" style="float:right" value="Processar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="cargos" name="cargos" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="text-align: center"><input type="radio" name="checkbox" id="checkbox" value="${cargos.idCargo};${cargos.descricaoCargo}"/></display:column>
					<display:column property="idCargo" title="Código" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="descricaoCargo" title="Descrição" style="cursor: pointer; vertical-align: middle;" /> 
					<display:column property="descricaoClasseHay" title="Classe Hay" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 					
					<display:column property="agrupaFiliaisFormatado" title="Agrupa Filiais" style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>