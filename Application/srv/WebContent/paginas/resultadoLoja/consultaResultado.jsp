<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type='text/javascript' src='srvdwr/interface/DataBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.resultadoLoja.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Resultado PPR - Loja</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">

				<input type='hidden' name='tabSelecionada' id='tabSelecionada' value="<c:out value='${tabSelecionada}' />">
				<input type='hidden' name='tabAnterior' id='tabAnterior' value='<c:out value='${tabAnterior}' />'>
				<input type='hidden' name='proxTab' id='proxTab' value='1'>
				<input type='hidden' name='periodoFiltro' id='periodoFiltro' value='<c:out value='${periodoFiltro}' />'>
				<input type='hidden' name='tipoFilial' id='tipoFilial' value='<c:out value='${tipoFilial}' />'>

				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    		<tr>
								<td class="componente" width="33%">
									<label class="label">Per&iacute;odo:</label><span class="requerido">*</span>
									<select id="periodoF" name="periodoF" class="campo">
										<option value="" selected>Selecione...</option>
									</select>
								</td>
								<td class="componente"  width="33%">
									<label class="label">Loja:</label><span class="requerido">*</span>
									<select id="idFilialF" name="idFilialF" class="campo">
										<option value="" selected>Selecione...</option>
                                    	<c:forEach var="itemFilial" items="${listaFilial}">
                                    		<option value="<c:out value='${itemFilial.codFilial}'/>" <c:if test="${itemFilial.codFilial == idFilialF}">selected</c:if> ><c:out value='${itemFilial.codFilial} - ${itemFilial.descricao}'/></option>
										</c:forEach>
									</select>
								</td>
                                <td class="componente"  width="34%">
									<label class="label">Funcion&aacute;rio:</label>
									<select id="idFuncF" name="idFuncF" class="campo" <c:if test="${listaFuncionario == null}">disabled="disabled"</c:if>>
										<option value="" selected>Selecione...</option>
                                    	<c:forEach var="itemFunc" items="${listaFuncionario}">
                                    		<option value="<c:out value='${itemFunc.idFuncionario}'/>" <c:if test="${itemFunc.idFuncionario == idFuncFiltro}">selected</c:if> ><c:out value='${itemFunc.idFuncionario}'/> - <c:out value='${itemFunc.nomeFuncionario}'/></option>
										</c:forEach>
									</select>
                                </td>
                            </tr>
                    		<tr>
								<td class="componente" width="33%">
									<a id="simuladoresPPR" href="#"><b>Simuladores PPR</b></a>
								</td>
								<td class="componente" width="33%">
								</td>
                                <td class="componente" width="34%">
                                </td>
                            </tr>
                    		<tr>
								<td class="componente" colspan="3">
									<c:if test="${sessionScope.mensagemVO.exibeMensagem}">
										<div style="background-color:#e86268;border: 1px solid #7592AE;font-size:8pt;font-weight:bold; width=100%;">
											&nbsp;</br>
											<c:out value="${mensagemVO.textoMensagem}"/>
											</br>&nbsp;
										</div>
									</c:if>
									<c:if test="${mensagemVO.exibePopup}">
										<script type="text/javascript">
											alert('<c:out value="${mensagemVO.textoPopup}"/>');
										</script>
									</c:if>
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
						<a style="width: 32.4%;" id="tabPessoal"      href="#" onclick="validarAbasComErro(1,true);"> Loja</a>
						<a style="width: 32.4%;" id="tabResidencial"  href="#" onclick="validarAbasComErro(2,true);"> Operacional</a>
						<a style="width: 32.4%;" id="tabProfissional" href="#" onclick="validarAbasComErro(3,true);"> Lider</a>
					</div>
					<span id="relatorios">
						<c:if test="${tabSelecionada == 1}">
							<div>
								<jsp:include page="/paginas/resultadoLoja/resultadoLoja.jsp"></jsp:include>
							</div>
						</c:if>
						<c:if test="${tabSelecionada == 2}">
							<div>
								<jsp:include page="/paginas/resultadoLoja/resultadoOperacional.jsp"></jsp:include>
							</div>
						</c:if>
						<c:if test="${tabSelecionada == 3}">
							<div>
								<jsp:include page="/paginas/resultadoLoja/resultadoLideres.jsp"></jsp:include>
							</div>
						</c:if>
					</span>

				</div>  
				<BR>
				<script type="text/javascript">
					var tabSelecionada = "<c:out value='${tabSelecionada}' />";
					if(tabSelecionada.length > 0 ){
						opentab(tabSelecionada,false);
					}
				</script>
			</form>
		</div>
	</div>
</div>
