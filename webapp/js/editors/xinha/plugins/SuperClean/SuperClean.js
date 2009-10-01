/** superClean combines HTMLTidy, Word Cleaning and font stripping into a single function
 *  it works a bit differently in how it asks for parameters */

function SuperClean(editor, args)
{
  this.editor = editor;
  var superclean = this;
  editor._superclean_on = false;
  editor.config.registerButton('superclean', this._lc("Clean up HTML"), [_editor_url +'iconsets/Tango/ed_buttons_main.png',6,4], true, function(e, objname, obj) { superclean._superClean(null, obj); });

  // See if we can find 'killword' and replace it with superclean
  editor.config.addToolbarElement("superclean", "killword", 0);
}

SuperClean._pluginInfo =
{
  name     : "SuperClean",
  version  : "1.0",
  developer: "James Sleeman, Niko Sams",
  developer_url: "http://www.gogo.co.nz/",
  c_owner      : "Gogo Internet Services",
  license      : "htmlArea",
  sponsor      : "Gogo Internet Services",
  sponsor_url  : "http://www.gogo.co.nz/"
};

SuperClean.prototype._lc = function(string) {
    return Xinha._lc(string, 'SuperClean');
};

Xinha.Config.prototype.SuperClean =
{
  // set to the URL of a handler for html tidy, this handler
  //  (see tidy.php for an example) must that a single post variable
  //  "content" which contains the HTML to tidy, and return javascript like
  //  editor.setHTML('<strong>Tidied Html</strong>')
  // it's called through XMLHTTPRequest
  'tidy_handler': Xinha.getPluginDir("SuperClean") + '/tidy.php',

  //avaliable filters (these are built-in filters)
  // You can either use
  //    'filter_name' : "Label/Description String"
  // or 'filter_name' : {label: "Label", checked: true/false, filterFunction: function(html) { ... return html;} }
  // filterFunction in the second format above is optional.

  'filters': { 'tidy': Xinha._lc('General tidy up and correction of some problems.', 'SuperClean'),
               'word_clean': Xinha._lc('Clean bad HTML from Microsoft Word', 'SuperClean'),
               'remove_faces': Xinha._lc('Remove custom typefaces (font "styles").', 'SuperClean'),
               'remove_sizes': Xinha._lc('Remove custom font sizes.', 'SuperClean'),
               'remove_colors': Xinha._lc('Remove custom text colors.', 'SuperClean'),
               'remove_lang': Xinha._lc('Remove lang attributes.', 'SuperClean'),
               'remove_fancy_quotes': {label:Xinha._lc('Replace directional quote marks with non-directional quote marks.', 'SuperClean'), checked:false}
  //additional custom filters (defined in plugins/SuperClean/filters/word.js)
               //'paragraph': 'remove paragraphs'},
               //'word': 'exteded Word-Filter' },
              },
  //if false all filters are applied, if true a dialog asks what filters should be used
  'show_dialog': false
};

SuperClean.filterFunctions = { };


SuperClean.prototype.onGenerateOnce = function()
{

  if(this.editor.config.tidy_handler)
  {
    //for backwards compatibility
    this.editor.config.SuperClean.tidy_handler = this.editor.config.tidy_handler;
    this.editor.config.tidy_handler = null;
  }
  if(!this.editor.config.SuperClean.tidy_handler && this.editor.config.filters.tidy) {
    //unset tidy-filter if no tidy_handler
    this.editor.config.filters.tidy = null;
  }
  SuperClean.loadAssets();
  this.loadFilters();
};

SuperClean.prototype.onUpdateToolbar = function()
{ 
  if (!(SuperClean.methodsReady && SuperClean.html))
  {
    this.editor._toolbarObjects.superclean.state("enabled", false);
  }
  else this.onUpdateToolbar = null;
};

SuperClean.loadAssets = function()
{
  var self = SuperClean;
  if (self.loading) return;
  self.loading = true;
  Xinha._getback(Xinha.getPluginDir("SuperClean") + '/pluginMethods.js', function(getback) { eval(getback); self.methodsReady = true; });
  Xinha._getback( Xinha.getPluginDir("SuperClean") + '/dialog.html', function(getback) { self.html = getback; } );
};

SuperClean.prototype.loadFilters = function()
{
  var sc = this;
  //load the filter-functions
  for(var filter in this.editor.config.SuperClean.filters)
  {
    if (/^(remove_colors|remove_sizes|remove_faces|remove_lang|word_clean|remove_fancy_quotes|tidy)$/.test(filter)) continue; //skip built-in functions
    
    if(!SuperClean.filterFunctions[filter])
    {
      var filtDetail = this.editor.config.SuperClean.filters[filter];
      if(typeof filtDetail.filterFunction != 'undefined')
      {
        SuperClean.filterFunctions[filter] = filterFunction;
      }
      else
      {
        Xinha._getback(Xinha.getPluginDir("SuperClean") + '/filters/'+filter+'.js',
                      function(func) {
                        eval('SuperClean.filterFunctions.'+filter+'='+func+';');
                        sc.loadFilters();
                      });
      }
      return;
    }
  }
};