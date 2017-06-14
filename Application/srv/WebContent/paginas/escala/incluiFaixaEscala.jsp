<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
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
		
		UnidadeBusinessAjax.obtemUnidades(
			function (unidades) {
				if (unidades != null) {
					var options1 = $('#idUnidadeFaixaES').attr('options');
					var options2 = $('#idUnidadeRealizadoES').attr('options');
					for (var i=0; i<unidades.length; i++) {
						var unidadeVO = unidades[i];
						options1[options1.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
						options2[options2.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
					}
				}
		});
		
		$("#sequencialES").numeric();
		$("#faixaInicialES").keypress(function(){
			return mascara(this, moedaSemRSNegativo);
		});
		$("#faixaFinalES").keypress(function(){
			return mascara(this, moedaSemRSNegativo);
		});
		$("#realizadoES").keypress(function(){
			return mascara(this, moedaSemRS);
		});
		
		
		$("#detMeta").validate({
	    	 rules: {
	    			sequencialES:   			{required:true},
	    			idUnidadeFaixaES: 			{required:true},
	    			faixaInicialES: 			{required:true},
	    			faixaFinalES: 				{required:true},
	    			idUnidadeRealizadoES: 		{required:true},
	    			realizadoES: 				{required:true}
		     },
		     messages: {
			     	sequencialES:    			{required:	   	'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idUnidadeFaixaES:  			{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	faixaInicialES:  			{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	faixaFinalES:  				{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	idUnidadeRealizadoES:  		{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	realizadoES:  				{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "faixaEscala.do?operacao=incluiFaixaEscala";   
		            document.detMeta.submit();
	         }
    	});
    	
		$("#idEscalaFiltro").val(idEscalaF);
		    	
    });
    
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 500px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idEscalaFiltro"  	 name="idEscalaF"   value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="30%">
						<label class="label">Sequência:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="sequencialES" name="sequencialES" value="" maxlength="2" size="2"  />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Unidade Faixa:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeFaixaES" name="idUnidadeFaixaES" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Faixa Inicial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="faixaInicialES" name="faixaInicialES" value="" maxlength="14" size="15" />
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Faixa Final:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="faixaFinalES" name="faixaFinalES" value="" maxlength="14" size="15" />
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Unidade Realizado:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeRealizadoES" name="idUnidadeRealizadoES" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Realizado:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="realizadoES" name="realizadoES" value="" maxlength="14" size="15" />
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