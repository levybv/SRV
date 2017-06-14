
var fields;

$(document).ready(function() {	
	
	// TECLAS DE ATALHO
	 $(document).bind('keydown', 'esc', function () {fecharTodosOsModais();liberarBotao();});
	
	//AUTO-TAB
	$('input').keyup(
		function(event){
			var size = $(this).attr('maxlength');
			autoTab(this,size,event,false);
		}
	);

	// VALIDAÇÕES E CONDIÇÕES
	$("#form").validate({
    	rules: {
     	},
     	messages: {
        },
        submitHandler : function(form){
        }
    });
		
	// FOCO DOS CAMPOS
	$('input:[type=text], select').focus(function () {
		jQuery(this).removeClass("campo");
		jQuery(this).addClass("focusGained");   
	});
	$('input:[type=text], select').blur(function () {
		jQuery(this).removeClass("focusGained");
		jQuery(this).addClass("campo");
	});
	
	fields = $('input, select');
	fieldsall = fields;
	if ($.browser.mozilla) {
		$(fields).keypress(checkForEnter);
	} else {
		$(fields).keydown(checkForEnter);
	} 
});