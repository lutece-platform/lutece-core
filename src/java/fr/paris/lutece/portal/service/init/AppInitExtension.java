/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.portal.service.init;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.annotation.Eager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBean;

/**
 * The initialization service of the application.
 */
public class AppInitExtension implements Extension
{
    private static final String PATH_CONF = "/WEB-INF/conf/";

    protected void initPropertiesServices(@Observes final BeforeBeanDiscovery bd)
    {
        Thread.currentThread().setName("Lutece-MainThread");
        if (Files.notExists(Paths.get(AppPathService.getWebAppPath() + PATH_CONF)))
        {
            String _strResourcesDir = getClass().getResource("/").toString().replaceFirst("file:", "").replaceFirst("test-classes", "lutece")
                    .replaceFirst("classes", "lutece");
            AppPathService.init(_strResourcesDir);
        }
        System.setProperty("log4j.configurationFile", "file:" + AppPathService.getWebAppPath() + "/WEB-INF/conf/log.properties");
        AppLogService.info(" {} {} {} ...\n", AppInfo.LUTECE_BANNER_VERSION, "Starting  version", AppInfo.getVersion());
        AppInit.initPropertiesServices(PATH_CONF, AppPathService.getWebAppPath());
    }

    // list of beans needing to be eagerly initialized
    private final List<Bean<?>> eagerBeans = new ArrayList<Bean<?>>();

    // called when a bean is processed
    public <T> void onEagerBean(@Observes ProcessBean<T> e)
    {
        Annotated anno = e.getAnnotated();
        if (anno.isAnnotationPresent(Eager.class) && anno.isAnnotationPresent(ApplicationScoped.class))
        {
            AppLogService.debug("Eager Bean found " + e.getBean());
            eagerBeans.add(e.getBean());
        }
    }

    // when all beans are deployed, we force "eager" init
    public void forceEagerCall(@Observes AfterDeploymentValidation event, BeanManager bm)
    {
        AppLogService.debug("force Eager call on beans " + eagerBeans);
        eagerBeans.forEach(bean -> bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean)).toString());

    }
}
