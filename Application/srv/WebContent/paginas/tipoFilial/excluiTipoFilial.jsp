<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
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
		
		$("#detFilial").validate({
	    	
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detFilial.action = "tipoFilial.do?operacao=excluirTipoFilial";   
	            document.detFilial.submit();
	         }
    	});
      
      $("#codTipoFilialF").val(codTpFilial);
      $("#descrTipoFilialF").val(descricaoTpFil); 
      $("#codFilial").html(codTpFilial);
      $("#descricaoFil").html(descricaoTpFil);      

	});
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 320px;">
	<form name="detFilial" id="detFilial" method="post"> 
			<input type='hidden' name="codEmp" value="1">
			
			<input type="hidden" id="codTipoFilialF"    name="codTipoFilialF"    value=""/>
			<input type="hidden" id="descrTipoFilialF"  name="descrTipoFilialF"  value=""/>
		
		<br>
		</br>
		
			<table class="tabelaComponente">
			<tbody> 
					
					<tr>
						<td class="componente" >
							<label class="label" >C&oacute;digo Filial:</label> 
							<span id="codFilial"></span>
						</td>
					</tr>
					<tr>
						<td class="componente" >
							<label class="label" >Descri&ccedil;&atilde;o Filial:</label> 
							<span id="descricaoFil"></span>
						</td>
					</tr>
					
					
				</tbody>
		</table>
		
		<br>
		</br>
			
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