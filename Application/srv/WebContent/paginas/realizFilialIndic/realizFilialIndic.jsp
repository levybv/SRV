<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="pt_BR" scope="request"/>

<script type='text/javascript' src='srvdwr/interface/DataBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.realizFilialIndic.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >

	//chave
	var idIndicador	=-1;
	var idEmpresa	=-1;
	var idFilial	=-1;
	var ano			=-1;
	var mes			=-1;
	
	//Filtro
	var mesF					="";
	var anoF					="";
	var idFilialF				="";
	var idIndicadorF			="";
	var descricaoIndicadorF		="";
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Realizado Filial</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<input type='hidden' name='periodoFiltro' id='periodoFiltro' value='<c:out value='${periodoFiltro}' />'>

				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="30%">
									<label class="label">Per&iacute;odo:</label><span class="requerido">*</span>
									<select id="periodoF" name="periodoF" class="campo">
										<option value="" selected>Selecione...</option>
									</select>
 	                        	</td>
 	                        	<td class="componente" width="30%">                        		
									<label class="label">Loja:</label>
									<select id="idFilialF" name="idFilialF" class="campo">
										<option value="" selected>Selecione...</option>
                                    	<c:forEach var="itemFilial" items="${listaFilial}">
                                    		<option value="<c:out value='${itemFilial.codFilial}'/>" <c:if test="${itemFilial.codFilial == idFilialF}">selected</c:if> ><c:out value='${itemFilial.codFilial} - ${itemFilial.descricao}'/></option>
										</c:forEach>
									</select>
                                </td>
 	                        	<td class="componente" width="30%">
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
					Realizado Filial
					<input id="botaoImportar" type="button" class="buttonAtualizar" style="float:right" value="Importar" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaRealFilIndic" name="listaRealFilIndic" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column property="ano" title="Ano" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%; "/>
					<display:column property="mes" title="Mês" style="text-align: center; cursor: pointer; vertical-align: middle; width:5%; "/>
					<display:column title="Filial" style="text-align: center; cursor: pointer; vertical-align: middle; ">
						<c:out value="${listaRealFilIndic.filialVO.codFilial}"/> - <c:out value="${listaRealFilIndic.filialVO.descricao}"/>
					</display:column>
					<display:column title="Indicador" style="text-align: center; cursor: pointer; vertical-align: middle; ">
						<c:out value="${listaRealFilIndic.indicadorVO.codIndicador}"/> - <c:out value="${listaRealFilIndic.indicadorVO.descricaoIndicador}"/>
					</display:column>
					<display:column property="numRealizado" title="Realizado" format="{0,number,#,##0.##}" style="text-align: center; cursor: pointer; vertical-align: middle; "/>
					<display:column property="dataIniSitSrv" title="Data" format="{0,date,dd/MM/yyyy HH:mm:ss}" style="text-align: center; cursor: pointer; vertical-align: middle; "/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>