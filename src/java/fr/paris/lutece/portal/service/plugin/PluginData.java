package fr.paris.lutece.portal.service.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;


/**
 * Represents metadata and configuration information for a Lutece plugin.
 * <p>
 * This class stores plugin-level data such as CSS and JavaScript resources,
 * administrative files, Freemarker templates, portlet types, insert services,
 * and connection services.
 * </p>
 */
public class PluginData {

    /** The plugin's unique name. */
    private String _strPluginName;

    /** List of XPage applications defined by the plugin. */
    private List<XPageApplicationEntry> _listXPageApplications;

    /** CSS style sheets mapped by their mode (identified by integer keys). */
    private Map<Integer, List<String>> _listCssStyleSheets;

    /** JavaScript files mapped by their mode (identified by integer keys). */
    private Map<Integer, List<String>> _listJavascriptFiles;

    /** Administrative CSS style sheets. */
    private List<String> _listAdminCssStyleSheets;

    /** Administrative JavaScript files. */
    private List<String> _listAdminJavascriptFiles;

    /** List of rights declared by the plugin. */
    private List<Right> _listRights;

    /** List of portlet types provided by the plugin. */
    private List<PortletType> _listPortletTypes;

    /** List of insert services registered by the plugin. */
    private List<InsertService> _listInsertServices;

    /** List of Freemarker auto-include files. */
    private List<String> _listFreemarkerAutoIncludes;

    /** Map of Freemarker auto-import directives (alias → template path). */
    private Map<String, String> _mapFreemarkerAutoImports;

    /** Parameters defined in the plugin XML configuration. */
    private Map<String, String> _mapParams = new HashMap<>();

    /** Connection service associated with the plugin. */
    private PluginConnectionService _connectionService;

    /**
     * Constructs a new {@code PluginData} instance for the given plugin name.
     *
     * @param pluginName
     *            The name of the plugin.
     */
    public PluginData(String pluginName) {
        _strPluginName = pluginName;
    }

    /**
     * Returns the plugin name.
     *
     * @return The plugin name.
     */
    public String getPluginName() {
        return _strPluginName;
    }

    /**
     * Returns the connection service associated with this plugin.
     *
     * @return The {@link PluginConnectionService} instance.
     */
    public PluginConnectionService getConnectionService() {
        return _connectionService;
    }

    /**
     * Sets the connection service for this plugin.
     *
     * @param connectionService
     *            The {@link PluginConnectionService} to associate.
     */
    public void setConnectionService(PluginConnectionService connectionService) {
        this._connectionService = connectionService;
    }

    /**
     * Returns the list of XPage applications.
     *
     * @return The list of {@link XPageApplicationEntry} instances.
     */
    public List<XPageApplicationEntry> getXPageApplications() {
        return _listXPageApplications;
    }

    /**
     * Sets the list of XPage applications.
     *
     * @param listXPageApplications
     *            The list of {@link XPageApplicationEntry} instances.
     */
    public void setXPageApplications(List<XPageApplicationEntry> listXPageApplications) {
        this._listXPageApplications = listXPageApplications;
    }

    /**
     * Returns the CSS style sheets mapped by mode.
     *
     * @return A map of CSS file lists.
     */
    public Map<Integer, List<String>> getCssStyleSheets() {
        return _listCssStyleSheets;
    }

    /**
     * Sets the CSS style sheets.
     *
     * @param listCssStyleSheets
     *            A map of CSS file lists.
     */
    public void setCssStyleSheets(Map<Integer, List<String>> listCssStyleSheets) {
        this._listCssStyleSheets = listCssStyleSheets;
    }

    /**
     * Returns the JavaScript files mapped by mode.
     *
     * @return A map of JavaScript file lists.
     */
    public Map<Integer, List<String>> getJavascriptFiles() {
        return _listJavascriptFiles;
    }

    /**
     * Sets the JavaScript files.
     *
     * @param listJavascriptFiles
     *            A map of JavaScript file lists.
     */
    public void setJavascriptFiles(Map<Integer, List<String>> listJavascriptFiles) {
        this._listJavascriptFiles = listJavascriptFiles;
    }

