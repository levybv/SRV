<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/interface/PerfilBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UsuarioBusinessAjax.js'></script>
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
			fecharModal2();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal2();
		});
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 675px;">
	<form name="detFilial" id="detFilial" method="post"> 
			<br/>
			<table class="tabelaComponente" border="1">
				<tbody> 
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.12:</label>
						</td>
						<td class="componente">
							- Inclus&atilde;o da funcionalidade "Telemarketing" <br/>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.11:</label>
						</td>
						<td class="componente">
							- Ativa&ccedil;&atilde;o/Desativa&ccedil;&atilde;o de escalas por per&iacute;odo <br/>
							- Melhoria de mensagens em "troca de senha" <br/>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.10:</label>
						</td>
						<td class="componente">
							- Inclus&atilde;o da funcionalidade Eleg&iacute;vel Telemarketing <br/>
							- Inclus&atilde;o da funcionalidade Realizado Filial <br/>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.9:</label>
						</td>
						<td class="componente">
							- Inclus&atilde;o do sentido dos indicadores; no c&aacute;lculo do b&ocirc;nus <br/>
							- Considera&ccedil;&atilde;o no c&aacute;lculo do b&ocirc;nus <br/>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.8:</label>
						</td>
						<td class="componente">
							- Par&acirc;metros "Indicador" - Adequa&ccedil;&atilde;o campos obrigat&oacute;rios<br/>
							- Adequa&ccedil;&atilde;o B&ocirc;nus Anual para ano 2016 <br/>
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.7:</label>
						</td>
						<td class="componente">
							- Importa&ccedil;&atilde;o de "Desempenho Colaborador" atrav&eacute;s de arquivo<br/>
							- Nova funcionalidade (Importa&ccedil;&atilde;o PPT)<br/>
							- Parametriza&ccedil;&atilde;o de indicadores por per&iacute;odo (B&ocirc;nus Anual)
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.6:</label>
						</td>
						<td class="componente">
							- Melhorias de configura&ccedil;&otilde;es "Desempenho Colaborador"<br/>
							- Login integrado com AD (Active Direcory)
						</td>
					</tr>
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.5:</label>
						</td>
						<td class="componente">
							- Consulta "Resultado PPR - Lojas"
						</td>
					</tr>					
					<tr>
						<td class="componente">
							<label class="label">Vers&atilde;o 2.4:</label> 
						</td>
						<td class="componente">
	  						- Aceite eletr&ocirc;nico do contrato de metas (B&ocirc;nus Anual)
						</td>
					</tr>
				</tbody>
			</table>
			<br/>
			
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;">
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoCancelar" type="button" class="button" value="Sair" />
				</div>
			</div>
		</div>
</form>
</div>