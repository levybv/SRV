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
		
		jQuery(function ($) {
			$("#cnpj").mask("99.999.999/9999-99");
			$("#dataInaug").mask("99/99/9999");
		});
		
		FilialBusinessAjax.obterListaTipoFiliais(
		function (filiais) {
			if (filiais != null) {
					var options1 = document.getElementById('idDescrTipoFiliais');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options1[options1.length] = new Option(filialVO.descricao,filialVO.codTpFilial,false, false);
					}
							
				}
	    });
		
		
		$("#detFilial").validate({
	    	rules: {
	    	idDescrTipoFiliais: {required:true},
	    	descricao: {required:true},
	    	codFil: {required:true},
	    	checkAtivo: {required:true},
	    	uf: {required:true},
	    	cnpj: {required:true},
	    	percentualMeta: {required:true},
	    	
		     	},
		     	messages: {
		     	idDescrTipoFiliais:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	descricao:           {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	codFil:              {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	checkAtivo:          {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	uf:                  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	cnpj:                {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	percentualMeta:      {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		         },                  
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detFilial.action = "filial.do?operacao=incluirFilial";   
	            document.detFilial.submit();
	         }
    	});
      

	});
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 320px;">
	<form name="detFilial" id="detFilial" method="post"> 
			<input type='hidden' name="codEmp" value="1">
			<input type='hidden' id="codFilial1" name="codFilial1" value="">
			
			</br>
			
			<table class="tabelaComponente">
				<tbody> 
					
					<tr> 
						<td class="componente">
							<label class="label" >Descri&ccedil;&atilde;o<span class="requerido">*</span></label> 
	  						<input type="text" class="campo2" id="descricao" name="descricao" value="" size="40" maxlength="50" />
						</td>
					</tr>
									
					<tr>
					    <td class="componente">
                            <label class="label" for="filialDesc">Tipo Filial:</label>
							<select id="idDescrTipoFiliais" name="idDescrTipoFiliais" class="campo">
								<option value="" selected>Selecione...</option>
                             </select>
                        </td>
					</tr>
					
					<tr>
						<td class="componente">
							<label class="label" >C&oacute;digo Filial:</label> 
							<input type="text" class="campo" id="codFil"  name="codFil" maxlength="5" size="5"/>
						</td>
					</tr>
					
					<tr>
						<td class="componente">
							<label class="label" >Ativo:</label> 
							<input type="checkbox" class="campo" id="checkAtivo"  name="checkAtivo"   />
						</td>
					</tr>
					
					<tr>
						<td class="componente">
							<label class="label" >UF:</label> 
							<input type="text" class="campo" id="uf"  name="uf" maxlength="2" size="10" />
						</td>
					</tr>
					
					<tr>
						<td class="componente">
							<label class="label" >CNPJ:</label> 
	  						<input type="text" class="campo" id="cnpj" name="cnpj" value="" size="25" maxlength="25" />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Percentual Meta:</label> 
							<input type="checkbox" class="campo" id="percentualMeta"  name="percentualMeta"   />
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label" >Data de inauguração:</label> 
							<input type="text" class="campo" id="dataInaug" name="dataInaug" value="" size="12" maxlength="10" />
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