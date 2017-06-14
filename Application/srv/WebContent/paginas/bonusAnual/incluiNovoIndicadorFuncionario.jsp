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

		$("#verbaRHID").numeric();

		$("#valorPremioID").keypress(function(){
			return mascara(this, moeda);
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

		//Dados da tela atual
		$("#periodoFiltroN").val($("#periodoAtual").val());
		$("#idFuncionarioSelecionadoN").val($("#idFuncionarioSelecionado").val());
		$("#nomeFuncionarioSelecionadoN").val($("#nomeFuncionarioSelecionado").val());
		$("#cargoFuncionarioSelecionadoN").val($("#cargoFuncionarioSelecionado").val());
		$("#centroCustoFuncionarioSelecionadoN").val($("#centroCustoFuncionarioSelecionado").val());		

		//Dados da tela anterior
		$("#idFuncionarioN").val($("#idFuncionario").val());
		$("#nomeFuncionarioN").val($("#nomeFuncionario").val());
		$("#crachaN").val($("#cracha").val());
		$("#cpfFuncionarioN").val($("#cpfFuncionario").val());		

		$("#detIndicador").validate({
	    	rules: {
	    			descricaoID:     	{required:true},
	    			idGrupoID:   	 	{required:true},
	    			formulaConceitoID:  {required:true}	
		     	},
		     	messages: {
			     	descricaoID:		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idGrupoID:        	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	formulaConceitoID:	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detIndicador.action = "bonusAnualFuncionario.do?operacao=incluiNovoIndicador";
	            document.detIndicador.submit();
	         }
    	});

		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoFiltro").val(descricaoF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 560px">
	<form name="detIndicador" id="detIndicador" method="post"> 
			
		<!--Dados da tela atual-->
		<input type='hidden' id="periodoFiltroN"  					 name="periodos"   							value=""/>
		<input type='hidden' id="idFuncionarioSelecionadoN"    		 name="idFuncionarioSelecionado"     		value=""/>
		<input type='hidden' id="nomeFuncionarioSelecionadoN"    	 name="nomeFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="cargoFuncionarioSelecionadoN"    	 name="cargoFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="centroCustoFuncionarioSelecionadoN" name="centroCustoFuncionarioSelecionado" 	value=""/>

		<!--Dados da tela anterior-->
		<input type='hidden' id="idFuncionarioN"    	name="idFuncionario"     		value=""/>
		<input type='hidden' id="nomeFuncionarioN"   	name="nomeFuncionario"    		value=""/>
		<input type='hidden' id="crachaN"    			name="cracha"    				value=""/>
		<input type='hidden' id="cpfFuncionarioN"  		name="cpfFuncionario" 			value=""/>
					
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" colspan="2">
						<label class="label">
							<br>
							<span class="requerido">Observação: </span>
							Caso o indicador desejado não esteja na listagem anterior, poderá ser criado agora.
							Porém só aparecerá na listagem após a prévia revisão e autorização do RH.
							<br><br>
						</label> 
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoID" name="descricaoID" value="" size="60" maxlength="100" />
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Conceito:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<textarea rows="4" cols="45" id="formulaConceitoID" name="formulaConceitoID"></textarea>
						Restam <span id="charsLeft"></span> carateres.
  						<!--input type="text" class="campo2" id="formulaConceitoID" name="formulaConceitoID" value="" size="60" maxlength="600" / -->
					</td>	
				</tr>				

				<tr>
					<td class="componente" >
						<label class="label">Fórmula:</label> 
					</td>
					<td class="componente" >
						<textarea rows="4" cols="45" id="formulaID" name="formulaID"></textarea>
						Restam <span id="charsLeft2"></span> carateres.
  						<!--input type="text" class="campo2" id="formulaID" name="formulaID" value="" size="60" maxlength="300" / -->
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
						<input type="checkbox" name="flg_Preench_Ating_igual_RealzID" id="flg_Preench_Ating_igual_RealzID">
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