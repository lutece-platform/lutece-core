<@row>
	<div class="four columns pull-nine">
		<div id="quick-search">
		<@tform action='jsp/site/Portal.jsp' method='get'>
			<@input type='hidden' name='page' value='search' />
			<@formGroup labelFor='searchfield' labelKey='#i18n{portal.site.page_menu_tools.labelSearch}' params='accesskey="4"'>
				<@input type='text' name='query' id='searchfield' /> 
				<@button type='submit' value='ok' id='searchbutton' title='Lancer la recherche' />
			</@formGroup>
		</@tform>
		</div>
	</div>
</@row>
<@row>
	<div class="four columns pull-nine">
		<div id="menu-tools">
			<@ul>
				<li><@link href='jsp/site/Portal.jsp?page=map' params='accesskey="3"'>#i18n{portal.site.site_map.pathLabel}</@link></li>
				<li><@link href='jsp/site/Portal.jsp?page=contact' params='accesskey="7"'>#i18n{portal.site.page_menu_tools.xpage.contact}</@link></li>
			</@ul>
		</div>
	</div>
</@row>