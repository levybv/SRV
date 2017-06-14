<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.acompMetaFilial.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >

	//Filtro
	var mesF					="";
	var anoF					="";
	var idFilialF				="";
	var idIndicadorF			="";
	var descricaoIndicadorF		="";
	var apenasNaoRealzF		    ="";
	var acompMetaFilialF	    ="";
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Acompanhamento de Metas por Filial</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
			
				<input type="hidden" name="acompMetaFilial" id="acompMetaFilial" value="S"/>
			
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label" for="filialDesc">Mês:</label>
 	                        	</td>
 	                        	<td class="componente" width="20%">
									<select id="mesF" name="mesF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<option value="1" <c:if test="${1 == mesF}">selected</c:if> >Janeiro</option>
                                		<option value="2" <c:if test="${2 == mesF}">selected</c:if> >Fevereiro</option>
										<option value="3" <c:if test="${3 == mesF}">selected</c:if> >Março</option>
                                		<option value="4" <c:if test="${4 == mesF}">selected</c:if> >Abril</option>
                                		<option value="5" <c:if test="${5 == mesF}">selected</c:if> >Maio</option>
                                		<option value="6" <c:if test="${6 == mesF}">selected</c:if> >Junho</option>                                		
                                		<option value="7" <c:if test="${7 == mesF}">selected</c:if> >Julho</option>
                                		<option value="8" <c:if test="${8 == mesF}">selected</c:if> >Agosto</option>
										<option value="9" <c:if test="${9 == mesF}">selected</c:if> >Setembro</option>
                                		<option value="10" <c:if test="${10 == mesF}">selected</c:if> >Outubro</option>
                                		<option value="11" <c:if test="${11 == mesF}">selected</c:if> >Novembro</option>
                                		<option value="12" <c:if test="${12 == mesF}">selected</c:if> >Dezembro</option>                                		
                                    </select>
                                </td>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="filialDesc">Ano:</label>
 	                        	</td>
 	                        	<td class="componente" width="25%">                        		
                                	<input id="anoF" name="anoF" value="<c:out value='${anoF}'/>" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente" width="5%">
 	                        		<label class="label" for="filialDesc">Filial:</label>
 	                        	</td>
 	                        	<td class="componente" width="25%"> 	                        		
                                	<input id="idFilialF" name="idFilialF" value="<c:out value='${idFilialF}'/>" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Cód. Indicador:</label>
 	                        	</td>
 	                        	<td class="componente"> 	                        		
                                	<input id="idIndicadorF" name="idIndicadorF" value="<c:out value='${idIndicadorF}'/>" class="campo2" maxlength="9" style="width: 100px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Descrição:</label>
 	                        	</td>
 	                        	<td class="componente"  colspan="3"> 	                        		
                                	<input id="descricaoIndicadorF" name="descricaoIndicadorF" value="<c:out value='${descricaoIndicadorF}'/>" class="campo2" maxlength="100" style="width: 300px;" value="" type="text"/>
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" colspan="6">
 	                        		<center>
	 	                        		<input type="checkbox" name="apenasNaoRealizadasF" id="apenasNaoRealizadasF" <c:if test="${apenasNaoRealizadasF}"> CHECKED </c:if> />
	 	                        		<label class="label" for="filialDesc">Apenas importações não realizadas</label>
	 	                        	</center>
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
					Acompanhamento de Metas por Filial
					<input id="importar" type="button" class="buttonAtualizar" style="float:right" value="Importar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="metasFiliais" name="metasFiliais" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column property="indicadorFormatado" title="Indicador" style="cursor: pointer; vertical-align: middle; width:40%; "/> 
					<display:column property="filialFormatado" title="Filial" style="cursor: pointer; vertical-align: middle; width:40%;" /> 
					<display:column title="Valor Meta" style="width:20%; text-align:center; ">
						<c:if test="${metasFiliais.temMeta == 'true'}">
							${metasFiliais.valorMetaFormatado}
						</c:if>	
						<c:if test="${metasFiliais.temMeta != 'true'}">
							<font color="red"><b> ${metasFiliais.valorMetaFormatado} </b></font>
						</c:if>	
					</display:column>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>