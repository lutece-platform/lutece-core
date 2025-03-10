<#-- Macro: cPaginationLinks

Description: permet de définir un élément de la pagination.

Parameters:

@param - class - string - optional - ajoute une classe CSS à la pagination
@param - paginator - list - required - Objet Paginator du core LUTECE ou liste avec comme attributs les url et index de pagination.
@param - fragment - string - optional - permet d'ajouter un suffixe à l'url de la page
-->
<#macro cPaginationLinks paginator fragment='' class=''>
<#local nbLinkPagesToDisplay = 10 />
<#local offsetPrev = nbLinkPagesToDisplay / 2 />
<#local offsetNext = nbLinkPagesToDisplay / 2 />
<#if ( paginator.pageCurrent <= nbLinkPagesToDisplay - offsetPrev )>
<#local offsetPrev = paginator.pageCurrent - 1 />
<#local offsetNext = nbLinkPagesToDisplay - offsetPrev />
<#elseif ( paginator.pageCurrent + offsetNext > paginator.pagesCount )>
<#local offsetNext = paginator.pagesCount - paginator.pageCurrent />
<#local offsetPrev = nbLinkPagesToDisplay - offsetNext />
</#if>
<ul class="pagination mb-0<#if class!=''> ${class}</#if>">
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<li class="page-item">
		<a class="page-link" href="${paginator.firstPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="${paginator.labelFirst}">
			<svg xmlns="http://www.w3.org/2000/svg" width="9.638" height="15.153" viewBox="0 0 9.638 15.153">
				<g transform="translate(8.279 13.676) rotate(180)">
					<g transform="translate(0 0)">
						<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
					</g>
				</g>
			</svg>
		</a>
	</li>
</#if>
<#if (paginator.pageCurrent > 1) >
	<li class="page-item">
		<a class="page-link" href="${paginator.previousPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="#i18n{theme.labelPrev}">
			<svg xmlns="http://www.w3.org/2000/svg" width="9.638" height="15.153" viewBox="0 0 9.638 15.153">
				<g transform="translate(8.279 13.676) rotate(180)">
					<g transform="translate(0 0)">
						<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
					</g>
				</g>
			</svg>
		</a>
	</li>
<#else>
	<li class="page-item disabled">
		<a class="page-link" href="${paginator.firstPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="${paginator.labelFirst}">
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153">
				<g transform="translate(8.279 13.676) rotate(180)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
		</a>
	</li>
</#if>
<#if ( paginator.pageCurrent - offsetPrev > 1 )>
	<li class="page-item group">
		<a class="page-link cesure" href="${(paginator.pagesLinks?first).url?xhtml}<#if fragment !=''>#${fragment}</#if>" title="#i18n{theme.labelNextGroup}"><strong>...</strong></a>
	</li>
</#if>
<#list paginator.pagesLinks as link>
	<#if ( link.index == paginator.pageCurrent )>
		<li class="page-item active" disabled aria-current="page" >
			<span class="page-link" title="page ${link.name} sur ${paginator.pagesCount}">${link.name}</span>
		</li>
	<#else>
		<li class="page-item">
			<a class="page-link" title="page ${link.name} sur ${paginator.pagesCount}" href="${link.url?xhtml}<#if fragment !=''>#${fragment}</#if>">${link.name}</a>
		</li>
	</#if>
</#list>
<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount) >
	<li class="page-item group">
		<a class="page-link cesure" href="${(paginator.pagesLinks?last).url?xhtml}<#if fragment !=''>#${fragment}</#if>" title="#i18n{theme.labelNextGroup}" >
			<strong>...</strong>
		</a>
	</li>
</#if>
<#if (paginator.pageCurrent < paginator.pagesCount) >
	<li class="page-item">
		<a class="page-link" href="${paginator.nextPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="#i18n{theme.labelNext}">
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153">
				<g transform="translate(1.359 1.477)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
		</a>
	</li>
	<#if ( paginator.pageCurrent + offsetNext < paginator.pagesCount) >
		<li class="page-item">
			<a class="page-link" href="${paginator.lastPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="${paginator.labelLast}">
				${paginator.labelLast}
			</a>
		</li>
	</#if>
<#else>
	<li class="page-item disabled">
		<a class="page-link" href="${paginator.lastPageLink?xhtml}<#if fragment !=''>#${fragment}</#if>" title="${paginator.labelLast}" >
			<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 9.638 15.153">
				<g transform="translate(1.359 1.477)">
					<path class="a" d="M1.309,0,0,1.423,4.3,6.1,0,10.776,1.309,12.2,6.92,6.1Z"/>
				</g>
			</svg>
		</a>
	</li>
</#if>
</ul>
</#macro>