<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.tipoFilial.js" charset="ISO-8859-1"></script>
<script type="text/javascript">

	
</script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Tipo Filiais</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				
				<input type="hidden" id="codTipoFilialEnc"    name="codTipoFilialEnc"    value=""/>
				<input type="hidden" id="descrTipoFilialEnc"  name="descrTipoFilialEnc"  value=""/>
				<input type="hidden" id="dtTipoFilialEnc"     name="dtTipoFilialEnc"     value=""/>
				<input type="hidden" id="codUsuTipoFilialEnc" name="codUsuTipoFilialEnc" value=""/>
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" style="width:25%">
 	                        		<label class="label" for="filialDesc">Descri&ccedil;&atilde;o Tipo Filial:</label>
                                	<input id="dscrFilial" name="dscrFilial" value="<c:out value='${dscrFilial}'/>" class="campo2" maxlength="25" style="width: 130px;" value="" type="text"/>
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
				<div class="barTitle">Tipo Filiais 
				  <input id="excluir"   name="excluir"    type="button" class="buttonAtualizar" value="Excluir" style="float:right;"/>
				  <input id="atualizar" name="atualizar"  type="button" class="buttonAtualizar" value="Alterar" style="float:right;"/>
				  <input id="incluir"   name="incluir"    type="button" class="buttonAtualizar" value="Incluir" style="float:right;"/>
				<br/></div>
				
				
				<display:table pagesize='10' id="listaTipoFiliais" name="listaTipoFiliais" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="Selecione"    style="text-align:center;"><input type="radio" name="checkbox" id="checkbox" value="${listaTipoFiliais.codFilial}@${listaTipoFiliais.descricao}@${listaTipoFiliais.idUsuarioUltimaAlteracao}@${listaTipoFiliais.dataUltimaAlteracao}"></display:column> 					
					<display:column property="codFilial" title="C&oacute;digo"            style="text-align:center; cursor: pointer; width: 20%; vertical-align: middle;" />  
					<display:column property="descricao" title="Descri&ccedil;&atilde;o"  style="cursor: pointer; width: 70%; vertical-align: middle; text-align:center;"/>             
				</display:table>
				<BR>

				
			</form>
		</div>
	</div>
</div>

