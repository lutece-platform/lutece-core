/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.service.file.implementation;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.file.IFileRBACService;
import fr.paris.lutece.portal.service.file.IFileStoreService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.CdiHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * Producer for default file store service provider.
 * Creates and configures a FileStoreServiceProvider instance using CDI injection
 * instead of manual service lookup.
 */
@ApplicationScoped
public class DefaultFileStoreServiceProviderProducer {
    
    // Configuration properties with validation
    @Inject
    @ConfigProperty(name = "lutece.defaultFileServiceProvider.fileStoreService")
    private String fileStoreImplName;
    
    @Inject
    @ConfigProperty(name = "lutece.defaultFileServiceProvider.rbacService") 
    private String rbacImplName;
    
    @Inject
    @ConfigProperty(name = "lutece.defaultFileServiceProvider.downloadService")
    private String downloadImplName;
    
    // CDI service injection using Instance<T> for optional dependencies
    @Inject
    private Instance<IFileStoreService> fileStoreServices;
    
    @Inject
    private Instance<IFileDownloadUrlService> downloadServices;
    
    @Inject
    private Instance<IFileRBACService> rbacServices;
    
    @PostConstruct
    private void validateConfiguration() {
    	 AppLogService.info("Initializing DefaultFileStoreServiceProviderProducer");
    	 requireNonBlank(fileStoreImplName, "lutece.defaultFileServiceProvider.fileStoreService");
    	 requireNonBlank(rbacImplName, "lutece.defaultFileServiceProvider.rbacService");
    	 requireNonBlank(downloadImplName, "lutece.defaultFileServiceProvider.downloadService");
    	 AppLogService.info("Configuration validated successfully");
    }
    
    /**
     * Produces a configured FileStoreServiceProvider instance.
     * Uses CDI Instance&lt;T&gt; to safely resolve services with proper error handling.
     *
     * @return configured IFileStoreServiceProvider instance
     * @throws IllegalStateException if required services cannot be resolved
     */
    @Produces
    @ApplicationScoped
    @Named("defaultDatabaseFileStoreProvider")
    public IFileStoreServiceProvider createFileStoreServiceProvider() {
        
        AppLogService.debug("Creating FileStoreServiceProvider with services: {}, {}, {}", 
            fileStoreImplName, downloadImplName, rbacImplName);
        
        // Resolve services using CDI Instance<T> with proper error handling
        IFileStoreService fileStoreService = CdiHelper.resolve(
            fileStoreServices, fileStoreImplName
        );
        
        IFileDownloadUrlService downloadService = CdiHelper.resolve(
            downloadServices, downloadImplName
        );
        
        IFileRBACService rbacService = CdiHelper.resolve(
            rbacServices, rbacImplName
        );
        
        // Create provider with resolved services
        FileStoreServiceProvider provider = new FileStoreServiceProvider(
            "defaultDatabaseFileStoreProvider",
            fileStoreService,
            downloadService, 
            rbacService,
            true
        );
        
        AppLogService.debug("FileStoreServiceProvider '{}' created successfully", 
            provider.getName());
        
        return provider;
    }
    
    /**
     * Validates that a given configuration property value is not {@code null} or blank.
     * <p>
     * This method is typically used to ensure that mandatory configuration properties
     * are correctly defined before the application context is fully initialized.
     * If the provided value is missing or contains only whitespace, an
     * {@link IllegalStateException} is thrown, stopping the initialization process.
     * </p>
     *
     * <h4>Example:</h4>
     * <pre>{@code
     * requireNonBlank(fileStoreImplName, "lutece.defaultFileServiceProvider.fileStoreService");
     * }</pre>
     *
     * @param value the configuration property value to validate; may be {@code null}
     * @param propertyName the name of the property being validated, used in the error message
     * @throws IllegalStateException if the value is {@code null} or blank
     */
    private void requireNonBlank(String value, String propertyName) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Property '" + propertyName + "' is required and must not be blank");
        }
    }
    
    @PreDestroy
    private void cleanup() {
        AppLogService.debug("DefaultFileStoreServiceProviderProducer is being destroyed");
    }
}
