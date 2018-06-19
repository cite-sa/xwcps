(function ($, undefined) {
	$.widget("ui.HarvesterInlineEditor", $.ui.CiteInlineEditor, {
		options: {
		},

		_refreshView: function () {
			this._super();

			//var item = this.option('item');
			
			this.element.empty();

			this.initEditor();
			//this.showData(item);
			//this.setCurrentDisplayModeAndApply($.ui.CiteBaseControl.DisplayMode.Edit);
		},
		
		initEditor: function () {
			var self = this;
			
			var definitionTable = $('<table></table>');
			this.element.append(definitionTable);
			
			{
				var r = $('<tr></tr>');
				var c = $('<td></td>');
				this.sveEndpoint = $('<div id="' + $.ui.CiteBaseControl.generateControlId() + '"></div>');
				c.append(this.sveEndpoint);

				var c2 = $('<td></td>');
				var lbl = $('<label class="formFieldLabel" for="' + this.sveEndpoint[0].id + '">' + 'Endpoint' +'</label>');
				c2.append(lbl);

				r.append(c2);
				r.append(c);
				definitionTable.append(r);
				
				this.sveEndpoint.CiteStringValueEditor({
					currentDisplayMode: $.ui.CiteBaseControl.DisplayMode.Edit,
					autoInitialize: true
				});
			}
			
			{
				var r = $('<tr></tr>');
				var c = $('<td></td>');
				this.sveEndpointAlias = $('<div id="' + $.ui.CiteBaseControl.generateControlId() + '"></div>');
				c.append(this.sveEndpointAlias);

				var c2 = $('<td></td>');
				var lbl = $('<label class="formFieldLabel" for="' + this.sveEndpointAlias[0].id + '">' + 'Endpoint Alias' +'</label>');
				c2.append(lbl);

				r.append(c2);
				r.append(c);
				definitionTable.append(r);
				
				this.sveEndpointAlias.CiteStringValueEditor({
					currentDisplayMode: $.ui.CiteBaseControl.DisplayMode.Edit,
					autoInitialize: true
				});
			}
			
			{
				var r = $('<tr></tr>');
				var c = $('<td></td>');
				this.svePeriod = $('<div id="' + $.ui.CiteBaseControl.generateControlId() + '"></div>');
				c.append(this.svePeriod);

				var c2 = $('<td></td>');
				var lbl = $('<label class="formFieldLabel" for="' + this.svePeriod[0].id + '">' + 'Period' +'</label>');
				c2.append(lbl);

				r.append(c2);
				r.append(c);
				definitionTable.append(r);
				
				this.svePeriod.CiteStringValueEditor({
					currentDisplayMode: $.ui.CiteBaseControl.DisplayMode.Edit,
					autoInitialize: true
				});
			}
			
			{
				var r = $('<tr></tr>');
				var c = $('<td></td>');
				this.asgPeriodType = $('<div id="' + $.ui.CiteBaseControl.generateControlId() + '"></div>');
				c.append(this.asgPeriodType);

				var c2 = $('<td></td>');
				var lbl = $('<label class="formFieldLabel" for="' + this.asgPeriodType[0].id + '">' + 'Period Type' +'</label>');
				c2.append(lbl);

				r.append(c2);
				r.append(c);
				definitionTable.append(r);
				
				var suggestions = [{ 'Text': 'Seconds' , 'Value': 'SECONDS' },
								{ 'Text': 'Minutes' , 'Value': 'MINUTES' },
								{ 'Text': 'Hours' , 'Value': 'HOURS' },
								{ 'Text': 'Days' , 'Value': 'DAYS' }];
				
				this.asgPeriodType.CiteAutoSuggest({
					currentDisplayMode: $.ui.CiteBaseControl.DisplayMode.Edit,
					suggestionMode: $.ui.CiteAutoSuggest.SuggestionMode.Static,
					uiMode: $.ui.CiteAutoSuggest.UIMode.DropDown,
					selectionNameProperty: 'Text',
					selectionValueProperty: 'Value',
					staticSuggestions: suggestions,
					autoInitialize: true
				});
			}
			
			{
				var r = $('<tr></tr>');
				var c1 = $('<td></td>');
				this.saveButton = $('<button id="cancel" name="save" class="btn btn-default" value="1">Save</button>');
				c1.append(this.saveButton);
				
				var c2 = $('<td></td>');
				this.cancelButton = $('<button id="cancel" name="cancel" class="btn btn-default" value="1">Cancel</button>');
				c2.append(this.cancelButton);

				r.append(c1);
				r.append(c2);
				definitionTable.append(r);
				
				this.saveButton.on('click', function() {
					eval(self.options.saveCallback)();
				})
				
				this.cancelButton.on('click', function() {
					eval(self.options.cancelCallback)();
				})
			}
		},
		
		getData: function () {
			var result = {};
			
			result.endpoint = this.sveEndpoint.CiteStringValueEditor('getValue');
			result.endpointAlias = this.sveEndpointAlias.CiteStringValueEditor('getValue');
			result.period = this.svePeriod.CiteStringValueEditor('getValue');
			
			var values = this.asgPeriodType.CiteAutoSuggest('getSelectedValues');
			result.periodType = (values.length == 0) ? '' : values[0];
			
			console.log(result);
			
			return result;
		}
	})
}(jQuery));