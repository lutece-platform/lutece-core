<#--
Macro: selectIconFont

Description: Generates an accessible icon picker with search functionality.
Icons are loaded from a JSON file and displayed in a custom dropdown.

Parameters:
- id (string, optional): the ID of the icon picker.
- class (string, optional): the CSS class of the trigger button.
- name (string, optional): the name for form submission.
- showListLabel (boolean, optional): whether to display the label of each option.
- showListIcon (boolean, optional): whether to display the icon of each option.
- searchShow (boolean, optional): whether to show the search input.
- searchFocus (boolean, optional): not used, kept for backward compatibility.
- searchHighlight (boolean, optional): not used, kept for backward compatibility.
- type (string, optional): the type of the icon file.
- prefix (string, optional): the prefix of the icon CSS class.
- iconsUrl (string, optional): the URL of the JSON file containing icon data.
- resources (boolean, optional): not used, kept for backward compatibility.
- defaultValue (string, optional): the default selected icon name.

Snippet:

    Default icon picker with search:

    <@selectIconFont id='iconPicker' name='entity_icon' />

    Icon picker with a pre-selected icon and no search bar:

    <@selectIconFont id='categoryIcon' name='category_icon' defaultValue='folder' searchShow=false />

-->
<#macro selectIconFont id='selectIcon' class='' name='resource-icon' showListLabel=true showListIcon=true searchShow=true searchFocus=false searchHighlight=true type='json' prefix='ti' iconsUrl='themes/admin/shared/css/vendor/tabler/tabler-icons.json' resources=true defaultValue='' deprecated...>
<@deprecatedWarning args=deprecated />
<#-- Hidden input for form submission -->
<input type="hidden" name="${name}" id="${id}" value="${defaultValue!}">
<#-- Icon picker -->
<div class="lutece-icon-picker" id="${id}-ctn">
	<button type="button" class="lutece-icon-picker-btn<#if class?has_content> ${class}<#else> form-select</#if>" id="${id}-btn" <#if !searchShow>role="combobox" aria-controls="${id}-list" aria-activedescendant=""</#if> aria-haspopup="listbox" aria-expanded="false">
		<span class="lutece-icon-picker-preview" id="${id}-pv">
			<#if defaultValue?has_content>
				<i class="${prefix} ${prefix}-${defaultValue}" aria-hidden="true"></i>
				<#if showListLabel><span>${defaultValue}</span></#if>
			</#if>
		</span>
		<i class="ti ti-chevron-down" aria-hidden="true"></i>
	</button>
	<div class="lutece-icon-picker-dropdown" id="${id}-dd" hidden>
		<#if searchShow>
		<div class="lutece-icon-picker-search">
			<i class="ti ti-search" aria-hidden="true"></i>
			<input type="text" id="${id}-q" role="combobox" aria-expanded="true" aria-autocomplete="list" aria-controls="${id}-list" aria-activedescendant="" placeholder="#i18n{portal.util.labelSearch}" autocomplete="off">
		</div>
		</#if>
		<ul role="listbox" id="${id}-list" class="lutece-icon-picker-list"<#if !searchShow> tabindex="-1"</#if>></ul>
		<div class="lutece-icon-picker-no-result" id="${id}-nr" hidden>#i18n{portal.util.labelNoItem}</div>
		<div role="status" aria-live="polite" aria-atomic="true" id="${id}-sr" class="visually-hidden"></div>
	</div>
