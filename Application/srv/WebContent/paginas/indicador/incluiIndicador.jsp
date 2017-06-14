<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoIndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/EscalaBusinessAjax.js'></script>

<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>


<script type="text/javascript" src="js/lib/jquery.limit-1.2.source.js"  charset="UTF8" ></script>

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
		
		
		//Formatação dos campos
		
		$("#verbaRHID").numeric();
		
		$("#checkOutraFonteID").click(function(){
			if ($("#checkOutraFonteID").attr('checked') == true) {
				$("#outraFonteID").attr('disabled','');
				$("#fonteID").attr('disabled','disabled');
				$("#outraFonteID").focus();
			} else {
				$("#outraFonteID").attr('disabled','disabled');
				$("#fonteID").attr('disabled','');
			}
			
		});	
		
		$('#formulaConceitoID').limit('600','#charsLeft');
		$('#formulaID').limit('300','#charsLeft2');
		
		GrupoIndicadorBusinessAjax.obtemGruposIndicadoresCorporativos(
			function (grupos) {
				if (grupos != null) {
					var options = $('#idGrupoID').attr('options');
					for (var i=0; i<grupos.length; i++) {
						var grupoVO = grupos[i];
						options[options.length] = new Option(grupoVO.descricaoGrupoIndicador, grupoVO.idGrupoIndicador, false, false);
					}
				}
		});
		
    	EscalaBusinessAjax.obtemEscalas(
			function (escalas) {
				if (escalas != null) {
					var options = $('#idEscalaID').attr('options');
					for (var i=0; i<escalas.length; i++) {
						var escalaVO = escalas[i];
						options[options.length] = new Option(escalaVO.descricaoEscala, escalaVO.idEscala, false, false);
					}
				}
		});			

		IndicadorBusinessAjax.obtemListaFonte(function (listaFonte) {
			if (listaFonte != null) {
				var options = $('#fonteID').attr('options');
				for (var i=0; i<listaFonte.length; i++) {
					options[options.length] = new Option(listaFonte[i], listaFonte[i], false, false);
				}
			}
    	});

		IndicadorBusinessAjax.obtemListaDiretoria(function (listaDiretoria) {
			if (listaDiretoria != null) {
				var options = $('#diretoriaID').attr('options');
				for (var i=0; i<listaDiretoria.length; i++) {
					options[options.length] = new Option(listaDiretoria[i], listaDiretoria[i], false, false);
				}
			}
    	});

		$("#detIndicador").validate({
	    	rules: {
	    			descricaoID:     	{required:true},
	    			sentidoIndicadorID:	{required:true},
	    			idGrupoID:   	 	{required:true}
		     	},
		     	messages: {
			     	descricaoID:		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	sentidoIndicadorID: {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idGrupoID:        	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detIndicador.action = "indicador.do?operacao=incluiIndicador";   
	            document.detIndicador.submit();
	         }
    	});
    	
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoFiltro").val(descricaoF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 725px">
	<form name="detIndicador" id="detIndicador" method="post"> 
			
		<input type='hidden' id="idIndicadorFiltro"  name="idIndicadorF"   value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"     value=""/>
					
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoID" name="descricaoID" value="" size="60" maxlength="100" />
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Conceito:</label> 
					</td>
					<td class="componente" >
						<textarea class="campo2" rows="4" cols="80" id="formulaConceitoID" name="formulaConceitoID"></textarea>
						Restam <span id="charsLeft"></span> carateres.
  						<!--input type="text" class="campo2" id="formulaConceitoID" name="formulaConceitoID" value="" size="60" maxlength="600" / -->
					</td>	
				</tr>				
				
				<tr>
					<td class="componente" >
						<label class="label">Fórmula:</label> 
					</td>
					<td class="componente" >
						<textarea rows="4" class="campo2" cols="80" id="formulaID" name="formulaID"></textarea>
						Restam <span id="charsLeft2"></span> carateres.
  						<!--input type="text" class="campo2" id="formulaID" name="formulaID" value="" size="60" maxlength="300" / -->
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Sentido do Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="sentidoIndicadorID" name="sentidoIndicadorID" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="C" >Melhor para Cima</option>
							<option value="B" >Melhor para Baixo</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Grupo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idGrupoID" name="idGrupoID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Verba RH:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="verbaRHID" name="verbaRHID" value="" size="6" maxlength="6" />
					</td>	
				</tr>		
				<tr>
					<td class="componente" >
						<label class="label">Ativo:</label> 
					</td>
					<td class="componente" >
						<select id="ativoID" name="ativoID" class="campo">
							<option value="S" selected>Sim</option>
							<option value="N">N&atilde;o</option>
						</select>
					</td>
				</tr>		
				<tr>
					<td class="componente" >
						<label class="label">Escala:</label> 
					</td>
					<td class="componente" >
						<select id="idEscalaID" name="idEscalaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Preenchimento do Atingimento Igual ao Realizado:</label> 
					</td>
					<td class="componente" >
						<input type="checkbox" class="campo" name="flg_Preench_Ating_igual_RealzID" id="flg_Preench_Ating_igual_RealzID">
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Diretoria:</label> 
					</td>
					<td class="componente" >
						<select id="diretoriaID" name="diretoriaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>					
				<tr>
					<td class="componente">
						<label class="label">Fonte:</label> 
					</td>
					<td class="componente">
						<select id="fonteID" name="fonteID" class="campo" style="width:200px;">
							<option value="" selected>Selecione...</option>
						</select>
						&nbsp;&nbsp;&nbsp;
						<label class="label">Outra:</label>
						<input type="checkbox" class="campo" name="checkOutraFonteID" id="checkOutraFonteID"/>
						<input type="text" class="campo2" id="outraFonteID" name="outraFonteID" value="" size="40" maxlength="100" disabled="disabled" />
					</td>
				</tr>					
				<tr>
					<td class="componente">
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