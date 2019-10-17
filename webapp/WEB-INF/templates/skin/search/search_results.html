<div class="row">
    <div class="col-xs-12 col-sm-4">
        <form method="get" action="jsp/site/Portal.jsp" class="form-horizontal">
            <input type="hidden" name="page" value="search" />
            <div class="form-group">
                <div class="input-group">
                    <input type="text" class="form-control input-sm" name="query" value="${query?if_exists}" />
                    <span class="input-group-btn">
                        <button class="btn btn-default btn-sm" type="submit" title="#i18n{portal.search.search_results.buttonSearch}">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
                </div>
            </div>
            <div class="form-group">
                <#-- Number of documents per page selector -->
                <label class="col-sm-9">#i18n{portal.search.search_results.labelNbDocsPerPage}</label>
                <div class="col-sm-3">
                    <select name="items_per_page" class="form-control input-sm">
                        <#list [ "10" , "20" , "50" , "100" ] as nb>
                        <#if nb_items_per_page = nb >
                        <option selected="selected" value="${nb}">${nb}</option>
                        <#else>
                        <option value="${nb}">${nb}</option>
                        </#if>
                        </#list>
                    </select>
                </div>
            </div>
            <#if type_filter != "none">
            <div class="form-group">
                <#-- type_filter -->
                <label class="col-sm-4">#i18n{portal.search.search_results.typeFilter}</label>
                <div class="col-sm-8">
                <#if type_filter == "option">
                    <select name="type_filter" class="form-control input-sm">
                        <#list list_type_and_link as map>
                        <option value="${map.type}"><#if map_index != 0>${map.type}<#else>#i18n{portal.search.search_results.typeFilterNone}</#if></option>
                        </#list>
                    </select>
                <#else>
                    <#list list_type_and_link as map>
                    <#if type_filter != "checkbox" || map_index != 0>
                        <input type=${type_filter} name="type_filter" value="${map.type}" />
                        <#if map.link?exists && map.link != "">
                            <a href="${map.link!}"  ><#if map_index != 0>${map.type}<#else>#i18n{portal.search.search_results.typeFilterNone}</#if></a>
                        <#else>
                            <#if map_index != 0>${map.type}<#else>#i18n{portal.search.search_results.typeFilterNone}</#if>
                        </#if>
                    </#if>
                    </#list>
                </#if>
                </div>
            </div>
            </#if>
            <div class="form-group">
                <#-- default_operator and/or -->
                <label class="col-sm-5">#i18n{portal.search.search_results.operator}</label>
                <div class="col-sm-7">
                    <select name="default_operator" class="input-sm form-control">
                        <#list ["OR","AND"] as operator>
                        <option value="${operator}" <#if operator = default_operator> selected="selected"</#if> >
                                #i18n{portal.search.search_results.operator.${operator}}
                        </option>
                        </#list>
                    </select>
                </div>
            </div>
        <#if help_message!="">
        <#-- help_message text -->
        <p class="help-block">#i18n{portal.search.search_results.helpMessage}: ${help_message}</p>
        </#if>
        <#if date_filter == "1">
        <#-- date_filter 0/1 -->
        <p>#i18n{portal.search.search_results.dateFilter} :</p>
        <div class="form-group">
            <@fieldInputCalendarSite i18nLabelKey="portal.search.search_results.dateFilterAfter" inputName="date_after" />
            <@fieldInputCalendarSite i18nLabelKey="portal.search.search_results.dateFilterBefore" inputName="date_before" />
        </div>
        </#if>
        <#if taglist?has_content>
        <#if tag_filter == "1" && taglist?trim != "">
        <div class="form-group">
            <#-- tag_filter -->
            <#list taglist?split(" ") as tag>
            <#if tag?exists && tag?trim !="">
            <input type="submit" name="tag_filter" value="${tag}" />
            </#if>
            </#list>
        </div>
        </#if>
        </#if>
        </form>
    </div>
    <div class="col-xs-12 col-sm-8 col-md-8">
    <@fieldSet> 
        <legend>#i18n{portal.search.search_results.title}</legend>
        <#if error?has_content>
            <p class="alert alert-danger">${error}</p>
        </#if>
            <#-- included sponsored links if available - might be null -->
            ${sponsoredlinks_set!}
            <div class="pull-right">
                #i18n{portal.search.search_results.labelResultsCount} : <strong>${paginator.itemsCount}</strong> #i18n{portal.search.search_results.labelResultsRange} : <strong>${paginator.rangeMin} - ${paginator.rangeMax}</strong>
            </div>
            <@pagination paginator=paginator/>
            <hr>
            <ul class="nav navbar-nav nav navbar-nav-pills nav navbar-nav-stacked">
                <#list results_list as result>
                <li>
                    <a href="${result.url?xhtml}">
                        [ ${result.type} ] ${result.title}
                        <#if result.summary?has_content><br /><strong>${result.summary}</strong></#if>
                        <#if result.date?has_content><em class="pull-right">${result.date?date?string.short}</em></#if>
                    </a>
                <li>
                    </#list>
            </ul>
    </@fieldSet>
    </div>
</div>