</div>
<#-- JavaScript -->
<script>
(function() {
	var ID = '${id}', PFX = '${prefix}', SI = ${showListIcon?c}, SL = ${showListLabel?c}, DV = '${defaultValue!}';
	var el = function(s) { return document.getElementById(ID + s); };
	var hidden = el(''), btn = el('-btn'), dd = el('-dd');
	var search = <#if searchShow>el('-q')<#else>null</#if>;
	var list = el('-list'), nr = el('-nr'), sr = el('-sr'), pv = el('-pv');
	var combobox = search || btn;
	var allOpts = [], visOpts = [], focIdx = -1, isOpen = false;

	function mkOpt(name, i) {
		var li = document.createElement('li');
		li.setAttribute('role', 'option');
		li.setAttribute('aria-selected', name === hidden.value ? 'true' : 'false');
		li.id = ID + '-o' + i;
		li.dataset.v = name;
		if (SI) {
			var ic = document.createElement('i');
			<#noparse>ic.className = PFX + ' ' + PFX + '-' + name;</#noparse>
			ic.setAttribute('aria-hidden', 'true');
			li.appendChild(ic);
		}
		if (SL) {
			var sp = document.createElement('span');
			sp.textContent = name;
			li.appendChild(sp);
		}
		li.addEventListener('click', function() { pick(name); });
		return li;
	}

	function updatePreview(name) {
		pv.innerHTML = '';
		if (!name) return;
		if (SI) {
			var ic = document.createElement('i');
			<#noparse>ic.className = PFX + ' ' + PFX + '-' + name;</#noparse>
			ic.setAttribute('aria-hidden', 'true');
			pv.appendChild(ic);
		}
		if (SL) {
			var sp = document.createElement('span');
			sp.textContent = name;
			pv.appendChild(sp);
		}
	}

	function pick(name) {
		hidden.value = name;
		updatePreview(name);
		allOpts.forEach(function(o) {
			o.setAttribute('aria-selected', o.dataset.v === name ? 'true' : 'false');
		});
		shut();
		btn.focus();
	}

	function doFilter(q) {
		q = (q || '').toLowerCase().trim();
		visOpts = [];
		allOpts.forEach(function(o) {
			var match = !q || o.dataset.v.indexOf(q) !== -1;
			o.hidden = !match;
			if (match) visOpts.push(o);
		});
		nr.hidden = visOpts.length > 0;
		sr.textContent = visOpts.length + ' résultat' + (visOpts.length !== 1 ? 's' : '');
		focIdx = -1;
		clearFoc();
		if (visOpts.length > 0) setFoc(0);
	}

	function setFoc(i) {
		clearFoc();
		if (i < 0 || i >= visOpts.length) return;
		focIdx = i;
		visOpts[i].classList.add('is-focused');
		combobox.setAttribute('aria-activedescendant', visOpts[i].id);
		visOpts[i].scrollIntoView({ block: 'nearest' });
	}

	function clearFoc() {
		var f = list.querySelector('.is-focused');
		if (f) f.classList.remove('is-focused');
		combobox.setAttribute('aria-activedescendant', '');
	}

	function show() {
		if (isOpen) return;
		isOpen = true;
		dd.hidden = false;
		btn.setAttribute('aria-expanded', 'true');
		if (search) search.value = '';
		doFilter('');
		var sel = list.querySelector('[aria-selected="true"]');
		if (sel) {
			var idx = visOpts.indexOf(sel);
			if (idx >= 0) setFoc(idx);
		}
		if (search) search.focus();
		else list.focus();
		setTimeout(function() { document.addEventListener('mousedown', onOutside); }, 0);
	}

	function shut() {
		if (!isOpen) return;
		isOpen = false;
		dd.hidden = true;
		btn.setAttribute('aria-expanded', 'false');
		focIdx = -1;
		clearFoc();
		document.removeEventListener('mousedown', onOutside);
	}

	function onOutside(e) {
		if (!el('-ctn').contains(e.target)) shut();
	}

	function onKey(e) {
		var isSearch = search && e.target === search;
		switch (e.key) {
			case 'ArrowDown':
				e.preventDefault();
				if (!isOpen) { show(); return; }
				if (focIdx < visOpts.length - 1) setFoc(focIdx + 1);
				break;
			case 'ArrowUp':
				e.preventDefault();
				if (!isOpen) { show(); return; }
				if (focIdx > 0) setFoc(focIdx - 1);
				break;
			case 'Home':
				if (isOpen && !isSearch) { e.preventDefault(); setFoc(0); }
				break;
			case 'End':
				if (isOpen && !isSearch) { e.preventDefault(); setFoc(visOpts.length - 1); }
				break;
			case 'Enter':
				e.preventDefault();
				if (isOpen && focIdx >= 0 && focIdx < visOpts.length) {
					pick(visOpts[focIdx].dataset.v);
				} else if (!isOpen) {
					show();
				}
				break;
			case ' ':
				if (isSearch) return;
				e.preventDefault();
				if (isOpen && focIdx >= 0 && focIdx < visOpts.length) {
					pick(visOpts[focIdx].dataset.v);
				} else if (!isOpen) {
					show();
				}
				break;
			case 'Escape':
				if (isOpen) { e.preventDefault(); shut(); btn.focus(); }
				break;
			case 'Tab':
				if (isOpen) shut();
				break;
		}
	}

	btn.addEventListener('click', function() { isOpen ? shut() : show(); });
	btn.addEventListener('keydown', onKey);

	if (search) {
		var timer;
		search.addEventListener('input', function() {
			clearTimeout(timer);
			var v = search.value;
			timer = setTimeout(function() { doFilter(v); }, 120);
		});
		search.addEventListener('keydown', onKey);
	} else {
		list.addEventListener('keydown', onKey);
	}

	fetch('${iconsUrl}')
		.then(function(r) { return r.json(); })
		.then(function(data) {
			var frag = document.createDocumentFragment();
			Object.keys(data).forEach(function(name, i) {
				var opt = mkOpt(name, i);
				allOpts.push(opt);
				frag.appendChild(opt);
			});
			list.appendChild(frag);
			visOpts = allOpts.slice();
			if (DV) updatePreview(DV);
		})
		.catch(function() {
			nr.hidden = false;
		});
})();
</script>
</#macro>