<#-- Macro: paginationAdmin

Description: Generates a pagination bar for navigating through pages of a list, with options to display item count and select the number of items per page.

Parameters:
- paginator (object): an object that contains information about the current and next/previous pages in a list.
- combo (boolean, optional): whether to display a combo box for selecting the number of items per page (default is 0).
- form (boolean, optional): whether to wrap the item count and item per page controls in a form element (default is 1).
- nb_items_per_page (number, optional): the number of items to display per page (default is the value of the "nb_items_per_page" variable).
- showcount (boolean, optional): whether to display the item count (default is 1).
- showall (boolean, optional): whether to display a link to show all items on a single page (default is 0).

-->
<#macro paginationAdmin paginator class='' combo=0 form=1 nb_items_per_page=nb_items_per_page showcount=1 showall=0>
<#if paginator??>
<@div class='d-flex ${class}'>
<#if (paginator.pagesCount > 1) >
<@paginationPageLinks paginator=paginator />
</#if>
<#if form == 1 >
<@tform type='' class='d-flex w-100'>
<@paginationItemCount paginator=paginator combo=combo nb_items_per_page=nb_items_per_page showcount=showcount showall=showall />
</@tform>
<#else>
<@paginationItemCount paginator=paginator combo=combo nb_items_per_page=nb_items_per_page showcount=showcount showall=showall />
</#if>
</@div>
</#if>
</#macro>

<#--
  Macro: paginationAjax
 Description: Pagination macro with AJAX data loading – simplified version
 Parameters:
- paginator: paginator object containing pagination information
- columns: list of columns to display (array of {name, property, sortable})
- ajaxUrl: URL for loading data via AJAX
- tableId: unique ID of the table (default: "ajaxTable")
- combo: display the selector for number of items per page (0 or 1, default: 1)
- showcount: display the total number of items (0 or 1, default: 1)
- actions: (optional) object with actions
	view: {url, icon, title}
	edit: {url, icon, title, offcanvas}
	delete: {url, icon, title, confirm}

