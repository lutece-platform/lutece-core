// Table Operations Plugin for HTMLArea-3.0
// Implementation by Mihai Bazon.  Sponsored by http://www.bloki.com
//
// htmlArea v3.0 - Copyright (c) 2002 interactivetools.com, inc.
// This notice MUST stay intact for use (see license.txt).
//
// A free WYSIWYG editor replacement for <textarea> fields.
// For full source code and docs, visit http://www.interactivetools.com/
//
// Version 3.0 developed by Mihai Bazon for InteractiveTools.
//   http://dynarch.com/mishoo
//
// $Id: TableOperations.js 1075 2008-10-09 22:02:30Z nicholasbs $

// Object that will encapsulate all the table operations provided by
// HTMLArea-3.0 (except "insert table" which is included in the main file)
Xinha.Config.prototype.TableOperations = {
  'showButtons' : true // Set to false to hide all but inserttable and toggleborders buttons on the toolbar
  // this is useful if you have the ContextMenu plugin and want to save toolbar space
  // (the context menu can perform all the button operations)
}

function TableOperations(editor) {
  this.editor = editor;

  var cfg = editor.config;
  var bl = TableOperations.btnList;
  var self = this;

  // register the toolbar buttons provided by this plugin

  // Remove existing inserttable and toggleborders, we will replace it in our group  
  cfg.removeToolbarElement(' inserttable toggleborders '); 
  
  var toolbar = ["linebreak", "inserttable", "toggleborders"];
  
  
  for (var i = 0; i < bl.length; ++i) {
    var btn = bl[i];
    if (!btn) {
      if(cfg.TableOperations.showButtons) toolbar.push("separator");
    } else {
      var id = "TO-" + btn[0];
      cfg.registerButton(id, Xinha._lc(btn[2], "TableOperations"), editor.imgURL(btn[0] + ".gif", "TableOperations"), false,
                         function(editor, id) {
                           // dispatch button press event
                           self.buttonPress(editor, id);
                         }, btn[1]);
      if(cfg.TableOperations.showButtons) toolbar.push(id);
    }
  }
  

  // add a new line in the toolbar
  cfg.toolbar.push(toolbar);
  
  if ( typeof PopupWin == 'undefined' )
  {
    Xinha._loadback(_editor_url + 'modules/Dialogs/popupwin.js');
  }
  if ( typeof Xinha.InlineStyler == 'undefined' )
  {
    Xinha._loadback(_editor_url + 'modules/InlineStyler/InlineStyler.js');
  }
  
}

TableOperations._pluginInfo = {
  name          : "TableOperations",
  version       : "1.0",
  developer     : "Mihai Bazon",
  developer_url : "http://dynarch.com/mishoo/",
  c_owner       : "Mihai Bazon",
  sponsor       : "Zapatec Inc.",
  sponsor_url   : "http://www.bloki.com",
  license       : "htmlArea"
};

TableOperations.prototype._lc = function(string) {
  return Xinha._lc(string, 'TableOperations');
};

/************************
 * UTILITIES
 ************************/

// retrieves the closest element having the specified tagName in the list of
// ancestors of the current selection/caret.
TableOperations.prototype.getClosest = function(tagName) {
  var editor = this.editor;
  var ancestors = editor.getAllAncestors();
  var ret = null;
  tagName = ("" + tagName).toLowerCase();
  for (var i = 0; i < ancestors.length; ++i) {
    var el = ancestors[i];
    if (el.tagName.toLowerCase() == tagName) {
      ret = el;
      break;
    }
  }
  return ret;
};

