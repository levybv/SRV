<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="true" import="java.util.*" %>

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/default-ie6.css" />
		<script src="js/lib/DD_belatedPNG.js" type="text/javascript"></script>
		<script type="text/javascript">
		  DD_belatedPNG.fix('#atualizarCotas');
		</script>
	<![endif]-->

	<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
	<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
	<script type='text/javascript' src='srvdwr/engine.js'></script>	
	<script type="text/javascript" src="js/ms/srv.indicador.js" charset="ISO-8859-1"></script>


<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Indicadores</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<input type='hidden' name='tabSelecionada' id='tabSelecionada' value="<c:out value='${tabSelecionada}' />">
				<input type='hidden' name='proxTab' id='proxTab' value='1'>
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    		<tr>
								<td  class="componente" width="5%">
									<label class="label">Grupo:</label><span class="requerido">*</span>
								</td>
								<td  class="componente" width="45%">
									<select id="grupos" name="grupos" class="campo" onChange="populaIndicadores(this.value);populaLideres(this.value);">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<option value="2" <c:if test="${2 == grupoSelecionado}">selected</c:if> >VENDAS</option>
										<option value="3" <c:if test="${3 == grupoSelecionado}">selected</c:if> >PRODUTOS E SERVI&Ccedil;OS FINANCEIROS</option>
                                		<option value="4" <c:if test="${4 == grupoSelecionado}">selected</c:if> >EMPR&Eacute;STIMO PESSOAL (SAX)</option>
                                		<option value="5" <c:if test="${5 == grupoSelecionado}">selected</c:if> >SEGURO EMPR&Eacute;STIMO PESSOAL (SAX)</option>
                                		<option value="6" <c:if test="${6 == grupoSelecionado}">selected</c:if> >GRUPO CALL CENTER</option>                                		
                                    </select>
                                </td>
								<td  class="componente"  width="7%">
									 <label class="label" >Indicadores:</label><span class="requerido">*</span>								
								</td>
                                <td  class="componente"  width="43%">
									<select id="indicadores" name="indicadores" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaIndicadores}">
	                                    	<option value="<c:out value='${item.codIndicador}' />" <c:if test="${item.codIndicador == indicadorSelecionado}">selected</c:if> ><c:out value='${item.descricaoIndicador}' /></option>
										</c:forEach>
                                    </select>
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">M&ecirc;s:</label><span class="requerido">*</span>
 	                        	</td>
 	                        	<td class="componente">
									<select id="mes" name="mes" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="itemMes" items="${listaMes}">
                                    		<option value="<c:out value='${itemMes.mes}' />" <c:if test="${itemMes.mes == mesSelecionado}">selected</c:if> ><c:out value='${itemMes.mesStr}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Lojas:</label>
 	                        	</td>                                
 	                        	<td class="componente">
									<select id="loja" name="loja" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaLojas}">
                                    		<option value="<c:out value='${item.codFilial}' />@<c:out value='${item.codEmpresa}' />" <c:if test="${item.codFilial== lojaSelecionada && item.codEmpresa == empresaSelecionada}">selected</c:if> ><c:out value='${item.codFilial }' /> - <c:out value='${item.descricao }' /></option>
										</c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Ano:</label><span class="requerido">*</span>
 	                        	</td>
 	                        	<td class="componente">
									<select id="ano" name="ano" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="itemAno" items="${listaAno}">
                                    		<option value="<c:out value='${itemAno.ano}' />" <c:if test="${itemAno.ano == anoSelecionado}">selected</c:if> ><c:out value='${itemAno.ano}' /></option>
										</c:forEach>
                                    </select>
                                </td>
								<td class="componente">
 	                        		<label class="label" for="filialDesc">L&iacute;deres:</label>
 	                        	</td>                               
                            	<td class="componente">
									<select id="lider" name="lider" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="item" items="${listaLideres}">
                                    		<option value="<c:out value='${item.idFuncionario}' />" <c:if test="${item.idFuncionario == liderSelecionado}">selected</c:if> ><c:out value='${item.descricaoCargo}' /> <c:out value='${item.nomeFuncionario}' /></option>
										</c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div> 
                <div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoLimpar" type="button" class="button" value="Limpar"/>
							<input id="botaoConsultar" type="button" class="button" value="Filtrar" onclick="filtrar();" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
                <br>
				<div id="tabnav">
					<div id="tabs">
						<c:choose>
							<c:when test="${2 != grupoSelecionado}">
								<a style="width: 32.4%;" id="tabPessoal"      href="#" onclick="validarAbasComErro(1,true);"> <font size="2">Loja</font></a>
								<a style="width: 32.4%;" id="tabResidencial"  href="#" onclick="validarAbasComErro(2,true);"> Operacional</a>
								<a style="width: 32.4%;" id="tabProfissional" href="#" onclick="validarAbasComErro(3,true);"> Lider</a>
								<span id="VM"><span>
							</c:when>
							<c:otherwise>
								<a style="width: 24.1%;" id="tabPessoal"      href="#" onclick="validarAbasComErro(1,true);"> <font size="2">Loja</font></a>
								<a style="width: 24.1%;" id="tabResidencial"  href="#" onclick="validarAbasComErro(2,true);"> Operacional</a>
								<a style="width: 24.1%;" id="tabProfissional" href="#" onclick="validarAbasComErro(3,true);"> Lider</a>
								<span id="VM"><a style="width: 24.1%;" id="tabVM" href="#" onclick="validarAbasComErro(4,true);"> VM</a></span>
							</c:otherwise>
						</c:choose>
					</div>
					
					<span id="relatorios">
						<c:if test="${tabSelecionada == 1}">
							<div>   
								<jsp:include page="/paginas/indicador/loja.jsp"></jsp:include>
							</div>
						</c:if>
						<c:if test="${tabSelecionada == 2}">
							<div> 
								<jsp:include page="/paginas/indicador/operacional.jsp"></jsp:include>
							</div> 
						</c:if>
						<c:if test="${tabSelecionada == 3}">
							<div> 
								<jsp:include page="/paginas/indicador/lideres.jsp"></jsp:include>
							</div>
						</c:if>
						<c:if test="${tabSelecionada == 4}">
							<div> 
								<jsp:include page="/paginas/indicador/vm.jsp"></jsp:include>
							</div>
						</c:if>
					</span>

				</div>  
				<BR>
				<script type="text/javascript">
					var tabSelecionada = "<c:out value='${tabSelecionada}' />";
					if(tabSelecionada.length > 0 ){
						opentab(tabSelecionada,false);
						if(tabSelecionada == 4){
							document.getElementById('tabVM').style.backgroundColor = "#87CEEB";
						}
					}
				</script>
			</form>
		</div>
	</div>
</div>