-->
<#macro paginationAjax paginator columns ajaxUrl tableId="ajaxTable" combo=1 showcount=1 actions={}>
  
  <#-- Container principal avec card HTML standard -->
  <div class="card mb-4">
    <div class="card-body">
      <div id="${tableId}-container" class="ajax-pagination-container">
        
        <#-- Affichage du nombre d'éléments -->
        <#if showcount == 1>
          <div class="mb-3">
            <span class="badge bg-info">
              <i class="ti ti-list-numbers"></i>
              <span id="${tableId}-count">${paginator.itemsCount}</span> ${paginator.labelItemCount}
            </span>
          </div>
        </#if>
        
        <#-- Table avec macro Lutece -->
        <@table class="table-responsive" hover=true striped=true>
          <thead>
            <tr>
              <#list columns as column>
                <th class="${(column.sortable?? && column.sortable)?then('sortable cursor-pointer', '')}" data-sort="${column.property!}">
                  ${column.name}
                  <#if column.sortable?? && column.sortable>
                    <i class="ti ti-arrows-sort ms-1"></i>
                  </#if>
                </th>
              </#list>
              <#-- Colonnes d'actions -->
              <#if actions?has_content>
                <th class="text-center" style="width: 150px;">
                  <i class="ti ti-settings"></i> Actions
                </th>
              </#if>
            </tr>
          </thead>
          <tbody id="${tableId}-body">
            <#-- Le contenu sera chargé dynamiquement -->
            <tr>
              <td colspan="${columns?size + (actions?has_content?then(1, 0))}" class="text-center">
                <i class="ti ti-loader-2 animate-spin"></i> Chargement...
              </td>
            </tr>
          </tbody>
        </@table>
        
        <#-- Contrôles de pagination -->
        <div class="row mt-3">
          <div class="col-md-6">
            <#-- Sélecteur du nombre d'items par page -->
            <#if combo == 1>
              <div class="d-flex align-items-center">
                <label for="${tableId}-items-per-page" class="me-2">Éléments par page:</label>
                <select id="${tableId}-items-per-page" class="form-select form-select-sm" style="width: auto;">
                  <option value="5"<#if paginator.itemsPerPage == 5> selected</#if>>5</option>
                  <option value="10"<#if paginator.itemsPerPage == 10> selected</#if>>10</option>
                  <option value="20"<#if paginator.itemsPerPage == 20> selected</#if>>20</option>
                  <option value="50"<#if paginator.itemsPerPage == 50> selected</#if>>50</option>
                  <option value="100"<#if paginator.itemsPerPage == 100> selected</#if>>100</option>
                </select>
              </div>
            </#if>
          </div>
          
          <div class="col-md-6 text-end">
            <#-- Liens de pagination -->
            <nav aria-label="Pagination">
              <ul id="${tableId}-pagination" class="pagination pagination-sm mb-0 justify-content-end">
                <#-- Les liens seront générés dynamiquement -->
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <#-- Script JavaScript -->
  <script>
  document.addEventListener('DOMContentLoaded', function() {
    // Namespace pour éviter les conflits
    window.LutecePaginationAjax = window.LutecePaginationAjax || {};
    
    const paginationAjax = (function() {
      const tableId = '${tableId}';
      const ajaxUrl = '${ajaxUrl}';
      const columns = [
        <#list columns as column>
          {
            name: '${column.name?js_string}',
            property: '${column.property?js_string}'<#if column.sortable??>,
            sortable: ${column.sortable?c}</#if>
          }<#if column_has_next>,</#if>
        </#list>
      ];
      
      // Configuration des actions
      const actions = {
        <#if actions.view??>
          view: {
            url: '${actions.view.url?js_string}',
            icon: '${actions.view.icon!'eye'?js_string}',
            title: '${actions.view.title!'Voir'?js_string}',
            btnClass: '${actions.view.btnClass!'info'?js_string}',
            size: '${actions.view.size!'sm'?js_string}'
          }<#if actions.edit?? || actions.delete??>,</#if>
        </#if>
        <#if actions.edit??>
          edit: {
            url: '${actions.edit.url?js_string}',
            icon: '${actions.edit.icon!'pencil'?js_string}',
            title: '${actions.edit.title!'Modifier'?js_string}',
            btnClass: '${actions.edit.btnClass!'primary'?js_string}',
            size: '${actions.edit.size!'sm'?js_string}'<#if actions.edit.offcanvas??>,
            offcanvas: {
              targetElement: '${actions.edit.offcanvas.targetElement!'#modify-form'?js_string}',
              position: '${actions.edit.offcanvas.position!'end'?js_string}',
              redirectForm: ${actions.edit.offcanvas.redirectForm!'false'?c}
            }</#if>
          }<#if actions.delete??>,</#if>
        </#if>
        <#if actions.delete??>
          delete: {
            url: '${actions.delete.url?js_string}',
            icon: '${actions.delete.icon!'trash'?js_string}',
            title: '${actions.delete.title!'Supprimer'?js_string}',
            btnClass: '${actions.delete.btnClass!'danger'?js_string}',
            size: '${actions.delete.size!'sm'?js_string}',
            confirm: ${actions.delete.confirm!'true'?c}<#if actions.delete.confirmMessage??>,
            confirmMessage: '${actions.delete.confirmMessage?js_string}'</#if>
          }
        </#if>
      };
      
      let currentPage = ${paginator.pageCurrent};
      let itemsPerPage = ${paginator.itemsPerPage};
      let totalItems = ${paginator.itemsCount};
      let totalPages = ${paginator.pagesCount};
      let currentSort = null;
      let sortAsc = true;
 
      // Fonction pour charger les données d'une page
      function loadPage(pageNumber) {
        const tbody = document.getElementById(tableId + '-body');
        const totalCols = columns.length + (Object.keys(actions).length > 0 ? 1 : 0);
        
        // Afficher le loader
        tbody.innerHTML = '<tr><td colspan="' + totalCols + '" class="text-center"><i class="ti ti-loader-2 animate-spin"></i> Chargement...</td></tr>';
        
        // Paramètres de la requête
        const params = new URLSearchParams({
          page: pageNumber,
          items_per_page: itemsPerPage
        });
        
        // Ajouter le tri si actif
        if (currentSort) {
          params.append('sort', currentSort);
          params.append('asc', sortAsc);
        }
        
        // Requête AJAX
        fetch(ajaxUrl + (ajaxUrl.includes('?') ? '&' : '?') + params.toString(), {
          headers: {
            'X-Requested-With': 'XMLHttpRequest'
          }
        })
        .then(response => {
          if (!response.ok) {
            throw new Error('Erreur réseau: ' + response.status);
          }
          return response.json();
        })
        .then(data => {
          updateTable(data);
          currentPage = pageNumber;
          updatePagination();
          
          // Mettre à jour le compteur
          if (data.totalItems !== undefined) {
            totalItems = data.totalItems;
            document.getElementById(tableId + '-count').textContent = totalItems;
          }
          
          // Mettre à jour l'URL
          const newUrl = new URL(window.location);
          newUrl.searchParams.set('page_index', pageNumber);
          newUrl.searchParams.set('items_per_page', itemsPerPage);
          window.history.pushState({page: pageNumber}, '', newUrl);
        })
        .catch(error => {
          console.error('Error:', error);
          tbody.innerHTML = '<tr><td colspan="' + totalCols + '" class="text-center text-danger"><i class="ti ti-alert-circle"></i> ' + error.message + '</td></tr>';
        });
      }
      
      // Fonction pour mettre à jour le tableau
      function updateTable(response) {
        const tbody = document.getElementById(tableId + '-body');
        tbody.innerHTML = '';
        
        // Gérer différents formats de réponse
        const data = Array.isArray(response) ? response : (response.data || response.items || []);
        
        if (!data || data.length === 0) {
          const totalCols = columns.length + (Object.keys(actions).length > 0 ? 1 : 0);
          tbody.innerHTML = '<tr><td colspan="' + totalCols + '" class="text-center text-muted"><i class="ti ti-database-off"></i> Aucune donnée à afficher</td></tr>';
          return;
        }
        
        // Créer les lignes du tableau
        data.forEach(item => {
          const tr = document.createElement('tr');
          
          columns.forEach(column => {
            const td = document.createElement('td');
            
            // Gérer les propriétés imbriquées
            let value = item;
            const properties = column.property.split('.');
            properties.forEach(prop => {
              value = value ? value[prop] : '';
            });
            
           // Gérer les valeurs spéciales
            if (value === null || value === undefined) {
              value = '';
            } else if (typeof value === 'boolean') {
              value = value 
                ? '<i class="ti ti-check text-success"></i>' 
                : '<i class="ti ti-x text-danger"></i>';
            }
            
            td.innerHTML = value;
            tr.appendChild(td);
          });
          
          // Ajouter la colonne d'actions
          if (Object.keys(actions).length > 0) {
            const tdActions = document.createElement('td');
            tdActions.className = 'text-center';
            tdActions.innerHTML = generateActionButtons(item);
            tr.appendChild(tdActions);
          }
          
          tbody.appendChild(tr);
        });
      }
      
      // Fonction pour générer les boutons d'actions
      function generateActionButtons(item) {
        let buttons = '<div class="btn-group btn-group-sm" role="group">';
        
        // Bouton View
        if (actions.view) {
          const viewUrl = replaceUrlPlaceholders(actions.view.url, item);
          buttons += '<a href="' + viewUrl + '" class="btn btn-' + actions.view.btnClass + ' btn-' + actions.view.size + '" title="' + actions.view.title + '"><i class="ti ti-' + actions.view.icon + '"></i></a>';
        }
        
        // Bouton Edit
        if (actions.edit) {
          const editUrl = replaceUrlPlaceholders(actions.edit.url, item);
          
          if (actions.edit.offcanvas) {
            const offcanvasId = 'offcanvas-' + tableId + '-' + item.id;
            buttons += '<button type="button" class="btn btn-' + actions.edit.btnClass + ' btn-' + actions.edit.size + '" data-bs-toggle="offcanvas" data-bs-target="#' + offcanvasId + '" title="' + actions.edit.title + '"><i class="ti ti-' + actions.edit.icon + '"></i></button>';
            
            // Créer l'offcanvas
            setTimeout(() => createOffcanvas(offcanvasId, editUrl, actions.edit, item), 0);
          } else {
           buttons += '<a href="' + editUrl + '" class="btn btn-' + actions.edit.btnClass + ' btn-' + actions.edit.size + '" title="' + actions.edit.title + '"><i class="ti ti-' + actions.edit.icon + '"></i></a>';
          }
        }
        
        // Bouton Delete
        if (actions.delete) {
          const deleteUrl = replaceUrlPlaceholders(actions.delete.url, item);
          const confirmMsg = actions.delete.confirmMessage || 'Êtes-vous sûr de vouloir supprimer cet élément ?';
          
          buttons += '<a href="' + deleteUrl + '" class="btn btn-' + actions.delete.btnClass + ' btn-' + actions.delete.size + '" title="' + actions.delete.title + '"';
          
          if (actions.delete.confirm) {
            buttons += ' onclick="return confirm(\'' + confirmMsg + '\');"';
          }
          
          buttons += '><i class="ti ti-' + actions.delete.icon + '"></i></a>';
        }
        
       buttons += '</div>';
        return buttons;
      }
      
      // Fonction pour remplacer les placeholders
      function replaceUrlPlaceholders(url, item) {
        return url.replace(/\{(\w+)\}/g, (match, property) => {
          return item[property] || '';
        });
      }
      
      // Fonction pour créer un offcanvas
      function createOffcanvas(offcanvasId, url, editConfig, item) {
       if (document.getElementById(offcanvasId)) {
          return;
        }
        
        const offcanvas = document.createElement('div');
        offcanvas.className = 'offcanvas offcanvas-' + (editConfig.offcanvas.position || 'end');
        offcanvas.id = offcanvasId;
        offcanvas.setAttribute('tabindex', '-1');
        
        const title = editConfig.title + (item.id ? ' - ' + item.id : '');
       
        offcanvas.innerHTML = '<div class="offcanvas-header"><h5 class="offcanvas-title"><i class="ti ti-' + editConfig.icon + '"></i> ' + title + '</h5><button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button></div><div class="offcanvas-body"><div class="text-center p-5"><i class="ti ti-loader-2 animate-spin fs-1"></i><p class="mt-3">Chargement...</p></div></div>';
        
        document.body.appendChild(offcanvas);
        
        const offcanvasEl = new bootstrap.Offcanvas(offcanvas);
        offcanvas.addEventListener('shown.bs.offcanvas', function () {
          const body = offcanvas.querySelector('.offcanvas-body');
         
          fetch(url, {
            headers: {'X-Requested-With': 'XMLHttpRequest'}
          })
         .then(response => response.text())
          .then(html => {
            body.innerHTML = html;
          })
          .catch(error => {
            body.innerHTML = '<div class="alert alert-danger"><i class="ti ti-alert-circle"></i> Erreur lors du chargement</div>';
          });
        });
        
        offcanvas.addEventListener('hidden.bs.offcanvas', function () {
          offcanvas.remove();
          if (editConfig.offcanvas.autoRefresh !== false) {
           loadPage(currentPage);
          }
        });
      }
      
      // Fonction pour mettre à jour la pagination
      function updatePagination() {
        const paginationContainer = document.getElementById(tableId + '-pagination');
       paginationContainer.innerHTML = '';
        
        const maxLinks = 10;
        let startPage = Math.max(1, currentPage - Math.floor(maxLinks / 2));
        let endPage = Math.min(totalPages, startPage + maxLinks - 1);
        
       if (endPage - startPage + 1 < maxLinks) {
          startPage = Math.max(1, endPage - maxLinks + 1);
       }
        
        const addLink = (text, page, isDisabled = false, isActive = false) => {
          const li = document.createElement('li');
         li.className = 'page-item' + (isDisabled ? ' disabled' : '') + (isActive ? ' active' : '');
          
          const a = document.createElement('a');
         a.className = 'page-link';
          a.href = '#';
          a.innerHTML = text;
          
          if (!isDisabled && !isActive && page) {
            a.addEventListener('click', function(e) {
              e.preventDefault();
             loadPage(page);
            });
          }
          
          li.appendChild(a);
         paginationContainer.appendChild(li);
        };
        
        // Navigation
        addLink('<i class="ti ti-chevrons-left"></i>', 1, currentPage === 1);
        addLink('<i class="ti ti-chevron-left"></i>', currentPage - 1, currentPage === 1);
        
        if (startPage > 1) {
          addLink('...', null, true);
        }
       
        for (let i = startPage; i <= endPage; i++) {
          addLink(i, i, false, i === currentPage);
        }
        
        if (endPage < totalPages) {
          addLink('...', null, true);
        }
        
        addLink('<i class="ti ti-chevron-right"></i>', currentPage + 1, currentPage === totalPages);
       addLink('<i class="ti ti-chevrons-right"></i>', totalPages, currentPage === totalPages);
      }
      
      // Gérer le tri
      document.querySelectorAll('#' + tableId + '-container th.sortable').forEach(th => {
        th.addEventListener('click', function() {
          const sortField = this.getAttribute('data-sort');
          
          if (currentSort === sortField) {
            sortAsc = !sortAsc;
          } else {
            currentSort = sortField;
            sortAsc = true;
          }
          
          document.querySelectorAll('#' + tableId + '-container th.sortable i').forEach(icon => {
            icon.className = 'ti ti-arrows-sort ms-1';
          });
          
          const icon = this.querySelector('i');
          if (icon) {
            icon.className = sortAsc ? 'ti ti-sort-ascending ms-1' : 'ti ti-sort-descending ms-1';
         }
          
          loadPage(1);
        });
      });
      
      // Gérer le changement du nombre d'items par page
      <#if combo == 1>
	      const itemsPerPageSelect = document.getElementById(tableId + '-items-per-page');                                                                                                                                       
         if (itemsPerPageSelect) {                                                                                                                                                                                               
         itemsPerPageSelect.addEventListener('change', function() {
            const newItemsPerPage = this.value;
            const currentUrl = window.location.href.split('?')[0];                                                                                                                                                             
            const params = new URLSearchParams(window.location.search);                                                                                                                                                        
            params.set('items_per_page', newItemsPerPage);                                                                                                                                                                     
            params.set('page_index', '1');                                                                                                                                                                                     
            window.location.href = currentUrl + '?' + params.toString();                                                                                                                                                       
          });                                                                                                                                                                                                                  
        }          
      </#if>    
      // API publique
      const api = {
       loadPage: loadPage,
        refresh: () => loadPage(currentPage),
        getCurrentPage: () => currentPage,
        getTotalPages: () => totalPages
      };
      
      // Charger la première page
      loadPage(currentPage);
      
      return api;
    })();
    
    // Rendre l'API accessible globalement
    window.LutecePaginationAjax['${tableId}'] = paginationAjax;
  });
  </script>
 </#macro>