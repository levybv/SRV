<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/MetaFuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		MetaFuncionarioBusinessAjax.obtemMetaFuncionario(mes, ano, idFuncionario, idIndicador,
			function (metaFuncionarioVO) {
				$("#idPeriodoMF").val(metaFuncionarioVO.ano + "-" + metaFuncionarioVO.mes);
				$("#idFuncionarioMF").val(metaFuncionarioVO.idFuncionario);
				$("#idIndicadorMF").val(metaFuncionarioVO.idIndicador);
				$("#divPeriodo").html(metaFuncionarioVO.periodoFormatado);
				$("#divIndicador").html(metaFuncionarioVO.indicadorFormatado);
				$("#divFuncionario").html(metaFuncionarioVO.funcionarioFormatado);
				$("#divMeta").html(metaFuncionarioVO.valorMetaFormatado);
				if (metaFuncionarioVO.descricaoMeta != null) {
					$("#divDescricao").html(metaFuncionarioVO.descricaoMeta);
				}
		}); 				
		
		$("#detMeta").validate({
	    	rules: {},
		    messages: {},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "metaFuncionario.do?operacao=excluiMetaFuncionario";   
	            document.detMeta.submit();
	        }
    	});
    	
		$("#mesFiltro").val(mesF);
		$("#anoFiltro").val(anoF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoIndicadorFiltro").val(descricaoIndicadorF);
		$("#idFuncionarioFiltro").val(idFuncionarioF);
		$("#nomeFuncionarioFiltro").val(nomeFuncionarioF);
		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 520px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
			<!--Filtro-->
			<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
			<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
			<input type='hidden' id="idIndicadorFiltro"    		name="idIndicadorF"     		value=""/>
			<input type='hidden' id="descricaoIndicadorFiltro"  name="descricaoIndicadorF"     	value=""/>
			<input type='hidden' id="idFuncionarioFiltro"       name="idFuncionarioF"     		value=""/>
			<input type='hidden' id="nomeFuncionarioFiltro"     name="nomeFuncionarioF"     	value=""/>
			
			<!--Chave-->
			<input type='hidden' id="idPeriodoMF"  			name="idPeriodoMF"   				value=""/>
			<input type='hidden' id="idFuncionarioMF"    	name="idFuncionarioMF"     			value=""/>
			<input type='hidden' id="idIndicadorMF"    		name="idIndicadorMF"     			value=""/>
			
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="20%">
							<label class="label">Período:</label> 
						</td>
						<td class="componente" >
							<div id="divPeriodo"></div>
						</td>
					</tr>				
					<tr>
						<td class="componente" >
							<label class="label">Indicador:</label> 
						</td>
						<td class="componente" >
							<div id="divIndicador"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" >
							<label class="label">Funcionário:</label> 
						</td>
						<td class="componente" >
							<div id="divFuncionario"></div>
						</td>
					</tr>					
					<tr>
						<td class="componente" >
							<label class="label">Valor Meta:</label> 
						</td>
						<td class="componente" >
							<div id="divMeta"></div>
						</td>
					</tr>				
					<tr>
						<td class="componente" >
							<label class="label">Descrição:</label> 
						</td>
						<td class="componente" >
							<div id="divDescricao"></div>
						</td>
					</tr>				
				</tbody>
			</table>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Excluir" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div>