// this function gets called when some button from the TableOperations toolbar
// was pressed.
TableOperations.prototype.buttonPress = function(editor, button_id) {
  this.editor = editor;
  var mozbr = Xinha.is_gecko ? "<br />" : "";

  // helper function that clears the content in a table row
  function clearRow(tr) {
    var tds = tr.getElementsByTagName("td");
    for (var i = tds.length; --i >= 0;) {
      var td = tds[i];
      td.rowSpan = 1;
      td.innerHTML = mozbr;
    }
  }

  function splitRow(td) {
    var n = parseInt("" + td.rowSpan);
    var nc = parseInt("" + td.colSpan);
    td.rowSpan = 1;
    tr = td.parentNode;
    var itr = tr.rowIndex;
    var trs = tr.parentNode.rows;
    var index = td.cellIndex;
    while (--n > 0) {
      tr = trs[++itr];
      var otd = editor._doc.createElement("td");
      otd.colSpan = td.colSpan;
      otd.innerHTML = mozbr;
      tr.insertBefore(otd, tr.cells[index]);
    }
    editor.forceRedraw();
    editor.updateToolbar();
  }

  function splitCol(td) {
    var nc = parseInt("" + td.colSpan);
    td.colSpan = 1;
    tr = td.parentNode;
    var ref = td.nextSibling;
    while (--nc > 0) {
      var otd = editor._doc.createElement("td");
      otd.rowSpan = td.rowSpan;
      otd.innerHTML = mozbr;
      tr.insertBefore(otd, ref);
    }
    editor.forceRedraw();
    editor.updateToolbar();
  }

  function splitCell(td) {
    var nc = parseInt("" + td.colSpan);
    splitCol(td);
    var items = td.parentNode.cells;
    var index = td.cellIndex;
    while (nc-- > 0) {
      splitRow(items[index++]);
    }
  }

  function selectNextNode(el) {
    var node = el.nextSibling;
    while (node && node.nodeType != 1) {
      node = node.nextSibling;
    }
    if (!node) {
      node = el.previousSibling;
      while (node && node.nodeType != 1) {
        node = node.previousSibling;
      }
    }
    if (!node) {
      node = el.parentNode;
    }
    editor.selectNodeContents(node);
  }

  function cellMerge(table, cell_index, row_index, no_cols, no_rows) {
    var rows = [];
    var cells = [];
    try {
      for (i=row_index; i<row_index+no_rows; i++) {
        var row = table.rows[i];
        for (j=cell_index; j<cell_index+no_cols; j++) {
          if (row.cells[j].colSpan > 1 || row.cells[j].rowSpan > 1) {
            splitCell(row.cells[j]);
          }
          cells.push(row.cells[j]);
        }
        if (cells.length > 0) {
          rows.push(cells);
          cells = [];
        }
      }
    } catch(e) { 
      alert("Invalid selection");
      return false;
    }
    var row_index1 = rows[0][0].parentNode.rowIndex;
    var row_index2 = rows[rows.length-1][0].parentNode.rowIndex;
    var row_span2 = rows[rows.length-1][0].rowSpan;
    var HTML = "";
    for (i = 0; i < rows.length; ++i) {
      var cells = rows[i];
      for (var j = 0; j < cells.length; ++j) {
        var cell = cells[j];
        HTML += cell.innerHTML;
        (i || j) && (cell.parentNode.removeChild(cell));
      }
    }
    var td = rows[0][0];
    td.innerHTML = HTML;
    td.rowSpan = row_index2 - row_index1 + row_span2;
    var col_span = 0;
    for(j=0; j<rows[0].length; j++) {
      col_span += rows[0][j].colSpan;
    }
    td.colSpan = col_span;
    editor.selectNodeContents(td);
    editor.forceRedraw();
    editor.focusEditor();
  }

  switch (button_id) {
    // ROWS

  case "TO-row-insert-above":
  case "TO-row-insert-under":
    var tr = this.getClosest("tr");
    if (!tr) {
      break;
    }
    var otr = tr.cloneNode(true);
    clearRow(otr);
    tr.parentNode.insertBefore(otr, /under/.test(button_id) ? tr.nextSibling : tr);
    editor.forceRedraw();
    editor.focusEditor();
    break;
  case "TO-row-delete":
    var tr = this.getClosest("tr");
    if (!tr) {
      break;
    }
    var par = tr.parentNode;
    if (par.rows.length == 1) {
      alert(Xinha._lc("Xinha cowardly refuses to delete the last row in table.", "TableOperations"));
      break;
    }
    // set the caret first to a position that doesn't
    // disappear.
    selectNextNode(tr);
    par.removeChild(tr);
    editor.forceRedraw();
    editor.focusEditor();
    editor.updateToolbar();
    break;
  case "TO-row-split":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    splitRow(td);
    break;

    // COLUMNS

  case "TO-col-insert-before":
  case "TO-col-insert-after":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    var rows = td.parentNode.parentNode.rows;
    var index = td.cellIndex;
    var lastColumn = (td.parentNode.cells.length == index + 1);
    for (var i = rows.length; --i >= 0;) {
      var tr = rows[i];
      var otd = editor._doc.createElement("td");
      otd.innerHTML = mozbr;
      if (lastColumn && Xinha.is_ie) 
      {
        tr.insertBefore(otd);
      } 
      else 
      {
        var ref = tr.cells[index + (/after/.test(button_id) ? 1 : 0)];
        tr.insertBefore(otd, ref);
      }
    }
    editor.focusEditor();
    break;
  case "TO-col-split":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    splitCol(td);
    break;
  case "TO-col-delete":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    var index = td.cellIndex;
    if (td.parentNode.cells.length == 1) {
      alert(Xinha._lc("Xinha cowardly refuses to delete the last column in table.", "TableOperations"));
      break;
    }
    // set the caret first to a position that doesn't disappear
    selectNextNode(td);
    var rows = td.parentNode.parentNode.rows;
    for (var i = rows.length; --i >= 0;) {
      var tr = rows[i];
      tr.removeChild(tr.cells[index]);
    }
    editor.forceRedraw();
    editor.focusEditor();
    editor.updateToolbar();
    break;

    // CELLS

  case "TO-cell-split":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    splitCell(td);
    break;
  case "TO-cell-insert-before":
  case "TO-cell-insert-after":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    var tr = td.parentNode;
    var otd = editor._doc.createElement("td");
    otd.innerHTML = mozbr;
    tr.insertBefore(otd, /after/.test(button_id) ? td.nextSibling : td);
    editor.forceRedraw();
    editor.focusEditor();
    break;
  case "TO-cell-delete":
    var td = this.getClosest("td");
    if (!td) {
      break;
    }
    if (td.parentNode.cells.length == 1) {
      alert(Xinha._lc("Xinha cowardly refuses to delete the last cell in row.", "TableOperations"));
      break;
    }
    // set the caret first to a position that doesn't disappear
    selectNextNode(td);
    td.parentNode.removeChild(td);
    editor.forceRedraw();
    editor.updateToolbar();
    break;
  case "TO-cell-merge":
    //Mozilla, as opposed to IE, allows the selection of several cells, which is fine :)
    var sel = editor._getSelection();
    if (!Xinha.is_ie && sel.rangeCount > 1) {
      var range = sel.getRangeAt(0);
      var td = range.startContainer.childNodes[range.startOffset];
      var tr = td.parentNode;
      var cell_index = td.cellIndex;
      var row_index = tr.rowIndex;
      var row_index2 = 0;
      var rownum = row_index;
      var no_cols = 0;
      var row_colspan = 0;
      var td2, tr2;
      for(i=0; i<sel.rangeCount; i++) {
        range = sel.getRangeAt(i);
        td2 = range.startContainer.childNodes[range.startOffset];
        tr2 = td2.parentNode;
        if(tr2.rowIndex != rownum) {
          rownum = tr2.rowIndex;
          row_colspan = 0;
        }
        row_colspan += td2.colSpan;
        if(row_colspan > no_cols) {
          no_cols = row_colspan;
        }
        if(tr2.rowIndex + td2.rowSpan - 1 > row_index2) {
          row_index2 = tr2.rowIndex + td2.rowSpan - 1;
        }
      }
      var no_rows = row_index2 - row_index + 1;
      var table = tr.parentNode;
      cellMerge(table, cell_index, row_index, no_cols, no_rows); 
    } else {
      // Internet Explorer "browser" or not more than one cell selected in Moz
      var td = this.getClosest("td");
      if (!td) {
        alert(Xinha._lc("Please click into some cell", "TableOperations"));
        break;
      }
      var tr = td.parentNode;
      var cell_index = td.cellIndex;
      var row_index = tr.rowIndex;
      // pass cellMerge and the indices so apply() can call cellMerge and know 
      // what cell was selected when the dialog was opened
      this.dialogMerge(cellMerge, cell_index, row_index);
    }
    break;

    // PROPERTIES

  case "TO-table-prop":
    this.dialogTableProperties();
    break;

  case "TO-row-prop":
    this.dialogRowCellProperties(false);
    break;

  case "TO-cell-prop":
    this.dialogRowCellProperties(true);
    break;

  default:
    alert("Button [" + button_id + "] not yet implemented");
  }
};

