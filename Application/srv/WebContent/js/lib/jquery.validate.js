/*
 * jQuery validation plug-in 1.7
 *
 * http://bassistance.de/jquery-plugins/jquery-plugin-validation/
 * http://docs.jquery.com/Plugins/Validation
 *
 * Copyright (c) 2006 - 2008 JÃ¶rn Zaefferer
 *
 * $Id$
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */
(function($) {

$.extend($.fn, {
	// http://docs.jquery.com/Plugins/Validation/validate
	validate: function( options ) {

		// if nothing is selected, return nothing; can't chain anyway
		if (!this.length) {
			options && options.debug && window.console && console.warn( "nothing selected, can't validate, returning nothing" );
			return;
		}

		// check if a validator for this form was already created
		var validator = $.data(this[0], 'validator');
		if ( validator ) {
			return validator; 
		}
		
		validator = new $.validator( options, this[0] );
		$.data(this[0], 'validator', validator); 
		
		if ( validator.settings.onsubmit ) {
		
			// allow suppresing validation by adding a cancel class to the submit button
			this.find("input, button").filter(".cancel").click(function() {
				validator.cancelSubmit = true;
			});
			
			// when a submitHandler is used, capture the submitting button
			if (validator.settings.submitHandler) {
				this.find("input, button").filter(":submit").click(function() {
					validator.submitButton = this;
				});
			}
		
			// validate the form on submit
			this.submit( function( event ) {
				if ( validator.settings.debug )
					// prevent form submit to be able to see console output
					event.preventDefault();
					
				function handle() {
					if ( validator.settings.submitHandler ) {
						if (validator.submitButton) {
							// insert a hidden input as a replacement for the missing submit button
							var hidden = $("<input type='hidden'/>").attr("name", validator.submitButton.name).val(validator.submitButton.value).appendTo(validator.currentForm);
						}
						validator.settings.submitHandler.call( validator, validator.currentForm );
						if (validator.submitButton) {
							// and clean up afterwards; thanks to no-block-scope, hidden can be referenced
							hidden.remove();
						}
						return false;
					}
					return true;
				}
					
				// prevent submit for invalid forms or custom submit handlers
				if ( validator.cancelSubmit ) {
					validator.cancelSubmit = false;
					return handle();
				}
				if ( validator.form() ) {
					if ( validator.pendingRequest ) {
						validator.formSubmitted = true;
						return false;
					}
					return handle();
				} else {
					validator.focusInvalid();
					return false;
				}
			});
		}
		
		return validator;
	},
	// http://docs.jquery.com/Plugins/Validation/valid
	valid: function() {
        if ( $(this[0]).is('form')) {
            return this.validate().form();
        } else {
            var valid = true;
            var validator = $(this[0].form).validate();
            this.each(function() {
				valid &= validator.element(this);
            });
            return valid;
        }
    },
	// attributes: space seperated list of attributes to retrieve and remove
	removeAttrs: function(attributes) {
		var result = {},
			$element = this;
		$.each(attributes.split(/\s/), function(index, value) {
			result[value] = $element.attr(value);
			$element.removeAttr(value);
		});
		return result;
	},
	// http://docs.jquery.com/Plugins/Validation/rules
	rules: function(command, argument) {
		var element = this[0];
		
		if (command) {
			var settings = $.data(element.form, 'validator').settings;
			var staticRules = settings.rules;
			var existingRules = $.validator.staticRules(element);
			switch(command) {
			case "add":
				$.extend(existingRules, $.validator.normalizeRule(argument));
				staticRules[element.name] = existingRules;
				if (argument.messages)
					settings.messages[element.name] = $.extend( settings.messages[element.name], argument.messages );
				break;
			case "remove":
				if (!argument) {
					delete staticRules[element.name];
					return existingRules;
				}
				var filtered = {};
				$.each(argument.split(/\s/), function(index, method) {
					filtered[method] = existingRules[method];
					delete existingRules[method];
				});
				return filtered;
			}
		}
		
		var data = $.validator.normalizeRules(
		$.extend(
			{},
			$.validator.metadataRules(element),
			$.validator.classRules(element),
			$.validator.attributeRules(element),
			$.validator.staticRules(element)
		), element);
		
		// make sure required is at front
		if (data.required) {
			var param = data.required;
			delete data.required;
			data = $.extend({required: param}, data);
		}
		
		return data;
	}
});

// Custom selectors
$.extend($.expr[":"], {
	// http://docs.jquery.com/Plugins/Validation/blank
	blank: function(a) {return !$.trim("" + a.value);},
	// http://docs.jquery.com/Plugins/Validation/filled
	filled: function(a) {return !!$.trim("" + a.value);},
	// http://docs.jquery.com/Plugins/Validation/unchecked
	unchecked: function(a) {return !a.checked;}
});

// constructor for validator
$.validator = function( options, form ) {
	this.settings = $.extend( true, {}, $.validator.defaults, options );
	this.currentForm = form;
	this.init();
};

$.validator.format = function(source, params) {
	if ( arguments.length == 1 ) 
		return function() {
			var args = $.makeArray(arguments);
			args.unshift(source);
			return $.validator.format.apply( this, args );
		};
	if ( arguments.length > 2 && params.constructor != Array  ) {
		params = $.makeArray(arguments).slice(1);
	}
	if ( params.constructor != Array ) {
		params = [ params ];
	}
	$.each(params, function(i, n) {
		source = source.replace(new RegExp("\\{" + i + "\\}", "g"), n);
	});
	return source;
};

$.extend($.validator, {
	
	defaults: {
		messages: {},
		groups: {},
		rules: {},
		errorClass: "error",
		validClass: "valid",
		errorElement: "label",
		focusInvalid: true,
		errorContainer: $( [] ),
		errorLabelContainer: $( [] ),
		onsubmit: true,
		ignore: [],
		ignoreTitle: false,
		
		onfocusin: function(element) {
			this.lastActive = element;
			// hide error label and remove error class on focus if enabled
			if ( this.settings.focusCleanup && !this.blockFocusCleanup ) {
				this.settings.unhighlight && this.settings.unhighlight.call( this, element, this.settings.errorClass, this.settings.validClass );
				this.errorsFor(element).hide();
			}
		},
		onfocusout: function(element) {
			if ( !this.checkable(element) && (element.name in this.submitted || !this.optional(element)) ) {
				this.element(element);
			}
		},
		onkeyup: function(element) {
			var valor = $(element).val();
			if (valor == null) valor = "";

			try {			
				if (valor.length > 0) {
					while (valor.indexOf("_") != -1) { 
						valor = valor.replace("_", ""); 
					}
				}
			} catch(e) {}
			
			if( valor.length > 0){
				this.errorsFor(element).hide();1
			}
			if ( (element.name in this.submitted || element == this.lastElement) &&  valor.length ==  $(element).attr('maxlength')) {
				this.element(element);
			}
		},
		onclick: function(element) {
			// click on selects, radiobuttons and checkboxes
			if ( element.name in this.submitted )
				this.element(element);
			// or option elements, check parent select in that case
			else if (element.parentNode.name in this.submitted)
				this.element(element.parentNode);
		},
		highlight: function( element, errorClass, validClass ) {
			$(element).addClass(errorClass).removeClass(validClass);
		},
		unhighlight: function( element, errorClass, validClass ) {
			$(element).removeClass(errorClass).addClass(validClass);
		}
	},

	// http://docs.jquery.com/Plugins/Validation/Validator/setDefaults
	setDefaults: function(settings) {
		$.extend( $.validator.defaults, settings );
	},

	messages: {
		required: "This field is required.",
		remote: "Please fix this field.",
		email: "Please enter a valid email address.",
		url: "Please enter a valid URL.",
		date: "Please enter a valid date.",
		dateISO: "Please enter a valid date (ISO).",
		number: "Please enter a valid number.",
		digits: "Please enter only digits.",
		creditcard: "Please enter a valid credit card number.",
		equalTo: "Please enter the same value again.",
		accept: "Please enter a value with a valid extension.",
		//maxlength: $.validator.format("Please enter no more than {0} characters."),
		minlength: $.validator.format("Please enter at least {0} characters."),
		rangelength: $.validator.format("Please enter a value between {0} and {1} characters long."),
		range: $.validator.format("Please enter a value between {0} and {1}."),
		max: $.validator.format("Please enter a value less than or equal to {0}."),
		min: $.validator.format("Please enter a value greater than or equal to {0}.")
	},
	
	autoCreateRanges: false,
	
	prototype: {
		
		init: function() {
			this.labelContainer = $(this.settings.errorLabelContainer);
			this.errorContext = this.labelContainer.length && this.labelContainer || $(this.currentForm);
			this.containers = $(this.settings.errorContainer).add( this.settings.errorLabelContainer );
			this.submitted = {};
			this.valueCache = {};
			this.pendingRequest = 0;
			this.pending = {};
			this.invalid = {};
			this.reset();
			
			var groups = (this.groups = {});
			$.each(this.settings.groups, function(key, value) {
				$.each(value.split(/\s/), function(index, name) {
					groups[name] = key;
				});
			});
			var rules = this.settings.rules;
			$.each(rules, function(key, value) {
				rules[key] = $.validator.normalizeRule(value);
			});
			
			function delegate(event) {
				var validator = $.data(this[0].form, "validator"),
					eventType = "on" + event.type.replace(/^validate/, "");
				validator.settings[eventType] && validator.settings[eventType].call(validator, this[0] );
			}
			$(this.currentForm)
				.validateDelegate(":text, :password, :file, select, textarea", "focusin focusout keyup", delegate)
				.validateDelegate(":radio, :checkbox, select, option", "click", delegate);

			if (this.settings.invalidHandler)
				$(this.currentForm).bind("invalid-form.validate", this.settings.invalidHandler);
		},

		// http://docs.jquery.com/Plugins/Validation/Validator/form
		form: function() {
			this.checkForm();
			$.extend(this.submitted, this.errorMap);
			this.invalid = $.extend({}, this.errorMap);
			if (!this.valid())
				$(this.currentForm).triggerHandler("invalid-form", [this]);
			this.showErrors();
			return this.valid();
		},
		
		checkForm: function() {
			this.prepareForm();
			for ( var i = 0, elements = (this.currentElements = this.elements()); elements[i]; i++ ) {
				this.check( elements[i] );
			}
			return this.valid(); 
		},
		
		// http://docs.jquery.com/Plugins/Validation/Validator/element
		element: function( element ) {
			element = this.clean( element );
			this.lastElement = element;
			this.prepareElement( element );
			this.currentElements = $(element);
			var result = this.check( element );
			if ( result ) {
				delete this.invalid[element.name];
			} else {
				this.invalid[element.name] = true;
			}
			if ( !this.numberOfInvalids() ) {
				// Hide error containers on last error
				this.toHide = this.toHide.add( this.containers );
			}
			this.showErrors();
			return result;
		},

		// http://docs.jquery.com/Plugins/Validation/Validator/showErrors
		showErrors: function(errors) {
			if(errors) {
				// add items to error list and map
				$.extend( this.errorMap, errors );
				this.errorList = [];
				for ( var name in errors ) {
					this.errorList.push({
						message: errors[name],
						element: this.findByName(name)[0]
					});
				}
				// remove items from success list
				this.successList = $.grep( this.successList, function(element) {
					return !(element.name in errors);
				});
			}
			this.settings.showErrors
				? this.settings.showErrors.call( this, this.errorMap, this.errorList )
				: this.defaultShowErrors();
		},
		
		// http://docs.jquery.com/Plugins/Validation/Validator/resetForm
		resetForm: function() {
			if ( $.fn.resetForm )
				$( this.currentForm ).resetForm();
			this.submitted = {};
			this.prepareForm();
			this.hideErrors();
			this.elements().removeClass( this.settings.errorClass );
		},
		
		numberOfInvalids: function() {
			return this.objectLength(this.invalid);
		},
		
		objectLength: function( obj ) {
			var count = 0;
			for ( var i in obj )
				count++;
			return count;
		},
		
		hideErrors: function() {
			this.addWrapper( this.toHide ).hide();
		},
		
		valid: function() {
			return this.size() == 0;
		},
		
		size: function() {
			listAllError = this.errorList;
			return this.errorList.length;
		},
		
		focusInvalid: function() {
			if( this.settings.focusInvalid ) {
				try {
					$(this.findLastActive() || this.errorList.length && this.errorList[0].element || [])
					.filter(":visible")
					.focus()
					// manually trigger focusin event; without it, focusin handler isn't called, findLastActive won't have anything to find
					.trigger("focusin");
				} catch(e) {
					// ignore IE throwing errors when focusing hidden elements
				}
			}
		},
		
		findLastActive: function() {
			var lastActive = this.lastActive;
			return lastActive && $.grep(this.errorList, function(n) {
				return n.element.name == lastActive.name;
			}).length == 1 && lastActive;
		},
		
		elements: function() {
			var validator = this,
				rulesCache = {};
			// Dennys: Deixa o filtro compativel com outros browsers
			var restricao = ":submit, :reset, :image, [disabled], [readonly]";
			
			if(	!$.browser.msie ) {
				restricao = ":submit, :reset, :image, [disabled], [readonly], [readonly='']";
			} else if(parseInt($.browser.version.substring(0,1), 10) >= 9 ) {
				restricao = ":submit, :reset, :image, [disabled], [readonly], [readonly='']";
			}
			
			// select all valid inputs inside the form (no submit or reset buttons)
			// workaround $jQuery([]).add until http://dev.jquery.com/ticket/2114 is solved
			return $([]).add(this.currentForm.elements)
			.filter(":input")
			.not(restricao)
			.not( this.settings.ignore )
			.filter(function() {
				!this.name && validator.settings.debug && window.console && console.error( "%o has no name assigned", this);
			
				// select only the first element for each name, and only those with rules specified
				if ( this.name in rulesCache || !validator.objectLength($(this).rules()) )
					return false;
				
				rulesCache[this.name] = true;
				return true;
			});
		},
		
		clean: function( selector ) {
			return $( selector )[0];
		},
		
		errors: function() {
			return $( this.settings.errorElement + "." + this.settings.errorClass, this.errorContext );
		},
		
		reset: function() {
			this.successList = [];
			this.errorList = [];
			this.errorMap = {};
			this.toShow = $([]);
			this.toHide = $([]);
			this.currentElements = $([]);
		},
		
		prepareForm: function() {
			this.reset();
			this.toHide = this.errors().add( this.containers );
		},
		
		prepareElement: function( element ) {
			this.reset();
			this.toHide = this.errorsFor(element);
		},
	
		check: function( element ) {
			element = this.clean( element );
			
			// if radio/checkbox, validate first element in group instead
			if (this.checkable(element)) {
				element = this.findByName( element.name )[0];
			}
			
			var rules = $(element).rules();
			var dependencyMismatch = false;
			for( method in rules ) {
				var rule = { method: method, parameters: rules[method] };
				try {
					var result = $.validator.methods[method].call( this, element.value.replace(/\r/g, ""), element, rule.parameters );
					
					// if a method indicates that the field is optional and therefore valid,
					// don't mark it as valid when there are no other rules
					if ( result == "dependency-mismatch" ) {
						dependencyMismatch = true;
						continue;
					}
					dependencyMismatch = false;
					
					if ( result == "pending" ) {
						this.toHide = this.toHide.not( this.errorsFor(element) );
						return;
					}
					
					if( !result ) {
						this.formatAndAdd( element, rule );
						return false;
					}
				} catch(e) {
					this.settings.debug && window.console && console.log("exception occured when checking element " + element.id
						 + ", check the '" + rule.method + "' method", e);
					throw e;
				}
			}
			if (dependencyMismatch)
				return;
			if ( this.objectLength(rules) )
				this.successList.push(element);
			return true;
		},
		
		// return the custom message for the given element and validation method
		// specified in the element's "messages" metadata
		customMetaMessage: function(element, method) {
			if (!$.metadata)
				return;
			
			var meta = this.settings.meta
				? $(element).metadata()[this.settings.meta]
				: $(element).metadata();
			
			return meta && meta.messages && meta.messages[method];
		},
		
		// return the custom message for the given element name and validation method
		customMessage: function( name, method ) {
			var m = this.settings.messages[name];
			return m && (m.constructor == String
				? m
				: m[method]);
		},
		
		// return the first defined argument, allowing empty strings
		findDefined: function() {
			for(var i = 0; i < arguments.length; i++) {
				if (arguments[i] !== undefined)
					return arguments[i];
			}
			return undefined;
		},
		
		defaultMessage: function( element, method) {
			return this.findDefined(
				this.customMessage( element.name, method ),
				this.customMetaMessage( element, method ),
				// title is never undefined, so handle empty string as undefined
				!this.settings.ignoreTitle && element.title || undefined,
				$.validator.messages[method],
				"<strong>Warning: No message defined for " + element.name + "</strong>"
			);
		},
		
		formatAndAdd: function( element, rule ) {
			var message = this.defaultMessage( element, rule.method ),
				theregex = /\$?\{(\d+)\}/g;
			if ( typeof message == "function" ) {
				message = message.call(this, rule.parameters, element);
			} else if (theregex.test(message)) {
				message = jQuery.format(message.replace(theregex, '{$1}'), rule.parameters);
			}			
			this.errorList.push({
				message: message,
				element: element
			});
			
			this.errorMap[element.name] = message;
			this.submitted[element.name] = message;
		},
		
		addWrapper: function(toToggle) {
			if ( this.settings.wrapper )
				toToggle = toToggle.add( toToggle.parent( this.settings.wrapper ) );
			return toToggle;
		},
		
		defaultShowErrors: function() {
			for ( var i = 0; this.errorList[i]; i++ ) {
				var error = this.errorList[i];
				this.settings.highlight && this.settings.highlight.call( this, error.element, this.settings.errorClass, this.settings.validClass );
				this.showLabel( error.element, error.message);
			}
			if( this.errorList.length ) {
				this.toShow = this.toShow.add( this.containers );
			}
			if (this.settings.success) {
				for ( var i = 0; this.successList[i]; i++ ) {
					this.showLabel( this.successList[i] );
				}
			}
			if (this.settings.unhighlight) {
				for ( var i = 0, elements = this.validElements(); elements[i]; i++ ) {
					this.settings.unhighlight.call( this, elements[i], this.settings.errorClass, this.settings.validClass );
				}
			}
			this.toHide = this.toHide.not( this.toShow );
			this.hideErrors();
			this.addWrapper( this.toShow ).show();
		},
		
		validElements: function() {
			return this.currentElements.not(this.invalidElements());
		},
		
		invalidElements: function() {
			return $(this.errorList).map(function() {
				return this.element;
			});
		},
		
		showLabel: function(element, message) {
			var label = this.errorsFor( element );
			if ( label.length ) {
				// refresh error/success class
				label.removeClass().addClass( this.settings.errorClass );
			
				// check if we have a generated label, replace the message then
				label.attr("generated") && label.html(message);
			} else {
				// create label
				label = $("<" + this.settings.errorElement + "/>")
					.attr({"for":  this.idOrName(element), generated: true})
					.addClass(this.settings.errorClass)
					.html(message || "");
				if ( this.settings.wrapper ) {
					// make sure the element is visible, even in IE
					// actually showing the wrapped element is handled elsewhere
					label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
				}
				if ( !this.labelContainer.append(label).length )
					this.settings.errorPlacement
						? this.settings.errorPlacement(label, $(element) )
						: label.insertAfter(element);
			}
			if ( !message && this.settings.success ) {
				label.text("");
				typeof this.settings.success == "string"
					? label.addClass( this.settings.success )
					: this.settings.success( label );
			}
			this.toShow = this.toShow.add(label);
		},
		
		errorsFor: function(element) {
			var name = this.idOrName(element);
    		return this.errors().filter(function() {
				return $(this).attr('for') == name;
			});
		},
		
		idOrName: function(element) {
			return this.groups[element.name] || (this.checkable(element) ? element.name : element.id || element.name);
		},

		checkable: function( element ) {
			return /radio|checkbox/i.test(element.type);
		},
		
		findByName: function( name ) {
			// select by name and filter by form for performance over form.find("[name=...]")
			var form = this.currentForm;
			return $(document.getElementsByName(name)).map(function(index, element) {
				return element.form == form && element.name == name && element  || null;
			});
		},
		
		getLength: function(value, element) {
			switch( element.nodeName.toLowerCase() ) {
			case 'select':
				return $("option:selected", element).length;
			case 'input':
				if( this.checkable( element) )
					return this.findByName(element.name).filter(':checked').length;
			}
			return value.length;
		},
	
		depend: function(param, element) {
			return this.dependTypes[typeof param]
				? this.dependTypes[typeof param](param, element)
				: true;
		},
	
		dependTypes: {
			"boolean": function(param, element) {
				return param;
			},
			"string": function(param, element) {
				return !!$(param, element.form).length;
			},
			"function": function(param, element) {
				return param(element);
			}
		},
		
		optional: function(element) {
			return !$.validator.methods.required.call(this, $.trim(element.value), element) && "dependency-mismatch";
		},
		
		startRequest: function(element) {
			if (!this.pending[element.name]) {
				this.pendingRequest++;
				this.pending[element.name] = true;
			}
		},
		
		stopRequest: function(element, valid) {
			this.pendingRequest--;
			// sometimes synchronization fails, make sure pendingRequest is never < 0
			if (this.pendingRequest < 0)
				this.pendingRequest = 0;
			delete this.pending[element.name];
			if ( valid && this.pendingRequest == 0 && this.formSubmitted && this.form() ) {
				$(this.currentForm).submit();
				this.formSubmitted = false;
			} else if (!valid && this.pendingRequest == 0 && this.formSubmitted) {
				$(this.currentForm).triggerHandler("invalid-form", [this]);
				this.formSubmitted = false;
			}
		},
		
		previousValue: function(element) {
			return $.data(element, "previousValue") || $.data(element, "previousValue", {
				old: null,
				valid: true,
				message: this.defaultMessage( element, "remote" )
			});
		}
		
	},
	
	classRuleSettings: {
		required: {required: true},
		email: {email: true},
		url: {url: true},
		date: {date: true},
		dateISO: {dateISO: true},
		dateDE: {dateDE: true},
		number: {number: true},
		numberDE: {numberDE: true},
		digits: {digits: true},
		creditcard: {creditcard: true}
	},
	
	addClassRules: function(className, rules) {
		className.constructor == String ?
			this.classRuleSettings[className] = rules :
			$.extend(this.classRuleSettings, className);
	},
	
	classRules: function(element) {
		var rules = {};
		var classes = $(element).attr('class');
		classes && $.each(classes.split(' '), function() {
			if (this in $.validator.classRuleSettings) {
				$.extend(rules, $.validator.classRuleSettings[this]);
			}
		});
		return rules;
	},
	
	attributeRules: function(element) {
		var rules = {};
		var $element = $(element);
		
		for (method in $.validator.methods) {
			var value = $element.attr(method);
			if (value) {
				rules[method] = value;
			}
		}
		
		// maxlength may be returned as -1, 2147483647 (IE) and 524288 (safari) for text inputs
		if (rules.maxlength && /-1|2147483647|524288/.test(rules.maxlength)) {
			delete rules.maxlength;
		}
		
		return rules;
	},
	
	metadataRules: function(element) {
		if (!$.metadata) return {};
		
		var meta = $.data(element.form, 'validator').settings.meta;
		return meta ?
			$(element).metadata()[meta] :
			$(element).metadata();
	},
	
	staticRules: function(element) {
		var rules = {};
		var validator = $.data(element.form, 'validator');
		if (validator.settings.rules) {
			rules = $.validator.normalizeRule(validator.settings.rules[element.name]) || {};
		}
		return rules;
	},
	
	normalizeRules: function(rules, element) {
		// handle dependency check
		$.each(rules, function(prop, val) {
			// ignore rule when param is explicitly false, eg. required:false
			if (val === false) {
				delete rules[prop];
				return;
			}
			if (val.param || val.depends) {
				var keepRule = true;
				switch (typeof val.depends) {
					case "string":
						keepRule = !!$(val.depends, element.form).length;
						break;
					case "function":
						keepRule = val.depends.call(element, element);
						break;
				}
				if (keepRule) {
					rules[prop] = val.param !== undefined ? val.param : true;
				} else {
					delete rules[prop];
				}
			}
		});
		
		// evaluate parameters
		$.each(rules, function(rule, parameter) {
			rules[rule] = $.isFunction(parameter) ? parameter(element) : parameter;
		});
		
		// clean number parameters
		$.each(['minlength', 'maxlength', 'min', 'max'], function() {
			if (rules[this]) {
				rules[this] = Number(rules[this]);
			}
		});
		$.each(['rangelength', 'range'], function() {
			if (rules[this]) {
				rules[this] = [Number(rules[this][0]), Number(rules[this][1])];
			}
		});
		
		if ($.validator.autoCreateRanges) {
			// auto-create ranges
			if (rules.min && rules.max) {
				rules.range = [rules.min, rules.max];
				delete rules.min;
				delete rules.max;
			}
			if (rules.minlength && rules.maxlength) {
				rules.rangelength = [rules.minlength, rules.maxlength];
				delete rules.minlength;
				delete rules.maxlength;
			}
		}
		
		// To support custom messages in metadata ignore rule methods titled "messages"
		if (rules.messages) {
			delete rules.messages;
		}
		
		return rules;
	},
	
	// Converts a simple string to a {string: true} rule, e.g., "required" to {required:true}
	normalizeRule: function(data) {
		if( typeof data == "string" ) {
			var transformed = {};
			$.each(data.split(/\s/), function() {
				transformed[this] = true;
			});
			data = transformed;
		}
		return data;
	},
	
	// http://docs.jquery.com/Plugins/Validation/Validator/addMethod
	addMethod: function(name, method, message) {
		$.validator.methods[name] = method;
		$.validator.messages[name] = message != undefined ? message : $.validator.messages[name];
		if (method.length < 3) {
			$.validator.addClassRules(name, $.validator.normalizeRule(name));
		}
	},

	methods: {

		// http://docs.jquery.com/Plugins/Validation/Methods/required
		required: function(value, element, param) {
		
			value = retirarMascara(value);
		
		
			// check if dependency is met
			if ( !this.depend(param, element) )
				return "dependency-mismatch";
			switch( element.nodeName.toLowerCase() ) {
			case 'select':
				// could be an array for select-multiple or a string, both are fine this way
				var val = $(element).val();
				return val && val.length > 0;
			case 'input':
				if ( this.checkable(element) )
					return this.getLength(value, element) > 0;
			default:
				return $.trim(value).length > 0;
			}
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/remote
		remote: function(value, element, param) {
			if ( this.optional(element) )
				return "dependency-mismatch";
			
			var previous = this.previousValue(element);
			if (!this.settings.messages[element.name] )
				this.settings.messages[element.name] = {};
			previous.originalMessage = this.settings.messages[element.name].remote;
			this.settings.messages[element.name].remote = previous.message;
			
			param = typeof param == "string" && {url:param} || param; 
			 
			if ( previous.old !== value ) {
				previous.old = value;
				var validator = this;
				this.startRequest(element);
				var data = {};
				data[element.name] = value;
				$.ajax($.extend(true, {
					url: param,
					mode: "abort",
					port: "validate" + element.name,
					dataType: "json",
					data: data,
					success: function(response) {
						validator.settings.messages[element.name].remote = previous.originalMessage;
						var valid = response === true;
						if ( valid ) {
							var submitted = validator.formSubmitted;
							validator.prepareElement(element);
							validator.formSubmitted = submitted;
							validator.successList.push(element);
							validator.showErrors();
						} else {
							var errors = {};
							var message = (previous.message = response || validator.defaultMessage( element, "remote" ));
							errors[element.name] = $.isFunction(message) ? message(value) : message;
							validator.showErrors(errors);
						}
						previous.valid = valid;
						validator.stopRequest(element, valid);
					}
				}, param));
				return "pending";
			} else if( this.pending[element.name] ) {
				return "pending";
			}
			return previous.valid;
		},

		// http://docs.jquery.com/Plugins/Validation/Methods/minlength
		minlength: function(value, element, param) {
			return this.optional(element) || this.getLength($.trim(value), element) >= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/maxlength
//		maxlength: function(value, element, param) {
//			return this.optional(element) || this.getLength($.trim(value), element) <= param;
//		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/rangelength
		rangelength: function(value, element, param) {
			var length = this.getLength($.trim(value), element);
			return this.optional(element) || ( length >= param[0] && length <= param[1] );
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/min
		min: function( value, element, param ) {
			return this.optional(element) || value >= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/max
		max: function( value, element, param ) {
			return this.optional(element) || value <= param;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/range
		range: function( value, element, param ) {
			return this.optional(element) || ( value >= param[0] && value <= param[1] );
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/email
		email: function(value, element) {
			// contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
			return this.optional(element) || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/url
		url: function(value, element) {
			// contributed by Scott Gonzalez: http://projects.scottsplayground.com/iri/
			return this.optional(element) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
		},
        
		// http://docs.jquery.com/Plugins/Validation/Methods/date
		date: function(value, element) {
			return this.optional(element) || !/Invalid|NaN/.test(new Date(value));
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/dateISO
		dateISO: function(value, element) {
			return this.optional(element) || /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/number
		number: function(value, element) {
			return this.optional(element) || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value);
		},
	
		// http://docs.jquery.com/Plugins/Validation/Methods/digits
		digits: function(value, element) {
			return this.optional(element) || /^\d+$/.test(value);
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/creditcard
		// based on http://en.wikipedia.org/wiki/Luhn
		creditcard: function(value, element) {
			if ( this.optional(element) )
				return "dependency-mismatch";
			// accept only digits and dashes
			if (/[^0-9-]+/.test(value))
				return false;
			var nCheck = 0,
				nDigit = 0,
				bEven = false;

			value = value.replace(/\D/g, "");

			for (var n = value.length - 1; n >= 0; n--) {
				var cDigit = value.charAt(n);
				var nDigit = parseInt(cDigit, 10);
				if (bEven) {
					if ((nDigit *= 2) > 9)
						nDigit -= 9;
				}
				nCheck += nDigit;
				bEven = !bEven;
			}

			return (nCheck % 10) == 0;
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/accept
		accept: function(value, element, param) {
			param = typeof param == "string" ? param.replace(/,/g, '|') : "png|jpe?g|gif";
			return this.optional(element) || value.match(new RegExp(".(" + param + ")$", "i")); 
		},
		
		// http://docs.jquery.com/Plugins/Validation/Methods/equalTo
		equalTo: function(value, element, param) {
			// bind to the blur event of the target in order to revalidate whenever the target field is updated
			// TODO find a way to bind the event just once, avoiding the unbind-rebind overhead
			var target = $(param).unbind(".validate-equalTo").bind("blur.validate-equalTo", function() {
				$(element).valid();
			});
			return value == target.val();
		}
		
	}
	
});

// deprecated, use $.validator.format instead
$.format = $.validator.format;

})(jQuery);

// ajax mode: abort
// usage: $.ajax({ mode: "abort"[, port: "uniqueport"]});
// if mode:"abort" is used, the previous request on that port (port can be undefined) is aborted via XMLHttpRequest.abort() 
;(function($) {
	var ajax = $.ajax;
	var pendingRequests = {};
	$.ajax = function(settings) {
		// create settings for compatibility with ajaxSetup
		settings = $.extend(settings, $.extend({}, $.ajaxSettings, settings));
		var port = settings.port;
		if (settings.mode == "abort") {
			if ( pendingRequests[port] ) {
				pendingRequests[port].abort();
			}
			return (pendingRequests[port] = ajax.apply(this, arguments));
		}
		return ajax.apply(this, arguments);
	};
})(jQuery);

// provides cross-browser focusin and focusout events
// IE has native support, in other browsers, use event caputuring (neither bubbles)

// provides delegate(type: String, delegate: Selector, handler: Callback) plugin for easier event delegation
// handler is only called when $(event.target).is(delegate), in the scope of the jquery-object for event.target 
;(function($) {
	// only implement if not provided by jQuery core (since 1.4)
	// TODO verify if jQuery 1.4's implementation is compatible with older jQuery special-event APIs
	if (!jQuery.event.special.focusin && !jQuery.event.special.focusout && document.addEventListener) {
		$.each({
			focus: 'focusin',
			blur: 'focusout'	
		}, function( original, fix ){
			$.event.special[fix] = {
				setup:function() {
					this.addEventListener( original, handler, true );
				},
				teardown:function() {
					this.removeEventListener( original, handler, true );
				},
				handler: function(e) {
					arguments[0] = $.event.fix(e);
					arguments[0].type = fix;
					return $.event.handle.apply(this, arguments);
				}
			};
			function handler(e) {
				e = $.event.fix(e);
				e.type = fix;
				return $.event.handle.call(this, e);
			}
		});
	};
	$.extend($.fn, {
		validateDelegate: function(delegate, type, handler) {
			return this.bind(type, function(event) {
				var target = $(event.target);
				if (target.is(delegate)) {
					return handler.apply(target, arguments);
				}
			});
		}
	});
})(jQuery);
jQuery.validator.addMethod("cpf", function(value, element) {
	
	value = retirarMascara(value);
	
	if(value.length == 0 || value == ''){
		return true;
	}
	
	cpf = value;
		
	while(cpf.length < 11) cpf = "0"+ cpf;
	var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
	var a = [];
	var b = new Number;
	var c = 11;
	for (i=0; i<11; i++){
		a[i] = cpf.charAt(i);
		if (i < 9) b += (a[i] * --c);
	}
	if ((x = b % 11) < 2) { a[9] = 0; } else { a[9] = 11-x; }
	b = 0;
	c = 11;
	for (y=0; y<10; y++) b += (a[y] * c--);
	if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }
	if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)) return false;
	return true;
}, "Informe um CPF válido.");  


jQuery.validator.addMethod("validaNome", function(value,element){
	var valor = jQuery.trim(value);
	
	if (valor == ''){
		return true;
	}else{
		var arrayValor = valor.split(" ");
		if (arrayValor.length >= 2){
			return true;
		}else{
			return false;
		}
	}
	return false;
}, "O campo Nome deve ser preenchido");

jQuery.validator.addMethod("validaNomeZeroFill", function(value,element){
	var valor = jQuery.trim(value);
	if (valor==""){
		return true;
	}else{
		var arrayValor = valor.split(" ");
		if (arrayValor.length < 2){
			return false;
		}else{
			return true;
		}
	}
	return true;
}, "O campo Nome deve ser preenchqqddido");
jQuery.validator.addMethod("dateBR", function(value, element) {
	
	dateValue = value;
	
	dateValue = retirarMascara(dateValue);
	
	if(dateValue.length == 0) {
		return true;
	}
	
	 //contando chars
	if(value.length!=10) return false;
	// verificando data
	var data 		= value;
	var dia 		= data.substr(0,2);
	var barra1		= data.substr(2,1);
	var mes 		= data.substr(3,2);
	var barra2		= data.substr(5,1);
	var ano 		= data.substr(6,4);
	if(data.length!=10||barra1!="/"||barra2!="/"||isNaN(dia)||isNaN(mes)||isNaN(ano)||dia>31||mes>12)return false;
	if((mes==4||mes==6||mes==9||mes==11) && dia==31)return false;
	if(mes==2  &&  (dia>29||(dia==29 && ano%4!=0)))return false;
	if(ano < 1900)return false;
	if(ano > 2100)return false;
	return true;
}, "Informe uma data válida");  // Mensagem padrão 

jQuery.validator.addMethod("cnpj", function(cnpj, element) {
	
	cnpjValue = cnpj;
	
	
	cnpjValue = retirarMascara(cnpjValue);
	
	if(cnpjValue.length == 0) {
		return true;
	}
	
    cnpj = jQuery.trim(cnpjValue);
	
	// DEIXA APENAS OS NÚMEROS

   var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais;
   digitos_iguais = 1;

   if (cnpj.length < 14 && cnpj.length < 15){
      return false;
   }
   for (i = 0; i < cnpj.length - 1; i++){
      if (cnpj.charAt(i) != cnpj.charAt(i + 1)){
         digitos_iguais = 0;
         break;
      }
   }

   if (!digitos_iguais){
      tamanho = cnpj.length - 2;
      numeros = cnpj.substring(0,tamanho);
      digitos = cnpj.substring(tamanho);
      soma = 0;
      pos = tamanho - 7;

      for (i = tamanho; i >= 1; i--){
         soma += numeros.charAt(tamanho - i) * pos--;
         if (pos < 2){
            pos = 9;
         }
      }
      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
      if (resultado != digitos.charAt(0)){
         return false;
      }
      tamanho = tamanho + 1;
      numeros = cnpj.substring(0,tamanho);
      soma = 0;
      pos = tamanho - 7;
      for (i = tamanho; i >= 1; i--){
         soma += numeros.charAt(tamanho - i) * pos--;
         if (pos < 2){
            pos = 9;
         }
      }
      resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
      if (resultado != digitos.charAt(1)){
         return false;
      }
      return true;
   }else{
      return false;
   }
}, "Informe um CNPJ válido."); // Mensagem padrão 

jQuery.validator.addMethod("notEqual", function(value, element, param) {
   return value == $(param).val() ? false : true;
}, "Este valor não pode ser igual"); // Mensagem padrão 

jQuery.validator.addMethod("diferenteDe", function(value, element, strCompara) {
   return value == strCompara ? false : true;
}, "Este valor não foi alterado"); // Mensagem padrão

jQuery.validator.addMethod("replaceAll", function(value, token, newtoken) {

	while (value.indexOf(token) != -1) {
		value = value.replace(token, newtoken);
	}
	return string;
	
}, "Troca todos os valores."); // Mensagem padrão

jQuery.validator.addMethod("validaDDDMarisa", function(value, element) {
	var previous = campoValidateAjax(element);	
	var DDD = value;
	
	DDD = retirarMascara(DDD);
	
	if(DDD.length < 10) {
		return true;
	}
	
	DDD = value.substring(1,3);

	if(previous.old !== DDD){
		previous.old = DDD;
		valorRetorno = true;
		
		$.ajax({
			url : 'telefone.do?operacao=validarDDD&ddd=' + DDD,
			type : 'POST',
			cache: false, 
			async : false,
			success : function(retorno){
				
				if($.trim(retorno) == 'E'){
					valorRetorno = false;
				} else if($.trim(retorno) == 'N') {
					valorRetorno = false;
				} else {
					valorRetorno = true;
				}
			}
		});	
		previous.valid = valorRetorno;
		return valorRetorno;
	
	}else
		return previous.valid;
	
}, "Informe um número de DDD válido."); 

jQuery.validator.addMethod("validaTelefoneIgualMarisa", function(value, element) {
	telefoneValue = value;
	
	telefoneValue = retirarMascara(telefoneValue);
	
	if(telefoneValue.length == 0) {
		return true;
	}
	
	if(telefoneValue.length < 10) {
		return false;
	}
	
	numeroTelefone = value.substring(5,14);

	
	numeroTelefone = retirarMascara(numeroTelefone);
	
	if(numeroTelefone.length != 8) {
		return false;
	}

	// todos os números iguais
	if (numeroTelefone != null && numeroTelefone.length > 0) {
		numero = numeroTelefone.substring(0,1);
		for (i = 1; i < numeroTelefone.length; i++) {
			if (numero != numeroTelefone.substring(i,(i+1))) {
				return true;
			}
		}
		
		return false;
	}
	
	return true;
	
}, "Informe um número de telefone válido."); 

jQuery.validator.addMethod("validaTelefoneMarisa", function(value, element) {
	telefoneValue = value;
	
	telefoneValue = retirarMascara(telefoneValue);	
	
	if(telefoneValue.length == 0) {
		return true;
	}
	
	if(telefoneValue.length < 10) {
		return false;
	}
	
	numeroTelefone = value.substring(5,14);
	if ((numeroTelefone.indexOf("7") == 0) 
			|| (numeroTelefone.indexOf("8") == 0) || (numeroTelefone.indexOf("9") == 0)) {
		return false;
	}

	numeroTelefone = retirarMascara(numeroTelefone);
	
	if(numeroTelefone.length != 8) {
		return false;
	}

	// todos os números iguais
	if (numeroTelefone != null && numeroTelefone.length > 0) {
		numero = numeroTelefone.substring(0,1);
		for (i = 1; i < numeroTelefone.length; i++) {
			if (numero != numeroTelefone.substring(i,(i+1))) {
				return true;
			}
		}
		
		return false;
	}
	
	return true;
	
}, "Informe um número de telefone válido."); 

jQuery.validator.addMethod("validaCelularMarisa", function(value, element) {
	telefoneValue = value;
	
	telefoneValue = retirarMascara(telefoneValue);
	if(telefoneValue.length == 0) {
		return true;
	}
	
	if(telefoneValue.length < 10) {
		return false;
	}
	
	numeroTelefone = value.substring(5,14);
	numeroDDD      = value.substring(1,3);
	
	if (!((numeroTelefone.indexOf("6") == 0) || (numeroTelefone.indexOf("7") == 0) || (numeroTelefone.indexOf("8") == 0) || (numeroTelefone.indexOf("9") == 0))) {
		if ( numeroDDD.indexOf("11") == 0 && ( numeroTelefone.indexOf("5") == 0 ) ){
			return true;
		}else{
			return false;
		}	
	}

	numeroTelefone = retirarMascara(numeroTelefone);
	
	if(numeroTelefone.length != 8) {
		return false;
	}

	// todos os números iguais
	if (numeroTelefone != null && numeroTelefone.length > 0) {
			numero = numeroTelefone.substring(0,1);
			for (i = 1; i < numeroTelefone.length; i++) {
			if (numero != numeroTelefone.substring(i,(i+1))) {
				return true;
			}
			}
			
			return false;
		}
		
	return true;

}, "Informe um número de telefone celular válido.");

jQuery.validator.addMethod("validaCEP", function(value, element) {
	
	numeroCEP = value;
	
	numeroCEP = retirarMascara(numeroCEP);
		
	if(numeroCEP.length == 0 || numeroCEP.length == 8) {
		return true;
	}
		
	return false;

}, "Informe um número de CEP válido."); 

jQuery.validator.addMethod("validaCodigoBeneficio", function(value, element) {
	
	numeroCodigoBeneficio = value;
	
	numeroCodigoBeneficio = retirarMascara(numeroCodigoBeneficio);
	
	if(numeroCodigoBeneficio.length == 0 || numeroCodigoBeneficio.length == 10) {
		return true;
	}
		
	return false;

}, "Informe um Código do Benefício válido."); 

jQuery.validator.addMethod("validaNomeMarisa", function(value, element) {
	
	value = jQuery.trim(value);
	var nomeCliente = value.split(" ");

	if(nomeCliente.length >= 2) {
		return true;
	}
		
	return false;

}, "O campo Nome precisa ter pelo menos duas palavras."); 

jQuery.validator.addMethod("validaIdadeMarisa", function(value, element) {
	
	var dataValue = value;
	
	dataValue = retirarMascara(dataValue);
	
	if(dataValue == "") {
		return true;
	}
	
	var hoje = new Date();
	 
	var arrayData = value.split("/");
	 
	if (arrayData.length == 3) {
	  // Decompoem a data em array
	  var ano = parseInt( arrayData[2] );
	  var mes = parseInt( arrayData[1] );
	  var dia = parseInt( arrayData[0] );
	  
	  // Valida a data informada
	  if ( arrayData[0] > 31 || arrayData[1] > 12 ) {
	   return false;
	  }  
	  
	  ano = ( ano.length == 2 ) ? ano += 1900 : ano;
	  
	  // Subtrai os anos das duas datas
	  
	  var idade = (hoje.getFullYear()) - ano;
	  var nMes 	= (hoje.getMonth() + 1) - mes;
	  var nDias = hoje.getDate() - dia;
	  
	  if (idade > 16) {
	  	return true;
	  }else if (idade == 16) {
  		if (nMes > 0 || (nMes == 0 && nDias >= 0)) 
  			return true;
	  }
	}

	return false;

}, "Idade do cliente menor que a idade mínima para Titular.");

// VALIDATES ------->>>>  CADASTRO PL
jQuery.validator.addMethod("dataMaior", function(value, element) {
	
	var vl = value;
	var aS = vl.split("/");
	var dataValue = value;
	
	dataValue = retirarMascara(dataValue);
	
	var atualDate = new Date();
	var newDate = new Date();
	
	if (dataValue == ""){
		return true;
	}else{
		if (aS.length == 3){
			newDate.setDate(aS[0]);
			newDate.setMonth(aS[1]-1);
			newDate.setFullYear(aS[2]);
		}else{
			return true;
		}
		if(atualDate > newDate){
			return true;
		}
		return false;
	}
	
}, "A data informada é maior que a atual.");

jQuery.validator.addMethod("dataMaiorQueAtual", function(value, element) {
	
	var vl = value;
	var aS = vl.split("/");
	var dataValue = value;
	
	dataValue = retirarMascara(dataValue);
	
	var atualDate = new Date();
	var newDate = new Date();
	
	if (dataValue == ""){
		return true;
	}else{
		if (aS.length == 3){
			newDate.setDate(aS[0]);
			newDate.setMonth(aS[1]-1);
			newDate.setFullYear(aS[2]);
		}else{
			return true;
		}

		if(atualDate >= newDate){
			return true;
		}
		return false;
	}
	
}, "A data informada é maior que a atual.");

jQuery.validator.addMethod("campoRequerido", function(value, element) {
	
	var valorAno = '';
	var valorMes = '';
	
	
	if(element.id == 'cliResDtDesdeMes'){
		 valorAno = $('#cliResDtDesdeAno').val();
		 valorMes = $('#cliResDtDesdeMes').val();
		
	}else if(element.id == 'cliEmpDtAdmMes'){
		 valorAno = $('#cliEmpDtAdmAno').val();
		 valorMes = $('#cliEmpDtAdmMes').val();
		 
	}else if(element.id == 'cliTempoContaDtDesdeMes'){
		 valorAno = $('#cliTempoContaDtDesdeAno').val();
		 valorMes = $('#cliTempoContaDtDesdeMes').val();	
	}
	
	var valorAnoInt = parseInt(valorAno, 10); 
	var valorMesInt = parseInt(valorMes, 10);
	
	 if(element.id == 'cliTempoContaDtDesdeMes' && $('#cliBcoNome').val() == ''){
		 return true;
	 }

	if ($.trim(valorAno) == '' && $.trim(valorMes) == '') {
		return false;
	}else if($.trim(valorAno) == '' && valorMesInt < 1){
		return false;
	}else if($.trim(valorMes) == '' && valorAnoInt < 1){
		return false;
	}else if(valorAnoInt < 1 && valorMesInt < 1){
		return false;
	}
	
	return true;
	
}, "A data informada é Inválida.");


jQuery.validator.addMethod("validarDataAnoMes", function(value, element) {
	
	var valorAno = '';
	var valorMes = '';
	var regExp = /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/;
	
	if(element.id == 'cliResDtDesdeMes'){
		 valorAno = $('#cliResDtDesdeAno').val();
		 valorMes = $('#cliResDtDesdeMes').val();
		
	}else if(element.id == 'cliEmpDtAdmMes'){
		 valorAno = $('#cliEmpDtAdmAno').val();
		 valorMes = $('#cliEmpDtAdmMes').val();
		 
	}else if(element.id == 'cliTempoContaDtDesdeMes'){
		 valorAno = $('#cliTempoContaDtDesdeAno').val();
		 valorMes = $('#cliTempoContaDtDesdeMes').val();	
	}
	
	if($.trim(valorAno) == '' && $.trim(valorMes) == '')
		return true;
	
	if(valorAno > 99)
		return false;
	if(valorMes > 11)
		return false;
	
	if(!regExp.test(valorAno) && $.trim(valorAno) != '')
		return false;
	
	if(!regExp.test(valorMes) && $.trim(valorMes) != '')
		return false;
	
	return true;
	
}, "A data informada é Inválida.");

jQuery.validator.addMethod("tipoContaRequerido", function(value, element) {
	 
	if($('#cliBcoNome').val() == '')
		 return true;
	 
	if($.trim(value) == '')
		return false;
	
	return true;
		
}, "");

jQuery.validator.addMethod("dataMenor", function(value, element) {
	
	var vl = value;
	var aS = vl.split("/");
	var atualDate = new Date();
	var newDate = new Date();
	
	var dataValue = value;
	
	dataValue = retirarMascara(dataValue);
	
	if (dataValue == "") {
		return true;
	} else {
		if (aS.length == 3) {
			newDate.setDate(aS[0]);
			newDate.setMonth(aS[1] - 1);
			newDate.setFullYear(aS[2]);
		}
		else {
			return false;
		}
		
		if (atualDate < newDate) {
			return true;
		}
		
		return false;
	}
	
}, "A data informada é menor que a atual.");

jQuery.validator.addMethod("validaDataMaiorNasc", function(value, element) {
	
	var cliDtNascto = $('#cliDtNascto').val();
	var cliDtEmissaoCpf = $('#cliDtEmissaoCpf').val(); 
	var cliDtExpedicaoDocumento = $('#cliDtExpedicaoDocumento').val();
	
	var dataValue = value;
	
		dataValue = retirarMascara(dataValue);
	
	if (dataValue == "") {
		return true;
	}
	
	if(jQuery.trim(cliDtNascto) == "") {
		return true;
	}
	
	if (element.id == 'cliDtEmissaoCpf'){
		var aN = cliDtNascto.split("/");
		var aE = cliDtEmissaoCpf.split("/");
		
		var aDtNascto = new Date();
		var aDtEmissaoCpf = new Date();
		
		if (aN.length == 3 && aE.length == 3) {
			aDtNascto.setDate(aN[0]);
			aDtNascto.setMonth(aN[1] - 1);
			aDtNascto.setFullYear(aN[2]);
			
			aDtEmissaoCpf.setDate(aE[0]);
			aDtEmissaoCpf.setMonth(aE[1] - 1);
			aDtEmissaoCpf.setFullYear(aE[2]);
		} else {
			return false;
		}

		if (aDtEmissaoCpf >= aDtNascto) {
			return true;
		}
		
		return false;
		
	} else if(element.id == 'cliDtExpedicaoDocumento') {
		
		
		var aN = cliDtNascto.split("/");
		var aE = cliDtExpedicaoDocumento.split("/");
		
		var aDtNascto = new Date();
		var aDtExpedicaoDocumento = new Date();
		
		if (aN.length == 3 && aE.length == 3) {
			aDtNascto.setDate(aN[0]);
			aDtNascto.setMonth(aN[1] - 1);
			aDtNascto.setFullYear(aN[2]);
			
			aDtExpedicaoDocumento.setDate(aE[0]);
			aDtExpedicaoDocumento.setMonth(aE[1] - 1);
			aDtExpedicaoDocumento.setFullYear(aE[2]);
		} else {
			return false;
		}
		
		if (aDtExpedicaoDocumento > aDtNascto) {
			return true;
		}
		
		return false;
	}
	
	return false;

}, "Data informada não pode ser menor que a Data de Nascimento.");

jQuery.validator.addMethod("validaPeriodo", function(value, element) {
	
	var periodoInicial = $('#periodoInicial').val();
	var periodoFinal = $('#periodoFinal').val(); 
	
	var dataValue = periodoInicial;
	
	dataValue = retirarMascara(dataValue);
	
	if (dataValue == "") {
		return true;
	}
	
	dataValue = periodoFinal;
	
	dataValue = retirarMascara(dataValue);
	
	if (dataValue == "") {
		return true;
	}
	
		var aInicial = periodoInicial.split("/");
		var aFinal = periodoFinal.split("/");
		
		var aDtInicial = new Date();
		var aDtFinal = new Date();
		
		if (aInicial.length == 3 && aFinal.length == 3) {
			aDtInicial.setDate(aInicial[0]);
			aDtInicial.setMonth(aInicial[1] - 1);
			aDtInicial.setFullYear(aInicial[2]);
			
			aDtFinal.setDate(aFinal[0]);
			aDtFinal.setMonth(aFinal[1] - 1);
			aDtFinal.setFullYear(aFinal[2]);
		} else {
			return false;
		}
		
		if (aDtInicial <= aDtFinal) {
			return true;
		}
	
	return false;

}, "A Data Inicial não pode ser maior que a Data Final.");

jQuery.validator.addMethod("validaNumRenda", function(value, element) {
	
	var valorRenda = value;
	
	valorRenda = retirarMascara(valorRenda);
	
	var valorF = valorRenda;
    var valor = "";
    var numbers = "0123456789";  
    
    for(i=0; i<valorF.length; i++){    
        if(numbers.indexOf(valorF.charAt(i)) < 0){
            valor += valorF.charAt(i);
        }        
    }
    
	if(valor.length == 0) {
		return true;
	} else {
		$("#" + element.id).focus();	
		return false;
	}
	
}, "Permitido somente números.");

jQuery.validator.addMethod("validarOutrasRendas", function(value, element) {
	
	var outrasRendas = parseInt($.trim(retirarMascara($('#cliOutrasRendas').val())));
	var renda = parseInt($.trim(retirarMascara($('#cliEmpSalario').val())));
	
	if(outrasRendas === '' || renda === ''){
		return true;
	}
	if(outrasRendas > renda){
		return false;
	}
		
	return true;
	
}, "Permitido somente números.");

jQuery.validator.addMethod("validarValorMinimoRenda", function(value, element) {
	
	var renda1 = parseInt($.trim(retirarMascara(value)));
	
	if(renda1 === ''){
		return true;
	}
	
	if(renda1 > 0) {
		return true;
	} else {
		return false;
	}
		
	return true;
	
}, "O valor da renda informado tem que ser maior que zero.");

jQuery.validator.addMethod("notNull", function(value, element) {
	return true;
}, "O campo RG deve ser spreenchido.");

jQuery.validator.addMethod("validaAno", function(value, element) {
	ano = value;
	if (ano >= 0 && ano < 100){
		return true;
	}
	return false;
}, "Valores aceitos entre 0 e 99.");

jQuery.validator.addMethod("validaMes", function(value, element) {
	mes = value;
	if (mes >= 0 && mes < 12){
		return true;
	}
	return false;
}, "Valores aceitos entre 0 e 11.");

jQuery.validator.addMethod("validaTempo", function(value, element) {
	
	var v = value;
	
	var arV = v.split("/");
	while (arV[0].indexOf("_") != -1) {arV[0] = arV[0].replace("_","");}
	while (arV[1].indexOf("_") != -1) {arV[1] = arV[1].replace("_","");}
	
	var v0 = jQuery.trim(arV[0]);
	var v1 = jQuery.trim(arV[1]);
	
	if ((v0 > 99 || v0 < 0)){
		return false;
	}else if((v1 > 11 || v1 < 0)){
		return false;
	}else{
		return true;
	}
	return true;
	
}, "Valores aceitos para ano (0 e 99) e para mês (0 a 11).");

jQuery.validator.addMethod("validaCreditoLoja", function(value, element) {
	if($('#cliCrediarioDomi').val() == '8#1' && value == '' )
		return false;
	return true;
}, "Selecione uma Loja.");

jQuery.validator.addMethod("cpfContraint", function(value, element) {
	
	var cliCpf = $('#cliCpf').val();
	var cliCampCPF = $('#cliCampCPF').val(); 
	var cliCPFConj = $('#cliCPFConj').val();
	var adiCliCpf1 = $('#adiCliCpf1').val();
	var adiCliCpf2 = $('#adiCliCpf2').val();
	var adiCliCpf3 = $('#adiCliCpf3').val();
	var adiCliCpf4 = $('#adiCliCpf4').val();
		
	if (element.id=='cliCpf'){
		if ((value == cliCampCPF || value == cliCPFConj || value == adiCliCpf1 
				|| value == adiCliCpf2 || value == adiCliCpf3 || value == adiCliCpf4) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	if (element.id=='cliCampCPF'){
		if ((value == cliCpf || value == cliCPFConj || value == adiCliCpf1 
				|| value == adiCliCpf2 || value == adiCliCpf3 || value == adiCliCpf4 ) && value != "" ){
			return false;
		}else{
			return true;
		}
	}

	if (element.id=='cliCPFConj'){
		if($(element).is(":disabled")){
			return false;
		}
		
		if ((value == cliCpf || value == cliCampCPF) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	if (element.id=='adiCliCpf1'){
		if ((value == cliCpf || value == cliCampCPF || value == adiCliCpf2 || value == adiCliCpf3 || value == adiCliCpf4) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	if (element.id=='adiCliCpf2'){
		if ((value == cliCpf || value == cliCampCPF || value == adiCliCpf1 || value == adiCliCpf3 || value == adiCliCpf4) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	if (element.id=='adiCliCpf3'){
		if ((value == cliCpf || value == cliCampCPF || value == adiCliCpf2 || value == adiCliCpf1 || value == adiCliCpf4) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	if (element.id=='adiCliCpf4'){
		if ((value == cliCpf || value == cliCampCPF || value == adiCliCpf2 || value == adiCliCpf3 || value == adiCliCpf1) && value != "" ){
			return false;
		}else{
			return true;
		}
	}
	
	return true;
	
}, "O CPF informado já foi utilizado.");

jQuery.validator.addMethod("semEspaco", function(value, element) {
	var flag = true;
	while (value.indexOf(" ") != -1) {
		value = value.replace(" ",""); 
		flag =  false;
	}
	return flag;
}, "Campo não deve possuir espaços.");

jQuery.validator.addMethod("naoConterCaracEspecial", function(value, element) {
	
	var ExprReg = /(^([0-9A-Za-zá-úÁ-Úâ-ûÂ-Ûà-ùÀ-Ùã-õÃ-Õ-\.\s]){1,50})+$/;

	if(element.id == 'cliIdeOrgaoEmi' || element.id == 'cliIdentidade' || element.id == 'cliIdentidadeConj')
		ExprReg = /(^([0-9A-Za-zá-úÁ-Úâ-ûÂ-Ûà-ùÀ-Ùã-õÃ-Õ-\.\/\-\s]){1,50})+$/;	
	else if(element.id == 'cliEmpEmpresa')
		ExprReg = /(^([0-9A-Za-zá-úÁ-Úâ-ûÂ-Ûà-ùÀ-Ùã-õÃ-Õ-\.()\/\-\s]){1,50})+$/;	
	
	if(value == '')
		return true;
	
	if(ExprReg.test(value))
		return true;
	else
		return false;

}, "O campo não pode conter caracter especial.");

jQuery.validator.addMethod("naoConterCaracEspecialNumero", function(value, element) {
	
	var ExprReg = /(^([0-9A-Za-zá-úÁ-Úâ-ûÂ-Ûà-ùÀ-Ùã-õÃ-Õ-\.\/\s]){1,50})+$/;
	
	if(value == '')
		return true;
	
	if(ExprReg.test(value))
		return true;
	else
		return false;

}, "O campo não pode conter caracter especial.");

jQuery.validator.addMethod("caracEspecial", function(value, element) {
	
	if(value == "") {
		return true;
	}
	
	var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
	
	if(reg.test(value)) {
		return true;
	}
	
	return false;
}, "O campo não pode conter caracter especial.");

jQuery.validator.addMethod("cepRequerido", function(value, element) {

	numeroCEP = value;
	elementId = $(element).attr('id');
	
	numeroCEP = retirarMascara(numeroCEP);
	
	if(numeroCEP.length == 0 || numeroCEP.length < 8) {
		if (elementId == 'cliResCep') {
			$("#erroCliResCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
			$("#erroCliResCep").show();
		}
		if (elementId == 'cliEmpCep'){
			$("#erroCliEmpCep").html('<span class="erro"><img src="images/ico_error.gif" class="imagem" />O Campo CEP deve ser preenchido corretamente.</span>');
			$("#erroCliEmpCep").show();
		}
		
		return false;
	} 
	
	return true;
	
},"");

jQuery.validator.addMethod("validarEnderecoAssalariado", function(value, element) {
	var idCargo = $('#cargoNome').val();
	var cliNum = $('#cliResNum').val();
	var empNum = $('#cliEmpNum').val();
	var cliCep = $('#cliResCep').val();
	var empCep = $('#cliEmpCep').val();
	var tipoResidencia = $('#cliResCasaDomi').val();
	
	cliNum = $.trim(cliNum);
	empNum = $.trim(empNum);	
	cliCep = $.trim(retirarMascara(cliCep));
	empCep = $.trim(retirarMascara(empCep));	
	
	if(cliNum == '' || empNum == '' || cliCep == '' || empCep == '' )
		return true;
		
	if(idCargo == '1'){
		if(cliCep == empCep && cliNum == empNum){
			if(tipoResidencia != '5#5') {
				return false;
			}
		}
	}

	return true;
	
},'O CEP(Profissional) não pode igual ao CEP(Residencial).');

jQuery.validator.addMethod("cpfCampanha", function(value, element) {
	var cliCpf = $('#cliCpf').val();
	if (value == cliCpf && value != "" ){
		return false;
	}else{
		return true;
	}
	return true;
},"CPF informado já está sendo utilizado.");

jQuery.validator.addMethod("validarEmancipado", function(value, element) {
	var dataValue = value;
	var isEmancipado = $('#isEmancipado').val();
	
	dataValue = retirarMascara(dataValue);
	
	
	if(dataValue == "" || dataValue.length != 8) {
		return true;
	}
	
	var hoje = new Date();
	 
	var arrayData = value.split("/");
	 
	if (arrayData.length == 3) {
	  // Decompoem a data em array
	  var ano = parseInt( arrayData[2] );
	  var mes = parseInt( arrayData[1] );
	  var dia = parseInt( arrayData[0] );
	  
	  // Valida a data informada
	  if ( arrayData[0] > 31 || arrayData[1] > 12 ) {
	   return false;
	  }  
	  
	  ano = ( ano.length == 2 ) ? ano += 1900 : ano;
	  
	  // Subtrai os anos das duas datas
	  
	  var idade = (hoje.getFullYear()) - ano;
	  var nMes 	= (hoje.getMonth() + 1) - mes;
	  var nDias = hoje.getDate() - dia;
	  
	  
	  if(idade > 18){
	  	return true;
	  }else if(idade == 18){
	  	if(nMes > 0 || ( nMes == 0 && nDias >= 0) || isEmancipado == 'S')
			return true;
	  }else if(idade == 17 && isEmancipado == 'S'){
			return true;
	  }else if(idade == 16){
	  	if(nMes > 0 || ( nMes == 0 && nDias >= 0))
			return true;
	  }
	}
	return false;
	
},"CPF informado já está sendo utilizado.");

jQuery.validator.addMethod("validarEmancipadoAdicional", function(value, element) {
	
	var dataValue = value;
	dataValue = retirarMascara(dataValue);
	
	
	if(dataValue == "" || dataValue.length != 8) {
		return true;
	}
	
	var hoje = new Date();
	 
	var arrayData = value.split("/");
	 
	if (arrayData.length == 3) {
		// Decompoem a data em array
		var ano = parseInt(arrayData[2]);
		var mes = parseInt(arrayData[1]);
		var dia = parseInt(arrayData[0]);
		
		// Valida a data informada
		if (arrayData[0] > 31 || arrayData[1] > 12) {
			return false;
		}
		
		ano = (ano.length == 2) ? ano += 1900 : ano;
		
		// Subtrai os anos das duas datas
		
		var idade = (hoje.getFullYear()) - ano;
		var nMes = (hoje.getMonth() + 1) - mes;
		var nDias = hoje.getDate() - dia;
		
		if (idade > 16) {
			return true;
		}
		else 
			if (idade == 16) {
				if (nMes > 0 || (nMes == 0 && nDias >= 0)) 
					return true;
			}
	}
	  return false;
},"CPF informado já está sendo utilizado.");

jQuery.validator.addMethod("validarCelEmailAbaPessoalProfissional", function(value, element) {
	
	if(element.id == 'cliEmail'){
		if($('#faturaEmail').is(':checked')){
			if($.trim(value) == '')
				return false;
		}
	}else if(element.id == 'cliNumCelular'){
		if($('#saldoSMS').is(':checked')){
			if($.trim(value) == '')
				return false;
		}
	}
	return true;	
},"");  

jQuery.validator.addMethod("validarFuncionarioExistente", function(value, element) {
	var valCpf = value;
	var validacao;
	
	valCpf = retirarMascara(valCpf);
	
	if(valCpf == '')
		return true;

	$.ajax({
		type:'POST',
		url:'funcionario.do?operacao=consultarFuncionario',
		data:{cliCpf:valCpf},
		cache:false,
		async:false,
		success: function(retorno){
			if($.trim(retorno) != 'N')
				validacao = true;
			else
				validacao = false;
		}	
	});
	return validacao;
	
},"");


jQuery.validator.addMethod("validarAdicionalExistente", function(value, element) {
	var previous = campoValidateAjax(element);
	var valAdiCpf = value;
	var valCliCpf = $('#cliCpf').val();
	var validacao;
	
	valAdiCpf = retirarMascara(valAdiCpf);
	valCliCpf = retirarMascara(valCliCpf);
	
	if(valAdiCpf == '') {
		return true;
	}
	
	
	
	if(previous.old !== valAdiCpf){
		previous.old = valAdiCpf;
		
		$.ajax({
			type:'POST',
			url:'adicional.do?operacao=consultarAdicional',
			data:{adiCpf:valAdiCpf,cliCpf:valCliCpf},
			cache:false,
			async:false,
			success: function(retorno){
				if($.trim(retorno) == 'S')
					validacao = true;
				else
					validacao = false;
			}	
		});
		previous.valid = validacao;
		return validacao;
	}else
		return previous.valid;
		
},"");

jQuery.validator.addMethod("validarExpedicaoDocumentoRequerido", function(value, element) {
	var valor = value;
	if($('#cliDocIdentificacao').val() == 5){
		valor = $.trim( retirarMascara(valor));
		if(valor == '')
			return false;
	}
	return true;
   
},"");

jQuery.validator.addMethod("validarViaDocumentoRequerido", function(value, element) {
	var valor = value;
	if($('#cliDocIdentificacao').val() == 5){
		valor = $.trim( retirarMascara(valor));
		if(valor == '')
			return false;
	}
	return true;
	
},"");

jQuery.validator.addMethod("validadeDocumentoHabilitacao", function(value, element) {
	var valor = value;
	var valorDoc = $('#cliDocIdentificacao').val();
	
	if( valorDoc == 10){
		valor = $.trim( retirarMascara(valor));
		if(valor == '')
			return false;
	}
	return true;
	
},"");

function campoValidateAjax(element) {
	return $.data(element, "campoValidateAjax") || $.data(element, "campoValidateAjax", {
		old: null,
		valid: true
	});
}
