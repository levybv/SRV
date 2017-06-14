<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>

<script type='text/javascript' src='srvdwr/interface/TelemarketingBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>

<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {

		dwr.engine.setAsync(false);

		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});

		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
		        $("#div-botao").hide();
	           	$("#div-load").show();
		       	document.detMeta.action = "telemarketing.do?operacao=excluiTelemarketing";
	            document.detMeta.submit();
	         }
    	});

		TelemarketingBusinessAjax.obtemTlmktElegivel(numAnoE, numMesE, function (tlmktVO) {
			for (var i=0; i<tlmktVO.listaFuncionarioVO.length; i++) {
				var itemFuncVO = tlmktVO.listaFuncionarioVO[i];
				$("#funcDIV").html($("#funcDIV").html()+itemFuncVO.idFuncionario + " - " + itemFuncVO.nomeFuncionario + "</br>");
			}
			for (var i=0; i<tlmktVO.listaIndicadorTlmktVO.length; i++) {
				var itemIndicVO = tlmktVO.listaIndicadorTlmktVO[i];
				$("#indicDIV").html($("#indicDIV").html()+itemIndicVO.codIndicador + " - " + itemIndicVO.nomeIndicador + " (Meta: " + itemIndicVO.numMeta + " / Realizado: " + itemIndicVO.numRealizado + ")</br>");
			}
			
		});

		$("#anoDIV").html(numAnoE);
		$("#numAnoE").val(numAnoE);
		$("#mesDIV").html(numMesE);
		$("#numMesE").val(numMesE);
		//$("#funcDIV").html(idFuncE);
		//$("#indicDIV").html(idFuncE);
		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 530px">
	<form name="detMeta" id="detMeta" method="post"> 

		<!--Chave-->
		<input type='hidden' id="numAnoE" name="numAnoE" value=""/>
		<input type='hidden' id="numMesE" name="numMesE" value=""/>
		<input type='hidden' id="idFuncionarioE" name="idFuncionarioE" value=""/>

		<!--Filtro-->
		<input type='hidden' id="idFuncionarioFiltro"    	name="idFuncionarioF"     	value=""/>
		<input type='hidden' id="nomeFuncionarioFiltro"  	name="nomeFuncionarioF"    	value=""/>
		<input type='hidden' id="crachaFiltro"    			name="crachaF"     			value=""/>
		<input type='hidden' id="cpfFuncionarioFiltro"  	name="cpfFuncionarioF"   	value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="30%">
						<label class="label">Ano:</label> 
					</td>
					<td class="componente" >
						<div id="anoDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" width="30%">
						<label class="label">M&ecirc;s:</label> 
					</td>
					<td class="componente" >
						<div id="mesDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" width="30%">
						<label class="label">Funci&oacute;narios Eleg&iacute;veis:</label> 
					</td>
					<td class="componente" >
						<div id="funcDIV"></div>
					</td>
				</tr>				
				<tr>
					<td class="componente" width="30%">
						<label class="label">Indicadores (Meta/Realizado):</label> 
					</td>
					<td class="componente" >
						<div id="indicDIV"></div>
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