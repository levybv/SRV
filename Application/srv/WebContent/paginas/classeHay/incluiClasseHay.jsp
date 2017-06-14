<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/ClasseHayBusinessAjax.js'></script>
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
		
		jQuery.validator.addMethod('codigoValido', function(value, element, params) {
			dwr.engine.setAsync(false);
			ClasseHayBusinessAjax.codigoJaExiste(value, 
				function (codigoJaExiste) {
					if (codigoJaExiste) {
						$("#codigoJaExiste").val("S");
						return false;
					} else {
						$("#codigoJaExiste").val("N");
						return true;
					} 
			});
			return ($("#codigoJaExiste").val() == "N");
		});
		
		$("#idClasseHayCH").numeric();
		$("#salarioMinimoCH").keypress(function(){
			return mascara(this, moeda);
		});	
		$("#salarioMaximoCH").keypress(function(){
			return mascara(this, moeda);
		});			
		
		$("#detClasseHay").validate({
	    	rules: {
	    			idClasseHayCH:   {required:true, codigoValido:true},
	    			descricaoCH:     {required:true},
	    			salarioMinimoCH: {required:true},
	    			salarioMaximoCH: {required:true}
		     	},
		     	messages: {
			     	idClasseHayCH:    {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
			     	                   codigoValido:   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo j&aacute; cadastrado.</span>'},
			     	descricaoCH:      {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	salarioMinimoCH:  {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	salarioMaximoCH:  {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detClasseHay.action = "classeHay.do?operacao=incluiClasseHay";   
	            document.detClasseHay.submit();
	         }
    	});
    	
		$("#idClasseHayFiltro").val(idClasseHayF);
		$("#descricaoFiltro").val(descricaoF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 490px">
	<form name="detClasseHay" id="detClasseHay" method="post"> 
			
		<input type='hidden' id="codigoJaExiste" 	 name="codigoJaExiste" value="">
		<input type='hidden' id="idClasseHayFiltro"  name="idClasseHayF"   value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"     value=""/>
					
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" >
						<label class="label">Código:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="idClasseHayCH" name="idClasseHayCH" value="" size="4" maxlength="4" />
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoCH" name="descricaoCH" value="" size="40" maxlength="100" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Sal&aacute;rio M&iacute;nimo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="salarioMinimoCH" name="salarioMinimoCH" value="" maxlength="14" size="15"  />
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Sal&aacute;rio M&aacute;ximo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="salarioMaximoCH" name="salarioMaximoCH" value="" maxlength="14" size="15"  />
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>