// the list of buttons added by this plugin
TableOperations.btnList = [
  // table properties button
  ["table-prop",       "table", "Table properties"],
  null, // separator

  // ROWS
  ["row-prop",         "tr", "Row properties"],
  ["row-insert-above", "tr", "Insert row before"],
  ["row-insert-under", "tr", "Insert row after"],
  ["row-delete",       "tr", "Delete row"],
  ["row-split",        "td[rowSpan!=1]", "Split row"],
  null,

  // COLS
  ["col-insert-before", "td", "Insert column before"],
  ["col-insert-after",  "td", "Insert column after"],
  ["col-delete",        "td", "Delete column"],
  ["col-split",         "td[colSpan!=1]", "Split column"],
  null,

  // CELLS
  ["cell-prop",          "td", "Cell properties"],
  ["cell-insert-before", "td", "Insert cell before"],
  ["cell-insert-after",  "td", "Insert cell after"],
  ["cell-delete",        "td", "Delete cell"],
  ["cell-merge",         "tr", "Merge cells"],
  ["cell-split",         "td[colSpan!=1,rowSpan!=1]", "Split cell"]
];

TableOperations.prototype.dialogMerge = function(merge_func, cell_index, row_index) {
  var table = this.getClosest("table");
  var self = this;
  var editor = this.editor;

  if (!this.dialogMergeCellsHtml) {
    Xinha._getback(Xinha.getPluginDir("TableOperations") + '/popups/dialogMergeCells.html', function(getback) { self.dialogMergeCellsHtml = getback; self.dialogMerge(merge_func, cell_index, row_index); });
    return;
  }

  if (!this.dialogMergeCells) {
    this.dialogMergeCells = new Xinha.Dialog(editor, this.dialogMergeCellsHtml, 'TableOperations', {width:400});
    this.dialogMergeCells.getElementById('cancel').onclick = function() { self.dialogMergeCells.hide(); };
  }

  var dialog = this.dialogMergeCells;
  function apply() {
    dialog.hide();
    no_cols = parseInt(dialog.getElementById('f_cols').value,10) + 1;
    no_rows = parseInt(dialog.getElementById('f_rows').value,10) + 1;
    merge_func(table, cell_index, row_index, no_cols, no_rows);    
    return
  }

  this.dialogMergeCells.getElementById('ok').onclick = apply;
  this.dialogMergeCells.show();
  this.dialogMergeCells.getElementById('f_cols').focus();
}

