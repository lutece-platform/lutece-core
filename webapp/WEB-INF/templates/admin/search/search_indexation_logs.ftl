<@row>
	<@columns>
		<@box color='danger'>
			<@boxHeader title='#i18n{portal.search.indexer_logs.titleIndexerLogs}' />
			<@boxBody>
				<pre class="">${logs}</pre>
				<@aButton href="jsp/admin/search/ManageSearchIndexation.jsp" buttonIcon='times' title='#i18n{portal.util.labelBack}'  size='' color='secondary' />
			</@boxBody>
		</@box>
	</@columns>
</@row>
