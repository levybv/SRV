<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
<script type="text/javascript" src="js/ms/srv.digitalizado.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Importar Digitalizados</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1" enctype="multipart/form-data">
				<input type='hidden' name='tabSelecionada' id='tabSelecionada' value="<c:out value='${tabSelecionada}' />">
				<input type='hidden' name='proxTab' id='proxTab' value='1'>
				<div class="barTitle">Dados<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    		<tr>
								<td  class="componente" width="5%">
									<label class="label">M&ecirc;s:</label><span class="requerido">*</span>
								</td>
								<td  class="componente" width="45%">
									<select id="mes" name="mes" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaMes}">
                                    		<option value="<c:out value='${item.mes}' />" ><c:out value='${item.mesStr}' /></option>
										</c:forEach>
                                    </select>
                                </td>
								<td  class="componente" width="5%">
 	                        		<label class="label">Tipo:</label><span class="requerido">*</span>
								</td>
								<td  class="componente" width="45%">
									<select id="tipo" name="tipo" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<option value="PL">CARTAO PL</option>
                                    	<option value="SAX">SAX</option>
                                    </select>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a id="layoutDigitalizados" href="#"><b>Layout dos Arquivos</b></a>
                                </td>
                            </tr>
                            <tr>
								<td  class="componente" width="5%">
									<label class="label">Ano:</label><span class="requerido">*</span>
								</td>
								<td  class="componente" width="45%">
									<select id="ano" name="ano" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaAno}">
                                    		<option value="<c:out value='${item.ano}' />" ><c:out value='${item.ano}' /></option>
										</c:forEach>
                                    </select>
                                </td>
								<td class="componente" width="5%">
 	                        		<label class="label">Arquivo:</label><span class="requerido">*</span>
 	                        	</td>
                            	<td class="componente" width="45%">
									<input type="file" class="campo" name="arquivoUpload" id="arquivoUpload" size="70">
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar"/>
							<input id="botaoImportar" type="button" class="button" value="Importar"/>
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