    /**
     * Returns the list of administrative CSS files.
     *
     * @return A list of admin CSS file paths.
     */
    public List<String> getAdminCssStyleSheets() {
        return _listAdminCssStyleSheets;
    }

    /**
     * Sets the list of administrative CSS files.
     *
     * @param listAdminCssStyleSheets
     *            The list of admin CSS file paths.
     */
    public void setAdminCssStyleSheets(List<String> listAdminCssStyleSheets) {
        this._listAdminCssStyleSheets = listAdminCssStyleSheets;
    }

    /**
     * Returns the list of administrative JavaScript files.
     *
     * @return A list of admin JavaScript file paths.
     */
    public List<String> getAdminJavascriptFiles() {
        return _listAdminJavascriptFiles;
    }

    /**
     * Sets the list of administrative JavaScript files.
     *
     * @param listAdminJavascriptFiles
     *            The list of admin JavaScript file paths.
     */
    public void setAdminJavascriptFiles(List<String> listAdminJavascriptFiles) {
        this._listAdminJavascriptFiles = listAdminJavascriptFiles;
    }

    /**
     * Returns the list of plugin rights.
     *
     * @return A list of {@link Right} instances.
     */
    public List<Right> getRights() {
        return _listRights;
    }

    /**
     * Sets the list of plugin rights.
     *
     * @param listRights
     *            The list of {@link Right} instances.
     */
    public void setRights(List<Right> listRights) {
        this._listRights = listRights;
    }

    /**
     * Returns the list of portlet types provided by the plugin.
     *
     * @return A list of {@link PortletType} instances.
     */
    public List<PortletType> getPortletTypes() {
        return _listPortletTypes;
    }

    /**
     * Sets the list of portlet types.
     *
     * @param listPortletTypes
     *            The list of {@link PortletType} instances.
     */
    public void setPortletTypes(List<PortletType> listPortletTypes) {
        this._listPortletTypes = listPortletTypes;
    }

    /**
     * Returns the list of insert services.
     *
     * @return A list of {@link InsertService} instances.
     */
    public List<InsertService> getInsertServices() {
        return _listInsertServices;
    }

    /**
     * Sets the list of insert services.
     *
     * @param listInsertServices
     *            The list of {@link InsertService} instances.
     */
    public void setInsertServices(List<InsertService> listInsertServices) {
        this._listInsertServices = listInsertServices;
    }

    /**
     * Returns the list of Freemarker auto-include files.
     *
     * @return A list of template file names.
     */
    public List<String> getFreemarkerAutoIncludes() {
        return _listFreemarkerAutoIncludes;
    }

    /**
     * Sets the list of Freemarker auto-include files.
     *
     * @param listFreemarkerAutoIncludes
     *            A list of template file names.
     */
    public void setFreemarkerAutoIncludes(List<String> listFreemarkerAutoIncludes) {
        this._listFreemarkerAutoIncludes = listFreemarkerAutoIncludes;
    }

    /**
     * Returns the Freemarker auto-import map.
     *
     * @return A map of alias → template path.
     */
    public Map<String, String> getFreemarkerAutoImports() {
        return _mapFreemarkerAutoImports;
    }

    /**
     * Sets the Freemarker auto-import map.
     *
     * @param mapFreemarkerAutoImports
     *            A map of alias → template path.
     */
    public void setFreemarkerAutoImports(Map<String, String> mapFreemarkerAutoImports) {
        this._mapFreemarkerAutoImports = mapFreemarkerAutoImports;
    }

    /**
     * Returns the plugin parameters defined in its XML descriptor.
     *
     * @return A map of parameter names and values.
     */
    public Map<String, String> getParams() {
        return _mapParams;
    }

    /**
     * Sets the plugin parameters.
     *
     * @param mapParams
     *            A map of parameter names and values.
     */
    public void setParams(Map<String, String> mapParams) {
        this._mapParams = mapParams;
    }
}