TableOperations.prototype.dialogTableProperties = function() {

  var table = this.getClosest("table");
  var self = this;
  var editor = this.editor;

  if(!this.dialogTablePropertiesHtml){ // retrieve the raw dialog contents
    Xinha._getback( Xinha.getPluginDir("TableOperations") + '/popups/dialogTable.html', function(getback) { self.dialogTablePropertiesHtml = getback; self.dialogTableProperties(); });
    return;
  }
  if (!this.dialogTable) {
    // Now we have everything we need, so we can build the dialog.
    this.dialogTable = new Xinha.Dialog(editor, this.dialogTablePropertiesHtml, 'TableOperations',{width:440})
    this.dialogTable.getElementById('cancel').onclick = function() { self.dialogTable.hide()};
  }
  var dialog = this.dialogTable;
  
  var Styler = new Xinha.InlineStyler(table, this.editor, dialog);
  
  function apply() {
    var params = dialog.hide();
    Styler.applyStyle(params);
    
    for (var i in params) {
      if(typeof params[i] == 'function') continue;
      var val = params[i];
      //if (val == null) continue;
      if (typeof val == 'object' && val != null && val.tagName) val = val.value;
      switch (i) {
      case "caption":
        if (/\S/.test(val)) {
          // contains non white-space characters
          var caption = table.getElementsByTagName("caption")[0];
          if (!caption) {
            caption = dialog.editor._doc.createElement("caption");
            table.insertBefore(caption, table.firstChild);
          }
          caption.innerHTML = val;
        } else {
          // search for caption and delete it if found
          var caption = table.getElementsByTagName("caption")[0];
          if (caption) {
            caption.parentNode.removeChild(caption);
          }
        }
        break;
      case "summary":
        table.summary = val;
        break;
      case "width":
        table.style.width = ("" + val) + params.f_unit;
        break;
      case "align":
        table.align = val;
        break;
      case "spacing":
        table.cellSpacing = val;
        break;
      case "padding":
        table.cellPadding = val;
        break;
      case "borders":
        table.border = val;
        break;
      case "frames":
        table.frame = val;
        break;
      case "rules":
        table.rules = val;
        break;
      }
    }

    // various workarounds to refresh the table display (Gecko,
    // what's going on?! do not disappoint me!)
    self.editor.forceRedraw();
    self.editor.focusEditor();
    self.editor.updateToolbar();
    var save_collapse = table.style.borderCollapse;
    table.style.borderCollapse = "collapse";
    table.style.borderCollapse = "separate";
    table.style.borderCollapse = save_collapse;
  }
  
  var st_layout = Styler.createStyleLayoutFieldset();
  var p = dialog.getElementById("TO_layout");
  p.replaceChild(st_layout,p.firstChild);
  
  var st_prop = Styler.createStyleFieldset();
  p = dialog.getElementById("TO_style");
  p.replaceChild(st_prop,p.firstChild);

  this.dialogTable.getElementById('ok').onclick = apply;

  // gather element's values
  var values = {};
  var capel = table.getElementsByTagName("caption")[0];
  if (capel) {
    values['caption'] = capel.innerHTML;
  }
  else values['caption'] = "";
  values['summary'] = table.summary;
  
  values['spacing'] = table.cellSpacing;
  values['padding'] = table.cellPadding;
  var f_borders = table.border;
  
  values['frames'] = table.frame;
  values['rules'] = table.rules;
  
  this.dialogTable.show(values);
};

