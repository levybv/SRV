<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
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
	    	rules: {
	    	descricao: {required:true},
	    	cnpj: {cnpj: true,required:true}
		     	},
		     	messages: {
		     	descricao:  {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo descri&ccedil;&atilde;o &Eacute; obrigat&oacute;rio.</span>'},
		     	cnpj:   {cnpj: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> CNPJ inv&aacute;lido.</span>',   required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo CNPJ &Eacute; obrigat&oacute;rio.</span>'}		     	
        		//cliCpf: {cpf:  '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> CPF inválido.       </span>',   required: '  <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> O campo CPF é obrigatório.</span>'},
		         },                  
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detFilial.action = "filial.do?operacao=alterarModalFilial";   
	            document.detFilial.submit();
	         }
    	});
    	
    	jQuery(function ($) {
			$("#cnpj").mask("99.999.999/9999-99");
			$("#dataInaug").mask("99/99/9999");
		   
		});

		$("#codFilialL").html(filialCod);
		$("#codFilial1").val(filialCod);
	    $("#descricao").val(descricaoFil);
	    $("#dataInaug").val(dtInaug);
	    //$("#checkAtivo").val(flgAtivo);
	    $("#cnpj").val(cnpj);
	    //$("#percentualMeta").val(flagMeta100);
	    
	    if(flgAtivo == 'true'){
		   document.getElementById('checkAtivo').checked = true;
		}
		
		if(flagMeta100 == 'true'){
		   document.getElementById('percentualMeta').checked = true;
		}

			FilialBusinessAjax.obterListaTipoFiliais(function (filiais) {
				if (filiais != null) {
					var options1 = document.getElementById('idDescrTipoFiliais');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options1[options1.length] = new Option(filialVO.descricao,filialVO.codTpFilial,false, false);
					}
					if (filialVO.codTpFilial != null) {
						$("#idDescrTipoFiliais").val(tpfilialCod);
					}		
				}
			});

	});


</script>



<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 320px;">
	<form name="detFilial" id="detFilial" method="post"> 
			<input type='hidden' name="codEmp" value="1">
			<input type='hidden' id="codFilial1" name="codFilial1" value="">

			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" >
							<label class="label" for="cliCpf">Código:</label> 
							<span id='codFilialL'></span>
						</td>
					</tr>
					
					<tr> 
						<td class="componente">
							<label class="label" >Descri&ccedil;&atilde;o Filial<span class="requerido">*</span></label> 
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
							<label class="label" >Ativo:</label> 
							<input type="checkbox" class="campo" id="checkAtivo"  name="checkAtivo"   />
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