<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" >

	//chave
	var idEmpresa		=-1;
	var idFilial		=-1;
	var idFuncionario 	=-1;
	var ano				=-1;
	var mes				=-1;
	
	//Filtro
	var idFuncionarioF		="";
	var nomeFuncionarioF	="";
	var anoF				="";
	var mesF				="";
	
</script> 

<script type='text/javascript' src='srvdwr/interface/RelatorioCampanhaBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.relatorioCampanha.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Relat&oacute;rio Campanhas - Campanha</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    	   <tr>
                    	     <td class="componente" width="10%">
 	                        	  <label class="label">Ano:<span class="requerido">*</span></label>
 	                        	</td>
 	                        	<td class="componente" width="40%">                        		
                                   <input id="ano" name="ano" value="" class="campo2" maxlength="4" style="width: 60px;" type="text"/>
                              </td>
                    	   </tr> 
                        	<tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label">M&ecirc;s:<span class="requerido">*</span></label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
									<select id="mes" name="mes" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<option value="01" <c:if test="${01 == mes}">selected</c:if> >Janeiro</option>
                                		<option value="02" <c:if test="${02 == mes}">selected</c:if> >Fevereiro</option>
										<option value="03" <c:if test="${03 == mes}">selected</c:if> >Mar&ccedil;o</option>
                                		<option value="04" <c:if test="${04 == mes}">selected</c:if> >Abril</option>
                                		<option value="05" <c:if test="${05 == mes}">selected</c:if> >Maio</option>
                                		<option value="06" <c:if test="${06 == mes}">selected</c:if> >Junho</option>                                		
                                		<option value="07" <c:if test="${07 == mes}">selected</c:if> >Julho</option>
                                		<option value="08" <c:if test="${08 == mes}">selected</c:if> >Agosto</option>
										<option value="09" <c:if test="${09 == mes}">selected</c:if> >Setembro</option>
                                		<option value="10" <c:if test="${10 == mes}">selected</c:if> >Outubro</option>
                                		<option value="11" <c:if test="${11 == mes}">selected</c:if> >Novembro</option>
                                		<option value="12" <c:if test="${12 == mes}">selected</c:if> >Dezembro</option>                                		
                                    </select>
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label">Campanha:<span class="requerido">*</span></label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
									<select id="campanhaF" name="campanhaF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    </select>
                                </td>
                            </tr>
                    	   <tr>
                    	     <td class="componente" width="10%">
 	                        	  <label class="label">Período:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">                        		
                                   <input id="inicioF" name="inicioF" value="" class="campo2" maxlength="10" style="width: 70px;" type="text"/> à
                                   <input id="fimF" name="fimF" value="" class="campo2" maxlength="10" style="width: 70px;" type="text"/>
                              </td>
                    	   </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					<div id="div-botao">
						<input id="importar" type="submit" class="buttonAtualizar" style="float:right" value="Relat&oacute;rio" />
						<input id="botaoLimpar" type="button" class="button" style="float:right"  value="Limpar" />
						<br/>
					</div>
				</div>
				
				<BR>
			</form>
		</div>
	</div>
</div>