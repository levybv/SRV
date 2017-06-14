<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/SalarioBaseBusinessAjax.js'></script>
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
		
		SalarioBaseBusinessAjax.obtemSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes,
			function (salarioBaseVO) {
				$("#idPeriodoAF").val(salarioBaseVO.ano + "-" + salarioBaseVO.mes);
				$("#divPeriodo").html(salarioBaseVO.periodoFormatado);
				$("#idEmpresaAF").val(salarioBaseVO.idEmpresa);
				$("#idFilialAF").val(salarioBaseVO.idFilial);
				$("#divFilial").html(salarioBaseVO.filialFormatado);
				$("#idFuncionarioAF").val(salarioBaseVO.idFuncionario);
				$("#divFuncionario").html(salarioBaseVO.funcionarioFormatado);
		});
		
		$("#detMeta").validate({
	    	rules: {},
	     	messages: {},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "agrupaFilial.do?operacao=excluiAgrupamentoFilial";   
	            document.detMeta.submit();
	        }
    	});
    	
		$("#idFuncionarioFiltro").val(idFuncionarioF);
		$("#nomeFuncionarioFiltro").val(nomeFuncionarioF);
		$("#anoFiltro").val(anoF);
		$("#mesFiltro").val(mesF);
		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 520px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioF"     		value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioF"     	value=""/>
		<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
		<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
		
		<!--Chave-->
		<input type='hidden' id="idPeriodoAF"  			name="idPeriodoAF"   				value=""/>
		<input type='hidden' id="idEmpresaAF"    		name="idEmpresaAF"     				value=""/>
		<input type='hidden' id="idFilialAF"    		name="idFilialAF"     				value=""/>
		<input type='hidden' id="idFuncionarioAF"    	name="idFuncionarioAF"     			value=""/>
		
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
						<label class="label">Funcionário:</label> 
					</td>
					<td class="componente" >
						<div id="divFuncionario"></div>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<div id="divFilial"></div>
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