TableOperations.prototype.dialogRowCellProperties = function(cell) {
  // retrieve existing values
  var element = this.getClosest(cell ? "td" : "tr");
  var table = this.getClosest("table");

  var self = this;
  var editor = this.editor;

  if(!self.dialogRowCellPropertiesHtml) // retrieve the raw dialog contents
  {
    Xinha._getback( Xinha.getPluginDir("TableOperations") + '/popups/dialogRowCell.html', function(getback) { self.dialogRowCellPropertiesHtml = getback; self.dialogRowCellProperties(cell); });
    return;
  }
  if (!this.dialogRowCell) {
    // Now we have everything we need, so we can build the dialog.
    this.dialogRowCell = new Xinha.Dialog(editor, self.dialogRowCellPropertiesHtml, 'TableOperations',{width:440})
    this.dialogRowCell.getElementById('cancel').onclick = function() { self.dialogRowCell.hide()};
  }
  
  var dialog = this.dialogRowCell;
  dialog.getElementById('title').innerHTML = cell ? Xinha._lc("Cell Properties", "TableOperations") : Xinha._lc("Row Properties", "TableOperations");
  var Styler = new Xinha.InlineStyler(element, self.editor, dialog);
  
  function apply() {
    var params = dialog.hide();
    Styler.applyStyle(params);
    
    // various workarounds to refresh the table display (Gecko,
    // what's going on?! do not disappoint me!)
    self.editor.forceRedraw();
    self.editor.focusEditor();
    self.editor.updateToolbar();
    var save_collapse = table.style.borderCollapse;
    table.style.borderCollapse = "collapse";
    table.style.borderCollapse = "separate";
    table.style.borderCollapse = save_collapse;
  }
  
  var st_layout = Styler.createStyleLayoutFieldset();
  var p = dialog.getElementById("TO_layout");

  p.replaceChild(st_layout,p.firstChild);
  
  var st_prop = Styler.createStyleFieldset();
  p = dialog.getElementById("TO_style");
  p.replaceChild(st_prop,p.firstChild);

  this.dialogRowCell.getElementById('ok').onclick = apply;
  this.dialogRowCell.show();
};
